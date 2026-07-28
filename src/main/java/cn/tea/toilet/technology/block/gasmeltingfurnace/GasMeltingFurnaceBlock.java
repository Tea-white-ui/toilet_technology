package cn.tea.toilet.technology.block.gasmeltingfurnace;

import cn.tea.toilet.technology.block.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

/**
 * 高温金属熔炼用的燃气炉外壳。
 * 熔炼配方与处理逻辑将在后续功能中实现；当前仅提供持久化存储与自动化能力。
 */
public final class GasMeltingFurnaceBlock extends BaseEntityBlock {
    public static final MapCodec<GasMeltingFurnaceBlock> CODEC = simpleCodec(GasMeltingFurnaceBlock::new);

    public GasMeltingFurnaceBlock(Properties properties) {
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
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new GasMeltingFurnaceBlockEntity(pos, state);
    }

    @Override
    protected @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                            @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Block.box(0, 0, 0, 16, 16, 16);
    }

    @Override
    protected @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                                     @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return Block.box(0, 0, 0, 16, 16, 16);
    }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                            @NotNull BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock())) {
            level.invalidateCapabilities(pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}