package cn.tea.toilet.technology.block.septictank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SepticTankBiogasProductionTest {

    @Test
    void producesReducedAmountWhenFecesLiquidIsBelowTenBuckets() {
        SepticTankBiogasProduction.Batch batch = SepticTankBiogasProduction.planBatch(
                true, 9_999, 0, 64_000);

        assertEquals(125, batch.gasProduced());
        assertEquals(0, batch.liquidConsumed());
    }

    @Test
    void producesBaseAmountWithoutConsumingFecesLiquidBelowThreshold() {
        SepticTankBiogasProduction.Batch batch = SepticTankBiogasProduction.planBatch(
                true, 10_000, 31_999, 64_000);

        assertEquals(250, batch.gasProduced());
        assertEquals(0, batch.liquidConsumed());
    }

    @Test
    void producesBaseAmountAtThreshold() {
        SepticTankBiogasProduction.Batch batch = SepticTankBiogasProduction.planBatch(
                true, 10_000, 32_000, 64_000);

        assertEquals(250, batch.gasProduced());
        assertEquals(0, batch.liquidConsumed());
    }

    @Test
    void producesOneBucketAndConsumesHalfBucketAboveThreshold() {
        SepticTankBiogasProduction.Batch batch = SepticTankBiogasProduction.planBatch(
                true, 10_000, 32_001, 64_000);

        assertEquals(1_000, batch.gasProduced());
        assertEquals(500, batch.liquidConsumed());
    }

    @Test
    void refusesIncompleteBatchOrWrongLiquid() {
        assertEquals(SepticTankBiogasProduction.Batch.NONE,
                SepticTankBiogasProduction.planBatch(false, 10_000, 0, 64_000));
        assertEquals(SepticTankBiogasProduction.Batch.NONE,
                SepticTankBiogasProduction.planBatch(true, 9_999, 63_876, 64_000));
        assertEquals(SepticTankBiogasProduction.Batch.NONE,
                SepticTankBiogasProduction.planBatch(true, 10_000, 63_001, 64_000));
    }

    @Test
    void productionIntervalIsFiveGameTicks() {
        assertEquals(5, SepticTankBiogasProduction.INTERVAL_TICKS);
    }

    @Test
    void invalidStructureResetsProductionProgress() {
        assertEquals(0, SepticTankBiogasProduction.nextProgress(4, false));
    }
}
