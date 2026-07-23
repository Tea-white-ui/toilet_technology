package cn.tea.toilet.technology.block.septictank;

/** Slot roles shared by controller inventory validation and machine processing. */
public final class SepticTankSlotPolicy {
    private SepticTankSlotPolicy() {
    }

    public static boolean isMachineOutput(int slot) {
        return SepticTankInventoryLayout.isMachineOutput(slot);
    }

    public static boolean allowsExternalInsertion(int slot) {
        return SepticTankInventoryLayout.allowsExternalInsertion(slot);
    }
}
