package cn.tea.toilet.technology.block.septictank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SepticTankPortTypeTest {
    @Test
    void portRolesAllowOnlyTheirDeclaredTransferDirection() {
        assertTrue(SepticTankPortType.LIQUID_INPUT.allowsInsertion());
        assertFalse(SepticTankPortType.LIQUID_INPUT.allowsExtraction());
        assertFalse(SepticTankPortType.GAS_OUTPUT.allowsInsertion());
        assertTrue(SepticTankPortType.GAS_OUTPUT.allowsExtraction());
    }
}