package cn.tea.toilet.technology.block.biogaspond;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class BiogasPondWallBlock extends Block {
    public static final MapCodec<BiogasPondWallBlock> CODEC = simpleCodec(BiogasPondWallBlock::new);

    public BiogasPondWallBlock(Properties properties) { super(properties); }
    @Override protected @NotNull MapCodec<? extends Block> codec() { return CODEC; }

    @Override
    protected void onRemove(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState newState, boolean movedByPiston) {
        if (!state.is(newState.getBlock()) && !level.isClientSide()) notifyNearbyControllers(level, pos);
        super.onRemove(state, level, pos, newState, movedByPiston);
    }

    @Override
    protected void onPlace(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull BlockState oldState, boolean movedByPiston) {
        if (!oldState.is(state.getBlock()) && !level.isClientSide()) notifyNearbyControllers(level, pos);
        super.onPlace(state, level, pos, oldState, movedByPiston);
    }

    private static void notifyNearbyControllers(Level level, BlockPos wallPos) {
        for (BlockPos candidate : BlockPos.betweenClosed(wallPos.offset(-2, 0, -2), wallPos.offset(2, 4, 2))) {
            if (level.getBlockEntity(candidate) instanceof BiogasPondControllerBlockEntity controller) controller.revalidateStructure();
        }
    }
}
