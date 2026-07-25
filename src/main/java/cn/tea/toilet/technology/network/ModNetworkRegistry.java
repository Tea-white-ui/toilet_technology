package cn.tea.toilet.technology.network;
import cn.tea.toilet.technology.ModConstants;

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
 *
 * 干燥箱进度同步说明（P1 #7 修复）：
 * - 干燥箱的进度同步已由 DryingBoxMenu 内置的 ContainerData 机制负责（Minecraft 原生增量同步）
 * - 不再需要 DryingBoxSyncPayload/DryingBoxSyncHandler，已删除以避免双轨同步造成的双倍网络流量
 */
public class ModNetworkRegistry {

    /**
     * 注册所有网络包
     * 在 RegisterPayloadHandlersEvent 事件中调用
     *
     * @param registrar NeoForge 提供的包注册器
     */
    public static void register(PayloadRegistrar registrar) {
        ModConstants.LOGGER.info("Registering network packets");

        // 注册干燥架同步包（服务端 -> 客户端）
        registrar.playToClient(
            DryingRackSyncPayload.TYPE,
            DryingRackSyncPayload.STREAM_CODEC,
            createDryingRackSyncHandler()
        );


        ModConstants.LOGGER.info("Network packets registered successfully");
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

}