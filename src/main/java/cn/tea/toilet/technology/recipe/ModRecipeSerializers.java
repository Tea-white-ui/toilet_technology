package cn.tea.toilet.technology.recipe;

import cn.tea.toilet.technology.ToiletTechnology;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 配方类型和序列化器注册
 * 注册干燥配方的类型和序列化器到NeoForge注册表
 */
public class ModRecipeSerializers {

    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZERS =
            DeferredRegister.create(BuiltInRegistries.RECIPE_SERIALIZER, ToiletTechnology.MOD_ID);

    // 干燥配方序列化器
    public static final DeferredHolder<RecipeSerializer<?>, DryingRecipeSerializer> DRYING_RECIPE =
            RECIPE_SERIALIZERS.register("drying", DryingRecipeSerializer::new);

    public static void register(IEventBus bus) {
        RECIPE_SERIALIZERS.register(bus);
    }
}