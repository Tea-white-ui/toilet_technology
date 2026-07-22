package cn.tea.toilet.technology.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.network.PacketDistributor;

/**
 * 网络包发送工具类
 * 提供统一的包发送方法，简化网络通信
 * 
 * 使用示例：
 * ModPacketSender.sendToPlayer(player, new DryingBoxSyncPayload(...));
 * ModPacketSender.sendToTracking(level, pos, new DryingRackSyncPayload(...));
 */
public class ModPacketSender {
    
    /**
     * 发送网络包到指定玩家
     * 
     * @param player  目标玩家
     * @param payload 网络包数据
     */
    public static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload) {
        PacketDistributor.sendToPlayer(player, payload);
    }
    
    /**
     * 发送网络包到所有玩家
     * 
     * @param payload 网络包数据
     */
    public static void sendToAllPlayers(CustomPacketPayload payload) {
        PacketDistributor.sendToAllPlayers(payload);
    }
    
    /**
     * 发送网络包到指定维度所有玩家
     * 
     * @param level   维度（必须是服务端）
     * @param payload 网络包数据
     */
    public static void sendToDimension(Level level, CustomPacketPayload payload) {
        if (level instanceof ServerLevel serverLevel) {
            PacketDistributor.sendToPlayersInDimension(serverLevel, payload);
        }
    }
    
    /**
     * 发送网络包到所有正在追踪指定位置的玩家
     * 适用于 BlockEntity 数据同步，只发送给能看到该方块的客户端
     * 
     * @param level   世界（必须是服务端）
     * @param pos     方块位置
     * @param payload 网络包数据
     */
    public static void sendToTracking(Level level, BlockPos pos, CustomPacketPayload payload) {
        if (!(level instanceof ServerLevel serverLevel)) {
            return;
        }
        ChunkPos chunkPos = new ChunkPos(pos);
        PacketDistributor.sendToPlayersTrackingChunk(serverLevel, chunkPos, payload);
    }
}