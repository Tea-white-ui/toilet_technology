package cn.tea.toilet.technology.block.multiblock;

import cn.tea.toilet.technology.gas.GasAction;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.gas.IGasHandler;

import java.util.function.BooleanSupplier;

/** Revokes gas mutations on retained capability references after structure invalidation. */
public final class StructureGatedGasHandler implements IGasHandler {
    private final IGasHandler delegate;
    private final BooleanSupplier enabled;

    public StructureGatedGasHandler(IGasHandler delegate, BooleanSupplier enabled) {
        this.delegate = delegate;
        this.enabled = enabled;
    }

    @Override public int getGasTanks() { return delegate.getGasTanks(); }
    @Override public GasStack getGasInTank(int tank) { return delegate.getGasInTank(tank); }
    @Override public void setGasInTank(int tank, GasStack stack) {
        if (enabled.getAsBoolean()) {
            delegate.setGasInTank(tank, stack);
        }
    }
    @Override public long getGasTankCapacity(int tank) { return delegate.getGasTankCapacity(tank); }
    @Override public boolean isValid(int tank, GasStack stack) {
        return enabled.getAsBoolean() && delegate.isValid(tank, stack);
    }
    @Override public GasStack insertGas(int tank, GasStack stack, GasAction action) {
        return enabled.getAsBoolean() ? delegate.insertGas(tank, stack, action) : stack;
    }
    @Override public GasStack extractGas(int tank, long amount, GasAction action) {
        return enabled.getAsBoolean() ? delegate.extractGas(tank, amount, action) : GasStack.EMPTY;
    }
}
