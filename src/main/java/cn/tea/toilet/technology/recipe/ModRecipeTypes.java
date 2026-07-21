package cn.tea.toilet.technology.recipe;

import cn.tea.toilet.technology.ToiletTechnology;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 干燥配方类型注册
 * 注册干燥配方类型到NeoForge注册表
 */
public class ModRecipeTypes {

    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPES =
            DeferredRegister.create(BuiltInRegistries.RECIPE_TYPE, ToiletTechnology.MOD_ID);

    // 干燥配方类型
    public static final DeferredHolder<RecipeType<?>, RecipeType<DryingRecipe>> DRYING =
            RECIPE_TYPES.register("drying", () -> RecipeType.simple(
                    ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, "drying")
            ));

    public static void register(IEventBus bus) {
        RECIPE_TYPES.register(bus);
    }
}