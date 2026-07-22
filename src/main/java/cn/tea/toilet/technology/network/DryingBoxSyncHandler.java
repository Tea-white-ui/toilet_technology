package cn.tea.toilet.technology.network;

import cn.tea.toilet.technology.block.drying.DryingBoxBlockEntity;
import cn.tea.toilet.technology.gui.dryingbox.DryingBoxMenu;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * 干燥箱同步包处理器
 * 在客户端接收并处理干燥箱同步数据
 * 
 * 处理逻辑：
 * 1. 检查玩家是否打开了干燥箱 GUI
 * 2. 如果是，更新 GUI Menu 中的本地进度数据
 * 3. 数据更新后，GUI 会自动重绘进度条
 * 
 * 注意：
 * - 此代码仅在客户端执行
 * - 通过 workQueue 确保在主线程运行
 */
public class DryingBoxSyncHandler {
    
    /**
     * 处理干燥箱同步包
     * 
     * @param payload 接收到的同步数据包
     */
    public static void handleSync(DryingBoxSyncPayload payload) {
        Minecraft mc = Minecraft.getInstance();
        if (mc == null || mc.player == null) {
            return;
        }
        
        Player player = mc.player;
        AbstractContainerMenu menu = player.containerMenu;
        
        // 检查玩家是否打开了干燥箱 GUI
        if (menu instanceof DryingBoxMenu dryingBoxMenu) {
            // 验证位置是否匹配
            if (dryingBoxMenu.getBlockEntity() != null) {
                BlockPos menuPos = dryingBoxMenu.getBlockEntity().getBlockPos();
                if (menuPos.equals(payload.blockPos())) {
                    // 更新本地进度数据
                    dryingBoxMenu.updateClientProgress(
                            payload.dryingProgress(),
                            payload.dryingTotalTime()
                    );
                }
            }
        }
    }
}