package cn.tea.toilet.technology.block.biogaspond;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.gas.GasAction;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.gas.IGasHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BiogasPondPortBlockEntity extends BlockEntity {
    private final BiogasPondPortType portType;
    @Nullable private BlockPos controllerPos;
    private final IItemHandler itemHandler = new PortItemHandler();
    private final IFluidHandler fluidHandler = new PortFluidHandler();
    private final IGasHandler gasHandler = new PortGasHandler();

    public BiogasPondPortBlockEntity(BlockPos pos, BlockState state, BiogasPondPortType portType) {
        super(ModBlockEntities.BIOGAS_POND_PORT.get(), pos, state);
        this.portType = portType;
    }

    public BiogasPondPortType getPortType() {
        return portType;
    }

    public boolean allowsCapabilityFrom(@Nullable Direction side) {
        Direction facing = getBlockState().getValue(BiogasPondPortBlock.FACING);
        return side != null && portType.allowsCapabilityFrom(side == Direction.UP, side == facing);
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
    private BiogasPondControllerBlockEntity controller() {
        if (level == null || controllerPos == null || !(level.getBlockEntity(controllerPos) instanceof BiogasPondControllerBlockEntity controller)
                || !controller.isStructureValid()) {
            return null;
        }
        return controller;
    }

    @Nullable
    public IItemHandler getItemHandler(@Nullable Direction side) {
        return portType != BiogasPondPortType.FLUID_INPUT && portType != BiogasPondPortType.GAS_OUTPUT && allowsCapabilityFrom(side) ? itemHandler : null;
    }

    @Nullable
    public IFluidHandler getFluidHandler(@Nullable Direction side) {
        return portType == BiogasPondPortType.FLUID_INPUT && allowsCapabilityFrom(side) ? fluidHandler : null;
    }

    @Nullable
    public IGasHandler getGasHandler(@Nullable Direction side) {
        return portType == BiogasPondPortType.GAS_OUTPUT && allowsCapabilityFrom(side) ? gasHandler : null;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        if (controllerPos != null) tag.putLong("Controller", controllerPos.asLong());
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        controllerPos = tag.contains("Controller") ? BlockPos.of(tag.getLong("Controller")) : null;
    }

    private class PortItemHandler implements IItemHandler {
        @Override public int getSlots() { return 1; }
        @Override public @NotNull ItemStack getStackInSlot(int slot) {
            BiogasPondControllerBlockEntity controller = controller();
            if (controller == null || slot != 0) return ItemStack.EMPTY;
            int controllerSlot = portType == BiogasPondPortType.ITEM_INPUT
                    ? BiogasPondControllerBlockEntity.INPUT_SLOT : BiogasPondControllerBlockEntity.OUTPUT_SLOT;
            return controller.items.getStackInSlot(controllerSlot);
        }
        @Override public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            BiogasPondControllerBlockEntity controller = controller();
            return controller != null && slot == 0 && portType == BiogasPondPortType.ITEM_INPUT
                    ? controller.items.insertItem(BiogasPondControllerBlockEntity.INPUT_SLOT, stack, simulate) : stack;
        }
        @Override public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            BiogasPondControllerBlockEntity controller = controller();
            return controller != null && slot == 0 && portType == BiogasPondPortType.ITEM_OUTPUT
                    ? controller.items.extractItem(BiogasPondControllerBlockEntity.OUTPUT_SLOT, amount, simulate) : ItemStack.EMPTY;
        }
        @Override public int getSlotLimit(int slot) { return 64; }
        @Override public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return controller() != null && slot == 0 && portType == BiogasPondPortType.ITEM_INPUT;
        }
    }

    private class PortFluidHandler implements IFluidHandler {
        @Override public int getTanks() { return 1; }
        @Override public @NotNull FluidStack getFluidInTank(int tank) {
            BiogasPondControllerBlockEntity controller = controller();
            return controller == null || tank != 0 ? FluidStack.EMPTY : controller.liquidTank.getFluid();
        }
        @Override public int getTankCapacity(int tank) { return tank == 0 ? BiogasPondControllerBlockEntity.LIQUID_CAPACITY : 0; }
        @Override public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
            return controller() != null && tank == 0 && BiogasPondControllerBlockEntity.isFecesLiquid(stack);
        }
        @Override public int fill(FluidStack stack, FluidAction action) {
            BiogasPondControllerBlockEntity controller = controller();
            return controller == null ? 0 : controller.liquidTank.fill(stack, action);
        }
        @Override public @NotNull FluidStack drain(FluidStack stack, FluidAction action) { return FluidStack.EMPTY; }
        @Override public @NotNull FluidStack drain(int amount, FluidAction action) { return FluidStack.EMPTY; }
    }

    private class PortGasHandler implements IGasHandler {
        @Override public int getGasTanks() { return 1; }
        @Override public GasStack getGasInTank(int tank) {
            BiogasPondControllerBlockEntity controller = controller();
            return controller == null || tank != 0 ? GasStack.EMPTY : controller.gasTank.getStack();
        }
        @Override public void setGasInTank(int tank, GasStack stack) { }
        @Override public long getGasTankCapacity(int tank) { return tank == 0 ? BiogasPondControllerBlockEntity.GAS_CAPACITY : 0; }
        @Override public boolean isValid(int tank, GasStack stack) {
            BiogasPondControllerBlockEntity controller = controller();
            return controller != null && tank == 0 && controller.gasTank.isValid(stack);
        }
        @Override public GasStack insertGas(int tank, GasStack stack, GasAction action) {
            BiogasPondControllerBlockEntity controller = controller();
            return controller == null || tank != 0 ? stack : controller.gasTank.insert(stack, action);
        }
        @Override public GasStack extractGas(int tank, long amount, GasAction action) {
            BiogasPondControllerBlockEntity controller = controller();
            return controller == null || tank != 0 ? GasStack.EMPTY : controller.gasTank.extract(amount, action);
        }
    }
}
