package cn.tea.toilet.technology.block.septictank;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.block.TankInteriorLighting;
import cn.tea.toilet.technology.block.multiblock.MultiblockPortBinder;
import cn.tea.toilet.technology.block.multiblock.MultiblockValidationState;
import cn.tea.toilet.technology.block.multiblock.StructureGatedGasHandler;
import cn.tea.toilet.technology.block.septictank.SepticTankBiogasProduction;
import cn.tea.toilet.technology.compat.PoopSkyCompat;
import cn.tea.toilet.technology.api.gas.BasicGasTank;
import cn.tea.toilet.technology.api.gas.GasAction;

import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.api.gas.GasStack;
import cn.tea.toilet.technology.api.gas.IGasHandler;
import cn.tea.toilet.technology.fluid.ModFluids;
import cn.tea.toilet.technology.item.ModItems;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class SepticTankControllerBlockEntity extends BlockEntity {
    public static final int TANK_CAPACITY = 64_000;
    private static final int DATA_VERSION = 2;
    public static final int ITEM_INPUT_START = SepticTankInventoryLayout.ITEM_INPUT_START;
    public static final int ITEM_OUTPUT_START = SepticTankInventoryLayout.ITEM_OUTPUT_START;
    public static final int CONTAINER_INPUT = SepticTankInventoryLayout.CONTAINER_INPUT;
    public static final int CONTAINER_OUTPUT = SepticTankInventoryLayout.CONTAINER_OUTPUT;
    public static final int SLOT_COUNT = SepticTankInventoryLayout.SLOT_COUNT;

    private final MultiblockValidationState structureState = new MultiblockValidationState(20);
    private int biogasProductionTicks;

    public final ItemStackHandler items = new ItemStackHandler(SLOT_COUNT) {
        @Override public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return SepticTankInventoryLayout.isItemInput(slot)
                    || slot == CONTAINER_INPUT && isFilledFluidContainer(stack);
        }
        @Override protected void onContentsChanged(int slot) { setChanged(); }
    };

    public final FluidTank liquidTank = changedTank();
    /** Mod-owned gas storage; the optional Mekanism bridge exposes it as a chemical capability. */
    public final BasicGasTank gasTank = new BasicGasTank(
            TANK_CAPACITY,
            stack -> stack.is(GasRegistry.BIOGAS),
            this::setChanged
    );
    private final IItemHandler automationItems = new AutomationItemHandler();
    private final IItemHandler menuItems = new MenuItemHandler();
    private final IFluidHandler automationFluids = new StructureGatedFluidHandler(liquidTank, this::isStructureValid);
    private final IGasHandler automationGases = new StructureGatedGasHandler(new ControllerGasHandler(), this::isStructureValid);

    public SepticTankControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SEPTIC_TANK_CONTROLLER.get(), pos, state);
    }

    private FluidTank changedTank() {
        return new FluidTank(TANK_CAPACITY, SepticTankControllerBlockEntity::isAllowedLiquid) {
            @Override protected void onContentsChanged() { setChanged(); }
        };
    }

    public static boolean isAllowedLiquid(FluidStack stack) {
        return stack.is(Fluids.WATER)
                || stack.is(Fluids.FLOWING_WATER)
                || PoopSkyCompat.isFecesLiquid(stack);
    }

    public boolean revalidateStructure() {
        if (level == null) return false;
        BlockState state = getBlockState();
        boolean valid = state.hasProperty(SepticTankControllerBlock.FACING)
                && SepticTankStructure.validate(level, worldPosition, state.getValue(SepticTankControllerBlock.FACING));
        if (structureState.updateValidity(valid)) {
            setChanged();
            level.invalidateCapabilities(worldPosition);
        }
        if (valid) {
            TankInteriorLighting.apply(level, SepticTankStructure.interiorPositions(
                    worldPosition, state.getValue(SepticTankControllerBlock.FACING)));
        } else {
            TankInteriorLighting.clear(level, SepticTankStructure.interiorPositions(
                    worldPosition, state.getValue(SepticTankControllerBlock.FACING)));
        }
        synchronizePorts(valid);
        return valid;
    }

    private void synchronizePorts(boolean valid) {
        SepticTankPattern.Facing facing = switch (getBlockState().getValue(SepticTankControllerBlock.FACING)) {
            case SOUTH -> SepticTankPattern.Facing.SOUTH;
            case WEST -> SepticTankPattern.Facing.WEST;
            case EAST -> SepticTankPattern.Facing.EAST;
            default -> SepticTankPattern.Facing.NORTH;
        };
        MultiblockPortBinder.synchronize(valid,
                SepticTankPattern.layout(facing).walls().stream()
                        .map(cell -> worldPosition.offset(cell.x(), cell.y(), cell.z())).toList(),
                portPos -> level.getBlockEntity(portPos) instanceof SepticTankPortBlockEntity port ? port : null,
                port -> port.bindController(worldPosition),
                SepticTankPortBlockEntity::unbindController);
    }

    public boolean isStructureValid() { return structureState.isValid(); }
    public void clearInteriorLighting() {
        if (level != null && !level.isClientSide()) {
            TankInteriorLighting.clear(level, SepticTankStructure.interiorPositions(
                    worldPosition, getBlockState().getValue(SepticTankControllerBlock.FACING)));
        }
    }
    public IItemHandler getAutomationItems() { return automationItems; }
    public IItemHandler getMenuItems() { return menuItems; }
    public IFluidHandler getAutomationFluids() { return automationFluids; }
    public IGasHandler getAutomationGases() { return automationGases; }

    private static boolean isFilledFluidContainer(ItemStack stack) {
        var contained = FluidUtil.getFluidContained(stack);
        if (contained.isEmpty() || !isAllowedLiquid(contained.get())) return false;
        return FluidUtil.getFluidHandler(stack)
                .map(handler -> handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE))
                .filter(SepticTankControllerBlockEntity::isAllowedLiquid)
                .isPresent();
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SepticTankControllerBlockEntity entity) {
        if (level.isClientSide()) return;
        if (entity.structureState.tickAndShouldValidate()) entity.revalidateStructure();
        if (entity.isStructureValid()) {
            entity.processFluidContainer();
            entity.convertWaterWithFeces();
            entity.produceBiogas();
            entity.pushPortOutputs();
        } else {
            entity.biogasProductionTicks = SepticTankBiogasProduction.nextProgress(
                    entity.biogasProductionTicks, false);
        }
    }

    private void pushPortOutputs() {
        SepticTankPattern.Facing facing = switch (getBlockState().getValue(SepticTankControllerBlock.FACING)) {
            case SOUTH -> SepticTankPattern.Facing.SOUTH;
            case WEST -> SepticTankPattern.Facing.WEST;
            case EAST -> SepticTankPattern.Facing.EAST;
            default -> SepticTankPattern.Facing.NORTH;
        };
        for (SepticTankPattern.Cell cell : SepticTankPattern.layout(facing).walls()) {
            if (level.getBlockEntity(worldPosition.offset(cell.x(), cell.y(), cell.z())) instanceof SepticTankPortBlockEntity port) {
                port.pushOutput();
            }
        }
    }

    private void produceBiogas() {
        FluidStack liquid = liquidTank.getFluid();
        boolean containsFecesLiquid = PoopSkyCompat.isFecesLiquid(liquid);
        if (!containsFecesLiquid || liquid.isEmpty()) {
            biogasProductionTicks = 0;
            return;
        }
        biogasProductionTicks = SepticTankBiogasProduction.nextProgress(biogasProductionTicks, true);
        if (biogasProductionTicks < SepticTankBiogasProduction.INTERVAL_TICKS) return;
        biogasProductionTicks = 0;

        SepticTankBiogasProduction.Batch batch = SepticTankBiogasProduction.planBatch(
                true, liquid.getAmount(), gasTank.getStored(), gasTank.getCapacity());
        if (batch.gasProduced() == 0) return;

        GasStack remainder = gasTank.insert(
                new GasStack(GasRegistry.BIOGAS, batch.gasProduced()), GasAction.EXECUTE);
        if (!remainder.isEmpty()) return;
        if (batch.liquidConsumed() > 0) {
            liquidTank.drain(batch.liquidConsumed(), IFluidHandler.FluidAction.EXECUTE);
        }
        if (SepticTankBiogasProduction.shouldProduceResidue(level.random.nextInt(100))) {
            machineInsertResidue();
        }
        setChanged();
    }

    private void convertWaterWithFeces() {
        FluidStack fluid = liquidTank.getFluid();
        boolean containsWater = fluid.is(Fluids.WATER) || fluid.is(Fluids.FLOWING_WATER);
        if (!containsWater || fluid.isEmpty()) return;
        for (int slot = ITEM_INPUT_START; slot < SepticTankInventoryLayout.ITEM_INPUT_END; slot++) {
            if (PoopSkyCompat.isFecesItem(items.getStackInSlot(slot))) {
                liquidTank.setFluid(new FluidStack(ModFluids.FECES_LIQUID.get(), fluid.getAmount()));
                items.extractItem(slot, 1, false);
                setChanged();
                return;
            }
        }
    }

    private void processFluidContainer() {
        ItemStack input = items.getStackInSlot(CONTAINER_INPUT);
        if (input.isEmpty()) return;
        ItemStack singleContainer = input.copyWithCount(1);
        var simulated = FluidUtil.tryEmptyContainer(singleContainer, liquidTank, TANK_CAPACITY, null, false);
        if (!simulated.isSuccess() || !canMachineInsertOutput(CONTAINER_OUTPUT, simulated.getResult())) return;

        var executed = FluidUtil.tryEmptyContainer(singleContainer, liquidTank, TANK_CAPACITY, null, true);
        if (!executed.isSuccess()) return;
        items.extractItem(CONTAINER_INPUT, 1, false);
        machineInsertOutput(CONTAINER_OUTPUT, executed.getResult());
        setChanged();
    }

    private boolean canMachineInsertOutput(int slot, ItemStack stack) {
        if (!SepticTankSlotPolicy.isMachineOutput(slot)) return false;
        ItemStack existing = items.getStackInSlot(slot);
        if (existing.isEmpty()) return stack.getCount() <= items.getSlotLimit(slot);
        return ItemStack.isSameItemSameComponents(existing, stack)
                && existing.getCount() + stack.getCount() <= Math.min(existing.getMaxStackSize(), items.getSlotLimit(slot));
    }

    private void machineInsertOutput(int slot, ItemStack stack) {
        ItemStack existing = items.getStackInSlot(slot);
        if (existing.isEmpty()) {
            items.setStackInSlot(slot, stack.copy());
        } else {
            ItemStack combined = existing.copy();
            combined.grow(stack.getCount());
            items.setStackInSlot(slot, combined);
        }
    }

    private void machineInsertResidue() {
        ItemStack residue = new ItemStack(ModItems.BIOGAS_RESIDUE.get());
        for (int slot = ITEM_OUTPUT_START; slot < SepticTankInventoryLayout.ITEM_OUTPUT_END; slot++) {
            if (canMachineInsertOutput(slot, residue)) {
                machineInsertOutput(slot, residue);
                return;
            }
        }
    }

    @Override protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.putInt("DataVersion", DATA_VERSION);
        tag.put("Items", items.serializeNBT(registries));
        tag.put("LiquidTank", liquidTank.writeToNBT(registries, new CompoundTag()));
        tag.put("GasTank", gasTank.serializeNBT());
        tag.putInt("BiogasProductionTicks", biogasProductionTicks);
    }

    @Override protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Items")) {
            items.deserializeNBT(registries, normalizedInventoryTag(tag.getCompound("Items")));
        }
        if (tag.contains("LiquidTank")) {
            liquidTank.readFromNBT(registries, tag.getCompound("LiquidTank"));
            if (!liquidTank.isEmpty() && !isAllowedLiquid(liquidTank.getFluid())) {
                liquidTank.setFluid(FluidStack.EMPTY);
            }
        }
        if (tag.contains("GasTank")) loadGasTank(tag, registries);
        biogasProductionTicks = Math.max(0, Math.min(
                tag.getInt("BiogasProductionTicks"), SepticTankBiogasProduction.INTERVAL_TICKS - 1));
        structureState.reset();
    }

    private static CompoundTag normalizedInventoryTag(CompoundTag savedItems) {
        CompoundTag normalized = new CompoundTag();
        normalized.putInt("Size", SLOT_COUNT);
        ListTag validItems = new ListTag();
        ListTag serializedItems = savedItems.getList("Items", Tag.TAG_COMPOUND);
        for (int index = 0; index < serializedItems.size(); index++) {
            CompoundTag serializedStack = serializedItems.getCompound(index);
            int slot = serializedStack.getInt("Slot");
            if (slot >= 0 && slot < SLOT_COUNT) validItems.add(serializedStack.copy());
        }
        normalized.put("Items", validItems);
        return normalized;
    }

    private void loadGasTank(CompoundTag ownerTag, HolderLookup.Provider registries) {
        CompoundTag tankTag = ownerTag.getCompound("GasTank");
        if (ownerTag.getInt("DataVersion") < DATA_VERSION && tankTag.contains("Fluid")) {
            FluidTank legacyTank = new FluidTank(TANK_CAPACITY);
            legacyTank.readFromNBT(registries, tankTag);
            if (!legacyTank.isEmpty()) {
                gasTank.setStack(new GasStack(GasRegistry.BIOGAS,
                        Math.min(legacyTank.getFluidAmount(), TANK_CAPACITY)));
            }
            return;
        }
        gasTank.deserializeNBT(tankTag);
    }



    private class AutomationItemHandler implements IItemHandler {
        @Override public int getSlots() { return SLOT_COUNT; }
        @Override public @NotNull ItemStack getStackInSlot(int slot) { return items.getStackInSlot(slot); }
        @Override public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return isStructureValid() && SepticTankSlotPolicy.allowsExternalInsertion(slot) ? items.insertItem(slot, stack, simulate) : stack;
        }
        @Override public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return isStructureValid() && SepticTankInventoryLayout.allowsExternalExtraction(slot)
                    ? items.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
        @Override public int getSlotLimit(int slot) { return items.getSlotLimit(slot); }
        @Override public boolean isItemValid(int slot, @NotNull ItemStack stack) { return items.isItemValid(slot, stack); }
    }

    private class MenuItemHandler implements IItemHandlerModifiable {
        @Override public int getSlots() { return SLOT_COUNT; }
        @Override public @NotNull ItemStack getStackInSlot(int slot) { return items.getStackInSlot(slot); }
        @Override public void setStackInSlot(int slot, @NotNull ItemStack stack) {
            if (isStructureValid() && (stack.isEmpty() || items.isItemValid(slot, stack))) {
                items.setStackInSlot(slot, stack);
            }
        }
        @Override public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return isStructureValid() ? items.insertItem(slot, stack, simulate) : stack;
        }
        @Override public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return isStructureValid() && SepticTankInventoryLayout.allowsMenuExtraction(slot)
                    ? items.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
        @Override public int getSlotLimit(int slot) { return items.getSlotLimit(slot); }
        @Override public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return isStructureValid() && items.isItemValid(slot, stack);
        }
    }

    private class ControllerGasHandler implements IGasHandler {
        @Override public int getGasTanks() { return 1; }
        @Override public GasStack getGasInTank(int tank) {
            return tank == 0 ? gasTank.getStack() : GasStack.EMPTY;
        }
        @Override public void setGasInTank(int tank, GasStack stack) {
            // Direct replacement is not an automation operation; reject it without crashing callers.
        }
        @Override public long getGasTankCapacity(int tank) {
            return tank == 0 ? gasTank.getCapacity() : 0;
        }
        @Override public boolean isValid(int tank, GasStack stack) {
            return tank == 0 && gasTank.isValid(stack);
        }
        @Override public GasStack insertGas(int tank, GasStack stack, GasAction action) {
            return tank == 0 ? gasTank.insert(stack, action) : stack;
        }
        @Override public GasStack extractGas(int tank, long amount, GasAction action) {
            return tank == 0 ? gasTank.extract(amount, action) : GasStack.EMPTY;
        }
    }
}
