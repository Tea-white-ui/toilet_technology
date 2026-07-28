package cn.tea.toilet.technology.datagen;

import cn.tea.toilet.technology.ModConstants;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.item.ModItems;
import cn.tea.toilet.technology.recipe.GasMeltingRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/** 燃气熔炼炉配方集中注册入口；后续自定义配方应在此处追加。 */
public final class ModGasMeltingRecipeProvider {
    private ModGasMeltingRecipeProvider() { }

    public static void addRecipes(RecipeOutput output) {
        addRecipe(output, "metal_mesh_to_multi_layer_sintered_metal_mesh",
                Ingredient.of(ModItems.METAL_MESH.get()),
                new ItemStack(ModItems.MULTI_LAYER_SINTERED_METAL_MESH.get()),
                GasRegistry.BIOGAS, 10, 100);
    }

    private static void addRecipe(RecipeOutput output, String name, Ingredient input, ItemStack result,
                                  cn.tea.toilet.technology.gas.Gas gas, long gasAmount, int processingTime) {
        output.accept(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, name),
                new GasMeltingRecipe(input, result, gas, gasAmount, processingTime), null);
    }
}
