package cn.tea.toilet.technology.block.absorptiontower;

import cn.tea.toilet.technology.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public final class AbsorptionTowerStructure {
    public static final int HEIGHT = 4;

    private AbsorptionTowerStructure() {
    }

    public static boolean validate(Level level, BlockPos bottomPos) {
        boolean hasBottom = level.getBlockState(bottomPos).is(ModBlocks.ABSORPTION_TOWER_BOTTOM.get());
        boolean[] hasBodies = new boolean[HEIGHT - 1];
        for (int y = 1; y < HEIGHT; y++) {
            hasBodies[y - 1] = level.getBlockState(bottomPos.above(y)).is(ModBlocks.ABSORPTION_TOWER_BODY.get());
        }
        return hasRequiredBlocks(hasBottom, hasBodies);
    }

    static boolean hasRequiredBlocks(boolean hasBottom, boolean... hasBodies) {
        if (!hasBottom || hasBodies.length != HEIGHT - 1) return false;
        for (boolean hasBody : hasBodies) {
            if (!hasBody) return false;
        }
        return true;
    }
}
