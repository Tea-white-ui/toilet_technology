package cn.tea.toilet.technology.block.septictank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SepticTankInventoryLayoutTest {

    @Test
    void handlerSlotsHaveOneCentralRoleDefinition() {
        assertEquals(8, SepticTankInventoryLayout.SLOT_COUNT);
        assertEquals(0, SepticTankInventoryLayout.ITEM_INPUT_START);
        assertEquals(3, SepticTankInventoryLayout.ITEM_INPUT_END);
        assertEquals(3, SepticTankInventoryLayout.ITEM_OUTPUT_START);
        assertEquals(6, SepticTankInventoryLayout.ITEM_OUTPUT_END);
        assertEquals(6, SepticTankInventoryLayout.CONTAINER_INPUT);
        assertEquals(7, SepticTankInventoryLayout.CONTAINER_OUTPUT);
    }

    @Test
    void externalAndMenuPoliciesKeepOutputsProtected() {
        assertTrue(SepticTankInventoryLayout.allowsExternalInsertion(0));
        assertTrue(SepticTankInventoryLayout.allowsExternalInsertion(6));
        assertFalse(SepticTankInventoryLayout.allowsExternalInsertion(3));
        assertFalse(SepticTankInventoryLayout.allowsExternalInsertion(7));

        assertFalse(SepticTankInventoryLayout.allowsExternalExtraction(0));
        assertTrue(SepticTankInventoryLayout.allowsExternalExtraction(3));
        assertTrue(SepticTankInventoryLayout.allowsExternalExtraction(7));

        assertTrue(SepticTankInventoryLayout.allowsMenuExtraction(0));
        assertTrue(SepticTankInventoryLayout.allowsMenuExtraction(7));
    }
}
