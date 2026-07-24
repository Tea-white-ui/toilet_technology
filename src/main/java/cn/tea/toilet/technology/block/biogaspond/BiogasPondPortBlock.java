package cn.tea.toilet.technology.block.biogaspond;

import cn.tea.toilet.technology.block.ModBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.jetbrains.annotations.NotNull;

public class BiogasPondPortBlock extends BaseEntityBlock {
    public static final MapCodec<BiogasPondPortBlock> CODEC = simpleCodec(BiogasPondPortBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    private final BiogasPondPortType portType;

    public BiogasPondPortBlock(BiogasPondPortType portType, Properties properties) {
        super(properties);
        this.portType = portType;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    private BiogasPondPortBlock(Properties properties) {
        this(BiogasPondPortType.ITEM_INPUT, properties);
    }

    public BiogasPondPortType getPortType() {
        return portType;
    }

    @Override
    protected @NotNull MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new BiogasPondPortBlockEntity(pos, state, portType);
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
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                           @NotNull BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide() && !state.is(oldState.getBlock())) {
            BiogasPondStructure.revalidateNearby(level, pos);
        }
    }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                            @NotNull BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && !level.isClientSide()) {
            BiogasPondStructure.revalidateNearby(level, pos);
        }
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
