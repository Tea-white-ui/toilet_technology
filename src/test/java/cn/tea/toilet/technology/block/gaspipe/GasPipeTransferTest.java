package cn.tea.toilet.technology.block.gaspipe;

import cn.tea.toilet.technology.gas.GasAction;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.gas.IGasHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GasPipeTransferTest {
    @Test
    void movesAtMostThePerTickLimitThroughThePipeBuffer() {
        TestHandler source = new TestHandler(2_000, new GasStack(GasRegistry.BIOGAS, 1_000));
        TestHandler pipe = new TestHandler(1_000, GasStack.EMPTY);
        TestHandler destination = new TestHandler(2_000, GasStack.EMPTY);

        GasPipeTransfer.pull(source, pipe, 256);
        GasPipeTransfer.push(pipe, destination, 256);

        assertEquals(744, source.stack.amount());
        assertEquals(0, pipe.stack.amount());
        assertEquals(256, destination.stack.amount());
    }

    @Test
    void doesNotMixDifferentGasesInThePipeBuffer() {
        TestHandler source = new TestHandler(2_000, new GasStack(GasRegistry.METHANE, 500));
        TestHandler pipe = new TestHandler(1_000, new GasStack(GasRegistry.BIOGAS, 300));

        GasPipeTransfer.pull(source, pipe, 256);

        assertEquals(500, source.stack.amount());
        assertEquals(new GasStack(GasRegistry.BIOGAS, 300), pipe.stack);
    }

    private static final class TestHandler implements IGasHandler {
        private final long capacity;
        private GasStack stack;

        private TestHandler(long capacity, GasStack stack) {
            this.capacity = capacity;
            this.stack = stack;
        }

        @Override public int getGasTanks() { return 1; }
        @Override public GasStack getGasInTank(int tank) { return tank == 0 ? stack : GasStack.EMPTY; }
        @Override public void setGasInTank(int tank, GasStack stack) { if (tank == 0) this.stack = stack; }
        @Override public long getGasTankCapacity(int tank) { return tank == 0 ? capacity : 0; }
        @Override public boolean isValid(int tank, GasStack candidate) {
            return tank == 0 && !candidate.isEmpty() && (stack.isEmpty() || stack.is(candidate.gas()));
        }
        @Override public GasStack insertGas(int tank, GasStack candidate, GasAction action) {
            if (!isValid(tank, candidate)) return candidate;
            long accepted = Math.min(capacity - stack.amount(), candidate.amount());
            if (accepted <= 0) return candidate;
            if (action.executes()) stack = new GasStack(candidate.gas(), stack.amount() + accepted);
            return candidate.copyWithAmount(candidate.amount() - accepted);
        }
        @Override public GasStack extractGas(int tank, long amount, GasAction action) {
            if (tank != 0 || amount <= 0 || stack.isEmpty()) return GasStack.EMPTY;
            long extracted = Math.min(amount, stack.amount());
            GasStack result = stack.copyWithAmount(extracted);
            if (action.executes()) stack = stack.copyWithAmount(stack.amount() - extracted);
            return result;
        }
    }
}
