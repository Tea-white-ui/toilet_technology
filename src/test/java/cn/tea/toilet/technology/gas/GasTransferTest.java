package cn.tea.toilet.technology.gas;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GasTransferTest {
    @Test
    void transfersOnlyTheAmountAcceptedByTheDestination() {
        TestHandler source = new TestHandler(1_000, new GasStack(GasRegistry.BIOGAS, 900));
        TestHandler destination = new TestHandler(500, new GasStack(GasRegistry.BIOGAS, 300));

        GasStack transferred = GasTransfer.transfer(source, 0, destination, 0, 1_000);

        assertEquals(new GasStack(GasRegistry.BIOGAS, 200), transferred);
        assertEquals(700, source.stack.amount());
        assertEquals(500, destination.stack.amount());
    }

    @Test
    void leavesBothHandlersUntouchedWhenDestinationRejectsTheGas() {
        TestHandler source = new TestHandler(1_000, new GasStack(GasRegistry.BIOGAS, 900));
        TestHandler destination = new TestHandler(1_000, new GasStack(GasRegistry.METHANE, 300));

        GasStack transferred = GasTransfer.transfer(source, 0, destination, 0, 1_000);

        assertTrue(transferred.isEmpty());
        assertEquals(900, source.stack.amount());
        assertEquals(300, destination.stack.amount());
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
        @Override public boolean isValid(int tank, GasStack stack) {
            return tank == 0 && !stack.isEmpty() && (this.stack.isEmpty() || this.stack.is(stack.gas()));
        }
        @Override public GasStack insertGas(int tank, GasStack stack, GasAction action) {
            if (!isValid(tank, stack)) return stack;
            long accepted = Math.min(capacity - this.stack.amount(), stack.amount());
            if (accepted <= 0) return stack;
            if (action.executes()) this.stack = new GasStack(stack.gas(), this.stack.amount() + accepted);
            return stack.copyWithAmount(stack.amount() - accepted);
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
