package cn.tea.toilet.technology.block.biogaspond;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.gui.biogaspond.BiogasPondMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BiogasPondControllerBlock extends BaseEntityBlock {
    public static final MapCodec<BiogasPondControllerBlock> CODEC = simpleCodec(BiogasPondControllerBlock::new);

    public BiogasPondControllerBlock(Properties properties) { super(properties); }
    @Override protected @NotNull MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }
    @Override public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) { return new BiogasPondControllerBlockEntity(pos, state); }
    @Override public @NotNull RenderShape getRenderShape(@NotNull BlockState state) { return RenderShape.MODEL; }

    @Override @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.BIOGAS_POND_CONTROLLER.get(), BiogasPondControllerBlockEntity::tick);
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level,
                                                        @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand,
                                                        @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide()) openMenu(level, pos, player);
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level,
                                                         @NotNull BlockPos pos, @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide()) openMenu(level, pos, player);
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private static void openMenu(Level level, BlockPos pos, Player player) {
        if (!(level.getBlockEntity(pos) instanceof BiogasPondControllerBlockEntity controller)) return;
        if (!controller.revalidateStructure()) {
            player.displayClientMessage(Component.translatable("message.toilet_technology.biogas_pond_invalid"), true);
            return;
        }
        player.openMenu(new SimpleMenuProvider(
                (id, inventory, ignored) -> new BiogasPondMenu(id, inventory, controller, ContainerLevelAccess.create(level, pos)),
                Component.translatable("block.toilet_technology.biogas_pond_controller")));
    }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof BiogasPondControllerBlockEntity controller) {
            for (int slot = 0; slot < controller.items.getSlots(); slot++) {
                ItemStack dropped = controller.items.extractItem(slot, Integer.MAX_VALUE, false);
                if (!dropped.isEmpty()) Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), dropped);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
