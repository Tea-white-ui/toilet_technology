package cn.tea.toilet.technology.gui.biogaspond;

final class BiogasPondChemicalDisplay {
    private BiogasPondChemicalDisplay() {
    }

    static int encodeRegistryId(int registryId) {
        return registryId < 0 ? 0 : registryId + 1;
    }

    static int decodeRegistryId(int wireValue) {
        return wireValue == 0 ? -1 : wireValue - 1;
    }

    static int fillHeight(long amount, long capacity, int height) {
        if (amount <= 0 || capacity <= 0 || height <= 0) return 0;
        return (int) Math.max(1, Math.min(height, amount * height / capacity));
    }
}
