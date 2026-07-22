package cn.tea.toilet.technology.block.toilet;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.fluid.ModFluids;
import cn.tea.toilet.technology.network.ModPacketSender;
import cn.tea.toilet.technology.network.ToiletFluidSyncPayload;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

/**
 * 高级马桶方块实体
 * 负责管理马桶的流体存储
 * 
 * 网络同步说明：
 * - 使用自定义网络包 ToiletFluidSyncPayload 同步流体数据到客户端
 * - 仅在流体真正变化时发送，避免频繁网络通信
 * - 客户端接收后更新本地数据并触发渲染
 */
public class PremiumToiletBlockEntity extends BlockEntity {

    // 上次同步的tick计数，用于控制同步频率
    private int lastSyncTick = 0;

    public final FluidTank fluidTank = new FluidTank(8000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            // 只在服务端处理网络同步
            if (level != null && !level.isClientSide()) {
                // 使用自定义网络包替代 sendBlockUpdated
                syncToClients();
            }
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            if (getFluidAmount() > 0 && getFluid().is(ModFluids.FECES_LIQUID.get())) {
                if (action.simulate()) {
                    return getFluid().copyWithAmount(Math.min(getFluidAmount(), maxDrain));
                }
                return super.drain(maxDrain, action);
            }
            return super.drain(maxDrain, action);
        }
    };

    public PremiumToiletBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.PREMIUM_TOILET.get(), pos, blockState);
    }

    /**
     * 同步流体数据到所有客户端
     * 使用自定义网络包，比 sendBlockUpdated 更高效
     */
    private void syncToClients() {
        if (level == null || level.isClientSide()) {
            return;
        }
        
        // 不发送空的流体数据，避免编码错误
        if (fluidTank.isEmpty()) {
            return;
        }
        
        ToiletFluidSyncPayload payload = new ToiletFluidSyncPayload(
                getBlockPos(),
                fluidTank.getFluid()
        );
        
        ModPacketSender.sendToTracking(level, getBlockPos(), payload);
    }

    /**
     * Tick 逻辑 - 定期同步流体数据
     */
    public static void tick(Level level, BlockPos pos, BlockState state, PremiumToiletBlockEntity entity) {
        if (level.isClientSide()) {
            return;
        }
        
        // 每20tick同步一次流体数据
        entity.lastSyncTick++;
        if (entity.lastSyncTick >= 20 && !entity.fluidTank.isEmpty()) {
            entity.syncToClients();
            entity.lastSyncTick = 0;
        }
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        CompoundTag tag = super.getUpdateTag(registries);
        tag.put("FluidTank", fluidTank.writeToNBT(registries, new CompoundTag()));
        return tag;
    }

    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("FluidTank", fluidTank.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        super.loadAdditional(tag, registries);
        fluidTank.readFromNBT(registries, tag.getCompound("FluidTank"));
    }

    public FluidStack getFluid() {
        return this.fluidTank.getFluid();
    }
}