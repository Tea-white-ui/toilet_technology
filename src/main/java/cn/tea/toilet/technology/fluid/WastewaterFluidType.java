package cn.tea.toilet.technology.fluid;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;

/** 废水流体类型，定义客户端渲染使用的静止与流动纹理。 */
public class WastewaterFluidType extends FluidType {
    /** 静止状态流体纹理资源位置。 */
    public static final ResourceLocation STILL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("toilet_technology", "block/wastewater");
    /** 流动状态流体纹理资源位置。 */
    public static final ResourceLocation FLOWING_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("toilet_technology", "block/wastewater_flowing");

    public WastewaterFluidType(Properties properties) {
        super(properties);
    }
}