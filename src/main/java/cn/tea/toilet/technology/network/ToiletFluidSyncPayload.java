package cn.tea.toilet.technology.network;

import cn.tea.toilet.technology.ToiletTechnology;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

/**
 * 马桶流体同步网络包
 * 用于服务端向客户端同步马桶的流体数据
 * 
 * 使用场景：
 * 1. 流体量变化时同步给客户端
 * 2. 客户端 BER 渲染流体需要最新数据
 * 
 * 设计说明：
 * - FluidStack 使用内置的 STREAM_CODEC（需要 RegistryFriendlyByteBuf）
 * - 包含位置信息用于定位 BlockEntity
 */
public record ToiletFluidSyncPayload(
        BlockPos blockPos,
        FluidStack fluidStack
) implements CustomPacketPayload {
    
    /**
     * 网络包类型标识符
     */
    public static final Type<ToiletFluidSyncPayload> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, "toilet_fluid_sync")
    );
    
    /**
     * 流编解码器
     * 注意：FluidStack.STREAM_CODEC 需要 RegistryFriendlyByteBuf
     */
    public static final StreamCodec<RegistryFriendlyByteBuf, ToiletFluidSyncPayload> STREAM_CODEC = StreamCodec.composite(
            BlockPos.STREAM_CODEC,
            ToiletFluidSyncPayload::blockPos,
            FluidStack.STREAM_CODEC,
            ToiletFluidSyncPayload::fluidStack,
            ToiletFluidSyncPayload::new
    );
    
    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}