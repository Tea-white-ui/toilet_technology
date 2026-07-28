package cn.tea.toilet.technology.block.automation;

import cn.tea.toilet.technology.gas.ExternalGasHandlers;
import cn.tea.toilet.technology.api.gas.GasStack;
import cn.tea.toilet.technology.api.gas.GasTransfer;
import cn.tea.toilet.technology.api.gas.IGasHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

/** Transfers a machine output to the adjacent container on a designated face. */
public final class AutomaticOutput {
    private AutomaticOutput() {
    }

    /** Deliberately far above every current machine production rate. */
    public static final long GAS_TRANSFER_LIMIT = 1_000_000L;
    /** Deliberately far above every current machine production rate. */
    public static final int FLUID_TRANSFER_LIMIT = 1_000_000;

    public static GasStack pushGas(Level level, BlockPos sourcePos, Direction outputFace, IGasHandler source) {
        IGasHandler destination = ExternalGasHandlers.find(level, sourcePos.relative(outputFace), outputFace.getOpposite());
        return destination == null ? GasStack.EMPTY : GasTransfer.transfer(source, 0, destination, 0, GAS_TRANSFER_LIMIT);
    }

    public static FluidStack pushFluid(Level level, BlockPos sourcePos, Direction outputFace, IFluidHandler source) {
        IFluidHandler destination = level.getCapability(Capabilities.FluidHandler.BLOCK,
                sourcePos.relative(outputFace), outputFace.getOpposite());
        if (destination == null) return FluidStack.EMPTY;

        FluidStack offered = source.drain(FLUID_TRANSFER_LIMIT, IFluidHandler.FluidAction.SIMULATE);
        if (offered.isEmpty()) return FluidStack.EMPTY;
        int accepted = destination.fill(offered, IFluidHandler.FluidAction.SIMULATE);
        if (accepted <= 0) return FluidStack.EMPTY;

        FluidStack extracted = source.drain(Math.min(accepted, offered.getAmount()), IFluidHandler.FluidAction.EXECUTE);
        if (extracted.isEmpty()) return FluidStack.EMPTY;
        int inserted = destination.fill(extracted, IFluidHandler.FluidAction.EXECUTE);
        if (inserted != extracted.getAmount()) {
            throw new IllegalStateException("Fluid handler changed between simulation and execution");
        }
        return extracted;
    }
}