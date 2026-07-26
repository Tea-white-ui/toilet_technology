package cn.tea.toilet.technology.compat;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PoopSkyCompatTest {

    @Test
    void recognizesOnlyTheBasePoopSkyPoopAsEquivalentFecesItem() {
        assertTrue(PoopSkyCompatIds.isFecesItemId("toilet_technology:feces"));
        assertTrue(PoopSkyCompatIds.isFecesItemId("poopsky:poop"));
        assertFalse(PoopSkyCompatIds.isFecesItemId("poopsky:chili_poop"));
        assertFalse(PoopSkyCompatIds.isFecesItemId("minecraft:dirt"));
    }

    @Test
    void recognizesLocalAndPoopSkyEquivalentFecesLiquids() {
        assertTrue(PoopSkyCompatIds.isFecesLiquidId("toilet_technology:feces_liquid"));
        assertTrue(PoopSkyCompatIds.isFecesLiquidId("toilet_technology:feces_liquid_flowing"));
        assertTrue(PoopSkyCompatIds.isFecesLiquidId("poopsky:urine"));
        assertTrue(PoopSkyCompatIds.isFecesLiquidId("poopsky:flowing_urine"));
        assertFalse(PoopSkyCompatIds.isFecesLiquidId("minecraft:water"));
    }
}
