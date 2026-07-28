package cn.tea.toilet.technology.gui;

public final class TankMenuData {
    private static final long WORD_MASK = 0xFFFFL;

    private TankMenuData() {
    }

    public static int lowWord(long value) {
        return (int) value & 0xFFFF;
    }

    public static int highWord(long value) {
        return (int) (value >>> 16) & 0xFFFF;
    }

    public static long decodeLong(int lowWord, int highWord) {
        return (lowWord & WORD_MASK) | ((highWord & WORD_MASK) << 16);
    }

    public static int encodeRegistryId(int registryId) {
        return registryId < 0 ? 0 : registryId + 1;
    }

    public static int decodeRegistryId(int wireValue) {
        return wireValue == 0 ? -1 : wireValue - 1;
    }

    public static int fillHeight(long amount, long capacity, int height) {
        if (amount <= 0 || capacity <= 0 || height <= 0) {
            return 0;
        }
        return (int) Math.max(1, Math.min(height, amount * height / capacity));
    }
}
