package cn.tea.toilet.technology.recipe;

import cn.tea.toilet.technology.ToiletTechnology;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;

/**
 * 干燥配方类型注册
 * 注册干燥配方类型到NeoForge注册表
 */
public class ModRecipeTypes {

    // 干燥配方类型
    public static final RecipeType<DryingRecipe> DRYING = RecipeType.simple(
            ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, "drying")
    );

    public static void register() {
        // RecipeType 不需要显式注册到 DeferredRegister
        // 因为 RecipeType.simple() 已经创建了一个有效的 RecipeType
    }
}