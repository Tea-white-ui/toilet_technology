package cn.tea.toilet.technology.block.multiblock;

import net.minecraft.core.BlockPos;

import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;

/** Applies one controller validation result to every port position in a fixed multiblock. */
public final class MultiblockPortBinder {
    private MultiblockPortBinder() {
    }

    public static <P> void synchronize(boolean structureValid, Collection<BlockPos> portPositions,
                                       Function<BlockPos, P> findPort,
                                       Consumer<P> bindPort, Consumer<P> unbindPort) {
        for (BlockPos portPosition : portPositions) {
            P port = findPort.apply(portPosition);
            if (port == null) {
                continue;
            }
            if (structureValid) {
                bindPort.accept(port);
            } else {
                unbindPort.accept(port);
            }
        }
    }
}
