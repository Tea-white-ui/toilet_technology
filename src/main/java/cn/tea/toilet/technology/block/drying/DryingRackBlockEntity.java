package cn.tea.toilet.technology.block.drying;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.network.DryingRackSyncPayload;
import cn.tea.toilet.technology.network.ModPacketSender;
import cn.tea.toilet.technology.recipe.DryingRecipe;
import cn.tea.toilet.technology.recipe.ModRecipeTypes;
import cn.tea.toilet.technology.ToiletTechnology;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

/**
 * 干燥架方块实体
 * 负责管理干燥架的物品存储和干燥转化逻辑
 * 
 * 网络同步说明：
 * - 使用自定义网络包 DryingRackSyncPayload 同步数据到客户端
 * - 仅在物品变化或干燥进度更新时发送，避免每tick发送
 * - 客户端接收后更新本地数据并触发渲染
 */
public class DryingRackBlockEntity extends BlockEntity {

    // 物品处理器，管理4个槽位（每槽最多1个物品，与视觉设计一致）
    public final ItemStackHandler itemHandler = new ItemStackHandler(4) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            cachedRecipes[slot] = null;
            if (level != null && !level.isClientSide()) {
                syncToClients();
            }
        }

        @Override
        public int getSlotLimit(int slot) {
            return 1;
        }
    };

    // 每个槽位的干燥进度（单位：tick）
    public int[] dryingProgress = new int[4];
    // 每个槽位当前配方的干燥总时间（用于计算进度百分比）
    public int[] dryingTotalTime = new int[4];
    // 配方缓存，避免每tick遍历全部配方
    private final DryingRecipe[] cachedRecipes = new DryingRecipe[4];
    // 上次同步的tick计数，用于控制同步频率
    private int lastSyncTick = 0;

    public DryingRackBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.DRYING_RACK.get(), pos, blockState);
    }

    /**
     * 同步数据到所有客户端
     * 使用自定义网络包，比 sendBlockUpdated 更高效
     */
    private void syncToClients() {
        if (level == null || level.isClientSide()) {
            return;
        }
        
        DryingRackSyncPayload payload = new DryingRackSyncPayload(
                getBlockPos(),
                itemHandler.getStackInSlot(0),
                itemHandler.getStackInSlot(1),
                itemHandler.getStackInSlot(2),
                itemHandler.getStackInSlot(3),
                dryingProgress[0],
                dryingProgress[1],
                dryingProgress[2],
                dryingProgress[3],
                dryingTotalTime[0],
                dryingTotalTime[1],
                dryingTotalTime[2],
                dryingTotalTime[3]
        );
        
        ModPacketSender.sendToTracking(level, getBlockPos(), payload);
    }

    /**
     * Tick 逻辑 - 每游戏刻调用一次
     * 检查每个槽位的物品，如果匹配干燥配方则增加进度
     * 进度达到配方要求时转化物品
     *
     * @param level  当前世界
     * @param pos    方块位置
     * @param state  方块状态
     * @param entity 方块实体实例
     */
    public static void tick(Level level, BlockPos pos, BlockState state, DryingRackBlockEntity entity) {
        // 只在服务端执行干燥逻辑
        if (level.isClientSide()) {
            return;
        }

        boolean progressChanged = false;

        for (int i = 0; i < entity.itemHandler.getSlots(); i++) {
            ItemStack stack = entity.itemHandler.getStackInSlot(i);

            if (stack.isEmpty()) {
                if (entity.dryingProgress[i] != 0 || entity.dryingTotalTime[i] != 0) {
                    entity.dryingProgress[i] = 0;
                    entity.dryingTotalTime[i] = 0;
                    entity.cachedRecipes[i] = null;
                    progressChanged = true;
                }
                continue;
            }

            DryingRecipe recipe = entity.cachedRecipes[i];
            if (recipe == null) {
                recipe = findMatchingRecipe(level, stack);
                entity.cachedRecipes[i] = recipe;
            }

            if (recipe != null) {
                if (entity.dryingTotalTime[i] != recipe.getDryingTime()) {
                    entity.dryingProgress[i] = 0;
                    entity.dryingTotalTime[i] = recipe.getDryingTime();
                    progressChanged = true;
                }

                entity.dryingProgress[i]++;
                progressChanged = true;

                if (entity.dryingProgress[i] >= recipe.getDryingTime()) {
                    ItemStack output = recipe.getResultItem(level.registryAccess()).copy();
                    entity.itemHandler.setStackInSlot(i, ItemStack.EMPTY);
                    ItemStack remaining = entity.itemHandler.insertItem(i, output, false);
                    if (!remaining.isEmpty()) {
                        net.minecraft.world.Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), remaining);
                    }
                    ToiletTechnology.getLOGGER().debug("Drying complete: slot={}, input={}, output={}, pos={}", i, stack, output, pos);

                    entity.dryingProgress[i] = 0;
                    entity.dryingTotalTime[i] = 0;
                    entity.cachedRecipes[i] = null;
                    progressChanged = true;

                    entity.setChanged();
                }
            } else {
                if (entity.dryingProgress[i] != 0 || entity.dryingTotalTime[i] != 0) {
                    entity.dryingProgress[i] = 0;
                    entity.dryingTotalTime[i] = 0;
                    progressChanged = true;
                }
            }
        }

        entity.lastSyncTick++;
        if (entity.lastSyncTick >= 10 || progressChanged) {
            entity.syncToClients();
            entity.lastSyncTick = 0;
        }
    }

    private static DryingRecipe findMatchingRecipe(Level level, ItemStack stack) {
        var recipeHolders = level.getRecipeManager().getAllRecipesFor(ModRecipeTypes.DRYING.get());

        for (var recipeHolder : recipeHolders) {
            DryingRecipe recipe = recipeHolder.value();
            if (recipe.matches(stack)) {
                return recipe;
            }
        }

        return null;
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Items", itemHandler.serializeNBT(registries));
        tag.putIntArray("DryingProgress", dryingProgress);
        tag.putIntArray("DryingTotalTime", dryingTotalTime);
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        itemHandler.deserializeNBT(registries, tag.getCompound("Items"));
        if (tag.contains("DryingProgress")) {
            dryingProgress = tag.getIntArray("DryingProgress");
        }
        if (tag.contains("DryingTotalTime")) {
            dryingTotalTime = tag.getIntArray("DryingTotalTime");
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.put("Items", itemHandler.serializeNBT(registries));
        tag.putIntArray("DryingProgress", dryingProgress);
        tag.putIntArray("DryingTotalTime", dryingTotalTime);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

}