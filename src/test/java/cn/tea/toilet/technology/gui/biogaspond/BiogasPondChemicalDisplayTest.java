package cn.tea.toilet.technology.gui.biogaspond;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BiogasPondChemicalDisplayTest {
    @Test
    void encodesEmptyRegistryIdAsZero() {
        assertEquals(0, BiogasPondChemicalDisplay.encodeRegistryId(-1));
        assertEquals(-1, BiogasPondChemicalDisplay.decodeRegistryId(0));
    }

    @Test
    void roundTripsRegisteredChemicalId() {
        int registryId = 42;

        assertEquals(registryId, BiogasPondChemicalDisplay.decodeRegistryId(
                BiogasPondChemicalDisplay.encodeRegistryId(registryId)));
    }

    @Test
    void calculatesLargeChemicalFillHeightWithoutOverflow() {
        assertEquals(0, BiogasPondChemicalDisplay.fillHeight(0, 128_000, 31));
        assertEquals(1, BiogasPondChemicalDisplay.fillHeight(1, 128_000, 31));
        assertEquals(15, BiogasPondChemicalDisplay.fillHeight(64_000, 128_000, 31));
        assertEquals(31, BiogasPondChemicalDisplay.fillHeight(256_000, 128_000, 31));
    }
}
