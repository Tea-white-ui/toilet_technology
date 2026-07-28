package cn.tea.toilet.technology.block.sewagepurifier;

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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.EnumMap;
import java.util.Map;

public class SewagePurifierBlock extends BaseEntityBlock {
    public static final MapCodec<SewagePurifierBlock> CODEC = simpleCodec(SewagePurifierBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private static final VoxelShape NORTH_SHAPE = Shapes.or(
            box(1, 1, 0, 15, 15, 2),
            box(1, 1, 14, 15, 15, 16),
            box(2, 2, 2, 14, 14, 14),
            box(0, 5, 5, 2, 11, 11),
            box(14, 5, 5, 16, 11, 11),
            box(1, 1, 2, 15, 2, 14),
            box(1, 0, 2, 15, 1, 3),
            box(1, 0, 13, 15, 1, 14),
            box(1, 0, 3, 2, 1, 13),
            box(14, 0, 3, 15, 1, 13)
    );
    private static final Map<Direction, VoxelShape> SHAPES = createShapes();

    public SewagePurifierBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, net.minecraft.core.Direction.NORTH));
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SewagePurifierBlockEntity(pos, state);
    }

    @Override
    @Nullable
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        return level.isClientSide ? null : createTickerHelper(
                type, ModBlockEntities.SEWAGE_PURIFIER.get(), SewagePurifierBlockEntity::tick);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                        @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState state, @NotNull BlockGetter level,
                                                 @NotNull BlockPos pos, @NotNull CollisionContext context) {
        return SHAPES.get(state.getValue(FACING));
    }

    private static Map<Direction, VoxelShape> createShapes() {
        Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);
        shapes.put(Direction.NORTH, NORTH_SHAPE);
        shapes.put(Direction.EAST, rotateShape(Direction.EAST));
        shapes.put(Direction.SOUTH, rotateShape(Direction.SOUTH));
        shapes.put(Direction.WEST, rotateShape(Direction.WEST));
        return Map.copyOf(shapes);
    }

    private static VoxelShape rotateShape(Direction direction) {
        VoxelShape rotatedShape = Shapes.empty();
        for (var box : NORTH_SHAPE.toAabbs()) {
            double minX = box.minX * 16;
            double minY = box.minY * 16;
            double minZ = box.minZ * 16;
            double maxX = box.maxX * 16;
            double maxY = box.maxY * 16;
            double maxZ = box.maxZ * 16;

            rotatedShape = Shapes.or(rotatedShape, switch (direction) {
                case EAST -> box(16 - maxZ, minY, minX, 16 - minZ, maxY, maxX);
                case SOUTH -> box(16 - maxX, minY, 16 - maxZ, 16 - minX, maxY, 16 - minZ);
                case WEST -> box(minZ, minY, 16 - maxX, maxZ, maxY, 16 - minX);
                default -> throw new IllegalArgumentException("Expected a horizontal direction");
            });
        }
        return rotatedShape;
    }
}
