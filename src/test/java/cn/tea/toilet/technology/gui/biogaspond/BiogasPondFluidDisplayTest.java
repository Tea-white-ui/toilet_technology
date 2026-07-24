package cn.tea.toilet.technology.gui.biogaspond;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BiogasPondFluidDisplayTest {
    @Test
    void encodesEmptyRegistryIdAsZero() {
        assertEquals(0, BiogasPondFluidDisplay.encodeRegistryId(-1));
        assertEquals(-1, BiogasPondFluidDisplay.decodeRegistryId(0));
    }

    @Test
    void roundTripsRegisteredFluidId() {
        int registryId = 42;

        assertEquals(registryId, BiogasPondFluidDisplay.decodeRegistryId(
                BiogasPondFluidDisplay.encodeRegistryId(registryId)));
    }

    @Test
    void clampsFluidFillHeightToTankBounds() {
        assertEquals(0, BiogasPondFluidDisplay.fillHeight(0, 64_000, 34));
        assertEquals(1, BiogasPondFluidDisplay.fillHeight(1, 64_000, 34));
        assertEquals(17, BiogasPondFluidDisplay.fillHeight(32_000, 64_000, 34));
        assertEquals(34, BiogasPondFluidDisplay.fillHeight(80_000, 64_000, 34));
    }
}
