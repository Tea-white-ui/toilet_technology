package cn.tea.toilet.technology.block.biogasgenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BiogasGeneratorOperationTest {

    @Test
    void consumesOneHundredFeWhenBackTouchesSepticTankWall() {
        assertTrue(BiogasGeneratorOperation.canRun(true, 100));
        assertEquals(9_900, BiogasGeneratorOperation.energyAfterTick(true, 10_000));
    }

    @Test
    void doesNotConsumeEnergyWithoutWallOrEnoughEnergy() {
        assertFalse(BiogasGeneratorOperation.canRun(false, 10_000));
        assertFalse(BiogasGeneratorOperation.canRun(true, 99));
        assertEquals(99, BiogasGeneratorOperation.energyAfterTick(true, 99));
        assertEquals(10_000, BiogasGeneratorOperation.energyAfterTick(false, 10_000));
    }
}
