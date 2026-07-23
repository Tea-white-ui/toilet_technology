package cn.tea.toilet.technology.block.septictank;

import cn.tea.toilet.technology.block.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public final class SepticTankStructure {
    private SepticTankStructure() {
    }

    public static boolean validate(Level level, BlockPos controllerPos, Direction facing) {
        SepticTankPattern.Layout layout = SepticTankPattern.layout(toPatternFacing(facing));
        for (SepticTankPattern.Cell cell : layout.walls()) {
            if (!level.getBlockState(offset(controllerPos, cell)).is(ModBlocks.SEPTIC_TANK_WALL.get())) {
                return false;
            }
        }
        for (SepticTankPattern.Cell cell : layout.air()) {
            if (!level.getBlockState(offset(controllerPos, cell)).isAir()) {
                return false;
            }
        }
        return true;
    }

    public static boolean contains(SepticTankPattern.Layout layout, BlockPos controllerPos, BlockPos target) {
        return layout.all().stream().map(cell -> offset(controllerPos, cell)).anyMatch(target::equals);
    }

    private static BlockPos offset(BlockPos origin, SepticTankPattern.Cell cell) {
        return origin.offset(cell.x(), cell.y(), cell.z());
    }

    private static SepticTankPattern.Facing toPatternFacing(Direction facing) {
        return switch (facing) {
            case SOUTH -> SepticTankPattern.Facing.SOUTH;
            case WEST -> SepticTankPattern.Facing.WEST;
            case EAST -> SepticTankPattern.Facing.EAST;
            default -> SepticTankPattern.Facing.NORTH;
        };
    }
}
