package cn.tea.toilet.technology.gui.septictank;

import cn.tea.toilet.technology.block.septictank.SepticTankInventoryLayout;

import java.util.List;

/** Maps menu-slot indices to the fixed backing inventory without scattering magic numbers. */
final class SepticTankMenuLayout {
    static final int CONTAINER_INPUT = 0;
    static final int CONTAINER_OUTPUT = 1;
    static final int ITEM_INPUT_START = 2;
    static final int ITEM_INPUT_END = 5;
    static final int ITEM_OUTPUT_START = 5;
    static final int ITEM_OUTPUT_END = 8;
    static final int CUSTOM_SLOT_COUNT = 8;
    static final int PLAYER_START = CUSTOM_SLOT_COUNT;
    static final int PLAYER_END = PLAYER_START + 36;

    private static final int[] HANDLER_SLOTS = {
            SepticTankInventoryLayout.CONTAINER_INPUT,
            SepticTankInventoryLayout.CONTAINER_OUTPUT,
            0, 1, 2,
            3, 4, 5
    };

    private SepticTankMenuLayout() {
    }

    static int handlerSlot(int menuSlot) {
        if (menuSlot < 0 || menuSlot >= HANDLER_SLOTS.length) {
            throw new IndexOutOfBoundsException("Invalid septic tank menu slot: " + menuSlot);
        }
        return HANDLER_SLOTS[menuSlot];
    }

    static List<SlotRange> playerDestinationRanges(boolean acceptsContainerInput) {
        SlotRange itemInputs = new SlotRange(ITEM_INPUT_START, ITEM_INPUT_END);
        return acceptsContainerInput
                ? List.of(new SlotRange(CONTAINER_INPUT, CONTAINER_INPUT + 1), itemInputs)
                : List.of(itemInputs);
    }

    record SlotRange(int startInclusive, int endExclusive) {
    }
}
