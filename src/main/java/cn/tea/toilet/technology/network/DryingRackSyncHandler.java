package cn.tea.toilet.technology.network;

import cn.tea.toilet.technology.block.drying.DryingRackBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

/**
 * 干燥架同步包处理器
 * 在客户端接收并处理干燥架同步数据
 * 
 * 处理逻辑：
 * 1. 获取客户端的干燥架 BlockEntity
 * 2. 更新物品和进度数据
 * 3. 触发渲染更新
 * 
 * 注意：
 * - 此代码仅在客户端执行
 * - 通过 workQueue 确保在主线程运行
 */
@OnlyIn(Dist.CLIENT)
public final class DryingRackSyncHandler {
    
    /**
     * 处理干燥架同步包
     * 
     * @param payload 接收到的同步数据包
     */
    public static void handleSync(DryingRackSyncPayload payload) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.level == null) {
            return;
        }
        
        Level level = mc.level;
        BlockEntity be = level.getBlockEntity(payload.blockPos());
        
        if (be instanceof DryingRackBlockEntity rackEntity) {
            rackEntity.applyClientSync(payload);
        }
    }
}