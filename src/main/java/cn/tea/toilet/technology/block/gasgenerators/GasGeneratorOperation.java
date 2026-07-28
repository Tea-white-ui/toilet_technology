package cn.tea.toilet.technology.block.gasgenerators;

/** Pure fuel and capacity rules for the gas generator. Gas amounts use mB and energy uses FE. */
public final class GasGeneratorOperation {
    public static final int ENERGY_CAPACITY = 20_480;
    public static final long GAS_CAPACITY = 16_000;
    public static final Fuel BIOGAS = new Fuel(40, 100);
    public static final Fuel METHANE = new Fuel(20, 800);

    private GasGeneratorOperation() {
    }

    public static Fuel forGasId(String gasId) {
        return switch (gasId) {
            case "toilet_technology:biogas" -> BIOGAS;
            case "toilet_technology:methane" -> METHANE;
            default -> null;
        };
    }

    public static boolean canGenerate(Fuel fuel, long storedGas, int storedEnergy) {
        return fuel != null && storedGas > 0 && storedEnergy >= 0
                && storedEnergy <= ENERGY_CAPACITY - fuel.energyPerTick();
    }

    public static int energyAfterTick(Fuel fuel, int storedEnergy) {
        return fuel != null && storedEnergy <= ENERGY_CAPACITY - fuel.energyPerTick()
                ? storedEnergy + fuel.energyPerTick() : storedEnergy;
    }

    public record Fuel(int burnTime, int energyPerTick) {
    }
}
