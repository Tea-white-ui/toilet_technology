package cn.tea.toilet.technology.block.biogaspond;

import cn.tea.toilet.technology.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class BiogasPondStructure {
    private BiogasPondStructure() { }

    public static boolean validate(Level level, BlockPos controllerPos) {
        BiogasPondPattern.Layout layout = BiogasPondPattern.layout();
        int biogasGenerators = 0;
        for (BiogasPondPattern.Cell cell : layout.walls()) {
            BlockState state = level.getBlockState(offset(controllerPos, cell));
            if (!isShellBlock(state)) return false;
            if (state.is(ModBlocks.BIOGAS_GENERATOR.get()) && ++biogasGenerators > 1) return false;
        }
        for (BiogasPondPattern.Cell cell : layout.air()) {
            if (!level.getBlockState(offset(controllerPos, cell)).isAir()) return false;
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

    private static BlockPos offset(BlockPos origin, BiogasPondPattern.Cell cell) {
        return origin.offset(cell.x(), cell.y(), cell.z());
    }
}
