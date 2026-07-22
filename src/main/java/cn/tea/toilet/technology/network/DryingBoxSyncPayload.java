package cn.tea.toilet.technology.network;

import cn.tea.toilet.technology.ToiletTechnology;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

/**
 * 干燥箱同步网络包
 * 用于服务端向客户端同步干燥箱的干燥进度数据
 * 
 * 使用场景：
 * 1. 玩家打开干燥箱 GUI 时，服务端发送完整数据
 * 2. 干燥进度变化时，定期同步给查看该方块的客户端
 * 
 * 设计说明：
 * - 使用 record 类型，不可变且线程安全
 * - StreamCodec 自动处理序列化/反序列化
 * - 包含完整的位置信息和进度数据
 */
public record DryingBoxSyncPayload(
        BlockPos blockPos,
        int[] dryingProgress,
        int[] dryingTotalTime
) implements CustomPacketPayload {
    
    /**
     * 网络包类型标识符
     * 必须全局唯一，使用模组ID + 包名称的格式
     */
    public static final Type<DryingBoxSyncPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, "drying_box_sync")
    );
    
    /**
     * int数组的StreamCodec
     * 先写入数组长度，再逐个写入元素
     */
    public static final StreamCodec<ByteBuf, int[]> INT_ARRAY_CODEC = new StreamCodec<ByteBuf, int[]>() {
        @Override
        public void encode(ByteBuf buf, int[] array) {
            buf.writeInt(array.length);
            for (int value : array) {
                buf.writeInt(value);
            }
        }
        
        @Override
        public int[] decode(ByteBuf buf) {
            int length = buf.readInt();
            int[] array = new int[length];
            for (int i = 0; i < length; i++) {
                array[i] = buf.readInt();
            }
            return array;
        }
    };
    
    /**
     * 流编解码器
     * 负责将网络包数据写入/读取网络缓冲区
     * 
     * 序列化顺序必须与 record 构造函数参数顺序一致：
     * 1. BlockPos (使用内置的 STREAM_CODEC)
     * 2. int[] 进度数组
     * 3. int[] 总时间数组
     */
    public static final StreamCodec<ByteBuf, DryingBoxSyncPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            DryingBoxSyncPayload::blockPos,
            INT_ARRAY_CODEC,
            DryingBoxSyncPayload::dryingProgress,
            INT_ARRAY_CODEC,
            DryingBoxSyncPayload::dryingTotalTime,
            DryingBoxSyncPayload::new
    );
    
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    /**
     * 创建空的同步包（用于初始化）
     */
    public static DryingBoxSyncPayload empty(BlockPos pos) {
        return new DryingBoxSyncPayload(pos, new int[16], new int[16]);
    }
}