package cn.tea.toilet.technology.block.gasmeltingfurnace;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.gas.BasicGasTank;
import cn.tea.toilet.technology.gas.GasAction;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.gas.IGasHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/** Stores the future furnace's gas fuel, one input slot, and one output slot. */
public final class GasMeltingFurnaceBlockEntity extends BlockEntity {
    /** Two buckets, expressed in the project's millibucket-based gas unit. */
    public static final long GAS_CAPACITY = 2_000;
    public static final int INPUT_SLOT = 0;
    public static final int OUTPUT_SLOT = 1;

    private final BasicGasTank gasTank = new BasicGasTank(GAS_CAPACITY, stack -> true, this::setChanged);
    private final ItemStackHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };
    private final IItemHandler itemHandler = new FurnaceItemHandler();
    private final IGasHandler gasHandler = new FurnaceGasHandler();

    public GasMeltingFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GAS_MELTING_FURNACE.get(), pos, state);
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public IGasHandler getGasHandler() {
        return gasHandler;
    }

    private final class FurnaceItemHandler implements IItemHandler {
        @Override public int getSlots() { return 2; }
        @Override public @NotNull ItemStack getStackInSlot(int slot) { return inventory.getStackInSlot(slot); }
        @Override public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            return slot == INPUT_SLOT ? inventory.insertItem(slot, stack, simulate) : stack;
        }
        @Override public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            return slot == OUTPUT_SLOT ? inventory.extractItem(slot, amount, simulate) : ItemStack.EMPTY;
        }
        @Override public int getSlotLimit(int slot) { return inventory.getSlotLimit(slot); }
        @Override public boolean isItemValid(int slot, @NotNull ItemStack stack) { return slot == INPUT_SLOT; }
    }

    private final class FurnaceGasHandler implements IGasHandler {
        @Override public int getGasTanks() { return 1; }
        @Override public GasStack getGasInTank(int tank) { return tank == 0 ? gasTank.getStack() : GasStack.EMPTY; }
        @Override public void setGasInTank(int tank, GasStack stack) { if (tank == 0) gasTank.setStack(stack); }
        @Override public long getGasTankCapacity(int tank) { return tank == 0 ? GAS_CAPACITY : 0; }
        @Override public boolean isValid(int tank, GasStack stack) { return tank == 0 && gasTank.isValid(stack); }
        @Override public GasStack insertGas(int tank, GasStack stack, GasAction action) {
            return tank == 0 ? gasTank.insert(stack, action) : stack;
        }
        @Override public GasStack extractGas(int tank, long amount, GasAction action) {
            return tank == 0 ? gasTank.extract(amount, action) : GasStack.EMPTY;
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("GasTank", gasTank.serializeNBT());
        tag.put("Inventory", inventory.serializeNBT(registries));
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("GasTank")) gasTank.deserializeNBT(tag.getCompound("GasTank"));
        if (tag.contains("Inventory")) inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
    }
}