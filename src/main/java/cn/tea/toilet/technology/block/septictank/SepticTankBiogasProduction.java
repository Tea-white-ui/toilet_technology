package cn.tea.toilet.technology.block.septictank;

/** Pure batch-selection rules for septic-tank biogas production. Amounts use mB. */
public final class SepticTankBiogasProduction {
    public static final int INTERVAL_TICKS = 5;
    public static final int RESIDUE_PRODUCTION_CHANCE_PERCENT = 5;
    private static final int LOW_LIQUID_THRESHOLD = 10_000;
    private static final int HIGH_LIQUID_THRESHOLD = 32_000;
    private static final long LOW_LIQUID_GAS_OUTPUT = 10;
    private static final long BASE_GAS_OUTPUT = 20;
    private static final long HIGH_GAS_OUTPUT = 50;
    private static final int HIGH_LIQUID_COST = 25;

    private SepticTankBiogasProduction() {
    }

    public static int nextProgress(int currentProgress, boolean canProduce) {
        return canProduce ? currentProgress + 1 : 0;
    }

    /** Returns whether a 0..99 random roll produces one unit of biogas residue. */
    public static boolean shouldProduceResidue(int randomRoll) {
        return randomRoll >= 0 && randomRoll < RESIDUE_PRODUCTION_CHANCE_PERCENT;
    }

    public static Batch planBatch(boolean containsFecesLiquid, int liquidAmount,
            long gasAmount, long gasCapacity) {
        if (!containsFecesLiquid || liquidAmount <= 0) return Batch.NONE;

        boolean lowLiquid = liquidAmount < LOW_LIQUID_THRESHOLD;
        boolean highOutput = liquidAmount > HIGH_LIQUID_THRESHOLD;
        long gasProduced = lowLiquid ? LOW_LIQUID_GAS_OUTPUT
                : highOutput ? HIGH_GAS_OUTPUT : BASE_GAS_OUTPUT;
        int liquidConsumed = highOutput ? HIGH_LIQUID_COST : 0;
        if (liquidAmount < liquidConsumed || gasCapacity - gasAmount < gasProduced) return Batch.NONE;
        return new Batch(gasProduced, liquidConsumed);
    }

    public record Batch(long gasProduced, int liquidConsumed) {
        public static final Batch NONE = new Batch(0, 0);
    }
}