package cn.tea.toilet.technology.block.drying;

import cn.tea.toilet.technology.ModConstants;
import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.network.DryingRackSyncPayload;
import cn.tea.toilet.technology.network.ModPacketSender;
import cn.tea.toilet.technology.recipe.DryingRecipe;
import cn.tea.toilet.technology.recipe.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/**
 * Four-slot drying rack. The block entity coordinates recipes and persistence;
 * {@link DryingRackProgressState} owns the fixed-size progress snapshot shared
 * by server processing and client synchronization.
 */
public class DryingRackBlockEntity extends BlockEntity {
    public final ItemStackHandler itemHandler = new ItemStackHandler(DryingRackConfig.SLOT_COUNT) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            cachedRecipes[slot] = null;
            noRecipeMatch[slot] = false;
            if (level != null && !level.isClientSide()) {
                inventorySyncPending = true;
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }

        @Override
        public void setStackInSlot(int slot, @NotNull ItemStack stack) {
            suppressSlotSync++;
            try {
                super.setStackInSlot(slot, stack);
            } finally {
                finishSlotMutation();
            }
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            suppressSlotSync++;
            try {
                return super.insertItem(slot, stack, simulate);
            } finally {
                finishSlotMutation();
            }
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            suppressSlotSync++;
            try {
                return super.extractItem(slot, amount, simulate);
            } finally {
                finishSlotMutation();
            }
        }
    };

    private final DryingRackProgressState progressState = new DryingRackProgressState(DryingRackConfig.SLOT_COUNT);
    private final DryingRecipe[] cachedRecipes = new DryingRecipe[DryingRackConfig.SLOT_COUNT];
    private final boolean[] noRecipeMatch = new boolean[DryingRackConfig.SLOT_COUNT];
    private int suppressSlotSync;
    private boolean inventorySyncPending;
    private int lastSyncTick;

    public DryingRackBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DRYING_RACK.get(), pos, blockState);
    }

    private void syncToClients() {
        if (level == null || level.isClientSide()) {
            return;
        }
        ModPacketSender.sendToTracking(level, getBlockPos(), new DryingRackSyncPayload(
                getBlockPos(),
                itemHandler.getStackInSlot(0),
                itemHandler.getStackInSlot(1),
                itemHandler.getStackInSlot(2),
                itemHandler.getStackInSlot(3),
                progressState.progress(0),
                progressState.progress(1),
                progressState.progress(2),
                progressState.progress(3),
                progressState.totalTime(0),
                progressState.totalTime(1),
                progressState.totalTime(2),
                progressState.totalTime(3)
        ));
    }

    /** Applies the complete server snapshot on the client without duplicating payload field knowledge. */
    public void applyClientSync(DryingRackSyncPayload payload) {
        for (int slot = 0; slot < DryingRackConfig.SLOT_COUNT; slot++) {
            itemHandler.setStackInSlot(slot, payload.getSlotItem(slot));
        }
        progressState.replace(
                new int[]{payload.getProgress(0), payload.getProgress(1), payload.getProgress(2), payload.getProgress(3)},
                new int[]{payload.getTotalTime(0), payload.getTotalTime(1), payload.getTotalTime(2), payload.getTotalTime(3)});
        setChanged();
    }

    private void finishSlotMutation() {
        suppressSlotSync--;
        if (suppressSlotSync == 0 && inventorySyncPending) {
            inventorySyncPending = false;
            syncToClients();
        }
    }

    private void flushSync(boolean progressChanged) {
        lastSyncTick++;
        if (!progressChanged && lastSyncTick < 10) {
            return;
        }
        syncToClients();
        lastSyncTick = 0;
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DryingRackBlockEntity entity) {
        if (level.isClientSide()) {
            return;
        }

        boolean progressChanged = false;
        for (int slot = 0; slot < entity.itemHandler.getSlots(); slot++) {
            ItemStack stack = entity.itemHandler.getStackInSlot(slot);
            if (stack.isEmpty()) {
                progressChanged |= entity.resetSlot(slot);
                continue;
            }

            DryingRecipe recipe = entity.recipeFor(level, slot, stack);
            if (recipe == null) {
                progressChanged |= entity.resetProgress(slot);
                continue;
            }

            if (entity.progressState.totalTime(slot) != recipe.getDryingTime()) {
                entity.progressState.reset(slot);
                entity.progressState.setTotalTime(slot, recipe.getDryingTime());
                progressChanged = true;
            }

            entity.progressState.advance(slot);
            progressChanged = true;
            if (entity.progressState.progress(slot) >= recipe.getDryingTime()) {
                entity.completeDrying(level, pos, slot, stack, recipe);
            }
        }
        entity.flushSync(progressChanged);
    }

    private DryingRecipe recipeFor(Level level, int slot, ItemStack stack) {
        DryingRecipe recipe = cachedRecipes[slot];
        if (recipe == null && !noRecipeMatch[slot]) {
            recipe = findMatchingRecipe(level, stack);
            if (recipe == null) {
                noRecipeMatch[slot] = true;
            } else {
                cachedRecipes[slot] = recipe;
            }
        }
        return recipe;
    }

    private boolean resetSlot(int slot) {
        boolean changed = progressState.reset(slot);
        if (changed) {
            cachedRecipes[slot] = null;
            noRecipeMatch[slot] = false;
        }
        return changed;
    }

    private boolean resetProgress(int slot) {
        return progressState.reset(slot);
    }

    private void completeDrying(Level level, BlockPos pos, int slot, ItemStack input, DryingRecipe recipe) {
        ItemStack output = recipe.getResultItem(level.registryAccess());
        suppressSlotSync++;
        try {
            ItemStack extracted = itemHandler.extractItem(slot, 1, false);
            if (extracted.isEmpty()) {
                resetSlot(slot);
                return;
            }
            ItemStack remaining = itemHandler.insertItem(slot, output.copy(), false);
            if (!remaining.isEmpty()) {
                net.minecraft.world.Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), remaining);
            }
            resetSlot(slot);
            ModConstants.LOGGER.debug("Drying complete: slot={}, input={}, output={}, pos={}", slot, input, output, pos);
        } finally {
            finishSlotMutation();
        }
    }

    private static DryingRecipe findMatchingRecipe(Level level, ItemStack stack) {
        return level.getRecipeManager()
                .getRecipeFor(ModRecipeTypes.DRYING.get(), new net.minecraft.world.item.crafting.SingleRecipeInput(stack), level)
                .map(net.minecraft.world.item.crafting.RecipeHolder::value)
                .orElse(null);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Items", itemHandler.serializeNBT(registries));
        tag.putIntArray("DryingProgress", progressState.progressValues());
        tag.putIntArray("DryingTotalTime", progressState.totalTimeValues());
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("Items"));
        if (tag.contains("DryingProgress") && tag.contains("DryingTotalTime")) {
            progressState.replace(tag.getIntArray("DryingProgress"), tag.getIntArray("DryingTotalTime"));
        }
    }
}
