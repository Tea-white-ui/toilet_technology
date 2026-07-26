package cn.tea.toilet.technology.block.absorptiontower;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbsorptionTowerStructureTest {
    @Test
    void formsWhenBottomAndEveryBodyBlockArePresentRegardlessOfTheirFacing() {
        assertTrue(AbsorptionTowerStructure.hasRequiredBlocks(true, true, true, true));
    }

    @Test
    void doesNotFormWhenBottomOrAnyBodyBlockIsMissing() {
        assertFalse(AbsorptionTowerStructure.hasRequiredBlocks(false, true, true, true));
        assertFalse(AbsorptionTowerStructure.hasRequiredBlocks(true, true, false, true));
    }
}
