package cn.tea.toilet.technology.block.gaspipe;

import cn.tea.toilet.technology.api.gas.GasAction;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.api.gas.GasStack;
import cn.tea.toilet.technology.api.gas.IGasHandler;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GasPipeNetworkTransferTest {
    @Test
    void equalPressureEndpointsRemainCompletelyStill() {
        TestHandler first = new TestHandler(1_000, new GasStack(GasRegistry.BIOGAS, 500), true, true);
        TestHandler second = new TestHandler(1_000, new GasStack(GasRegistry.BIOGAS, 500), true, true);

        GasStack moved = GasPipeNetworkTransfer.transferOne(List.of(first, second), 256);

        assertTrue(moved.isEmpty());
        assertEquals(0, first.executedMutations + second.executedMutations);
        assertEquals(500, first.stack.amount());
        assertEquals(500, second.stack.amount());
    }

    @Test
    void pressureGradientEqualizesWithoutOvershooting() {
        TestHandler source = new TestHandler(1_000, new GasStack(GasRegistry.BIOGAS, 600), true, true);
        TestHandler destination = new TestHandler(1_000, new GasStack(GasRegistry.BIOGAS, 400), true, true);

        GasStack moved = GasPipeNetworkTransfer.transferOne(List.of(source, destination), 256);

        assertEquals(100, moved.amount());
        assertEquals(500, source.stack.amount());
        assertEquals(500, destination.stack.amount());
        assertTrue(GasPipeNetworkTransfer.transferOne(List.of(source, destination), 256).isEmpty());
    }

    @Test
    void outputOnlyEndpointFeedsInputOnlyEndpoint() {
        TestHandler source = new TestHandler(1_000, new GasStack(GasRegistry.BIOGAS, 900), false, true);
        TestHandler destination = new TestHandler(1_000, GasStack.EMPTY, true, false);

        GasStack moved = GasPipeNetworkTransfer.transferOne(List.of(source, destination), 256);

        assertEquals(256, moved.amount());
        assertEquals(644, source.stack.amount());
        assertEquals(256, destination.stack.amount());
    }

    @Test
    void fullBidirectionalSourceStillEqualizesTowardEmptierDestination() {
        TestHandler source = new TestHandler(1_000, new GasStack(GasRegistry.BIOGAS, 1_000), true, true);
        TestHandler destination = new TestHandler(1_000, GasStack.EMPTY, true, true);

        GasStack moved = GasPipeNetworkTransfer.transferOne(List.of(source, destination), 256);

        assertEquals(256, moved.amount());
        assertEquals(744, source.stack.amount());
        assertEquals(256, destination.stack.amount());
    }

    @Test
    void emptyBidirectionalDestinationCannotBeOvershot() {
        TestHandler source = new TestHandler(1_000, new GasStack(GasRegistry.BIOGAS, 1_000), true, true);
        TestHandler destination = new TestHandler(1_000, GasStack.EMPTY, true, true);

        GasStack moved = GasPipeNetworkTransfer.transferOne(List.of(source, destination), 1_000);

        assertEquals(500, moved.amount());
        assertEquals(500, source.stack.amount());
        assertEquals(500, destination.stack.amount());
        assertTrue(GasPipeNetworkTransfer.transferOne(List.of(source, destination), 1_000).isEmpty());
    }

    @Test
    void gasInOnePipeBufferFlowsIntoAnAdjacentEmptyPipeBuffer() {
        TestHandler filledPipe = new TestHandler(1_000, new GasStack(GasRegistry.BIOGAS, 1_000), true, true);
        TestHandler emptyPipe = new TestHandler(1_000, GasStack.EMPTY, true, true);

        GasStack moved = GasPipeNetworkTransfer.redistributePipeBuffers(List.of(filledPipe, emptyPipe), 256);

        assertEquals(256, moved.amount());
        assertEquals(744, filledPipe.stack.amount());
        assertEquals(256, emptyPipe.stack.amount());
    }

    private static final class TestHandler implements IGasHandler {
        private final long capacity;
        private final boolean allowsInsertion;
        private final boolean allowsExtraction;
        private GasStack stack;
        private int executedMutations;

        private TestHandler(long capacity, GasStack stack, boolean allowsInsertion, boolean allowsExtraction) {
            this.capacity = capacity;
            this.stack = stack;
            this.allowsInsertion = allowsInsertion;
            this.allowsExtraction = allowsExtraction;
        }

        @Override public int getGasTanks() { return 1; }
        @Override public GasStack getGasInTank(int tank) { return tank == 0 ? stack : GasStack.EMPTY; }
        @Override public void setGasInTank(int tank, GasStack stack) { }
        @Override public long getGasTankCapacity(int tank) { return tank == 0 ? capacity : 0; }
        @Override public boolean isValid(int tank, GasStack candidate) {
            return tank == 0 && allowsInsertion && !candidate.isEmpty()
                    && (stack.isEmpty() || stack.is(candidate.gas()));
        }
        @Override public GasStack insertGas(int tank, GasStack candidate, GasAction action) {
            if (!isValid(tank, candidate)) return candidate;
            long accepted = Math.min(capacity - stack.amount(), candidate.amount());
            if (accepted <= 0) return candidate;
            if (action.executes()) {
                stack = new GasStack(candidate.gas(), stack.amount() + accepted);
                executedMutations++;
            }
            return candidate.copyWithAmount(candidate.amount() - accepted);
        }
        @Override public GasStack extractGas(int tank, long amount, GasAction action) {
            if (tank != 0 || !allowsExtraction || amount <= 0 || stack.isEmpty()) return GasStack.EMPTY;
            long extracted = Math.min(amount, stack.amount());
            GasStack result = stack.copyWithAmount(extracted);
            if (action.executes()) {
                stack = stack.copyWithAmount(stack.amount() - extracted);
                executedMutations++;
            }
            return result;
        }
    }
}
