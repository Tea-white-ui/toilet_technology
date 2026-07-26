package cn.tea.toilet.technology.compat;

public final class PoopSkyCompatIds {
    public static final String POOPSKY_MOD_ID = "poopsky";
    static final String LOCAL_FECES = "toilet_technology:feces";
    static final String LOCAL_FECES_LIQUID = "toilet_technology:feces_liquid";
    static final String LOCAL_FLOWING_FECES_LIQUID = "toilet_technology:feces_liquid_flowing";
    public static final String POOPSKY_POOP = POOPSKY_MOD_ID + ":poop";
    static final String POOPSKY_URINE = POOPSKY_MOD_ID + ":urine";
    static final String POOPSKY_FLOWING_URINE = POOPSKY_MOD_ID + ":flowing_urine";

    private PoopSkyCompatIds() {
    }

    static boolean isFecesItemId(String id) {
        return LOCAL_FECES.equals(id) || POOPSKY_POOP.equals(id);
    }

    static boolean isFecesLiquidId(String id) {
        return LOCAL_FECES_LIQUID.equals(id)
                || LOCAL_FLOWING_FECES_LIQUID.equals(id)
                || POOPSKY_URINE.equals(id)
                || POOPSKY_FLOWING_URINE.equals(id);
    }
}
