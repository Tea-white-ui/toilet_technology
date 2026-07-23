package cn.tea.toilet.technology.network;

import cn.tea.toilet.technology.block.toilet.AbstractToiletBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * 马桶流体同步包处理器
 * 在客户端接收并处理马桶流体同步数据
 *
 * 处理逻辑：
 * 1. 获取客户端的马桶 BlockEntity
 * 2. 更新流体数据
 * 3. 触发渲染更新
 *
 * 注意：
 * - 此代码仅在客户端执行
 * - 通过 workQueue 确保在主线程运行
 * - 通过 AbstractToiletBlockEntity 上界匹配，同时覆盖 PremiumToilet 和 NetheriteToilet
 */
public class ToiletFluidSyncHandler {

    /**
     * 处理马桶流体同步包
     *
     * @param payload 接收到的同步数据包
     */
    public static void handleSync(ToiletFluidSyncPayload payload) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.level == null) {
            return;
        }

        Level level = mc.level;
        BlockEntity be = level.getBlockEntity(payload.blockPos());

        if (be instanceof AbstractToiletBlockEntity toiletEntity) {
            // 更新流体数据
            toiletEntity.fluidTank.setFluid(payload.fluidStack());

            // 标记数据已改变，触发渲染更新
            toiletEntity.setChanged();
        }
    }
}
