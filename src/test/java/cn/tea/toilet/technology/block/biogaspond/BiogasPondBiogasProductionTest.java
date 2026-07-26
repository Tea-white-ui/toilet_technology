package cn.tea.toilet.technology.block.biogaspond;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BiogasPondBiogasProductionTest {

    @Test
    void producesTenMillibucketsBelowTenBuckets() {
        assertEquals(10, BiogasPondBiogasProduction.planBatch(true, 9_999, 0, 128_000).gasProduced());
    }

    @Test
    void producesTwentyMillibucketsFromTenThroughThirtyTwoBuckets() {
        assertEquals(20, BiogasPondBiogasProduction.planBatch(true, 10_000, 0, 128_000).gasProduced());
        assertEquals(20, BiogasPondBiogasProduction.planBatch(true, 32_000, 0, 128_000).gasProduced());
    }

    @Test
    void producesFiftyMillibucketsAboveThirtyTwoBuckets() {
        BiogasPondBiogasProduction.Batch batch = BiogasPondBiogasProduction.planBatch(true, 32_001, 0, 128_000);

        assertEquals(50, batch.gasProduced());
        assertEquals(25, batch.liquidConsumed());
    }

    @Test
    void refusesProductionWithoutFecesLiquidOrCapacity() {
        assertEquals(BiogasPondBiogasProduction.Batch.NONE,
                BiogasPondBiogasProduction.planBatch(false, 64_000, 0, 128_000));
        assertEquals(BiogasPondBiogasProduction.Batch.NONE,
                BiogasPondBiogasProduction.planBatch(true, 9_999, 127_991, 128_000));
    }

    @Test
    void doublesGasOutputWithoutChangingLiquidConsumptionWhenGeneratorIsPowered() {
        BiogasPondBiogasProduction.Batch batch = BiogasPondBiogasProduction.planBatch(
                true, 32_001, 0, 128_000, 2);

        assertEquals(100, batch.gasProduced());
        assertEquals(50, batch.liquidConsumed());
    }

    @Test
    void scalesGasAndLiquidConsumptionForTwoAndThreeGenerators() {
        BiogasPondBiogasProduction.Batch twoGenerators = BiogasPondBiogasProduction.planBatch(
                true, 32_001, 0, 128_000, 4);
        BiogasPondBiogasProduction.Batch threeGenerators = BiogasPondBiogasProduction.planBatch(
                true, 32_001, 0, 128_000, 6);

        assertEquals(200, twoGenerators.gasProduced());
        assertEquals(100, twoGenerators.liquidConsumed());
        assertEquals(300, threeGenerators.gasProduced());
        assertEquals(150, threeGenerators.liquidConsumed());
    }

    @Test
    void doesNotConsumeLiquidAtExactlyThirtyTwoBuckets() {
        BiogasPondBiogasProduction.Batch batch = BiogasPondBiogasProduction.planBatch(
                true, 32_000, 0, 128_000, 6);

        assertEquals(120, batch.gasProduced());
        assertEquals(0, batch.liquidConsumed());
    }

    @Test
    void fallsBackToBaseOutputWhenGeneratorCannotSupplyEnergy() {
        BiogasPondBiogasProduction.Batch batch = BiogasPondBiogasProduction.planBatch(
                true, 32_001, 0, 128_000, 2, false);

        assertEquals(50, batch.gasProduced());
        assertEquals(25, batch.liquidConsumed());
    }

    @Test
    void refusesDoubledOutputWhenOnlyBaseOutputWouldFit() {
        assertEquals(BiogasPondBiogasProduction.Batch.NONE,
                BiogasPondBiogasProduction.planBatch(true, 10_000, 127_970, 128_000, 2));
    }

    @Test
    void productionIntervalIsFiveGameTicks() {
        assertEquals(5, BiogasPondBiogasProduction.INTERVAL_TICKS);
    }

    @Test
    void residueProductionUsesFivePercentOfOneHundredPossibleRolls() {
        assertTrue(BiogasPondBiogasProduction.shouldProduceResidue(0));
        assertTrue(BiogasPondBiogasProduction.shouldProduceResidue(4));
        assertFalse(BiogasPondBiogasProduction.shouldProduceResidue(5));
        assertFalse(BiogasPondBiogasProduction.shouldProduceResidue(99));
    }
}
