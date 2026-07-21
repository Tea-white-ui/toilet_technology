package cn.tea.toilet.technology.block.drying;

import com.mojang.serialization.MapCodec;
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

public class DryingRackBlock extends BaseEntityBlock {

    public static final MapCodec<DryingRackBlock> CODEC = simpleCodec(DryingRackBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    protected static final VoxelShape SHAPE = Shapes.or(
            box(0, 0, 0, 2, 16, 2),
            box(0, 0, 14, 2, 16, 16),
            box(14, 0, 0, 16, 16, 2),
            box(14, 0, 14, 16, 16, 16),
            box(2, 14, 0, 14, 16, 2),
            box(2, 14, 14, 14, 16, 16),
            box(0, 14, 2, 2, 16, 14),
            box(14, 14, 2, 16, 16, 14),
            box(0, 11, 2, 2, 12, 14),
            box(14, 11, 2, 16, 12, 14),
            box(2, 11, 14, 14, 12, 16),
            box(2, 11, 0, 14, 12, 2),
            box(2, 15, 2, 14, 16, 14)
    );

    public DryingRackBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new DryingRackBlockEntity(pos, state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPE;
    }
}
