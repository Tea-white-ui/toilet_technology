package cn.tea.toilet.technology.block.drying;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.recipe.DryingRecipe;
import cn.tea.toilet.technology.recipe.ModRecipeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.Containers;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

/**
 * 干燥架方块实体
 * 负责管理干燥架的物品存储和干燥转化逻辑
 */
public class DryingRackBlockEntity extends BlockEntity {

    // 物品处理器，管理4个槽位
    public final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide()) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };

    // 每个槽位的干燥进度（单位：tick）
    public int[] dryingProgress = new int[4];
    // 每个槽位当前配方的干燥总时间（用于计算进度百分比）
    public int[] dryingTotalTime = new int[4];

    public DryingRackBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DRYING_RACK.get(), pos, blockState);
    }

    /**
     * Tick 逻辑 - 每游戏刻调用一次
     * 检查每个槽位的物品，如果匹配干燥配方则增加进度
     * 进度达到配方要求时转化物品
     *
     * @param level 当前世界
     * @param pos   方块位置
     * @param state 方块状态
     * @param entity 方块实体实例
     */
    public static void tick(Level level, BlockPos pos, BlockState state, DryingRackBlockEntity entity) {
        // 只在服务端执行干燥逻辑
        if (level.isClientSide()) {
            return;
        }

        // 遍历所有槽位
        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            ItemStack stack = entity.itemHandler.getStackInSlot(i);

            // 如果槽位为空，重置进度
            if (stack.isEmpty()) {
                entity.dryingProgress[i] = 0;
                entity.dryingTotalTime[i] = 0;
                continue;
            }

            // 查找匹配的干燥配方
            DryingRecipe recipe = findMatchingRecipe(level, stack);

            if (recipe != null) {
                // 如果配方发生变化，重置进度
                if (entity.dryingTotalTime[i] != recipe.getDryingTime()) {
                    entity.dryingProgress[i] = 0;
                    entity.dryingTotalTime[i] = recipe.getDryingTime();
                }

                // 增加干燥进度
                entity.dryingProgress[i]++;

                // 检查是否完成干燥
                if (entity.dryingProgress[i] >= recipe.getDryingTime()) {
                    // 执行物品转化
                    ItemStack output = recipe.getResultItem(level.registryAccess()).copy();
                    entity.itemHandler.setStackInSlot(i, output);

                    // 重置进度
                    entity.dryingProgress[i] = 0;
                    entity.dryingTotalTime[i] = 0;

                    // 标记方块实体已改变，触发保存和网络同步
                    entity.setChanged();
                    level.sendBlockUpdated(pos, state, state, 2);
                }
            } else {
                // 没有匹配配方，重置进度
                entity.dryingProgress[i] = 0;
                entity.dryingTotalTime[i] = 0;
            }
        }
    }

    /**
     * 查找匹配当前物品的干燥配方
     *
     * @param level 当前世界
     * @param stack 要检查的物品
     * @return 匹配的配方，如果没有则返回null
     */
    private static DryingRecipe findMatchingRecipe(Level level, ItemStack stack) {
        // 获取所有干燥配方
        var recipeHolders = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.DRYING.get());

        // 遍历查找第一个匹配的配方
        for (var recipeHolder : recipeHolders) {
            DryingRecipe recipe = recipeHolder.value();
            if (recipe.matches(stack)) {
                return recipe;
            }
        }

        return null;
    }

    /**
     * 保存数据到NBT（用于世界保存/加载）
     */
    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        // 保存物品
        tag.put("Items", itemHandler.serializeNBT(registries));
        // 保存干燥进度
        tag.putIntArray("DryingProgress", dryingProgress);
        tag.putIntArray("DryingTotalTime", dryingTotalTime);
    }

    /**
     * 从NBT加载数据
     */
    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        // 加载物品
        itemHandler.deserializeNBT(registries, tag.getCompound("Items"));
        // 加载干燥进度
        if (tag.contains("DryingProgress")) {
            dryingProgress = tag.getIntArray("DryingProgress");
        }
        if (tag.contains("DryingTotalTime")) {
            dryingTotalTime = tag.getIntArray("DryingTotalTime");
        }
    }

    /**
     * 获取用于网络同步的NBT标签
     */
    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.put("Items", itemHandler.serializeNBT(registries));
        tag.putIntArray("DryingProgress", dryingProgress);
        tag.putIntArray("DryingTotalTime", dryingTotalTime);
        return tag;
    }

    /**
     * 获取用于客户端同步的更新包
     */
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
        if (slot < 0 || slot >= 4) return 0.0f;
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
        if (slot < 0 || slot >= 4) return false;
        return dryingProgress[slot] > 0 && dryingTotalTime[slot] > 0;
    }
}