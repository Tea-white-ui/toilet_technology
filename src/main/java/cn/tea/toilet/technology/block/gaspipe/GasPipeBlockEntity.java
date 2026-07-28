package cn.tea.toilet.technology.block.gaspipe;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.gas.BasicGasTank;
import cn.tea.toilet.technology.gas.GasAction;
import cn.tea.toilet.technology.gas.GasCapabilities;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.gas.IGasHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** A small local buffer that moves gas between adjacent native gas handlers. */
public final class GasPipeBlockEntity extends BlockEntity {
    public static final long CAPACITY = 1_000;
    public static final long TRANSFER_RATE = 256;

    private final BasicGasTank tank = new BasicGasTank(CAPACITY, stack -> true, this::setChanged);
    private final IGasHandler handler = new PipeGasHandler();
    private int roundRobinIndex;

    public GasPipeBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GAS_PIPE.get(), pos, state);
    }

    public IGasHandler getGasHandler(@Nullable Direction side) {
        return handler;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GasPipeBlockEntity pipe) {
        if (level.isClientSide()) return;
        Direction[] directions = Direction.values();
        int start = pipe.roundRobinIndex++ % directions.length;
        Direction receivedFrom = null;

        for (int offset = 0; offset < directions.length && pipe.tank.getStored() < CAPACITY; offset++) {
            Direction direction = directions[(start + offset) % directions.length];
            IGasHandler neighbor = level.getCapability(GasCapabilities.BLOCK, pos.relative(direction), direction.getOpposite());
            if (neighbor == null || neighbor == pipe.handler) continue;
            if (!GasPipeTransfer.pull(neighbor, pipe.handler, TRANSFER_RATE).isEmpty()) {
                receivedFrom = direction;
                break;
            }
        }

        if (pipe.tank.isEmpty()) return;
        for (int offset = 0; offset < directions.length; offset++) {
            Direction direction = directions[(start + offset) % directions.length];
            if (direction == receivedFrom) continue;
            IGasHandler neighbor = level.getCapability(GasCapabilities.BLOCK, pos.relative(direction), direction.getOpposite());
            if (neighbor == null || neighbor == pipe.handler) continue;
            if (!GasPipeTransfer.push(pipe.handler, neighbor, TRANSFER_RATE).isEmpty()) break;
        }
    }

    private final class PipeGasHandler implements IGasHandler {
        @Override public int getGasTanks() { return 1; }
        @Override public GasStack getGasInTank(int tankIndex) { return tankIndex == 0 ? tank.getStack() : GasStack.EMPTY; }
        @Override public void setGasInTank(int tankIndex, GasStack stack) { if (tankIndex == 0) tank.setStack(stack); }
        @Override public long getGasTankCapacity(int tankIndex) { return tankIndex == 0 ? CAPACITY : 0; }
        @Override public boolean isValid(int tankIndex, GasStack stack) { return tankIndex == 0 && tank.isValid(stack); }
        @Override public GasStack insertGas(int tankIndex, GasStack stack, GasAction action) {
            return tankIndex == 0 ? tank.insert(stack, action) : stack;
        }
        @Override public GasStack extractGas(int tankIndex, long amount, GasAction action) {
            return tankIndex == 0 ? tank.extract(amount, action) : GasStack.EMPTY;
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("GasTank", tank.serializeNBT());
        tag.putInt("RoundRobinIndex", roundRobinIndex);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("GasTank")) tank.deserializeNBT(tag.getCompound("GasTank"));
        roundRobinIndex = Math.max(0, tag.getInt("RoundRobinIndex"));
    }
}
