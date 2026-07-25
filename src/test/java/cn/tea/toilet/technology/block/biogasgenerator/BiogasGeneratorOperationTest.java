package cn.tea.toilet.technology.block.biogasgenerator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BiogasGeneratorOperationTest {

    @Test
    void consumes5120FePerTickWhenBackTouchesSepticTankWall() {
        assertTrue(BiogasGeneratorOperation.canRun(true, 5_120));
        assertEquals(4_880, BiogasGeneratorOperation.energyAfterTick(true, 10_000));
    }

    @Test
    void doesNotConsumeEnergyWithoutWallOrEnoughEnergy() {
        assertFalse(BiogasGeneratorOperation.canRun(false, 10_000));
        assertFalse(BiogasGeneratorOperation.canRun(true, 5_119));
        assertEquals(5_119, BiogasGeneratorOperation.energyAfterTick(true, 5_119));
        assertEquals(10_000, BiogasGeneratorOperation.energyAfterTick(false, 10_000));
    }
}
