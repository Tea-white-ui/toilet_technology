package cn.tea.toilet.technology.block.septictank;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.gui.septictank.SepticTankMenu;
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
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SepticTankControllerBlock extends BaseEntityBlock {
    public static final MapCodec<SepticTankControllerBlock> CODEC = simpleCodec(SepticTankControllerBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public SepticTankControllerBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, net.minecraft.core.Direction.NORTH));
    }

    @Override protected @NotNull MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }
    @Override public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) { return new SepticTankControllerBlockEntity(pos, state); }
    @Override public @NotNull RenderShape getRenderShape(@NotNull BlockState state) { return RenderShape.MODEL; }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(FACING); }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.SEPTIC_TANK_CONTROLLER.get(), SepticTankControllerBlockEntity::tick);
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
                                                         @NotNull BlockPos pos, @NotNull Player player,
                                                         @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide()) openMenu(level, pos, player);
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private static void openMenu(Level level, BlockPos pos, Player player) {
        if (!(level.getBlockEntity(pos) instanceof SepticTankControllerBlockEntity controller)) return;
        if (!controller.revalidateStructure()) {
            player.displayClientMessage(Component.translatable("message.toilet_technology.septic_tank_invalid"), true);
            return;
        }
        player.openMenu(new SimpleMenuProvider(
                (id, inventory, ignored) -> new SepticTankMenu(id, inventory, controller, ContainerLevelAccess.create(level, pos)),
                Component.translatable("block.toilet_technology.septic_tank_controller")));
    }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && level.getBlockEntity(pos) instanceof SepticTankControllerBlockEntity controller) {
            controller.clearInteriorLighting();
            for (int i = 0; i < controller.items.getSlots(); i++) {
                ItemStack dropped = controller.items.extractItem(i, Integer.MAX_VALUE, false);
                if (!dropped.isEmpty()) Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), dropped);
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
