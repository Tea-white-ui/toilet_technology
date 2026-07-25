package cn.tea.toilet.technology.block.toilet;

import cn.tea.toilet.technology.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 高级马桶方块实体
 *
 * 公共逻辑（FluidTank 生命周期 / NBT / BlockEntity 同步）已抽到 {@link AbstractToiletBlockEntity}，
 * 这里只声明该子类的特化参数：
 * - 容量：8000 mB
 * - 非无限粪液源（drain 正常消耗）
 *
 * 流体变化时，基类通过标准 BlockEntity 更新包同步给客户端 BER。
 */
public class PremiumToiletBlockEntity extends AbstractToiletBlockEntity {

    public PremiumToiletBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.PREMIUM_TOILET.get(), pos, blockState);
    }

    @Override
    protected int getTankCapacity() {
        // 字面值常量：在基类构造函数中通过虚方法调用也安全
        return 8000;
    }

    @Override
    protected boolean isInfiniteFecesSource() {
        return false;
    }
}
