package cn.tea.toilet.technology.block.multiblock;

import net.minecraft.core.BlockPos;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultiblockPortBinderTest {
    @Test
    void bindsOnlyPortsFoundAtTheProvidedStructurePositions() {
        BlockPos first = new BlockPos(1, 2, 3);
        BlockPos second = new BlockPos(4, 5, 6);
        AtomicInteger bindings = new AtomicInteger();
        AtomicInteger unbindings = new AtomicInteger();

        MultiblockPortBinder.synchronize(true, List.of(first, second),
                position -> position.equals(first) ? "port" : null,
                port -> bindings.incrementAndGet(),
                port -> unbindings.incrementAndGet());

        assertEquals(1, bindings.get());
        assertEquals(0, unbindings.get());
    }

    @Test
    void unbindsEveryDiscoveredPortWhenTheStructureIsInvalid() {
        BlockPos first = new BlockPos(1, 2, 3);
        BlockPos second = new BlockPos(4, 5, 6);
        AtomicInteger bindings = new AtomicInteger();
        AtomicInteger unbindings = new AtomicInteger();

        MultiblockPortBinder.synchronize(false, List.of(first, second),
                position -> "port-" + position.getX(),
                port -> bindings.incrementAndGet(),
                port -> unbindings.incrementAndGet());

        assertEquals(0, bindings.get());
        assertEquals(2, unbindings.get());
    }
}
