package cn.tea.toilet.technology.fluid;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.fluids.FluidType;

/**
 * 基础污水流体类型类
 * 定义粪便液体的纹理资源位置
 * 继承自 FluidType，用于配置流体的视觉和物理属性
 */
public class BaseSewageFluidType extends FluidType {
    /** 静止状态流体纹理资源位置 */
    public static final ResourceLocation STILL_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("toilet_technology", "block/feces_liquid");
    /** 流动状态流体纹理资源位置 */
    public static final ResourceLocation FLOWING_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("toilet_technology", "block/feces_liquid_flowing");

    /**
     * 构造函数
     * 
     * @param properties 流体属性配置对象
     */
    public BaseSewageFluidType(Properties properties) {
        super(properties);
    }
}