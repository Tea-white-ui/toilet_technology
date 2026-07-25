package cn.tea.toilet.technology.block.toilet;


import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.NotNull;


/**
 * 高级马桶方块
 * 支持流体存储功能，玩家可以使用流体桶与马桶交互
 * 不同材质的马桶具有不同的效率系数（multiplier）
 * 效率系数影响玩家使用马桶时产生粪便的速度
 */
public  class PremiumToiletBlock extends ToiletBlock {

    /** 方块编解码器：包含方块属性和效率系数 */
    public static final MapCodec<PremiumToiletBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    propertiesCodec(),
                    Codec.DOUBLE.fieldOf("multiplier").forGetter(b -> b.multiplier)
            ).apply(instance, PremiumToiletBlock::new)
    );

    /**
     * 创建新的方块实体
     * 
     * @param pos 方块位置
     * @param state 方块状态
     * @return 高级马桶方块实体
     */
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PremiumToiletBlockEntity(pos, state);
    }

    /**
     * 获取方块编解码器
     * 
     * @return 编解码器
     */
    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    /** 效率系数：影响产生粪便的速度，值越小速度越快 */
    private final double multiplier;

    /**
     * 构造函数
     * 
     * @param properties 方块属性配置
     * @param multiplier 效率系数（0.6-1.0，值越小效率越高）
     */
    public PremiumToiletBlock(Properties properties, double multiplier) {
        super(properties);
        this.multiplier = multiplier;
    }

    /**
     * 获取效率系数
     * 
     * @return 效率系数值
     */
    public double getMultiplier() {
        return multiplier;
    }

    /**
     * 右键交互处理：支持流体桶与马桶的流体交互
     * 玩家可以使用空桶抽取粪便液体，或使用水桶/粪便桶填充马桶
     * 
     * @param stack 玩家手中的物品
     * @param state 方块状态
     * @param level 世界
     * @param pos 方块位置
     * @param player 玩家
     * @param hand 交互手
     * @param hitResult 点击结果
     * @return 交互结果
     */
    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        var fluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, null);
        if (fluidHandler != null && FluidUtil.interactWithFluidHandler(player, hand, fluidHandler)) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

}