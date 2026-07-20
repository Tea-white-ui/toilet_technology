package cn.tea.toilet.technology.fluid;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;

public class BaseSewageFluidType extends FluidType {
    public static final ResourceLocation STILL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("toilet_technology", "block/feces_liquid");
    public static final ResourceLocation FLOWING_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("toilet_technology", "block/feces_liquid_flowing");

    public BaseSewageFluidType(Properties properties) {
        super(properties);
    }
}
