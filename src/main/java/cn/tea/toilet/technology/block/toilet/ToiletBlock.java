package cn.tea.toilet.technology.block.toilet;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public abstract class ToiletBlock extends BaseEntityBlock {

    protected ToiletBlock(Properties properties) {
        super(properties);
    }

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    protected static final VoxelShape SHAPE_NORTH = Shapes.or(
            box(0, 0, 0, 16, 12, 16),
            box(0, 12, 0, 5, 16, 16),
            box(11, 12, 0, 16, 16, 16),
            box(5, 12, 0, 11, 16, 3),
            box(5, 12, 13, 11, 16, 16)
    );
    protected static final VoxelShape SHAPE_SOUTH = Shapes.or(
            box(0, 0, 0, 16, 12, 16),
            box(11, 12, 0, 16, 16, 16),
            box(0, 12, 0, 5, 16, 16),
            box(5, 12, 13, 11, 16, 16),
            box(5, 12, 0, 11, 16, 3)
    );
    protected static final VoxelShape SHAPE_WEST = Shapes.or(
            box(0, 0, 0, 16, 12, 16),
            box(0, 12, 11, 16, 16, 16),
            box(0, 12, 0, 16, 16, 5),
            box(0, 12, 5, 3, 16, 11),
            box(13, 12, 5, 16, 16, 11)
    );
    protected static final VoxelShape SHAPE_EAST = Shapes.or(
            box(0, 0, 0, 16, 12, 16),
            box(0, 12, 0, 16, 16, 5),
            box(0, 12, 11, 16, 16, 16),
            box(13, 12, 5, 16, 16, 11),
            box(0, 12, 5, 3, 16, 11)
    );

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return getToiletShape(state);
    }

    protected @NotNull VoxelShape getToiletShape(BlockState state) {
        return switch (state.getValue(FACING)) {
            case NORTH -> SHAPE_NORTH;
            case SOUTH -> SHAPE_SOUTH;
            case WEST -> SHAPE_WEST;
            case EAST -> SHAPE_EAST;
            default -> SHAPE_NORTH;
        };
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

}
