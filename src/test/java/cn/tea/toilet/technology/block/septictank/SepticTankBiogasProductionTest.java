package cn.tea.toilet.technology.block.septictank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SepticTankBiogasProductionTest {

    @Test
    void producesReducedAmountWhenFecesLiquidIsBelowTenBuckets() {
        SepticTankBiogasProduction.Batch batch = SepticTankBiogasProduction.planBatch(
                true, 9_999, 0, 64_000);

        assertEquals(10, batch.gasProduced());
        assertEquals(0, batch.liquidConsumed());
    }

    @Test
    void producesBaseAmountWithoutConsumingFecesLiquidBelowThreshold() {
        SepticTankBiogasProduction.Batch batch = SepticTankBiogasProduction.planBatch(
                true, 10_000, 31_999, 64_000);

        assertEquals(20, batch.gasProduced());
        assertEquals(0, batch.liquidConsumed());
    }

    @Test
    void producesBaseAmountAtThreshold() {
        SepticTankBiogasProduction.Batch batch = SepticTankBiogasProduction.planBatch(
                true, 32_000, 0, 64_000);

        assertEquals(20, batch.gasProduced());
        assertEquals(0, batch.liquidConsumed());
    }

    @Test
    void producesFiftyMillibucketsAndConsumesTwentyFiveAboveThreshold() {
        SepticTankBiogasProduction.Batch batch = SepticTankBiogasProduction.planBatch(
                true, 32_001, 0, 64_000);

        assertEquals(50, batch.gasProduced());
        assertEquals(25, batch.liquidConsumed());
    }

    @Test
    void refusesIncompleteBatchOrWrongLiquid() {
        assertEquals(SepticTankBiogasProduction.Batch.NONE,
                SepticTankBiogasProduction.planBatch(false, 10_000, 0, 64_000));
        assertEquals(SepticTankBiogasProduction.Batch.NONE,
                SepticTankBiogasProduction.planBatch(true, 9_999, 63_991, 64_000));
        assertEquals(SepticTankBiogasProduction.Batch.NONE,
                SepticTankBiogasProduction.planBatch(true, 32_001, 63_951, 64_000));
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
