package cn.tea.toilet.technology.block.toilet;

import cn.tea.toilet.technology.block.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

/**
 * 下界合金马桶方块实体
 *
 * 公共逻辑（FluidTank 生命周期 / NBT / BlockEntity 同步）已抽到 {@link AbstractToiletBlockEntity}，
 * 这里只声明该子类的特化参数：
 * - 容量：16000 mB（是高级马桶的 2 倍）
 * - 无限粪液源：drain 粪液不消耗（用户明确要求保留此设计）
 *
 * 特点：
 * - 独立的 BlockEntity 类型注册
 * - 完整的 NBT 保存/加载支持（继承自基类）
 * - 完整的客户端同步支持（继承自基类）
 */
public class NetheriteToiletBlockEntity extends AbstractToiletBlockEntity {

    public NetheriteToiletBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.NETHERITE_TOILET.get(), pos, blockState);
    }

    @Override
    protected int getTankCapacity() {
        // 字面值常量：在基类构造函数中通过虚方法调用也安全
        return 16000;
    }

    @Override
    protected boolean isInfiniteFecesSource() {
        return true;
    }
}
