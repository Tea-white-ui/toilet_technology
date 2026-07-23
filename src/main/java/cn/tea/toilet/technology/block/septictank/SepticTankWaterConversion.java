package cn.tea.toilet.technology.block.septictank;

/** Pure selection logic for converting septic-tank water with a feces item. */
public final class SepticTankWaterConversion {
    private SepticTankWaterConversion() {
    }

    public static int findInputSlot(boolean[] fecesInputs, boolean containsWater, int fluidAmount) {
        if (!containsWater || fluidAmount <= 0) return -1;
        for (int slot = 0; slot < fecesInputs.length; slot++) {
            if (fecesInputs[slot]) return slot;
        }
        return -1;
    }
}
