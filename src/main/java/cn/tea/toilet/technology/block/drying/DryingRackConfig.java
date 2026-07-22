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

    public static final float ITEM_HEIGHT = 1.04f;
    public static final float ITEM_SCALE = 0.45f;

    public static double[] rotateTo(double localX, double localZ, Direction facing) {
        return switch (facing.get2DDataValue()) {
            case 0 -> new double[]{localX, localZ};
            case 1 -> new double[]{localZ, 1.0 - localX};
            case 2 -> new double[]{1.0 - localX, 1.0 - localZ};
            case 3 -> new double[]{1.0 - localZ, localX};
            default -> new double[]{localX, localZ};
        };
    }

    /**
     * 逆向旋转：将世界坐标转回原生坐标（用于点击检测）
     * rotateTo的逆变换，对于 SOUTH/NORTH与 rotateTo相同，
     * 对于 WEST/EAST互换公式 */
    public static double[] unrotateFrom(double worldX, double worldZ, Direction facing) {
        return switch (facing.get2DDataValue()) {
            case 0 -> new double[]{worldX, worldZ};
            case 1 -> new double[]{1.0 - worldZ, worldX};
            case 2 -> new double[]{1.0 - worldX,1.0 - worldZ};
            case 3 -> new double[]{worldZ,1.0 - worldX};
            default -> new double[]{worldX, worldZ};
        };
    }
}