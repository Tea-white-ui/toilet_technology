package cn.tea.toilet.technology.block.biogaspond;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BiogasPondPortTypeTest {

    @Test
    void gasValveOnlyExposesItsCapabilityUpward() {
        assertTrue(BiogasPondPortType.GAS_OUTPUT.allowsCapabilityFrom(true, false));
        assertFalse(BiogasPondPortType.GAS_OUTPUT.allowsCapabilityFrom(false, true));
        assertFalse(BiogasPondPortType.GAS_OUTPUT.allowsCapabilityFrom(false, false));
    }

    @Test
    void horizontalPortsOnlyExposeTheirCapabilityFromTheFront() {
        for (BiogasPondPortType type : new BiogasPondPortType[] {
                BiogasPondPortType.ITEM_INPUT,
                BiogasPondPortType.FLUID_INPUT,
                BiogasPondPortType.ITEM_OUTPUT
        }) {
            assertTrue(type.allowsCapabilityFrom(false, true));
            assertFalse(type.allowsCapabilityFrom(false, false));
            assertFalse(type.allowsCapabilityFrom(true, false));
        }
    }

    @Test
    void portRolesAllowOnlyTheirDeclaredTransferDirection() {
        assertFalse(BiogasPondPortType.GAS_OUTPUT.allowsInsertion());
        assertTrue(BiogasPondPortType.GAS_OUTPUT.allowsExtraction());
        assertTrue(BiogasPondPortType.ITEM_INPUT.allowsInsertion());
        assertFalse(BiogasPondPortType.ITEM_INPUT.allowsExtraction());
        assertFalse(BiogasPondPortType.ITEM_OUTPUT.allowsInsertion());
        assertTrue(BiogasPondPortType.ITEM_OUTPUT.allowsExtraction());
        assertTrue(BiogasPondPortType.FLUID_INPUT.allowsInsertion());
        assertFalse(BiogasPondPortType.FLUID_INPUT.allowsExtraction());
    }
}
