package cn.tea.toilet.technology.block.toilet;

import cn.tea.toilet.technology.compat.PoopSkyCompat;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.NotNull;

/**
 * 马桶方块实体抽象基类
 *
 * 抽出 PremiumToiletBlockEntity / NetheriteToiletBlockEntity 的公共逻辑：
 * - FluidTank 生命周期（容量由子类决定）
 * - NBT 保存/加载
 * - 客户端同步（getUpdateTag / getUpdatePacket / sendBlockUpdated）
 *
 * 子类只需实现：
 * - {@link #getTankCapacity()} 返回储罐容量（mB）
 * - {@link #isInfiniteFecesSource()} 是否抽取粪液不消耗（下界合金马桶的"无限粪液"设计）
 *
 * 同步语义：
 * - 流体内容变化时通过 BlockEntity 更新包同步给追踪此区块的客户端
 * - 区块初次加载时通过 getUpdateTag 提供同一份渲染数据
 *
 * 实现注意：
 * - fluidTank 字段不能是 final + 字段初始化，因为 getTankCapacity() 是虚方法，
 *   在字段初始化阶段调用会拿到子类尚未初始化的状态。
 *   因此在构造函数末尾（super() 返回、子类字段已就位后）再构建 tank。
 */
public abstract class AbstractToiletBlockEntity extends BlockEntity {


    /** 流体储罐，容量由子类决定；在构造函数末尾初始化 */
    public final FluidTank fluidTank;

    protected AbstractToiletBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        // 注意：必须在 super() 之后再创建 tank，否则 getTankCapacity() 会拿到子类未初始化的字段
        this.fluidTank = new FluidTank(getTankCapacity()) {
            @Override
            protected void onContentsChanged() {
                setChanged();
                if (level != null && !level.isClientSide()) {
                    syncVisualState();
                }
            }

            @Override
            public @NotNull FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
                // 子类标记"无限粪液源"时：drain 直接返回请求的量但不实际扣除
                // 这是下界合金马桶的"无限粪液"设计（用户明确要求保留）
                if (isInfiniteFecesSource()
                        && getFluidAmount() > 0
                        && PoopSkyCompat.isFecesLiquid(getFluid())) {
                    int amount = Math.min(getFluidAmount(), maxDrain);
                    return getFluid().copyWithAmount(amount);
                }
                return super.drain(maxDrain, action);
            }
        };
    }

    /**
     * 获取储罐容量（mB）
     * 子类必须实现：PremiumToilet = 8000，NetheriteToilet = 16000
     */
    protected abstract int getTankCapacity();

    /**
     * 是否为"无限粪液源"
     * true 表示 drain 粪液时不消耗马桶内的量（下界合金马桶设计）
     * false 表示正常消耗
     */
    protected abstract boolean isInfiniteFecesSource();

    /**
     * 通知追踪此区块的客户端重新接收 BlockEntity 更新标签。
     *
     * 流体状态既在这里的 getUpdateTag 中编码，也在区块加载时由同一机制提供，
     * 因而不需要为同一数据维护第二条 NeoForge payload 通道。
     */
    private void syncVisualState() {
        if (level == null || level.isClientSide()) {
            return;
        }
        level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
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
     * 获取当前流体（供 BER 渲染使用）
     */
    public FluidStack getFluid() {
        return this.fluidTank.getFluid();
    }
}
