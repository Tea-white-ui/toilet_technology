package cn.tea.toilet.technology.gui.septictank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SepticTankChemicalDisplayTest {

    @Test
    void emptyChemicalUsesZeroAsWireValue() {
        assertEquals(0, SepticTankChemicalDisplay.encodeRegistryId(-1));
        assertEquals(-1, SepticTankChemicalDisplay.decodeRegistryId(0));
    }

    @Test
    void registeredChemicalIdRoundTripsThroughWireValue() {
        int registryId = 42;
        int encoded = SepticTankChemicalDisplay.encodeRegistryId(registryId);

        assertEquals(43, encoded);
        assertEquals(registryId, SepticTankChemicalDisplay.decodeRegistryId(encoded));
    }

    @Test
    void fillHeightIsClampedAndKeepsOnePixelForNonEmptyTank() {
        assertEquals(0, SepticTankChemicalDisplay.fillHeight(0, 64_000, 34));
        assertEquals(1, SepticTankChemicalDisplay.fillHeight(1, 64_000, 34));
        assertEquals(17, SepticTankChemicalDisplay.fillHeight(32_000, 64_000, 34));
        assertEquals(34, SepticTankChemicalDisplay.fillHeight(80_000, 64_000, 34));
        assertEquals(0, SepticTankChemicalDisplay.fillHeight(1_000, 0, 34));
    }
}
