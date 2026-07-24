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
}
