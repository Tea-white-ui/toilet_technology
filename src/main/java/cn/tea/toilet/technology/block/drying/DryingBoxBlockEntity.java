package cn.tea.toilet.technology.block.drying;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.network.DryingBoxSyncPayload;
import cn.tea.toilet.technology.network.ModPacketSender;
import cn.tea.toilet.technology.recipe.DryingRecipe;
import cn.tea.toilet.technology.recipe.ModRecipeTypes;
import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

/**
 * 干燥箱方块实体
 * 负责管理干燥箱的16格输入+16格输出物品存储和干燥转化逻辑
 */
public class DryingBoxBlockEntity extends BlockEntity {

    public static final int SLOTS = 16;

    // 输入槽位：玩家可放入和取出
    public final ItemStackHandler inputHandler = new ItemStackHandler(SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            ItemStack stack = getStackInSlot(slot);
            if (cachedRecipes[slot] != null) {
                if (level == null || level.isClientSide()) {
                    cachedRecipes[slot] = null;
                } else {
                    DryingRecipe cachedRecipe = cachedRecipes[slot];
                    if (!cachedRecipe.matches(new SingleRecipeInput(stack), level)) {
                        cachedRecipes[slot] = null;
                    }
                }
            }
        }
    };

    public final ItemStackHandler outputHandler = new ItemStackHandler(SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }
    };

    // 每个输入槽位的干燥进度（单位：tick）
    private int[] dryingProgress = new int[SLOTS];
    // 每个输入槽位当前配方的干燥总时间
    private int[] dryingTotalTime = new int[SLOTS];
    // 每个输入槽位的浮点干燥进度（用于精确计算小数进度）
    private float[] dryingProgressFractional = new float[SLOTS];
    // 配方缓存
    private final DryingRecipe[] cachedRecipes = new DryingRecipe[SLOTS];
    // 标记是否有正在进行的干燥任务，用于优化 tick 性能
    private boolean hasActiveDrying = false;
    // 上次同步的tick计数，用于控制同步频率
    private int lastSyncTick = 0;

    // 用于漏斗交互的包装 handler
    // 输入槽（0-15）：漏斗可以插入，但不能提取
    // 输出槽（16-31）：漏斗可以提取，但不能插入
    private final IItemHandler hopperHandler = new HopperItemHandler();

    private class HopperItemHandler implements IItemHandler {
        @Override
        public int getSlots() {
            return SLOTS * 2; // 输入槽 + 输出槽
        }

        @Override
        public @NotNull ItemStack getStackInSlot(int slot) {
            if (slot < SLOTS) {
                return inputHandler.getStackInSlot(slot);
            } else {
                return outputHandler.getStackInSlot(slot - SLOTS);
            }
        }

        @Override
        public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            // 只允许插入到输入槽（槽位 0-15）
            if (slot < SLOTS) {
                return inputHandler.insertItem(slot, stack, simulate);
            }
            // 输出槽不允许漏斗插入
            return stack;
        }

        @Override
        public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
            // 只允许从输出槽提取（槽位 16-31）
            if (slot >= SLOTS && slot < SLOTS * 2) {
                return outputHandler.extractItem(slot - SLOTS, amount, simulate);
            }
            // 输入槽不允许漏斗抽出
            return ItemStack.EMPTY;
        }

        @Override
        public int getSlotLimit(int slot) {
            if (slot < SLOTS) {
                return inputHandler.getSlotLimit(slot);
            } else {
                return outputHandler.getSlotLimit(slot - SLOTS);
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            // 只允许输入槽验证物品
            if (slot < SLOTS) {
                return inputHandler.isItemValid(slot, stack);
            }
            return false;
        }
    }

    public DryingBoxBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DRYING_BOX.get(), pos, blockState);
    }

    /**
     * 获取用于漏斗交互的 ItemHandler
     */
    public IItemHandler getHopperHandler() {
        return hopperHandler;
    }

    /**
     * 同步数据到所有客户端
     * 使用自定义网络包，比 sendBlockUpdated 更高效
     * 仅在数据变化时调用，避免每tick发送
     */
    private void syncToClients() {
        if (level == null || level.isClientSide()) {
            return;
        }
        
        DryingBoxSyncPayload payload = new DryingBoxSyncPayload(
                getBlockPos(),
                dryingProgress.clone(),
                dryingTotalTime.clone()
        );
        
        ModPacketSender.sendToTracking(level, getBlockPos(), payload);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, DryingBoxBlockEntity entity) {
        if (level.isClientSide()) {
            return;
        }

        boolean hasHeatSource = isHeatSourceBelow(level, pos);
        float speedMultiplier = hasHeatSource ? 1.5f : 0.9f;

        boolean anyDrying = false;
        boolean progressChanged = false;

        for (int i = 0; i < SLOTS; i++) {
            ItemStack inputStack = entity.inputHandler.getStackInSlot(i);

            if (inputStack.isEmpty()) {
                if (entity.dryingProgress[i] != 0 || entity.dryingTotalTime[i] != 0) {
                    entity.dryingProgress[i] = 0;
                    entity.dryingTotalTime[i] = 0;
                    entity.dryingProgressFractional[i] = 0.0f;
                    entity.cachedRecipes[i] = null;
                    progressChanged = true;
                }
                continue;
            }

            anyDrying = true;
            DryingRecipe recipe = entity.cachedRecipes[i];
            if (recipe == null) {
                recipe = findMatchingRecipe(level, inputStack);
                entity.cachedRecipes[i] = recipe;
            }

            if (recipe != null) {
                ItemStack outputStack = entity.outputHandler.getStackInSlot(i);
                ItemStack expectedOutput = recipe.getResultItem(level.registryAccess());
                
                if (!outputStack.isEmpty() && !ItemStack.isSameItemSameComponents(outputStack, expectedOutput)) {
                    if (entity.dryingProgress[i] != 0 || entity.dryingTotalTime[i] != 0) {
                        entity.dryingProgress[i] = 0;
                        entity.dryingTotalTime[i] = 0;
                        entity.dryingProgressFractional[i] = 0.0f;
                        entity.cachedRecipes[i] = null;
                        progressChanged = true;
                    }
                    continue;
                }
                
                ItemStack simulatedRemaining = entity.outputHandler.insertItem(i, expectedOutput.copy(), true);
                if (!simulatedRemaining.isEmpty()) {
                    if (entity.dryingProgress[i] != 0 || entity.dryingTotalTime[i] != 0) {
                        entity.dryingProgress[i] = 0;
                        entity.dryingTotalTime[i] = 0;
                        entity.dryingProgressFractional[i] = 0.0f;
                        entity.cachedRecipes[i] = null;
                        progressChanged = true;
                    }
                    continue;
                }

                int expectedTotalTime = recipe.getDryingTime();
                if (entity.dryingTotalTime[i] != expectedTotalTime) {
                    entity.dryingProgress[i] = 0;
                    entity.dryingTotalTime[i] = expectedTotalTime;
                    entity.dryingProgressFractional[i] = 0.0f;
                    progressChanged = true;
                }

                entity.dryingProgressFractional[i] += speedMultiplier;
                int progressIncrement = (int) entity.dryingProgressFractional[i];
                entity.dryingProgressFractional[i] -= progressIncrement;

                entity.dryingProgress[i] += progressIncrement;
                progressChanged = true;

                if (entity.dryingProgress[i] >= expectedTotalTime) {
                    if (tryCompleteDrying(entity, i, recipe, level)) {
                        progressChanged = true;
                    } else {
                        entity.dryingProgress[i] = 0;
                        entity.dryingTotalTime[i] = 0;
                        entity.dryingProgressFractional[i] = 0.0f;
                        entity.cachedRecipes[i] = null;
                        progressChanged = true;
                    }
                }
            } else {
                if (entity.dryingProgress[i] != 0 || entity.dryingTotalTime[i] != 0) {
                    entity.dryingProgress[i] = 0;
                    entity.dryingTotalTime[i] = 0;
                    entity.dryingProgressFractional[i] = 0.0f;
                    progressChanged = true;
                }
            }
        }

        entity.hasActiveDrying = anyDrying;

        if (progressChanged) {
            entity.setChanged();
            entity.syncToClients();
        } else {
            entity.lastSyncTick++;
            if (entity.lastSyncTick >= 10) {
                entity.syncToClients();
                entity.lastSyncTick = 0;
            }
        }
    }

    private static boolean tryCompleteDrying(DryingBoxBlockEntity entity, int slot, DryingRecipe recipe, Level level) {
        ItemStack output = recipe.getResultItem(level.registryAccess()).copy();

        ItemStack simulatedRemaining = entity.outputHandler.insertItem(slot, output, true);
        if (!simulatedRemaining.isEmpty()) {
            return false;
        }

        ItemStack extracted = entity.inputHandler.extractItem(slot, 1, false);
        if (extracted.isEmpty()) {
            ToiletTechnology.getLOGGER().warn("DryingBox failed to extract input at slot {}", slot);
            return false;
        }

        ItemStack actualRemaining = entity.outputHandler.insertItem(slot, output, false);
        if (!actualRemaining.isEmpty()) {
            entity.inputHandler.insertItem(slot, extracted, false);
            ToiletTechnology.getLOGGER().warn("DryingBox output insertion failed at slot {}, input restored", slot);
            return false;
        }

        ToiletTechnology.getLOGGER().debug("DryingBox complete: slot={}, output={}, pos={}", slot, output, entity.getBlockPos());

        entity.dryingProgress[slot] = 0;
        entity.dryingTotalTime[slot] = 0;
        entity.dryingProgressFractional[slot] = 0.0f;
        entity.cachedRecipes[slot] = null;

        return true;
    }

    private static boolean isHeatSourceBelow(Level level, BlockPos pos) {
        BlockPos belowPos = pos.below();
        BlockState belowState = level.getBlockState(belowPos);
        return belowState.is(ModTags.Blocks.HEAT_SOURCES);
    }

    private static DryingRecipe findMatchingRecipe(Level level, ItemStack stack) {
        var recipeManager = level.getRecipeManager();
        var recipeInput = new SingleRecipeInput(stack);
        Optional<RecipeHolder<DryingRecipe>> optional =
            recipeManager.getRecipeFor(ModRecipeTypes.DRYING.get(), recipeInput, level);
        return optional.map(RecipeHolder::value).orElse(null);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("InputItems", inputHandler.serializeNBT(registries));
        tag.put("OutputItems", outputHandler.serializeNBT(registries));
        tag.putIntArray("DryingProgress", dryingProgress);
        tag.putIntArray("DryingTotalTime", dryingTotalTime);
        
        ListTag fractionalList = new ListTag();
        for (float f : dryingProgressFractional) {
            fractionalList.add(FloatTag.valueOf(f));
        }
        tag.put("DryingProgressFractional", fractionalList);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        if (tag.contains("InputItems")) {
            inputHandler.deserializeNBT(registries, tag.getCompound("InputItems"));
        }
        if (tag.contains("OutputItems")) {
            outputHandler.deserializeNBT(registries, tag.getCompound("OutputItems"));
        }
        if (tag.contains("DryingProgress")) {
            int[] progress = tag.getIntArray("DryingProgress");
            if (progress.length == SLOTS) {
                dryingProgress = progress;
            } else {
                ToiletTechnology.getLOGGER().warn("DryingBox loaded DryingProgress with invalid length: {}, expected {}", progress.length, SLOTS);
                dryingProgress = new int[SLOTS];
            }
        }
        if (tag.contains("DryingTotalTime")) {
            int[] totalTime = tag.getIntArray("DryingTotalTime");
            if (totalTime.length == SLOTS) {
                dryingTotalTime = totalTime;
            } else {
                ToiletTechnology.getLOGGER().warn("DryingBox loaded DryingTotalTime with invalid length: {}, expected {}", totalTime.length, SLOTS);
                dryingTotalTime = new int[SLOTS];
            }
        }
        if (tag.contains("DryingProgressFractional")) {
            ListTag fractionalList = tag.getList("DryingProgressFractional", 5); // 5 = FloatTag ID
            if (fractionalList.size() == SLOTS) {
                for (int i = 0; i < SLOTS; i++) {
                    dryingProgressFractional[i] = fractionalList.getFloat(i);
                }
            } else {
                ToiletTechnology.getLOGGER().warn("DryingBox loaded DryingProgressFractional with invalid length: {}, expected {}", fractionalList.size(), SLOTS);
                dryingProgressFractional = new float[SLOTS];
            }
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.putIntArray("DryingProgress", dryingProgress);
        tag.putIntArray("DryingTotalTime", dryingTotalTime);
        return tag;
    }

    @Override
    public void handleUpdateTag(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.handleUpdateTag(tag, registries);
        if (tag.contains("DryingProgress")) {
            int[] progress = tag.getIntArray("DryingProgress");
            if (progress.length == SLOTS) {
                dryingProgress = progress;
            }
        }
        if (tag.contains("DryingTotalTime")) {
            int[] totalTime = tag.getIntArray("DryingTotalTime");
            if (totalTime.length == SLOTS) {
                dryingTotalTime = totalTime;
            }
        }
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }


    public int[] getDryingProgress() {
        return dryingProgress;
    }

    public int[] getDryingTotalTime() {
        return dryingTotalTime;
    }
}