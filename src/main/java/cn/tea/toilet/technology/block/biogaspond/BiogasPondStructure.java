package cn.tea.toilet.technology.block.biogaspond;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.block.TankInteriorLighting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class BiogasPondStructure {
    public static final int MAX_BIOGAS_GENERATORS = 3;

    private BiogasPondStructure() { }

    public static boolean validate(Level level, BlockPos controllerPos) {
        BiogasPondPattern.Layout layout = BiogasPondPattern.layout();
        int biogasGenerators = 0;
        for (BiogasPondPattern.Cell cell : layout.walls()) {
            BlockState state = level.getBlockState(offset(controllerPos, cell));
            if (!isShellBlock(state)) return false;
            if (state.is(ModBlocks.BIOGAS_GENERATOR.get()) && ++biogasGenerators > MAX_BIOGAS_GENERATORS) return false;
        }
        for (BiogasPondPattern.Cell cell : layout.air()) {
            if (!TankInteriorLighting.isInteriorState(level.getBlockState(offset(controllerPos, cell)))) return false;
        }
        return true;
    }

    public static boolean isShellBlock(BlockState state) {
        return state.is(ModBlocks.BIOGAS_POND_WALL.get())
                || state.is(ModBlocks.BIOGAS_GENERATOR.get())
                || state.getBlock() instanceof BiogasPondPortBlock;
    }

    public static void revalidateNearby(Level level, BlockPos changedPos) {
        for (BiogasPondPattern.Cell cell : BiogasPondPattern.layout().walls()) {
            BlockPos controllerPos = changedPos.subtract(new BlockPos(cell.x(), cell.y(), cell.z()));
            if (level.getBlockEntity(controllerPos) instanceof BiogasPondControllerBlockEntity controller) {
                controller.revalidateStructure();
            }
        }
    }

    public static java.util.List<BlockPos> interiorPositions(BlockPos controllerPos) {
        return BiogasPondPattern.layout().air().stream()
                .map(cell -> offset(controllerPos, cell))
                .toList();
    }

    private static BlockPos offset(BlockPos origin, BiogasPondPattern.Cell cell) {
        return origin.offset(cell.x(), cell.y(), cell.z());
    }
}
