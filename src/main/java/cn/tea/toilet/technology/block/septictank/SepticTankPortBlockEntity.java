package cn.tea.toilet.technology.block.septictank;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.gas.GasAction;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.gas.IGasHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SepticTankPortBlockEntity extends BlockEntity {
    private final SepticTankPortType portType;
    @Nullable private BlockPos controllerPos;
    private final IFluidHandler fluidHandler = new PortFluidHandler();
    private final IGasHandler gasHandler = new PortGasHandler();

    public SepticTankPortBlockEntity(BlockPos pos, BlockState state, SepticTankPortType portType) {
        super(ModBlockEntities.SEPTIC_TANK_PORT.get(), pos, state);
        this.portType = portType;
    }

    public SepticTankPortType getPortType() { return portType; }

    public boolean allowsCapabilityFrom(@Nullable Direction side) {
        if (side == null) return false;
        Direction facing = getBlockState().getValue(SepticTankPortBlock.FACING);
        return portType.allowsCapabilityFrom(side == Direction.UP, side == facing);
    }

    public void bindController(BlockPos pos) {
        if (!pos.equals(controllerPos)) {
            controllerPos = pos.immutable();
            setChanged();
            invalidateCapabilities();
        }
    }

    public void unbindController() {
        if (controllerPos != null) {
            controllerPos = null;
            setChanged();
            invalidateCapabilities();
        }
    }

    @Nullable
    private SepticTankControllerBlockEntity controller() {
        if (level == null || controllerPos == null
                || !(level.getBlockEntity(controllerPos) instanceof SepticTankControllerBlockEntity controller)
                || !controller.isStructureValid()) return null;
        return controller;
    }

    @Nullable public IFluidHandler getFluidHandler(@Nullable Direction side) {
        return portType == SepticTankPortType.LIQUID_INPUT && allowsCapabilityFrom(side) ? fluidHandler : null;
    }

    @Nullable public IGasHandler getGasHandler(@Nullable Direction side) {
        return portType == SepticTankPortType.GAS_OUTPUT && allowsCapabilityFrom(side) ? gasHandler : null;
    }

    @Override protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        if (controllerPos != null) tag.putLong("Controller", controllerPos.asLong());
    }

    @Override protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        controllerPos = tag.contains("Controller") ? BlockPos.of(tag.getLong("Controller")) : null;
    }

    private class PortFluidHandler implements IFluidHandler {
        @Override public int getTanks() { return 1; }
        @Override public @NotNull FluidStack getFluidInTank(int tank) {
            SepticTankControllerBlockEntity controller = controller();
            return controller == null || tank != 0 ? FluidStack.EMPTY : controller.liquidTank.getFluid();
        }
        @Override public int getTankCapacity(int tank) { return tank == 0 ? SepticTankControllerBlockEntity.TANK_CAPACITY : 0; }
        @Override public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            SepticTankControllerBlockEntity controller = controller();
            return controller != null && tank == 0 && SepticTankControllerBlockEntity.isAllowedLiquid(stack);
        }
        @Override public int fill(FluidStack stack, FluidAction action) {
            SepticTankControllerBlockEntity controller = controller();
            return controller == null ? 0 : controller.liquidTank.fill(stack, action);
        }
        @Override public @NotNull FluidStack drain(FluidStack stack, FluidAction action) { return FluidStack.EMPTY; }
        @Override public @NotNull FluidStack drain(int amount, FluidAction action) { return FluidStack.EMPTY; }
    }

    private class PortGasHandler implements IGasHandler {
        @Override public int getGasTanks() { return 1; }
        @Override public GasStack getGasInTank(int tank) {
            SepticTankControllerBlockEntity controller = controller();
            return controller == null || tank != 0 ? GasStack.EMPTY : controller.gasTank.getStack();
        }
        @Override public void setGasInTank(int tank, GasStack stack) { }
        @Override public long getGasTankCapacity(int tank) { return tank == 0 ? SepticTankControllerBlockEntity.TANK_CAPACITY : 0; }
        @Override public boolean isValid(int tank, GasStack stack) {
            SepticTankControllerBlockEntity controller = controller();
            return controller != null && tank == 0 && controller.gasTank.isValid(stack);
        }
        @Override public GasStack insertGas(int tank, GasStack stack, GasAction action) {
            SepticTankControllerBlockEntity controller = controller();
            return controller == null || tank != 0 ? stack : controller.gasTank.insert(stack, action);
        }
        @Override public GasStack extractGas(int tank, long amount, GasAction action) {
            SepticTankControllerBlockEntity controller = controller();
            return controller == null || tank != 0 ? GasStack.EMPTY : controller.gasTank.extract(amount, action);
        }
    }
}
