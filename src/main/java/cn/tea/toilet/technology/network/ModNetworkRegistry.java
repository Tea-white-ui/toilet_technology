package cn.tea.toilet.technology.network;

import cn.tea.toilet.technology.ToiletTechnology;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.handling.DirectionalPayloadHandler;
import net.neoforged.neoforge.network.handling.IPayloadHandler;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

/**
 * 网络包注册器
 * 负责注册所有自定义网络包及其处理器
 * 
 * 设计原则：
 * 1. 所有网络包统一在此管理，便于维护和扩展
 * 2. 使用 NeoForge 的 PayloadRegistrar 进行注册，确保类型安全
 * 3. 每个网络包有独立的 Type 和 StreamCodec
 * 4. 处理器自动在正确的线程（主线程）执行
 */
public class ModNetworkRegistry {
    
    /**
     * 注册所有网络包
     * 在 RegisterPayloadHandlersEvent 事件中调用
     * 
     * @param registrar NeoForge 提供的包注册器
     */
    public static void register(PayloadRegistrar registrar) {
        ToiletTechnology.LOGGER.info("Registering network packets");
        
        // 注册干燥箱同步包（服务端 -> 客户端）
        registrar.playToClient(
            DryingBoxSyncPayload.TYPE,
            DryingBoxSyncPayload.STREAM_CODEC,
            createDryingBoxSyncHandler()
        );
        
        // 注册干燥架同步包（服务端 -> 客户端）
        registrar.playToClient(
            DryingRackSyncPayload.TYPE,
            DryingRackSyncPayload.STREAM_CODEC,
            createDryingRackSyncHandler()
        );
        
        // 注册马桶流体同步包（服务端 -> 客户端）
        registrar.playToClient(
            ToiletFluidSyncPayload.TYPE,
            ToiletFluidSyncPayload.STREAM_CODEC,
            createToiletFluidSyncHandler()
        );
        
        ToiletTechnology.LOGGER.info("Network packets registered successfully");
    }
    
    /**
     * 创建干燥箱同步包处理器
     * 用于同步干燥箱的干燥进度数据到客户端
     */
    private static IPayloadHandler<DryingBoxSyncPayload> createDryingBoxSyncHandler() {
        return (payload, context) -> {
            // 在客户端主线程执行更新逻辑
            context.enqueueWork(() -> {
                DryingBoxSyncHandler.handleSync(payload);
            });
        };
    }
    
    /**
     * 创建干燥架同步包处理器
     * 用于同步干燥架的物品和进度数据到客户端
     */
    private static IPayloadHandler<DryingRackSyncPayload> createDryingRackSyncHandler() {
        return (payload, context) -> {
            context.enqueueWork(() -> {
                DryingRackSyncHandler.handleSync(payload);
            });
        };
    }
    
    /**
     * 创建马桶流体同步包处理器
     * 用于同步马桶的流体数据到客户端
     */
    private static IPayloadHandler<ToiletFluidSyncPayload> createToiletFluidSyncHandler() {
        return (payload, context) -> {
            context.enqueueWork(() -> {
                ToiletFluidSyncHandler.handleSync(payload);
            });
        };
    }
}