package cn.tea.toilet.technology.block.gasmeltingfurnace;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.api.gas.BasicGasTank;
import cn.tea.toilet.technology.api.gas.GasAction;
import cn.tea.toilet.technology.api.gas.GasStack;
import cn.tea.toilet.technology.api.gas.IGasHandler;
import cn.tea.toilet.technology.recipe.GasMeltingRecipe;
import cn.tea.toilet.technology.recipe.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/** 燃气熔炼炉：使用数据驱动的燃气熔炼配方处理单个输入槽。 */
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
    private int processingProgress;

    public GasMeltingFurnaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.GAS_MELTING_FURNACE.get(), pos, state);
    }

    /**
     * Exposes the furnace inventory to automation on every face: only the input slot accepts items
     * and only the output slot can be extracted. This also gives hoppers the same slot policy.
     */
    public IItemHandler getItemHandler(@Nullable Direction side) {
        return itemHandler;
    }

    /** Direct inventory access for the machine menu; automation remains restricted by the sided item handler. */
    public IItemHandler getMenuItems() {
        return inventory;
    }

    /**
     * Exposes the internal gas tank to automation on every face for both insertion and extraction.
     */
    public IGasHandler getGasHandler(@Nullable Direction side) {
        return gasHandler;
    }

    public int getProcessingProgress() {
        return processingProgress;
    }

    public int getProcessingTime() {
        GasMeltingRecipe recipe = findMatchingRecipe(level, inventory.getStackInSlot(INPUT_SLOT), gasTank.getStack());
        return recipe == null ? 0 : recipe.processingTime();
    }

    /**
     * Returns whether the furnace can process now, including immediately after a completed item
     * is replaced by the next input. This is synchronized separately from progress so the GUI
     * flame stays visible across consecutive processing cycles.
     */
    public boolean isProcessing() {
        GasMeltingRecipe recipe = findMatchingRecipe(level, inventory.getStackInSlot(INPUT_SLOT), gasTank.getStack());
        return recipe != null && canOutput(recipe);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, GasMeltingFurnaceBlockEntity blockEntity) {
        if (level.isClientSide()) return;

        ItemStack input = blockEntity.inventory.getStackInSlot(INPUT_SLOT);
        GasMeltingRecipe recipe = findMatchingRecipe(level, input, blockEntity.gasTank.getStack());
        if (recipe == null || !blockEntity.canOutput(recipe)) {
            blockEntity.setLit(level, pos, state, false);
            if (blockEntity.processingProgress != 0) {
                blockEntity.processingProgress = 0;
                blockEntity.setChanged();
            }
            return;
        }

        blockEntity.setLit(level, pos, state, true);
        blockEntity.processingProgress++;
        if (blockEntity.processingProgress < recipe.processingTime()) {
            blockEntity.setChanged();
            return;
        }

        blockEntity.finishProcessing(recipe);
        GasMeltingRecipe nextRecipe = findMatchingRecipe(level, blockEntity.inventory.getStackInSlot(INPUT_SLOT),
                blockEntity.gasTank.getStack());
        blockEntity.setLit(level, pos, state, nextRecipe != null && blockEntity.canOutput(nextRecipe));
    }

    private void setLit(Level level, BlockPos pos, BlockState state, boolean lit) {
        if (state.getValue(GasMeltingFurnaceBlock.LIT) != lit) {
            level.setBlock(pos, state.setValue(GasMeltingFurnaceBlock.LIT, lit), Block.UPDATE_CLIENTS);
        }
    }

    private static GasMeltingRecipe findMatchingRecipe(Level level, ItemStack input, GasStack gasStack) {
        if (level == null || input.isEmpty()) return null;
        return level.getRecipeManager()
                .getRecipesFor(ModRecipeTypes.GAS_MELTING.get(), new SingleRecipeInput(input), level)
                .stream()
                .map(RecipeHolder::value)
                .filter(recipe -> recipe.hasRequiredGas(gasStack))
                .findFirst()
                .orElse(null);
    }

    private boolean canOutput(GasMeltingRecipe recipe) {
        ItemStack result = recipe.output();
        ItemStack output = inventory.getStackInSlot(OUTPUT_SLOT);
        return output.isEmpty() || (ItemStack.isSameItemSameComponents(output, result)
                && output.getCount() + result.getCount() <= Math.min(output.getMaxStackSize(), result.getMaxStackSize()));
    }

    private void finishProcessing(GasMeltingRecipe recipe) {
        ItemStack result = recipe.output();
        ItemStack output = inventory.getStackInSlot(OUTPUT_SLOT);
        if (output.isEmpty()) {
            inventory.setStackInSlot(OUTPUT_SLOT, result);
        } else {
            output.grow(result.getCount());
            inventory.setStackInSlot(OUTPUT_SLOT, output);
        }
        inventory.getStackInSlot(INPUT_SLOT).shrink(1);
        gasTank.extract(recipe.gasAmount(), GasAction.EXECUTE);
        processingProgress = 0;
        setChanged();
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
        tag.putInt("ProcessingProgress", processingProgress);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("GasTank")) gasTank.deserializeNBT(tag.getCompound("GasTank"));
        if (tag.contains("Inventory")) inventory.deserializeNBT(registries, tag.getCompound("Inventory"));
        processingProgress = tag.getInt("ProcessingProgress");
    }
}