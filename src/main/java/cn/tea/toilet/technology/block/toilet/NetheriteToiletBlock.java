package cn.tea.toilet.technology.block.toilet;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;


/**
 * 下界合金马桶方块
 * 继承自 PremiumToiletBlock，但使用独立的 BlockEntity 类型和 tick 处理器
 */
public class NetheriteToiletBlock extends PremiumToiletBlock {

    /**
     * 构造函数
     *
     * @param properties 方块属性配置
     * @param multiplier 效率系数（0.6-1.0，值越小效率越高）
     */
    public NetheriteToiletBlock(Properties properties, double multiplier) {
        super(properties, multiplier);
    }

    /**
     * 创建新的方块实体
     * 
     * @param pos 方块位置
     * @param state 方块状态
     * @return 下界合金马桶方块实体
     */
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new NetheriteToiletBlockEntity(pos, state);
    }

}