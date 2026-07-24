package cn.tea.toilet.technology.gui.biogaspond;

final class BiogasPondFluidDisplay {
    private BiogasPondFluidDisplay() {
    }

    static int encodeRegistryId(int registryId) {
        return registryId < 0 ? 0 : registryId + 1;
    }

    static int decodeRegistryId(int wireValue) {
        return wireValue == 0 ? -1 : wireValue - 1;
    }

    static int fillHeight(int amount, int capacity, int height) {
        if (amount <= 0 || capacity <= 0 || height <= 0) return 0;
        return Math.max(1, Math.min(height, (int) ((long) amount * height / capacity)));
    }
}
