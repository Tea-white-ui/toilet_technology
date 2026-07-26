package cn.tea.toilet.technology.block.biogaspond;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.block.TankInteriorLighting;
import cn.tea.toilet.technology.block.biogasgenerator.BiogasGeneratorBlockEntity;
import cn.tea.toilet.technology.compat.PoopSkyCompat;
import cn.tea.toilet.technology.gas.BasicGasTank;
import cn.tea.toilet.technology.gas.GasAction;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.gas.IGasHandler;
import cn.tea.toilet.technology.item.ModItems;

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

import java.util.ArrayList;
import java.util.List;

public class BiogasPondControllerBlockEntity extends BlockEntity {
    public static final int GENERATOR_ENERGY_PER_BATCH = 128;
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
    public final FluidTank liquidTank = new FluidTank(LIQUID_CAPACITY, BiogasPondControllerBlockEntity::isFecesLiquid) {
        @Override protected void onContentsChanged() { setChanged(); }
    };
    public final BasicGasTank gasTank = new BasicGasTank(
            GAS_CAPACITY, stack -> stack.is(GasRegistry.BIOGAS), this::setChanged);
    private final IItemHandler automationItems = new GatedItemHandler();
    private final IFluidHandler automationFluids = new GatedFluidHandler();
    private final IGasHandler automationGases = new GatedGasHandler();

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
        if (valid) {
            TankInteriorLighting.apply(level, BiogasPondStructure.interiorPositions(worldPosition));
        } else {
            TankInteriorLighting.clear(level, BiogasPondStructure.interiorPositions(worldPosition));
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
    public void clearInteriorLighting() {
        if (level != null && !level.isClientSide()) {
            TankInteriorLighting.clear(level, BiogasPondStructure.interiorPositions(worldPosition));
        }
    }
    public static boolean isFecesLiquid(net.neoforged.neoforge.fluids.FluidStack stack) {
        return PoopSkyCompat.isFecesLiquid(stack);
    }
    public IItemHandler getMenuItems() { return automationItems; }
    public IItemHandler getAutomationItems() { return automationItems; }
    public IFluidHandler getAutomationFluids() { return automationFluids; }
    public IGasHandler getAutomationGases() { return automationGases; }

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
        boolean containsFecesLiquid = isFecesLiquid(liquid);
        if (!containsFecesLiquid || liquid.isEmpty()) {
            biogasProductionTicks = 0;
            return;
        }

        biogasProductionTicks = BiogasPondBiogasProduction.nextProgress(biogasProductionTicks, true);
        if (biogasProductionTicks < BiogasPondBiogasProduction.INTERVAL_TICKS) return;
        biogasProductionTicks = 0;

        List<BiogasGeneratorBlockEntity> generators = findBiogasGenerators();
        boolean generatorsCanSupplyEnergy = !generators.isEmpty()
                && generators.stream().allMatch(generator ->
                generator.energyStorage.getEnergyStored() >= GENERATOR_ENERGY_PER_BATCH);
        int gasMultiplier = generators.isEmpty() ? 1 : generators.size() * 2;
        BiogasPondBiogasProduction.Batch batch = BiogasPondBiogasProduction.planBatch(
                true, liquid.getAmount(), gasTank.getStored(), gasTank.getCapacity(), gasMultiplier,
                generatorsCanSupplyEnergy);
        if (batch.gasProduced() == 0) return;

        if (generatorsCanSupplyEnergy) {
            for (BiogasGeneratorBlockEntity generator : generators) {
                if (!generator.consumeEnergy(GENERATOR_ENERGY_PER_BATCH)) return;
            }
        }

        GasStack remainder = gasTank.insert(new GasStack(GasRegistry.BIOGAS, batch.gasProduced()), GasAction.EXECUTE);
        if (!remainder.isEmpty()) return;
        if (batch.liquidConsumed() > 0) liquidTank.drain(batch.liquidConsumed(), IFluidHandler.FluidAction.EXECUTE);
        if (BiogasPondBiogasProduction.shouldProduceResidue(level.random.nextInt(100))) {
            machineInsertResidue();
        }
        setChanged();
    }

