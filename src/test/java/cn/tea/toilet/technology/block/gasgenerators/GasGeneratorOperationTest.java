package cn.tea.toilet.technology.block.gasgenerators;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GasGeneratorOperationTest {
    @Test
    void biogasProducesOneHundredFePerTick() {
        GasGeneratorOperation.Fuel fuel = GasGeneratorOperation.forGasId("toilet_technology:biogas");

        assertEquals(new GasGeneratorOperation.Fuel(40, 100), fuel);
        assertTrue(GasGeneratorOperation.canGenerate(fuel, 1, 0));
        assertEquals(100, GasGeneratorOperation.energyAfterTick(fuel, 0));
    }

    @Test
    void methaneProducesEightHundredFePerTick() {
        GasGeneratorOperation.Fuel fuel = GasGeneratorOperation.forGasId("toilet_technology:methane");

        assertEquals(new GasGeneratorOperation.Fuel(20, 800), fuel);
        assertTrue(GasGeneratorOperation.canGenerate(fuel, 1, 19_680));
        assertEquals(20_480, GasGeneratorOperation.energyAfterTick(fuel, 19_680));
    }

    @Test
    void doesNotConsumeGasWhenStorageCannotAcceptTheWholeTick() {
        GasGeneratorOperation.Fuel fuel = GasGeneratorOperation.forGasId("toilet_technology:methane");

        assertFalse(GasGeneratorOperation.canGenerate(fuel, 1, 19_681));
        assertFalse(GasGeneratorOperation.canGenerate(fuel, 0, 0));
        assertFalse(GasGeneratorOperation.canGenerate(null, 1, 0));
        assertEquals(19_681, GasGeneratorOperation.energyAfterTick(fuel, 19_681));
    }
}
