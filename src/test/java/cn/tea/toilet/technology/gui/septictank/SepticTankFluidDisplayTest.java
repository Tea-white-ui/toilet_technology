package cn.tea.toilet.technology.gui.septictank;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SepticTankFluidDisplayTest {

    @Test
    void emptyFluidUsesZeroAsWireValue() {
        assertEquals(0, SepticTankFluidDisplay.encodeRegistryId(-1));
        assertEquals(-1, SepticTankFluidDisplay.decodeRegistryId(0));
    }

    @Test
    void registeredFluidIdRoundTripsThroughWireValue() {
        int registryId = 42;
        int encoded = SepticTankFluidDisplay.encodeRegistryId(registryId);

        assertEquals(43, encoded);
        assertEquals(registryId, SepticTankFluidDisplay.decodeRegistryId(encoded));
    }

    @Test
    void fillHeightIsClampedAndKeepsOnePixelForNonEmptyTank() {
        assertEquals(0, SepticTankFluidDisplay.fillHeight(0, 64_000, 34));
        assertEquals(1, SepticTankFluidDisplay.fillHeight(1, 64_000, 34));
        assertEquals(17, SepticTankFluidDisplay.fillHeight(32_000, 64_000, 34));
        assertEquals(34, SepticTankFluidDisplay.fillHeight(80_000, 64_000, 34));
        assertEquals(0, SepticTankFluidDisplay.fillHeight(1_000, 0, 34));
    }
}
