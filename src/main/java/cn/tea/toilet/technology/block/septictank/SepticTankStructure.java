package cn.tea.toilet.technology.block.septictank;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.block.TankInteriorLighting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public final class SepticTankStructure {
    private SepticTankStructure() {
    }

    public static boolean validate(Level level, BlockPos controllerPos, Direction facing) {
        SepticTankPattern.Layout layout = SepticTankPattern.layout(toPatternFacing(facing));
        for (SepticTankPattern.Cell cell : layout.walls()) {
            if (!isShellBlock(level.getBlockState(offset(controllerPos, cell)))) {
                return false;
            }
        }
        for (SepticTankPattern.Cell cell : layout.air()) {
            if (!TankInteriorLighting.isInteriorState(level.getBlockState(offset(controllerPos, cell)))) {
                return false;
            }
        }
        return true;
    }

    private static boolean isShellBlock(net.minecraft.world.level.block.state.BlockState state) {
        return state.is(ModBlocks.SEPTIC_TANK_WALL.get())
                || state.getBlock() instanceof SepticTankPortBlock;
    }

    public static void revalidateNearby(Level level, BlockPos changedPos) {
        for (SepticTankPattern.Facing facing : SepticTankPattern.Facing.values()) {
            for (SepticTankPattern.Cell cell : SepticTankPattern.layout(facing).walls()) {
                BlockPos controllerPos = changedPos.subtract(new BlockPos(cell.x(), cell.y(), cell.z()));
                if (level.getBlockEntity(controllerPos) instanceof SepticTankControllerBlockEntity controller) {
                    controller.revalidateStructure();
                }
            }
        }
    }

    public static boolean contains(SepticTankPattern.Layout layout, BlockPos controllerPos, BlockPos target) {
        return layout.all().stream().map(cell -> offset(controllerPos, cell)).anyMatch(target::equals);
    }

    public static java.util.List<BlockPos> interiorPositions(BlockPos controllerPos, Direction facing) {
        return SepticTankPattern.layout(toPatternFacing(facing)).air().stream()
                .map(cell -> offset(controllerPos, cell))
                .toList();
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
