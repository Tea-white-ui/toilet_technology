package cn.tea.toilet.technology.block.toilet;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

/**
 * 蹲便器方块
 * 基础马桶类型，使用时直接掉落粪便物品
 * 不支持流体存储功能
 */
public class SquatToiletBlock extends ToiletBlock {
    /** 方块编解码器，用于序列化和反序列化 */
    public static final MapCodec<SquatToiletBlock> CODEC = simpleCodec(SquatToiletBlock::new);

    /**
     * 构造函数
     * 
     * @param properties 方块属性配置
     */
    public SquatToiletBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    /**
     * 创建新的方块实体
     * 
     * @param pos 方块位置
     * @param state 方块状态
     * @return 蹲便器方块实体
     */
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SquatToiletBlockEntity(pos, state);
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


}