package cn.tea.toilet.technology.gui.septictank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SepticTankMenuLayoutTest {

    @Test
    void menuIndicesMapExplicitlyToHandlerSlots() {
        assertEquals(8, SepticTankMenuLayout.CUSTOM_SLOT_COUNT);
        assertEquals(6, SepticTankMenuLayout.handlerSlot(SepticTankMenuLayout.CONTAINER_INPUT));
        assertEquals(7, SepticTankMenuLayout.handlerSlot(SepticTankMenuLayout.CONTAINER_OUTPUT));
        assertEquals(0, SepticTankMenuLayout.handlerSlot(SepticTankMenuLayout.ITEM_INPUT_START));
        assertEquals(3, SepticTankMenuLayout.handlerSlot(SepticTankMenuLayout.ITEM_OUTPUT_START));
    }

    @Test
    void shiftClickFallsBackToItemInputsWhenContainerInputRejectsTheStack() {
        assertEquals(
                java.util.List.of(
                        new SepticTankMenuLayout.SlotRange(SepticTankMenuLayout.CONTAINER_INPUT, SepticTankMenuLayout.CONTAINER_INPUT + 1),
                        new SepticTankMenuLayout.SlotRange(SepticTankMenuLayout.ITEM_INPUT_START, SepticTankMenuLayout.ITEM_INPUT_END)
                ),
                SepticTankMenuLayout.playerDestinationRanges(true)
        );
        assertEquals(
                java.util.List.of(new SepticTankMenuLayout.SlotRange(
                        SepticTankMenuLayout.ITEM_INPUT_START,
                        SepticTankMenuLayout.ITEM_INPUT_END
                )),
                SepticTankMenuLayout.playerDestinationRanges(false)
        );
    }
}
