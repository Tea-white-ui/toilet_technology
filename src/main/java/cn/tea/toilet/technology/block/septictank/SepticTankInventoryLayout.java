package cn.tea.toilet.technology.block.septictank;

/** Single source of truth for the septic tank's fixed backing-inventory slots. */
public final class SepticTankInventoryLayout {
    public static final int ITEM_INPUT_START = 0;
    public static final int ITEM_INPUT_END = 3;
    public static final int ITEM_OUTPUT_START = 3;
    public static final int ITEM_OUTPUT_END = 6;
    public static final int CONTAINER_INPUT = 6;
    public static final int CONTAINER_OUTPUT = 7;
    public static final int SLOT_COUNT = 8;

    private SepticTankInventoryLayout() {
    }

    public static boolean isItemInput(int slot) {
        return slot >= ITEM_INPUT_START && slot < ITEM_INPUT_END;
    }

    public static boolean isMachineOutput(int slot) {
        return slot >= ITEM_OUTPUT_START && slot < ITEM_OUTPUT_END || slot == CONTAINER_OUTPUT;
    }

    public static boolean allowsExternalInsertion(int slot) {
        return isItemInput(slot) || slot == CONTAINER_INPUT;
    }

    public static boolean allowsExternalExtraction(int slot) {
        return isMachineOutput(slot);
    }

    public static boolean allowsMenuExtraction(int slot) {
        return slot >= 0 && slot < SLOT_COUNT;
    }
}
