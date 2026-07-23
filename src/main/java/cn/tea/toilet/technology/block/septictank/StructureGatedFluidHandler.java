package cn.tea.toilet.technology.block.septictank;

import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

import java.util.function.BooleanSupplier;

/** Revokes mutations on retained fluid capability references when the multiblock becomes invalid. */
final class StructureGatedFluidHandler implements IFluidHandler {
    private final IFluidHandler delegate;
    private final BooleanSupplier enabled;

    StructureGatedFluidHandler(IFluidHandler delegate, BooleanSupplier enabled) {
        this.delegate = delegate;
        this.enabled = enabled;
    }

    @Override public int getTanks() { return delegate.getTanks(); }
    @Override public @NotNull FluidStack getFluidInTank(int tank) { return delegate.getFluidInTank(tank); }
    @Override public int getTankCapacity(int tank) { return delegate.getTankCapacity(tank); }
    @Override public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return enabled.getAsBoolean() && delegate.isFluidValid(tank, stack);
    }
    @Override public int fill(FluidStack resource, FluidAction action) {
        return enabled.getAsBoolean() ? delegate.fill(resource, action) : 0;
    }
    @Override public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        return enabled.getAsBoolean() ? delegate.drain(resource, action) : FluidStack.EMPTY;
    }
    @Override public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        return enabled.getAsBoolean() ? delegate.drain(maxDrain, action) : FluidStack.EMPTY;
    }
}