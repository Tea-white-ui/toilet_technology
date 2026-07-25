package cn.tea.toilet.technology.block.absorptiontower;

import cn.tea.toilet.technology.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;

public final class AbsorptionTowerStructure {
    public static final int HEIGHT = 4;

    private AbsorptionTowerStructure() {
    }

    public static boolean validate(Level level, BlockPos bottomPos) {
        if (!level.getBlockState(bottomPos).is(ModBlocks.ABSORPTION_TOWER_BOTTOM.get())) return false;
        Direction facing = level.getBlockState(bottomPos).getValue(HorizontalDirectionalBlock.FACING);
        for (int y = 1; y < HEIGHT; y++) {
            var state = level.getBlockState(bottomPos.above(y));
            if (!state.is(ModBlocks.ABSORPTION_TOWER_BODY.get())
                    || state.getValue(HorizontalDirectionalBlock.FACING) != facing) {
                return false;
            }
        }
        return true;
    }
}
