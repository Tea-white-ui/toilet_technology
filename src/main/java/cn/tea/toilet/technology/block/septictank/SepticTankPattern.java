package cn.tea.toilet.technology.block.septictank;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Pure geometry for the fixed 3×4×3 septic tank. */
public final class SepticTankPattern {
    private SepticTankPattern() {
    }

    public enum Facing { NORTH, SOUTH, WEST, EAST }

    public record Cell(int x, int y, int z) {
        Cell rotate(Facing facing) {
            return switch (facing) {
                case NORTH -> this;
                case SOUTH -> new Cell(-x, y, -z);
                case WEST -> new Cell(z, y, -x);
                case EAST -> new Cell(-z, y, x);
            };
        }
    }

    public record Layout(Set<Cell> controllers, Set<Cell> walls, Set<Cell> air) {
        public List<Cell> all() {
            List<Cell> result = new ArrayList<>(controllers.size() + walls.size() + air.size());
            result.addAll(controllers);
            result.addAll(walls);
            result.addAll(air);
            return List.copyOf(result);
        }
    }

    public static Layout layout(Facing facing) {
        Set<Cell> controllers = Set.of(new Cell(0, 0, 0));
        Set<Cell> walls = new LinkedHashSet<>();
        Set<Cell> air = new LinkedHashSet<>();

        for (int y = -1; y <= 1; y++) {
            for (int x = -1; x <= 1; x++) {
                for (int z = 0; z <= 3; z++) {
                    Cell cell = new Cell(x, y, z).rotate(facing);
                    if (x == 0 && y == 0 && (z == 1 || z == 2)) {
                        air.add(cell);
                    } else if (!(x == 0 && y == 0 && z == 0)) {
                        walls.add(cell);
                    }
                }
            }
        }
        return new Layout(controllers, Set.copyOf(walls), Set.copyOf(air));
    }
}
