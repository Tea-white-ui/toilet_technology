package cn.tea.toilet.technology.gui.septictank;

final class SepticTankChemicalDisplay {
    private SepticTankChemicalDisplay() {
    }

    static int encodeRegistryId(int registryId) {
        return registryId + 1;
    }

    static int decodeRegistryId(int wireValue) {
        return wireValue - 1;
    }

    static int fillHeight(long amount, long capacity, int height) {
        if (amount <= 0 || capacity <= 0 || height <= 0) return 0;
        return (int) Math.max(1, Math.min(height, amount * height / capacity));
    }
}
