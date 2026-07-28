package cn.tea.toilet.technology.block.multiblock;

import cn.tea.toilet.technology.gas.GasAction;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.gas.IGasHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StructureGatedGasHandlerTest {
    @Test
    void blocksMutationsThroughAHandlerRetainedAfterTheStructureBecomesInvalid() {
        MutableEnabled enabled = new MutableEnabled(true);
        TestGasHandler delegate = new TestGasHandler();
        StructureGatedGasHandler handler = new StructureGatedGasHandler(delegate, enabled);
        GasStack biogas = new GasStack(GasRegistry.BIOGAS, 100);

        assertTrue(handler.isValid(0, biogas));
        assertTrue(handler.insertGas(0, biogas, GasAction.EXECUTE).isEmpty());
        enabled.value = false;

        assertFalse(handler.isValid(0, biogas));
        assertEquals(biogas, handler.insertGas(0, biogas, GasAction.EXECUTE));
        assertEquals(GasStack.EMPTY, handler.extractGas(0, 100, GasAction.EXECUTE));
        assertEquals(new GasStack(GasRegistry.BIOGAS, 100), delegate.stack);
    }

    private static final class MutableEnabled implements java.util.function.BooleanSupplier {
        private boolean value;

        private MutableEnabled(boolean value) {
            this.value = value;
        }

        @Override
        public boolean getAsBoolean() {
            return value;
        }
    }

    private static final class TestGasHandler implements IGasHandler {
        private GasStack stack = GasStack.EMPTY;

        @Override public int getGasTanks() { return 1; }
        @Override public GasStack getGasInTank(int tank) { return tank == 0 ? stack : GasStack.EMPTY; }
        @Override public void setGasInTank(int tank, GasStack stack) { if (tank == 0) this.stack = stack; }
        @Override public long getGasTankCapacity(int tank) { return tank == 0 ? 1_000 : 0; }
        @Override public boolean isValid(int tank, GasStack stack) { return tank == 0 && stack.is(GasRegistry.BIOGAS); }
        @Override public GasStack insertGas(int tank, GasStack stack, GasAction action) {
            if (!isValid(tank, stack)) return stack;
            if (action.executes()) this.stack = stack;
            return GasStack.EMPTY;
        }
        @Override public GasStack extractGas(int tank, long amount, GasAction action) {
            if (tank != 0 || stack.isEmpty()) return GasStack.EMPTY;
            GasStack result = stack.copyWithAmount(Math.min(amount, stack.amount()));
            if (action.executes()) stack = GasStack.EMPTY;
            return result;
        }
    }
}
