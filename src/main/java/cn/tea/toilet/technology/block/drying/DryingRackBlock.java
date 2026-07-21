package cn.tea.toilet.technology.block.drying;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
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

    @Override
    protected @NotNull ItemInteractionResult useItemOn(@NotNull ItemStack stack, @NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand hand, @NotNull BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return ItemInteractionResult.SUCCESS;
        }
        
        if (level.getBlockEntity(pos) instanceof DryingRackBlockEntity blockEntity) {
            if (player.isShiftKeyDown() && stack.isEmpty()) {
                return handleTakeItem(blockEntity, player, level, pos, hitResult, state);
            } else if (!player.isShiftKeyDown() && !stack.isEmpty()) {
                return handlePlaceItem(blockEntity, stack, player, level, pos);
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private @NotNull ItemInteractionResult handleTakeItem(DryingRackBlockEntity blockEntity, Player player, Level level, BlockPos pos, BlockHitResult hitResult, BlockState state) {
        Vec3 hitWorldPos = hitResult.getLocation();
        
        double localX = hitWorldPos.x - pos.getX();
        double localZ = hitWorldPos.z - pos.getZ();
        
        localX = Math.clamp(localX, 0.0, 1.0);
        localZ = Math.clamp(localZ, 0.0, 1.0);
        
        net.minecraft.core.Direction facing = state.getValue(FACING);
        double[] rotatedPos = DryingRackConfig.rotateTo(localX, localZ, facing);
        
        int closestSlot = -1;
        double minDistanceSq = Double.MAX_VALUE;
        
        for (int i = 0; i < DryingRackConfig.SLOT_COUNT; i++) {
            if (!blockEntity.itemHandler.getStackInSlot(i).isEmpty()) {
                double dx = rotatedPos[0] - DryingRackConfig.ITEM_POSITIONS[i][0];
                double dz = rotatedPos[1] - DryingRackConfig.ITEM_POSITIONS[i][1];
                double distanceSq = dx * dx + dz * dz;
                
                if (distanceSq < minDistanceSq) {
                    minDistanceSq = distanceSq;
                    closestSlot = i;
                }
            }
        }
        
        if (closestSlot != -1) {
            ItemStack itemInSlot = blockEntity.itemHandler.getStackInSlot(closestSlot);
            ItemStack extracted = blockEntity.itemHandler.extractItem(closestSlot, itemInSlot.getCount(), false);
            if (!extracted.isEmpty()) {
                if (!player.getInventory().add(extracted)) {
                    player.drop(extracted, false);
                }
                level.sendBlockUpdated(pos, state, state, 2);
                return ItemInteractionResult.sidedSuccess(level.isClientSide());
            }
        }
        
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    private @NotNull ItemInteractionResult handlePlaceItem(DryingRackBlockEntity blockEntity, ItemStack stack, Player player, Level level, BlockPos pos) {
        if (stack.isEmpty()) {
            return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        }

        for (int i = 0; i < DryingRackConfig.SLOT_COUNT; i++) {
            if (blockEntity.itemHandler.getStackInSlot(i).isEmpty()) {
                ItemStack toInsert = stack.copyWithCount(1);
                ItemStack remaining = blockEntity.itemHandler.insertItem(i, toInsert, false);
                if (remaining.isEmpty()) {
                    stack.shrink(1);
                    level.sendBlockUpdated(pos, blockEntity.getBlockState(), blockEntity.getBlockState(), 2);
                    return ItemInteractionResult.sidedSuccess(level.isClientSide());
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}