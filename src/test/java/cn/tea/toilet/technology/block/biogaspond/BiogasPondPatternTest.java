package cn.tea.toilet.technology.block.biogaspond;

import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BiogasPondPatternTest {

    @Test
    void layoutContainsTopCenterControllerNinetySevenWallsAndTwentySevenAirCells() {
        BiogasPondPattern.Layout layout = BiogasPondPattern.layout();

        assertEquals(Set.of(new BiogasPondPattern.Cell(0, 0, 0)), layout.controllers());
        assertEquals(97, layout.walls().size());
        assertEquals(27, layout.air().size());
        assertEquals(125, layout.all().size());
    }

    @Test
    void controllerIsCenteredInTopFaceAndInteriorIsThreeCubedAir() {
        BiogasPondPattern.Layout layout = BiogasPondPattern.layout();

        assertTrue(layout.air().contains(new BiogasPondPattern.Cell(-1, -1, -1)));
        assertTrue(layout.air().contains(new BiogasPondPattern.Cell(1, -3, 1)));
        assertTrue(layout.walls().contains(new BiogasPondPattern.Cell(-2, 0, -2)));
        assertTrue(layout.walls().contains(new BiogasPondPattern.Cell(2, -4, 2)));
    }

    @Test
    void everyCellInTheFiveCubedStructureIsUnique() {
        BiogasPondPattern.Layout layout = BiogasPondPattern.layout();

        assertEquals(125, new HashSet<>(layout.all()).size());
    }

    @Test
    void supportsUpToThreeBiogasGenerators() {
        assertEquals(3, BiogasPondStructure.MAX_BIOGAS_GENERATORS);
    }
}
