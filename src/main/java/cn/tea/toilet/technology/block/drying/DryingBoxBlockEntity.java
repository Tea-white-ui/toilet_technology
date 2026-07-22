package cn.tea.toilet.technology.block.drying;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.recipe.DryingRecipe;
import cn.tea.toilet.technology.recipe.ModRecipeTypes;
import cn.tea.toilet.technology.ToiletTechnology;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
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
            cachedRecipes[slot] = null;
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    // 输出槽位：玩家只能取出，不能放入
    public final ItemStackHandler outputHandler = new ItemStackHandler(SLOTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    // 每个输入槽位的干燥进度（单位：tick）
    private int[] dryingProgress = new int[SLOTS];
    // 每个输入槽位当前配方的干燥总时间
    private int[] dryingTotalTime = new int[SLOTS];
    // 配方缓存
    private final DryingRecipe[] cachedRecipes = new DryingRecipe[SLOTS];
    // 标记是否有正在进行的干燥任务，用于优化 tick 性能
    private boolean hasActiveDrying = false;

    public DryingBoxBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DRYING_BOX.get(), pos, blockState);
    }

    /**
     * Tick 逻辑 - 每游戏刻调用一次
     * 检查输入槽位的物品，如果匹配干燥配方则增加进度
     * 进度达到配方要求时将产物放入对应输出槽位
     */
    public static void tick(Level level, BlockPos pos, BlockState state, DryingBoxBlockEntity entity) {
        if (level.isClientSide()) {
            return;
        }

        boolean anyDrying = false;
        boolean progressChanged = false;

        for (int i = 0; i < SLOTS; i++) {
            ItemStack inputStack = entity.inputHandler.getStackInSlot(i);

            if (inputStack.isEmpty()) {
                if (entity.dryingProgress[i] != 0 || entity.dryingTotalTime[i] != 0) {
                    entity.dryingProgress[i] = 0;
                    entity.dryingTotalTime[i] = 0;
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
                int expectedTotalTime = recipe.getDryingTime();
                if (entity.dryingTotalTime[i] != expectedTotalTime) {
                    entity.dryingProgress[i] = 0;
                    entity.dryingTotalTime[i] = expectedTotalTime;
                    progressChanged = true;
                }

                entity.dryingProgress[i]++;
                progressChanged = true;

                if (entity.dryingProgress[i] >= expectedTotalTime) {
                    if (tryCompleteDrying(entity, i, recipe, level)) {
                        progressChanged = true;
                    } else {
                        // 输出槽位已满，停止干燥以避免无限等待
                        entity.dryingProgress[i] = 0;
                        entity.dryingTotalTime[i] = 0;
                        entity.cachedRecipes[i] = null;
                        progressChanged = true;
                    }
                }
            } else {
                if (entity.dryingProgress[i] != 0 || entity.dryingTotalTime[i] != 0) {
                    entity.dryingProgress[i] = 0;
                    entity.dryingTotalTime[i] = 0;
                    progressChanged = true;
                }
            }
        }

        entity.hasActiveDrying = anyDrying;

        if (progressChanged) {
            entity.setChanged();
        }
    }

    /**
     * 尝试完成干燥并将产物放入输出槽位
     * 采用先消耗输入再插入输出的策略，避免物品复制
     *
     * @return 是否成功完成干燥
     */
    private static boolean tryCompleteDrying(DryingBoxBlockEntity entity, int slot, DryingRecipe recipe, Level level) {
        ItemStack output = recipe.getResultItem(level.registryAccess()).copy();

        // 先模拟插入检查输出槽位是否能接受
        ItemStack simulatedRemaining = entity.outputHandler.insertItem(slot, output, true);
        if (!simulatedRemaining.isEmpty()) {
            return false;
        }

        // 先消耗输入物品（原子操作）
        ItemStack extracted = entity.inputHandler.extractItem(slot, 1, false);
        if (extracted.isEmpty()) {
            // 提取失败，不应该发生，但做防御性处理
            ToiletTechnology.getLOGGER().warn("DryingBox failed to extract input at slot {}", slot);
            return false;
        }

        // 插入输出物品
        ItemStack actualRemaining = entity.outputHandler.insertItem(slot, output, false);
        if (!actualRemaining.isEmpty()) {
            // 插入失败（理论上不应该发生，因为前面已经模拟检查过）
            // 将消耗的物品返还给输入槽位
            entity.inputHandler.insertItem(slot, extracted, false);
            ToiletTechnology.getLOGGER().warn("DryingBox output insertion failed at slot {}, input restored", slot);
            return false;
        }

        ToiletTechnology.getLOGGER().debug("DryingBox complete: slot={}, output={}, pos={}", slot, output, entity.getBlockPos());

        // 重置干燥状态
        entity.dryingProgress[slot] = 0;
        entity.dryingTotalTime[slot] = 0;
        entity.cachedRecipes[slot] = null;

        return true;
    }

    /**
     * 查找匹配当前物品的干燥配方
     * 使用 RecipeManager.getRecipeFor 方法，内部有优化和缓存机制
     */
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

    /**
     * 获取指定槽位的干燥进度百分比（0.0-1.0）
     * 用于客户端渲染进度条
     *
     * @param slot 槽位索引
     * @return 进度百分比，如果槽位为空或没有配方则返回0
     */
    public float getProgressPercent(int slot) {
        if (slot < 0 || slot >= SLOTS) return 0.0f;
        if (dryingTotalTime[slot] == 0) return 0.0f;
        return (float) dryingProgress[slot] / dryingTotalTime[slot];
    }

    /**
     * 检查指定槽位是否正在干燥
     *
     * @param slot 槽位索引
     * @return 如果正在干燥返回true
     */
    public boolean isDrying(int slot) {
        if (slot < 0 || slot >= SLOTS) return false;
        return dryingProgress[slot] > 0 && dryingTotalTime[slot] > 0;
    }

    /**
     * 获取干燥进度数组（用于 GUI 同步）
     */
    public int[] getDryingProgress() {
        return dryingProgress;
    }

    /**
     * 获取干燥总时间数组（用于 GUI 同步）
     */
    public int[] getDryingTotalTime() {
        return dryingTotalTime;
    }

    /**
     * 检查是否有正在进行的干燥任务
     */
    public boolean hasActiveDrying() {
        return hasActiveDrying;
    }
}