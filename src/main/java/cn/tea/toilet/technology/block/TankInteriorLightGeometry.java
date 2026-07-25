package cn.tea.toilet.technology.block;

import java.util.ArrayList;
import java.util.List;

/** Pure coordinate translation shared by multi-block tank interior lighting. */
public final class TankInteriorLightGeometry {
    public static final int LIGHT_LEVEL = 15;

    private TankInteriorLightGeometry() {
    }

    public record Offset(int x, int y, int z) {
    }

    public record Position(int x, int y, int z) {
    }

    public static List<Position> positions(Position controller, List<Offset> offsets) {
        List<Position> result = new ArrayList<>(offsets.size());
        for (Offset offset : offsets) {
            result.add(new Position(
                    controller.x() + offset.x(),
                    controller.y() + offset.y(),
                    controller.z() + offset.z()));
        }
        return List.copyOf(result);
    }
}
