package cn.tea.toilet.technology.block.septictank;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SepticTankPatternTest {

    @Test
    void layoutContainsOneControllerThirtyThreeWallsAndTwoAirCells() {
        SepticTankPattern.Layout layout = SepticTankPattern.layout(SepticTankPattern.Facing.NORTH);

        assertEquals(Set.of(new SepticTankPattern.Cell(0, 0, 0)), layout.controllers());
        assertEquals(33, layout.walls().size());
        assertEquals(2, layout.air().size());
        assertEquals(36, layout.all().size());
    }

    @Test
    void controllerIsCenteredInEndFaceAndInteriorExtendsBehindIt() {
        SepticTankPattern.Layout layout = SepticTankPattern.layout(SepticTankPattern.Facing.NORTH);

        assertEquals(Set.of(
                new SepticTankPattern.Cell(0, 0, 1),
                new SepticTankPattern.Cell(0, 0, 2)
        ), layout.air());
        assertTrue(layout.walls().contains(new SepticTankPattern.Cell(-1, -1, 0)));
        assertTrue(layout.walls().contains(new SepticTankPattern.Cell(1, 1, 3)));
    }

    @Test
    void everyHorizontalFacingProducesTheSameUniqueVolume() {
        for (SepticTankPattern.Facing facing : SepticTankPattern.Facing.values()) {
            SepticTankPattern.Layout layout = SepticTankPattern.layout(facing);
            assertEquals(36, new HashSet<>(layout.all()).size());
            assertEquals(2, layout.air().size());
            assertTrue(layout.air().stream().allMatch(cell -> cell.y() == 0));
        }
    }
}
