package cn.tea.toilet.technology.block.drying;

import net.minecraft.core.Direction;

/**
 * 干燥架配置类
 * 定义干燥架的视觉和交互参数：
 * - 物品槽位数量（4个）
 * - 物品在干燥架上的位置坐标
 * - 物品旋转角度（用于3D渲染）
 * - 坐标旋转工具方法（处理方块朝向）
 */
public class DryingRackConfig {

    /** 物品槽位数量：干燥架最多可放置4个物品 */
    public static final int SLOT_COUNT = 4;

    /** 物品位置坐标数组：4个槽位在干燥架上的相对位置（0.0-1.0范围） */
    public static final float[][] ITEM_POSITIONS = {
            {0.25f, 0.25f},
            {0.25f, 0.75f},
            {0.75f, 0.25f},
            {0.75f, 0.75f}
    };

    /** 物品旋转角度数组：每个槽位物品的渲染旋转角度（度） */
    public static final float[] ITEM_ROTATIONS = {
            45.0f,
            -45.0f,
            135.0f,
            -135.0f
    };

    /** 物品渲染高度：物品在干燥架上的Y轴位置 */
    public static final float ITEM_HEIGHT = 1.04f;
    /** 物品渲染缩放比例：物品在3D渲染时的大小 */
    public static final float ITEM_SCALE = 0.45f;

    /**
     * 正向旋转：将本地坐标转换为世界坐标（根据方块朝向）
     * 用于放置物品时计算物品的世界位置
     * 
     * @param localX 本地X坐标（0.0-1.0）
     * @param localZ 本地Z坐标（0.0-1.0）
     * @param facing 方块朝向
     * @return 旋转后的世界坐标 [worldX, worldZ]
     */
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
     * 逆向旋转：将世界坐标转回本地坐标（用于点击检测）
     * rotateTo的逆变换，对于 SOUTH/NORTH与 rotateTo相同，
     * 对于 WEST/EAST互换公式
     * 
     * @param worldX 世界X坐标
     * @param worldZ 世界Z坐标
     * @param facing 方块朝向
     * @return 本地坐标 [localX, localZ]
     */
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