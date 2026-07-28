package cn.tea.toilet.technology.gui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TankMenuDataTest {
    @Test
    void preservesLongAmountsAcrossTwoContainerDataSlots() {
        long amount = 64_000L;

        assertEquals(amount, TankMenuData.decodeLong(
                TankMenuData.lowWord(amount),
                TankMenuData.highWord(amount)));
    }

    @Test
    void preservesValuesAboveTheUnsignedSixteenBitBoundary() {
        long amount = 65_536L;

        assertEquals(amount, TankMenuData.decodeLong(
                TankMenuData.lowWord(amount),
                TankMenuData.highWord(amount)));
    }

    @Test
    void representsEmptyAndRegisteredTypeIdsWithoutCollision() {
        assertEquals(0, TankMenuData.encodeRegistryId(-1));
        assertEquals(-1, TankMenuData.decodeRegistryId(0));
        assertEquals(42, TankMenuData.decodeRegistryId(TankMenuData.encodeRegistryId(42)));
    }

    @Test
    void clampsTankFillHeightWhileKeepingNonEmptyContentsVisible() {
        assertEquals(0, TankMenuData.fillHeight(0, 64_000, 34));
        assertEquals(1, TankMenuData.fillHeight(1, 64_000, 34));
        assertEquals(17, TankMenuData.fillHeight(32_000, 64_000, 34));
        assertEquals(34, TankMenuData.fillHeight(80_000, 64_000, 34));
    }
}
