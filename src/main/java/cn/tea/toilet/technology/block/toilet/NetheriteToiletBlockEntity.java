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
 * 下界合金马桶方块实体
 * 特点：
 * - 流体容量更大（16000mB，是普通高级马桶的2倍）
 * - 独立的 BlockEntity 类型注册
 * - 完整的 NBT 保存/加载支持
 * - 完整的客户端同步支持
 */
public class NetheriteToiletBlockEntity extends BlockEntity {

    /** 上次同步的tick计数，用于控制同步频率 */
    private int lastSyncTick = 0;

    /** 上次同步的流体状态，用于检测变化避免重复发送 */
    private FluidStack lastSyncedFluid = FluidStack.EMPTY;

    /** 流体储罐，容量16000mB */
    public final FluidTank fluidTank = new FluidTank(16000) {
        @Override
        protected void onContentsChanged() {
            setChanged();
            if (level != null && !level.isClientSide()) {
                syncToClients();
            }
        }

        @Override
        public @NotNull FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
            if (getFluidAmount() > 0 && getFluid().is(ModFluids.FECES_LIQUID.get())) {
                int amount = Math.min(getFluidAmount(), maxDrain);
                FluidStack toDrain = getFluid().copyWithAmount(amount);
                
                // 粪液不消耗，无论是否执行操作都保持原有量不变
                return toDrain;
            }
            return super.drain(maxDrain, action);
        }
    };

    public NetheriteToiletBlockEntity(BlockPos pos, BlockState blockState) {
        super(ModBlockEntities.NETHERITE_TOILET.get(), pos, blockState);
    }

    /**
     * Tick 逻辑 - 定期同步流体数据
     */
    public static void tick(Level level, BlockPos pos, BlockState state, NetheriteToiletBlockEntity entity) {
        if (level.isClientSide()) {
            return;
        }
        
        entity.lastSyncTick++;
        if (entity.lastSyncTick >= 20) {
            entity.lastSyncTick = 0;
            entity.syncToClients();
        }
    }

    /**
     * 同步流体数据到所有客户端
     * 使用自定义网络包，比 sendBlockUpdated 更高效
     * 优化：只在流体真正变化时才发送网络包，避免重复同步
     */
    private void syncToClients() {
        if (level == null || level.isClientSide()) {
            return;
        }

        FluidStack currentFluid = fluidTank.getFluid();
        
        // 不发送空的流体数据，避免编码错误（Empty FluidStack not allowed）
        if (currentFluid.isEmpty()) {
            lastSyncedFluid = FluidStack.EMPTY;
            return;
        }

        // 检测流体是否真正变化，避免重复发送相同数据
        if (lastSyncedFluid.equals(currentFluid)) {
            return;
        }

        // 更新上次同步的流体状态
        lastSyncedFluid = currentFluid.copy();

        ToiletFluidSyncPayload payload = new ToiletFluidSyncPayload(
                getBlockPos(),
                currentFluid
        );

        ModPacketSender.sendToTracking(level, getBlockPos(), payload);
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

    /**
     * 获取当前流体
     */
    public FluidStack getFluid() {
        return this.fluidTank.getFluid();
    }
}