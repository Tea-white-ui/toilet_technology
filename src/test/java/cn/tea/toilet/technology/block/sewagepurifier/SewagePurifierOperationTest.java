package cn.tea.toilet.technology.block.sewagepurifier;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SewagePurifierOperationTest {

    @Test
    void processesOneBatchWhenAllRequirementsAreMet() {
        assertTrue(SewagePurifierOperation.canProcess(32_000, 128, 32_000, true));
        assertEquals(32_000, SewagePurifierOperation.outputWaterAfterProcess(0));
        assertEquals(0, SewagePurifierOperation.inputFecesLiquidAfterProcess(32_000));
        assertEquals(0, SewagePurifierOperation.energyAfterProcess(128));
    }

    @Test
    void doesNotProcessWhenAnyRequirementIsMissing() {
        assertFalse(SewagePurifierOperation.canProcess(31_999, 128, 32_000, true));
        assertFalse(SewagePurifierOperation.canProcess(32_000, 127, 32_000, true));
        assertFalse(SewagePurifierOperation.canProcess(32_000, 128, 31_999, true));
        assertFalse(SewagePurifierOperation.canProcess(32_000, 128, 32_000, false));
    }
}
