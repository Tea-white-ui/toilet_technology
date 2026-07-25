package cn.tea.toilet.technology.network;
import cn.tea.toilet.technology.ModConstants;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * 干燥架同步网络包
 * 用于服务端向客户端同步干燥架的物品和进度数据
 * 
 * 使用场景：
 * 1. 玩家放置/取出物品后，同步物品数据
 * 2. 干燥进度更新时，同步进度数据
 * 3. 客户端 BER 渲染需要最新的物品信息
 * 
 * 设计说明：
 * - 包含4个槽位的物品数据
 * - 包含4个槽位的干燥进度
 * - 使用 ItemStack.OPTIONAL_STREAM_CODEC 处理可能为空的物品
 * - 由于字段超过8个，使用手动编解码而非 StreamCodec.composite
 */
public record DryingRackSyncPayload(
        BlockPos blockPos,
        ItemStack slot0,
        ItemStack slot1,
        ItemStack slot2,
        ItemStack slot3,
        int progress0,
        int progress1,
        int progress2,
        int progress3,
        int totalTime0,
        int totalTime1,
        int totalTime2,
        int totalTime3
) implements CustomPacketPayload {
    
    /**
     * 网络包类型标识符
     */
    public static final Type<DryingRackSyncPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "drying_rack_sync")
    );
    
    /**
     * 流编解码器
     * 由于字段超过8个，手动实现 encode 和 decode 方法
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, DryingRackSyncPayload> STREAM_CODEC = new StreamCodec<>() {
        @Override
        public void encode(RegistryFriendlyByteBuf buf, DryingRackSyncPayload payload) {
            BlockPos.STREAM_CODEC.encode(buf, payload.blockPos);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, payload.slot0);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, payload.slot1);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, payload.slot2);
            ItemStack.OPTIONAL_STREAM_CODEC.encode(buf, payload.slot3);
            buf.writeInt(payload.progress0);
            buf.writeInt(payload.progress1);
            buf.writeInt(payload.progress2);
            buf.writeInt(payload.progress3);
            buf.writeInt(payload.totalTime0);
            buf.writeInt(payload.totalTime1);
            buf.writeInt(payload.totalTime2);
            buf.writeInt(payload.totalTime3);
        }
        
        @Override
        public DryingRackSyncPayload decode(RegistryFriendlyByteBuf buf) {
            BlockPos blockPos = BlockPos.STREAM_CODEC.decode(buf);
            ItemStack slot0 = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
            ItemStack slot1 = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
            ItemStack slot2 = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
            ItemStack slot3 = ItemStack.OPTIONAL_STREAM_CODEC.decode(buf);
            int progress0 = buf.readInt();
            int progress1 = buf.readInt();
            int progress2 = buf.readInt();
            int progress3 = buf.readInt();
            int totalTime0 = buf.readInt();
            int totalTime1 = buf.readInt();
            int totalTime2 = buf.readInt();
            int totalTime3 = buf.readInt();
            return new DryingRackSyncPayload(
                    blockPos, slot0, slot1, slot2, slot3,
                    progress0, progress1, progress2, progress3,
                    totalTime0, totalTime1, totalTime2, totalTime3
            );
        }
    };
    
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    /**
     * 获取指定槽位的物品
     */
    public ItemStack getSlotItem(int slot) {
        return switch (slot) {
            case 0 -> slot0;
            case 1 -> slot1;
            case 2 -> slot2;
            case 3 -> slot3;
            default -> ItemStack.EMPTY;
        };
    }
    
    /**
     * 获取指定槽位的干燥进度
     */
    public int getProgress(int slot) {
        return switch (slot) {
            case 0 -> progress0;
            case 1 -> progress1;
            case 2 -> progress2;
            case 3 -> progress3;
            default -> 0;
        };
    }
    
    /**
     * 获取指定槽位的总干燥时间
     */
    public int getTotalTime(int slot) {
        return switch (slot) {
            case 0 -> totalTime0;
            case 1 -> totalTime1;
            case 2 -> totalTime2;
            case 3 -> totalTime3;
            default -> 0;
        };
    }
}