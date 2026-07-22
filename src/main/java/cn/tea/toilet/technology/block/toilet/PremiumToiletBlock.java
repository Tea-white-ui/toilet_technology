package cn.tea.toilet.technology.block.toilet;

import cn.tea.toilet.technology.block.ModBlockEntities;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public  class PremiumToiletBlock extends ToiletBlock {

    public static final MapCodec<PremiumToiletBlock> CODEC = RecordCodecBuilder.mapCodec(instance ->
            instance.group(
                    propertiesCodec(),
                    Codec.DOUBLE.fieldOf("multiplier").forGetter(b -> b.multiplier)
            ).apply(instance, PremiumToiletBlock::new)
    );

    public PremiumToiletBlock(Properties properties) {
        this(properties, 1);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new PremiumToiletBlockEntity(pos, state);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    private final double multiplier;

    public PremiumToiletBlock(Properties properties, double multiplier) {
        super(properties);
        this.multiplier = multiplier;
    }

    public double getMultiplier() {
        return multiplier;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        var fluidHandler = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, null);
        if (fluidHandler != null && FluidUtil.interactWithFluidHandler(player, hand, fluidHandler)) {
            return ItemInteractionResult.sidedSuccess(level.isClientSide());
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    /**
     * 注册方块实体的 Tick 处理器
     * 用于定期同步流体数据到客户端
     */
    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.PREMIUM_TOILET.get(), PremiumToiletBlockEntity::tick);
    }
}