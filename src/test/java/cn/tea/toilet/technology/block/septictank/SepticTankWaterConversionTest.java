package cn.tea.toilet.technology.block.septictank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SepticTankWaterConversionTest {

    @Test
    void selectsFirstFecesInputWhenTankContainsWater() {
        assertEquals(1, SepticTankWaterConversion.findInputSlot(
                new boolean[]{false, true, true}, true, 1_000));
    }

    @Test
    void refusesConversionWithoutWaterOrFeces() {
        assertEquals(-1, SepticTankWaterConversion.findInputSlot(
                new boolean[]{true, false, false}, false, 1_000));
        assertEquals(-1, SepticTankWaterConversion.findInputSlot(
                new boolean[]{false, false, false}, true, 1_000));
        assertEquals(-1, SepticTankWaterConversion.findInputSlot(
                new boolean[]{true, false, false}, true, 0));
    }
}
