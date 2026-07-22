package cn.tea.toilet.technology.block.toilet;

import cn.tea.toilet.technology.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 蹲便器方块实体
 * 基础方块实体，用于支持蹲便器方块的交互逻辑
 * 不包含流体存储功能（与 PremiumToiletBlockEntity 区分）
 */
public class SquatToiletBlockEntity extends BlockEntity {

    /**
     * 构造函数
     * 
     * @param pos 方块位置
     * @param blockState 方块状态
     */
    public SquatToiletBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.TOILET.get(), pos, blockState);
    }
}