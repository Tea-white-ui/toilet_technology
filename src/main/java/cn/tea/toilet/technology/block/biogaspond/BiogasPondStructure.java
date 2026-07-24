package cn.tea.toilet.technology.block.biogaspond;

import cn.tea.toilet.technology.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class BiogasPondStructure {
    private BiogasPondStructure() { }

    public static boolean validate(Level level, BlockPos controllerPos) {
        BiogasPondPattern.Layout layout = BiogasPondPattern.layout();
        for (BiogasPondPattern.Cell cell : layout.walls()) {
            if (!level.getBlockState(offset(controllerPos, cell)).is(ModBlocks.BIOGAS_POND_WALL.get())) return false;
        }
        for (BiogasPondPattern.Cell cell : layout.air()) {
            if (!level.getBlockState(offset(controllerPos, cell)).isAir()) return false;
        }
        return true;
    }

    private static BlockPos offset(BlockPos origin, BiogasPondPattern.Cell cell) {
        return origin.offset(cell.x(), cell.y(), cell.z());
    }
}
