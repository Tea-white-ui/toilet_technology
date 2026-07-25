package cn.tea.toilet.technology.block.absorptiontower;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.gui.absorptiontower.AbsorptionTowerMenu;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class AbsorptionTowerBodyBlock extends BaseEntityBlock {
    public static final MapCodec<AbsorptionTowerBodyBlock> CODEC = simpleCodec(AbsorptionTowerBodyBlock::new);
    private static final VoxelShape SHAPE = Shapes.or(
            Block.box(1, 0, 1, 15, 16, 15),
            Block.box(2, 0, 0, 14, 16, 1),
            Block.box(0, 0, 2, 1, 16, 14),
            Block.box(2, 0, 15, 14, 16, 16),
            Block.box(15, 0, 2, 16, 16, 14));

    public AbsorptionTowerBodyBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HorizontalDirectionalBlock.FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(HorizontalDirectionalBlock.FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new AbsorptionTowerBodyBlockEntity(pos, state);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull net.minecraft.world.level.BlockGetter level,
                                            @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull net.minecraft.world.level.BlockGetter level,
                                                     @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level,
                                                        @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand,
                                                        @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide()) openMenu(level, pos, player);
        return ItemInteractionResult.sidedSuccess(level.isClientSide());
    }

    @Override
    protected @NotNull InteractionResult useWithoutItem(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                                                         @NotNull Player player, @NotNull BlockHitResult hitResult) {
        if (!level.isClientSide()) openMenu(level, pos, player);
        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    private static void openMenu(Level level, BlockPos pos, Player player) {
        for (int distance = 1; distance < AbsorptionTowerStructure.HEIGHT; distance++) {
            BlockPos controllerPos = pos.below(distance);
            if (!(level.getBlockEntity(controllerPos) instanceof AbsorptionTowerBottomBlockEntity controller)) continue;
            if (!controller.revalidateStructure()) return;
            player.openMenu(new SimpleMenuProvider(
                    (id, inventory, ignored) -> new AbsorptionTowerMenu(id, inventory, controller,
                            ContainerLevelAccess.create(level, controllerPos)),
                    Component.translatable("gui.toilet_technology.absorption_tower")));
            return;
        }
    }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull net.minecraft.world.level.Level level, @NotNull BlockPos pos,
                            @NotNull BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            for (int distance = 1; distance < AbsorptionTowerStructure.HEIGHT; distance++) {
                if (level.getBlockEntity(pos.below(distance)) instanceof AbsorptionTowerBottomBlockEntity bottom) {
                    bottom.revalidateStructure();
                    break;
                }
            }
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
