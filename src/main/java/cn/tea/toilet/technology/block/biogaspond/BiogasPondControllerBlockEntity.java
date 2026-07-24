package cn.tea.toilet.technology.block.biogaspond;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.chemical.ModChemicals;
import cn.tea.toilet.technology.fluid.ModFluids;
import mekanism.api.Action;
import mekanism.api.AutomationType;
import mekanism.api.chemical.BasicChemicalTank;
import mekanism.api.chemical.ChemicalStack;
import mekanism.api.chemical.IChemicalHandler;
import mekanism.api.chemical.IChemicalTank;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class BiogasPondControllerBlockEntity extends BlockEntity {
    public static final int GAS_CAPACITY = 128_000;
    public static final int LIQUID_CAPACITY = 64_000;
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;
    public static final int SLOT_COUNT = 2;

    private boolean structureValid;
    private int validationCooldown;
    private int biogasProductionTicks;
    public final ItemStackHandler items = new ItemStackHandler(SLOT_COUNT) {
        @Override public boolean isItemValid(int slot, @NotNull ItemStack stack) { return slot == INPUT_SLOT; }
        @Override protected void onContentsChanged(int slot) { setChanged(); }
    };
    public final FluidTank liquidTank = new FluidTank(LIQUID_CAPACITY) {
        @Override protected void onContentsChanged() { setChanged(); }
    };
    public final IChemicalTank gasTank = BasicChemicalTank.createModern(
            GAS_CAPACITY, stack -> stack.is(ModChemicals.BIOGAS.get()), this::setChanged);
    private final IItemHandler automationItems = new GatedItemHandler();
    private final IFluidHandler automationFluids = new GatedFluidHandler();
    private final IChemicalHandler automationChemicals = new GatedChemicalHandler();

    public BiogasPondControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.BIOGAS_POND_CONTROLLER.get(), pos, state);
    }

    public boolean revalidateStructure() {
        if (level == null) return false;
        boolean valid = BiogasPondStructure.validate(level, worldPosition);
        if (structureValid != valid) {
            structureValid = valid;
            setChanged();
            level.invalidateCapabilities(worldPosition);
        }
        synchronizePorts(valid);
        validationCooldown = 20;
        return valid;
    }

    private void synchronizePorts(boolean valid) {
        for (BiogasPondPattern.Cell cell : BiogasPondPattern.layout().walls()) {
            BlockPos portPos = worldPosition.offset(cell.x(), cell.y(), cell.z());
            if (level.getBlockEntity(portPos) instanceof BiogasPondPortBlockEntity port) {
                if (valid) port.bindController(worldPosition);
                else port.unbindController();
            }
        }
    }

    public boolean isStructureValid() { return structureValid; }
    public IItemHandler getMenuItems() { return automationItems; }
    public IItemHandler getAutomationItems() { return automationItems; }
    public IFluidHandler getAutomationFluids() { return automationFluids; }
    public IChemicalHandler getAutomationChemicals() { return automationChemicals; }

    public static void tick(Level level, BlockPos pos, BlockState state, BiogasPondControllerBlockEntity entity) {
        if (level.isClientSide()) return;
        if (--entity.validationCooldown <= 0) entity.revalidateStructure();
        if (entity.structureValid) {
            entity.produceBiogas();
        } else {
            entity.biogasProductionTicks = BiogasPondBiogasProduction.nextProgress(
                    entity.biogasProductionTicks, false);
        }
    }

    private void produceBiogas() {
        net.neoforged.neoforge.fluids.FluidStack liquid = liquidTank.getFluid();
        boolean containsFecesLiquid = liquid.is(ModFluids.FECES_LIQUID.get())
                || liquid.is(ModFluids.FECES_LIQUID_FLOWING.get());
        if (!containsFecesLiquid || liquid.isEmpty()) {
            biogasProductionTicks = 0;
            return;
        }

        biogasProductionTicks = BiogasPondBiogasProduction.nextProgress(biogasProductionTicks, true);
        if (biogasProductionTicks < BiogasPondBiogasProduction.INTERVAL_TICKS) return;
        biogasProductionTicks = 0;

        BiogasPondBiogasProduction.Batch batch = BiogasPondBiogasProduction.planBatch(
                true, liquid.getAmount(), gasTank.getStored(), gasTank.getCapacity());
        if (batch.gasProduced() == 0) return;

        ChemicalStack remainder = gasTank.insert(new ChemicalStack(ModChemicals.BIOGAS, batch.gasProduced()),
                Action.EXECUTE, AutomationType.INTERNAL);
        if (!remainder.isEmpty()) return;
        if (batch.liquidConsumed() > 0) liquidTank.drain(batch.liquidConsumed(), IFluidHandler.FluidAction.EXECUTE);
        setChanged();
    }

    @Override protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Items", items.serializeNBT(registries));
        tag.put("LiquidTank", liquidTank.writeToNBT(registries, new CompoundTag()));
        tag.put("GasTank", gasTank.serializeNBT(registries));
        tag.putInt("BiogasProductionTicks", biogasProductionTicks);
    }

    @Override protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Items")) items.deserializeNBT(registries, tag.getCompound("Items"));
        if (tag.contains("LiquidTank")) liquidTank.readFromNBT(registries, tag.getCompound("LiquidTank"));
        if (tag.contains("GasTank")) gasTank.deserializeNBT(registries, tag.getCompound("GasTank"));
        biogasProductionTicks = Math.max(0, Math.min(
                tag.getInt("BiogasProductionTicks"), BiogasPondBiogasProduction.INTERVAL_TICKS - 1));
        structureValid = false;
        validationCooldown = 1;
    }

    private class GatedItemHandler implements IItemHandlerModifiable {
        @Override public int getSlots() { return SLOT_COUNT; }
        @Override public @NotNull ItemStack getStackInSlot(int slot) { return items.getStackInSlot(slot); }
        @Override public void setStackInSlot(int slot, @NotNull ItemStack stack) {
            if (structureValid && (stack.isEmpty() || items.isItemValid(slot, stack))) items.setStackInSlot(slot, stack);
        }
        @Override public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return structureValid && slot == INPUT_SLOT ? items.insertItem(slot, stack, simulate) : stack;
        }
        @Override public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return structureValid ? items.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
        @Override public int getSlotLimit(int slot) { return items.getSlotLimit(slot); }
        @Override public boolean isItemValid(int slot, @NotNull ItemStack stack) { return structureValid && items.isItemValid(slot, stack); }
    }

    private class GatedFluidHandler implements IFluidHandler {
        @Override public int getTanks() { return 1; }
        @Override public @NotNull net.neoforged.neoforge.fluids.FluidStack getFluidInTank(int tank) { return liquidTank.getFluid(); }
        @Override public int getTankCapacity(int tank) { return LIQUID_CAPACITY; }
        @Override public boolean isFluidValid(int tank, @NotNull net.neoforged.neoforge.fluids.FluidStack stack) { return structureValid; }
        @Override public int fill(net.neoforged.neoforge.fluids.FluidStack stack, FluidAction action) { return structureValid ? liquidTank.fill(stack, action) : 0; }
        @Override public @NotNull net.neoforged.neoforge.fluids.FluidStack drain(net.neoforged.neoforge.fluids.FluidStack stack, FluidAction action) { return structureValid ? liquidTank.drain(stack, action) : net.neoforged.neoforge.fluids.FluidStack.EMPTY; }
        @Override public @NotNull net.neoforged.neoforge.fluids.FluidStack drain(int amount, FluidAction action) { return structureValid ? liquidTank.drain(amount, action) : net.neoforged.neoforge.fluids.FluidStack.EMPTY; }
    }

    private class GatedChemicalHandler implements IChemicalHandler {
        @Override public int getChemicalTanks() { return 1; }
        @Override public ChemicalStack getChemicalInTank(int tank) { return tank == 0 ? gasTank.getStack() : ChemicalStack.EMPTY; }
        @Override public void setChemicalInTank(int tank, ChemicalStack stack) { }
        @Override public long getChemicalTankCapacity(int tank) { return tank == 0 ? GAS_CAPACITY : 0; }
        @Override public boolean isValid(int tank, ChemicalStack stack) { return tank == 0 && structureValid && gasTank.isValid(stack); }
        @Override public ChemicalStack insertChemical(int tank, ChemicalStack stack, Action action) { return tank == 0 && structureValid ? gasTank.insert(stack, action, AutomationType.EXTERNAL) : stack; }
        @Override public ChemicalStack extractChemical(int tank, long amount, Action action) { return tank == 0 && structureValid ? gasTank.extract(amount, action, AutomationType.EXTERNAL) : ChemicalStack.EMPTY; }
    }
}
