package cn.tea.toilet.technology.block.absorptiontower;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class AbsorptionTowerOperationTest {
    @Test
    void processesOneBatchOnlyWhenAllStrictThresholdsAreExceeded() {
        assertTrue(AbsorptionTowerOperation.canProcess(51, 26, 26, 26));
        assertEquals(26, AbsorptionTowerOperation.inputGasAfterProcess(51));
        assertEquals(1, AbsorptionTowerOperation.inputWaterAfterProcess(26));
        assertEquals(25, AbsorptionTowerOperation.outputGasAfterProcess(0));
        assertEquals(25, AbsorptionTowerOperation.outputLiquidAfterProcess(0));
    }

    @Test
    void doesNotProcessAtOrBelowAnyRequiredThreshold() {
        assertFalse(AbsorptionTowerOperation.canProcess(50, 26, 26, 26));
        assertFalse(AbsorptionTowerOperation.canProcess(51, 25, 26, 26));
        assertFalse(AbsorptionTowerOperation.canProcess(51, 26, 25, 26));
        assertFalse(AbsorptionTowerOperation.canProcess(51, 26, 26, 25));
    }
}
