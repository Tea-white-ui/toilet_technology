package cn.tea.toilet.technology.block.sewagepurifier;

/** Pure batch rules for the sewage purifier. Fluid amounts use mB and energy uses FE. */
public final class SewagePurifierOperation {
    public static final int TANK_CAPACITY = 64_000;
    public static final int ENERGY_CAPACITY = 25_600;
    public static final int BATCH_FLUID = 32_000;
    public static final int ENERGY_PER_BATCH = 128;
    public static final int FECES_PER_BATCH = 4;

    private SewagePurifierOperation() {
    }

    public static boolean canProcess(int inputFecesLiquid, int storedEnergy, int outputWaterSpace, boolean fecesOutputAvailable) {
        return inputFecesLiquid >= BATCH_FLUID
                && storedEnergy >= ENERGY_PER_BATCH
                && outputWaterSpace >= BATCH_FLUID
                && fecesOutputAvailable;
    }

    public static int inputFecesLiquidAfterProcess(int inputFecesLiquid) {
        return inputFecesLiquid - BATCH_FLUID;
    }

    public static int outputWaterAfterProcess(int outputWater) {
        return outputWater + BATCH_FLUID;
    }

    public static int energyAfterProcess(int storedEnergy) {
        return storedEnergy - ENERGY_PER_BATCH;
    }
}
