package cn.tea.toilet.technology.block.septictank;

import cn.tea.toilet.technology.block.ModBlockEntities;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.gas.GasTankTransferInteraction;
import cn.tea.toilet.technology.item.GasTankItem;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class SepticTankPortBlock extends BaseEntityBlock {
    public static final MapCodec<SepticTankPortBlock> CODEC = simpleCodec(SepticTankPortBlock::new);
    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    private final SepticTankPortType portType;

    public SepticTankPortBlock(SepticTankPortType portType, Properties properties) {
        super(properties);
        this.portType = portType;
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    private SepticTankPortBlock(Properties properties) {
        this(SepticTankPortType.LIQUID_INPUT, properties);
    }

    public SepticTankPortType getPortType() { return portType; }

    @Override protected @NotNull MapCodec<? extends BaseEntityBlock> codec() { return CODEC; }
    @Override public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SepticTankPortBlockEntity(pos, state, portType);
    }
    @Override protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) { builder.add(FACING); }
    @Override public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }
    @Override public @NotNull RenderShape getRenderShape(@NotNull BlockState state) { return RenderShape.MODEL; }

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state,
                                                        @NotNull Level level, @NotNull BlockPos pos,
                                                        @NotNull Player player, @NotNull InteractionHand hand,
                                                        @NotNull BlockHitResult hitResult) {
        if (!(stack.getItem() instanceof GasTankItem)) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        if (level.isClientSide()) return ItemInteractionResult.SUCCESS;
        if (!(level.getBlockEntity(pos) instanceof SepticTankPortBlockEntity port)) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        GasStack transferred = GasTankTransferInteraction.transfer(
                port.getGasHandler(Direction.UP), stack, player.isShiftKeyDown());
        return transferred.isEmpty() ? ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION : ItemInteractionResult.SUCCESS;
    }

    @Override protected void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                                     @NotNull BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        if (!level.isClientSide() && !state.is(oldState.getBlock())) SepticTankStructure.revalidateNearby(level, pos);
    }

    @Override protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos,
                                      @NotNull BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && !level.isClientSide()) SepticTankStructure.revalidateNearby(level, pos);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
