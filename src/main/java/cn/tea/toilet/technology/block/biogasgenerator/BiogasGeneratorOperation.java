package cn.tea.toilet.technology.block.biogasgenerator;

/** Pure operating rules for the biogas generator. Energy amounts use FE. */
public final class BiogasGeneratorOperation {
    public static final int ENERGY_CAPACITY = 10_000;
    public static final int ENERGY_PER_TICK = 100;

    private BiogasGeneratorOperation() {
    }

    public static boolean canRun(boolean backTouchesSepticTankWall, int storedEnergy) {
        return backTouchesSepticTankWall && storedEnergy >= ENERGY_PER_TICK;
    }

    public static int energyAfterTick(boolean backTouchesSepticTankWall, int storedEnergy) {
        return canRun(backTouchesSepticTankWall, storedEnergy) ? storedEnergy - ENERGY_PER_TICK : storedEnergy;
    }
}