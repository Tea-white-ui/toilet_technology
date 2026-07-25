package cn.tea.toilet.technology.block.biogaspond;

/** Pure batch-selection rules for biogas-pond production. Amounts use mB. */
public final class BiogasPondBiogasProduction {
    public static final int INTERVAL_TICKS = 5;
    private static final int LOW_LIQUID_THRESHOLD = 10_000;
    private static final int HIGH_LIQUID_THRESHOLD = 32_000;
    private static final long LOW_LIQUID_GAS_OUTPUT = 10;
    private static final long BASE_GAS_OUTPUT = 20;
    private static final long HIGH_GAS_OUTPUT = 50;
    private static final int HIGH_LIQUID_COST = 25;

    private BiogasPondBiogasProduction() {
    }

    public static int nextProgress(int currentProgress, boolean canProduce) {
        return canProduce ? currentProgress + 1 : 0;
    }

    public static Batch planBatch(boolean containsFecesLiquid, int liquidAmount,
            long gasAmount, long gasCapacity) {
        return planBatch(containsFecesLiquid, liquidAmount, gasAmount, gasCapacity, 1);
    }

    public static Batch planBatch(boolean containsFecesLiquid, int liquidAmount,
            long gasAmount, long gasCapacity, int gasMultiplier) {
        if (!containsFecesLiquid || liquidAmount <= 0) return Batch.NONE;
        if (gasMultiplier < 1) return Batch.NONE;

        boolean highOutput = liquidAmount > HIGH_LIQUID_THRESHOLD;
        long baseGasProduced = liquidAmount < LOW_LIQUID_THRESHOLD ? LOW_LIQUID_GAS_OUTPUT
                : highOutput ? HIGH_GAS_OUTPUT : BASE_GAS_OUTPUT;
        long gasProduced = baseGasProduced * gasMultiplier;
        int liquidConsumed = highOutput ? HIGH_LIQUID_COST * gasMultiplier : 0;
        return liquidAmount < liquidConsumed || gasCapacity - gasAmount < gasProduced
                ? Batch.NONE : new Batch(gasProduced, liquidConsumed);
    }

    public static Batch planBatch(boolean containsFecesLiquid, int liquidAmount,
            long gasAmount, long gasCapacity, int poweredGasMultiplier, boolean generatorCanSupplyEnergy) {
        return planBatch(containsFecesLiquid, liquidAmount, gasAmount, gasCapacity,
                generatorCanSupplyEnergy ? poweredGasMultiplier : 1);
    }

    public record Batch(long gasProduced, int liquidConsumed) {
        public static final Batch NONE = new Batch(0, 0);
    }
}
