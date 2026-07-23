package cn.tea.toilet.technology;

import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/**
 * NeoForge AttachmentType 注册中心
 *
 * 用于把"易变的运行时状态"挂载到实体身上，替代过去的 static Map<UUID, ...> 方案：
 * - 实体死亡 / 下线 / 跨维度时由 NeoForge 自动清理，不会泄漏
 * - 避免 static Map 在玩家异常掉线时残留导致的内存/逻辑错误
 * - 不需要监听 PlayerLoggedOutEvent 手动清理
 *
 * 当前注册的 Attachment：
 * - TOILET_SQUAT_TICKS: 玩家蹲在马桶上的 tick 计数（PlayerToiletHandler 使用）
 */
public class ModAttachments {

    /** AttachmentType 延迟注册表 */
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ToiletTechnology.MOD_ID);

    /**
     * 玩家蹲马桶的 tick 计数
     * 默认 0，玩家实体被销毁时自动移除，无需手动清理
     */
    public static final Supplier<AttachmentType<Integer>> TOILET_SQUAT_TICKS =
            ATTACHMENT_TYPES.register("toilet_squat_ticks",
                    () -> AttachmentType.builder(() -> 0)
                            .serialize(Codec.INT)
                            .build());

    /**
     * 注册所有 AttachmentType 到事件总线
     *
     * @param bus NeoForge 模组事件总线
     */
    public static void register(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }
}
