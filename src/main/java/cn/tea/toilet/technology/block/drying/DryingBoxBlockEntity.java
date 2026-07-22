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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;

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
    public int[] dryingProgress = new int[SLOTS];
    // 每个输入槽位当前配方的干燥总时间
    public int[] dryingTotalTime = new int[SLOTS];
    // 配方缓存
    private final DryingRecipe[] cachedRecipes = new DryingRecipe[SLOTS];

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

        for (int i = 0; i < SLOTS; i++) {
            ItemStack inputStack = entity.inputHandler.getStackInSlot(i);

            if (inputStack.isEmpty()) {
                entity.dryingProgress[i] = 0;
                entity.dryingTotalTime[i] = 0;
                entity.cachedRecipes[i] = null;
                continue;
            }

            DryingRecipe recipe = entity.cachedRecipes[i];
            if (recipe == null) {
                recipe = findMatchingRecipe(level, inputStack);
                entity.cachedRecipes[i] = recipe;
            }

            if (recipe != null) {
                if (entity.dryingTotalTime[i] != recipe.getDryingTime()) {
                    entity.dryingProgress[i] = 0;
                    entity.dryingTotalTime[i] = recipe.getDryingTime();
                }

                entity.dryingProgress[i]++;

                if (entity.dryingProgress[i] >= recipe.getDryingTime()) {
                    // 尝试将产物放入对应输出槽位
                    ItemStack output = recipe.getResultItem(level.registryAccess()).copy();
                    ItemStack remaining = entity.outputHandler.insertItem(i, output, true); // 模拟插入

                    if (remaining.isEmpty()) {
                        // 输出槽位可以接受产物，执行转化
                        entity.inputHandler.extractItem(i, 1, false);
                        entity.outputHandler.insertItem(i, output, false);
                        ToiletTechnology.getLOGGER().debug("DryingBox complete: slot={}, output={}, pos={}", i, output, pos);

                        entity.dryingProgress[i] = 0;
                        entity.dryingTotalTime[i] = 0;
                        entity.cachedRecipes[i] = null;
                        entity.setChanged();
                    }
                    // 如果输出槽位已满（不同物品），保持进度等待输出槽位被清空
                }
            } else {
                entity.dryingProgress[i] = 0;
                entity.dryingTotalTime[i] = 0;
            }
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
            dryingProgress = tag.getIntArray("DryingProgress");
        }
        if (tag.contains("DryingTotalTime")) {
            dryingTotalTime = tag.getIntArray("DryingTotalTime");
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.put("InputItems", inputHandler.serializeNBT(registries));
        tag.put("OutputItems", outputHandler.serializeNBT(registries));
        tag.putIntArray("DryingProgress", dryingProgress);
        tag.putIntArray("DryingTotalTime", dryingTotalTime);
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
