package cn.tea.toilet.technology.block.biogaspond;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/** Pure geometry for a 5×5×5 hollow biogas pond with its controller at the top center. */
public final class BiogasPondPattern {
    private BiogasPondPattern() { }

    public record Cell(int x, int y, int z) { }

    public record Layout(Set<Cell> controllers, Set<Cell> walls, Set<Cell> air) {
        public List<Cell> all() {
            List<Cell> cells = new ArrayList<>(125);
            cells.addAll(controllers);
            cells.addAll(walls);
            cells.addAll(air);
            return List.copyOf(cells);
        }
    }

    public static Layout layout() {
        Set<Cell> controllers = Set.of(new Cell(0, 0, 0));
        Set<Cell> walls = new LinkedHashSet<>();
        Set<Cell> air = new LinkedHashSet<>();
        for (int y = -4; y <= 0; y++) for (int x = -2; x <= 2; x++) for (int z = -2; z <= 2; z++) {
            Cell cell = new Cell(x, y, z);
            if (x == 0 && y == 0 && z == 0) continue;
            if (x >= -1 && x <= 1 && y >= -3 && y <= -1 && z >= -1 && z <= 1) air.add(cell);
            else walls.add(cell);
        }
        return new Layout(controllers, Set.copyOf(walls), Set.copyOf(air));
    }
}
