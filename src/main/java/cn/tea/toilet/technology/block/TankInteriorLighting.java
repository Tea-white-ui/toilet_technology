package cn.tea.toilet.technology.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.state.BlockState;

/** Maintains invisible vanilla light blocks inside formed multi-block tanks. */
public final class TankInteriorLighting {
    public static final int LIGHT_LEVEL = TankInteriorLightGeometry.LIGHT_LEVEL;

    private TankInteriorLighting() {
    }

    public static boolean isInteriorState(BlockState state) {
        return state.isAir() || state.is(Blocks.LIGHT) && state.getValue(LightBlock.LEVEL) == LIGHT_LEVEL;
    }

    public static void apply(Level level, Iterable<BlockPos> positions) {
        BlockState lightState = Blocks.LIGHT.defaultBlockState().setValue(LightBlock.LEVEL, LIGHT_LEVEL);
        for (BlockPos lightPos : positions) {
            if (level.getBlockState(lightPos).isAir()) {
                level.setBlock(lightPos, lightState, 2 | 16);
            }
        }
    }

    public static void clear(Level level, Iterable<BlockPos> positions) {
        for (BlockPos lightPos : positions) {
            BlockState state = level.getBlockState(lightPos);
            if (state.is(Blocks.LIGHT) && state.getValue(LightBlock.LEVEL) == LIGHT_LEVEL) {
                level.setBlock(lightPos, Blocks.AIR.defaultBlockState(), 2 | 16);
            }
        }
    }
}