    private void machineInsertResidue() {
        ItemStack residue = new ItemStack(ModItems.BIOGAS_RESIDUE.get());
        ItemStack existing = items.getStackInSlot(OUTPUT_SLOT);
        if (existing.isEmpty()) {
            items.setStackInSlot(OUTPUT_SLOT, residue);
        } else if (ItemStack.isSameItemSameComponents(existing, residue)
                && existing.getCount() < Math.min(existing.getMaxStackSize(), items.getSlotLimit(OUTPUT_SLOT))) {
            ItemStack combined = existing.copy();
            combined.grow(1);
            items.setStackInSlot(OUTPUT_SLOT, combined);
        }
    }

    private List<BiogasGeneratorBlockEntity> findBiogasGenerators() {
        List<BiogasGeneratorBlockEntity> generators = new ArrayList<>();
        for (BiogasPondPattern.Cell cell : BiogasPondPattern.layout().walls()) {
            BlockPos generatorPos = worldPosition.offset(cell.x(), cell.y(), cell.z());
            if (level.getBlockState(generatorPos).is(ModBlocks.BIOGAS_GENERATOR.get())
                    && level.getBlockEntity(generatorPos) instanceof BiogasGeneratorBlockEntity generator) {
                generators.add(generator);
            }
        }
        return generators;
    }

    @Override protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Items", items.serializeNBT(registries));
        tag.put("LiquidTank", liquidTank.writeToNBT(registries, new CompoundTag()));
        tag.put("GasTank", gasTank.serializeNBT());
        tag.putInt("BiogasProductionTicks", biogasProductionTicks);
    }

    @Override protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Items")) items.deserializeNBT(registries, tag.getCompound("Items"));
        if (tag.contains("LiquidTank")) liquidTank.readFromNBT(registries, tag.getCompound("LiquidTank"));
        if (tag.contains("GasTank")) gasTank.deserializeNBT(tag.getCompound("GasTank"));
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
        @Override public boolean isFluidValid(int tank, @NotNull net.neoforged.neoforge.fluids.FluidStack stack) {
            return tank == 0 && structureValid && isFecesLiquid(stack);
        }
        @Override public int fill(net.neoforged.neoforge.fluids.FluidStack stack, FluidAction action) { return structureValid ? liquidTank.fill(stack, action) : 0; }
        @Override public @NotNull net.neoforged.neoforge.fluids.FluidStack drain(net.neoforged.neoforge.fluids.FluidStack stack, FluidAction action) { return structureValid ? liquidTank.drain(stack, action) : net.neoforged.neoforge.fluids.FluidStack.EMPTY; }
        @Override public @NotNull net.neoforged.neoforge.fluids.FluidStack drain(int amount, FluidAction action) { return structureValid ? liquidTank.drain(amount, action) : net.neoforged.neoforge.fluids.FluidStack.EMPTY; }
    }

    private class GatedGasHandler implements IGasHandler {
        @Override public int getGasTanks() { return 1; }
        @Override public GasStack getGasInTank(int tank) { return tank == 0 ? gasTank.getStack() : GasStack.EMPTY; }
        @Override public void setGasInTank(int tank, GasStack stack) { }
        @Override public long getGasTankCapacity(int tank) { return tank == 0 ? GAS_CAPACITY : 0; }
        @Override public boolean isValid(int tank, GasStack stack) { return tank == 0 && structureValid && gasTank.isValid(stack); }
        @Override public GasStack insertGas(int tank, GasStack stack, GasAction action) { return tank == 0 && structureValid ? gasTank.insert(stack, action) : stack; }
        @Override public GasStack extractGas(int tank, long amount, GasAction action) { return tank == 0 && structureValid ? gasTank.extract(amount, action) : GasStack.EMPTY; }
    }
}
