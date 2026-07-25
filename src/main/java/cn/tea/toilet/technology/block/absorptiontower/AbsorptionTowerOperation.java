package cn.tea.toilet.technology.block.absorptiontower;

/** Pure, transactional batch rules for the absorption tower bottom. Amounts use mB. */
public final class AbsorptionTowerOperation {
    public static final int BATCH_AMOUNT = 25;
    public static final int MINIMUM_INPUT_BIOGAS = 50;

    private AbsorptionTowerOperation() {
    }

    /**
     * The specified process uses strict thresholds: every required amount/space must be greater than one batch.
     */
    public static boolean canProcess(long inputBiogas, int inputWater, long gasOutputSpace, int liquidOutputSpace) {
        return inputBiogas > MINIMUM_INPUT_BIOGAS
                && inputWater > BATCH_AMOUNT
                && gasOutputSpace > BATCH_AMOUNT
                && liquidOutputSpace > BATCH_AMOUNT;
    }

    public static long inputGasAfterProcess(long inputGas) {
        return inputGas - BATCH_AMOUNT;
    }

    public static int inputWaterAfterProcess(int inputWater) {
        return inputWater - BATCH_AMOUNT;
    }

    public static long outputGasAfterProcess(long outputGas) {
        return outputGas + BATCH_AMOUNT;
    }

    public static int outputLiquidAfterProcess(int outputLiquid) {
        return outputLiquid + BATCH_AMOUNT;
    }
}
