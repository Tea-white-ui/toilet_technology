package cn.tea.toilet.technology.block.drying;

import net.minecraft.core.Direction;

public class DryingRackConfig {

    public static final int SLOT_COUNT = 4;

    public static final float[][] ITEM_POSITIONS = {
            {0.25f, 0.25f},
            {0.25f, 0.75f},
            {0.75f, 0.25f},
            {0.75f, 0.75f}
    };

    public static final float[] ITEM_ROTATIONS = {
            45.0f,
            -45.0f,
            135.0f,
            -135.0f
    };

    public static final float ITEM_HEIGHT = 1.05f;
    public static final float ITEM_SCALE = 0.45f;

    public static double[] rotateTo(double localX, double localZ, Direction facing) {
        switch (facing.get2DDataValue()) {
            case 0:
                return new double[]{localX, localZ};
            case 1:
                return new double[]{localZ, 1.0 - localX};
            case 2:
                return new double[]{1.0 - localX, 1.0 - localZ};
            case 3:
                return new double[]{1.0 - localZ, localX};
            default:
                return new double[]{localX, localZ};
        }
    }
}