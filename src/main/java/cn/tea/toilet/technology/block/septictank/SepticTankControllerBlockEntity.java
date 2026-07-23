package cn.tea.toilet.technology.block.septictank;

import cn.tea.toilet.technology.block.ModBlockEntities;
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
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

public class SepticTankControllerBlockEntity extends BlockEntity {
    public static final int TANK_CAPACITY = 64_000;
    public static final int ITEM_INPUT_START = 0;
    public static final int ITEM_OUTPUT_START = 3;
    public static final int CONTAINER_INPUT = 6;
    public static final int CONTAINER_OUTPUT = 7;
    public static final int SLOT_COUNT = 8;

    private boolean structureValid;
    private int validationCooldown;

    public final ItemStackHandler items = new ItemStackHandler(SLOT_COUNT) {
        @Override public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return slot < ITEM_OUTPUT_START
                    || slot == CONTAINER_INPUT && isFilledFluidContainer(stack);
        }
        @Override protected void onContentsChanged(int slot) { setChanged(); }
    };

    public final FluidTank liquidTank = changedTank();
    /** Native Mekanism chemical storage, exposed through its chemical capability. */
    public final IChemicalTank gasTank = BasicChemicalTank.create(TANK_CAPACITY, this::setChanged);
    private final IItemHandler automationItems = new AutomationItemHandler();
    private final IChemicalHandler automationChemicals = new StructureGatedChemicalHandler();

    public SepticTankControllerBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.SEPTIC_TANK_CONTROLLER.get(), pos, state);
    }

    private FluidTank changedTank() {
        return new FluidTank(TANK_CAPACITY) {
            @Override protected void onContentsChanged() { setChanged(); }
        };
    }

    public boolean revalidateStructure() {
        if (level == null) return false;
        BlockState state = getBlockState();
        boolean valid = state.hasProperty(SepticTankControllerBlock.FACING)
                && SepticTankStructure.validate(level, worldPosition, state.getValue(SepticTankControllerBlock.FACING));
        if (structureValid != valid) {
            structureValid = valid;
            setChanged();
            level.invalidateCapabilities(worldPosition);
        }
        validationCooldown = 20;
        return valid;
    }

    public boolean isStructureValid() { return structureValid; }
    public IItemHandler getAutomationItems() { return automationItems; }
    public IChemicalHandler getAutomationChemicals() { return automationChemicals; }

    private static boolean isFilledFluidContainer(ItemStack stack) {
        return FluidUtil.getFluidHandler(stack)
                .map(handler -> !handler.drain(Integer.MAX_VALUE, IFluidHandler.FluidAction.SIMULATE).isEmpty())
                .orElse(false);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, SepticTankControllerBlockEntity entity) {
        if (level.isClientSide()) return;
        if (--entity.validationCooldown <= 0) entity.revalidateStructure();
        if (entity.structureValid) entity.processFluidContainer();
    }

    private void processFluidContainer() {
        ItemStack input = items.getStackInSlot(CONTAINER_INPUT);
        if (input.isEmpty()) return;
        var simulated = FluidUtil.tryEmptyContainer(input, liquidTank, TANK_CAPACITY, null, false);
        if (!simulated.isSuccess() || !canMachineInsertOutput(CONTAINER_OUTPUT, simulated.getResult())) return;

        var executed = FluidUtil.tryEmptyContainer(input, liquidTank, TANK_CAPACITY, null, true);
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

    @Override protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Items", items.serializeNBT(registries));
        tag.put("LiquidTank", liquidTank.writeToNBT(registries, new CompoundTag()));
        tag.put("GasTank", gasTank.serializeNBT(registries));
    }

    @Override protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("Items")) {
            items.deserializeNBT(registries, tag.getCompound("Items"));
            if (items.getSlots() != SLOT_COUNT) {
                ItemStack[] loaded = new ItemStack[Math.min(items.getSlots(), SLOT_COUNT)];
                for (int slot = 0; slot < loaded.length; slot++) loaded[slot] = items.getStackInSlot(slot).copy();
                items.setSize(SLOT_COUNT);
                for (int slot = 0; slot < loaded.length; slot++) items.setStackInSlot(slot, loaded[slot]);
            }
        }
        if (tag.contains("LiquidTank")) liquidTank.readFromNBT(registries, tag.getCompound("LiquidTank"));
        if (tag.contains("GasTank")) gasTank.deserializeNBT(registries, tag.getCompound("GasTank"));
        structureValid = false;
        validationCooldown = 1;
    }

    private class AutomationItemHandler implements IItemHandler {
        @Override public int getSlots() { return SLOT_COUNT; }
        @Override public @NotNull ItemStack getStackInSlot(int slot) { return items.getStackInSlot(slot); }
        @Override public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return structureValid && SepticTankSlotPolicy.allowsExternalInsertion(slot) ? items.insertItem(slot, stack, simulate) : stack;
        }
        @Override public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return structureValid && (slot >= ITEM_OUTPUT_START && slot < CONTAINER_INPUT || slot == CONTAINER_OUTPUT)
                    ? items.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
        @Override public int getSlotLimit(int slot) { return items.getSlotLimit(slot); }
        @Override public boolean isItemValid(int slot, @NotNull ItemStack stack) { return items.isItemValid(slot, stack); }
    }

    private class StructureGatedChemicalHandler implements IChemicalHandler {
        private boolean allowsTransfer() {
            return SepticTankChemicalAccess.allowsTransfer(structureValid);
        }

        @Override public int getChemicalTanks() { return 1; }
        @Override public ChemicalStack getChemicalInTank(int tank) {
            return tank == 0 ? gasTank.getStack() : ChemicalStack.EMPTY;
        }
        @Override public void setChemicalInTank(int tank, ChemicalStack stack) {
            if (tank == 0 && allowsTransfer()) gasTank.setStack(stack);
        }
        @Override public long getChemicalTankCapacity(int tank) {
            return tank == 0 ? gasTank.getCapacity() : 0;
        }
        @Override public boolean isValid(int tank, ChemicalStack stack) {
            return tank == 0 && allowsTransfer() && gasTank.isValid(stack);
        }
        @Override public ChemicalStack insertChemical(int tank, ChemicalStack stack, Action action) {
            return tank == 0 && allowsTransfer() ? gasTank.insert(stack, action, AutomationType.EXTERNAL) : stack;
        }
        @Override public ChemicalStack extractChemical(int tank, long amount, Action action) {
            return tank == 0 && allowsTransfer() ? gasTank.extract(amount, action, AutomationType.EXTERNAL) : ChemicalStack.EMPTY;
        }
    }
}
