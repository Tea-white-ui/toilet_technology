package cn.tea.toilet.technology.block.septictank;

/** Slot roles shared by controller inventory validation and machine processing. */
public final class SepticTankSlotPolicy {
    private SepticTankSlotPolicy() {
    }

    public static boolean isMachineOutput(int slot) {
        return slot >= SepticTankControllerBlockEntity.ITEM_OUTPUT_START
                && slot < SepticTankControllerBlockEntity.CONTAINER_INPUT
                || slot == SepticTankControllerBlockEntity.CONTAINER_OUTPUT;
    }

    public static boolean allowsExternalInsertion(int slot) {
        return !isMachineOutput(slot);
    }
}
