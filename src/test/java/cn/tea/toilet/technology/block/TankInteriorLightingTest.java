package cn.tea.toilet.technology.block;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TankInteriorLightingTest {
    @Test
    void usesMaximumBlockLightForEveryInteriorCell() {
        assertEquals(15, TankInteriorLightGeometry.LIGHT_LEVEL);
    }

    @Test
    void translatesRelativeInteriorCellsFromTheControllerPosition() {
        TankInteriorLightGeometry.Position controller = new TankInteriorLightGeometry.Position(10, 64, -3);

        List<TankInteriorLightGeometry.Position> positions = TankInteriorLightGeometry.positions(controller, List.of(
                new TankInteriorLightGeometry.Offset(-1, -2, 3),
                new TankInteriorLightGeometry.Offset(0, 0, 0)
        ));

        assertEquals(List.of(new TankInteriorLightGeometry.Position(9, 62, 0), controller), positions);
    }

    @Test
    void preservesTheCompleteInteriorGeometryProvidedByTheStructure() {
        List<TankInteriorLightGeometry.Position> positions = TankInteriorLightGeometry.positions(new TankInteriorLightGeometry.Position(0, 0, 0), List.of(
                new TankInteriorLightGeometry.Offset(-1, -1, -1),
                new TankInteriorLightGeometry.Offset(1, -3, 1)
        ));

        assertTrue(positions.contains(new TankInteriorLightGeometry.Position(-1, -1, -1)));
        assertTrue(positions.contains(new TankInteriorLightGeometry.Position(1, -3, 1)));
    }
}
