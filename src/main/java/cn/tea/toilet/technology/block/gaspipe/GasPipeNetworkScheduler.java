package cn.tea.toilet.technology.block.gaspipe;

import cn.tea.toilet.technology.gas.ExternalGasHandlers;
import cn.tea.toilet.technology.api.gas.IGasHandler;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;

/** Discovers and schedules each loaded connected gas-pipe network at most once per server tick. */
final class GasPipeNetworkScheduler {
    private static final Direction[] DIRECTIONS = Direction.values();
    private static final Map<Level, TickState> TICK_STATES = new WeakHashMap<>();

    private GasPipeNetworkScheduler() {
    }

    static void tick(Level level, BlockPos start) {
        if (level.isClientSide()) return;
        TickState tickState = TICK_STATES.computeIfAbsent(level, ignored -> new TickState());
        long gameTime = level.getGameTime();
        if (tickState.gameTime != gameTime) {
            tickState.gameTime = gameTime;
            tickState.processedPipes.clear();
        }
        if (tickState.processedPipes.contains(start.asLong())) return;

        Network network = discover(level, start);
        for (BlockPos pipePos : network.pipes) tickState.processedPipes.add(pipePos.asLong());
        if (network.pipes.isEmpty()) return;

        for (GasPipeBlockEntity pipe : network.bufferedPipes) {
            if (!GasPipeNetworkTransfer.transferFrom(
                    pipe.getInternalGasHandler(), network.externalEndpoints, GasPipeBlockEntity.TRANSFER_RATE).isEmpty()) {
                return;
            }
        }
        if (!GasPipeNetworkTransfer.transferOne(
                network.externalEndpoints, GasPipeBlockEntity.TRANSFER_RATE).isEmpty()) {
            return;
        }
        GasPipeNetworkTransfer.redistributePipeBuffers(
                network.pipeHandlers, GasPipeBlockEntity.TRANSFER_RATE);
    }

    private static Network discover(Level level, BlockPos start) {
        if (!isLoaded(level, start) || !(level.getBlockState(start).getBlock() instanceof GasPipeBlock)) {
            return Network.EMPTY;
        }

        ArrayDeque<BlockPos> open = new ArrayDeque<>();
        LongOpenHashSet visited = new LongOpenHashSet();
        List<BlockPos> pipes = new ArrayList<>();
        List<IGasHandler> pipeHandlers = new ArrayList<>();
        List<GasPipeBlockEntity> bufferedPipes = new ArrayList<>();
        Set<IGasHandler> endpointIdentities = Collections.newSetFromMap(new IdentityHashMap<>());
        List<IGasHandler> externalEndpoints = new ArrayList<>();
        open.add(start.immutable());

        while (!open.isEmpty()) {
            BlockPos pipePos = open.removeFirst();
            if (!visited.add(pipePos.asLong())) continue;
            if (!isLoaded(level, pipePos) || !(level.getBlockState(pipePos).getBlock() instanceof GasPipeBlock)) continue;

            pipes.add(pipePos);
            if (level.getBlockEntity(pipePos) instanceof GasPipeBlockEntity pipe) {
                pipeHandlers.add(pipe.getInternalGasHandler());
                if (pipe.hasBufferedGas()) bufferedPipes.add(pipe);
            }

            for (Direction direction : DIRECTIONS) {
                BlockPos neighborPos = pipePos.relative(direction);
                if (!isLoaded(level, neighborPos)) continue;
                if (level.getBlockState(neighborPos).getBlock() instanceof GasPipeBlock) {
                    if (!visited.contains(neighborPos.asLong())) open.addLast(neighborPos.immutable());
                    continue;
                }

                IGasHandler endpoint = ExternalGasHandlers.find(level, neighborPos, direction.getOpposite());
                if (endpoint != null && endpointIdentities.add(endpoint)) externalEndpoints.add(endpoint);
            }
        }
        return new Network(pipes, pipeHandlers, bufferedPipes, externalEndpoints);
    }

    private static boolean isLoaded(Level level, BlockPos pos) {
        return level.hasChunk(pos.getX() >> 4, pos.getZ() >> 4);
    }

    private static final class TickState {
        private long gameTime = Long.MIN_VALUE;
        private final LongOpenHashSet processedPipes = new LongOpenHashSet();
    }

    private record Network(List<BlockPos> pipes, List<IGasHandler> pipeHandlers,
                           List<GasPipeBlockEntity> bufferedPipes,
                           List<IGasHandler> externalEndpoints) {
        private static final Network EMPTY = new Network(List.of(), List.of(), List.of(), List.of());
    }
}
