package cn.tea.toilet.technology.datagen;

import cn.tea.toilet.technology.ModConstants;
import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.item.ModItems;
import cn.tea.toilet.technology.recipe.GasMeltingRecipe;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

/** 燃气熔炼炉配方集中注册入口；后续自定义配方应在此处追加。 */
public final class ModGasMeltingRecipeProvider {
    private ModGasMeltingRecipeProvider() { }

    public static void addRecipes(RecipeOutput output) {
        addRecipe(output, "metal_mesh_to_multi_layer_sintered_metal_mesh",
                Ingredient.of(ModItems.METAL_MESH.get()),
                new ItemStack(ModItems.MULTI_LAYER_SINTERED_METAL_MESH.get()),
                GasRegistry.BIOGAS, 10, 100);

        addBiogasAndMethaneRecipes(output, "dried_feces_to_coal",
                Ingredient.of(ModItems.DRIED_FECES.get()), new ItemStack(Items.COAL), 25, 10);
        addBiogasAndMethaneRecipes(output, "dried_feces_block_to_coal",
                Ingredient.of(ModBlocks.DRIED_FECES_BLOCK.get()), new ItemStack(Items.COAL, 4), 80, 20);
        addBiogasAndMethaneRecipes(output, "biogas_residue_to_bone_meal",
                Ingredient.of(ModItems.BIOGAS_RESIDUE.get()), new ItemStack(Items.BONE_MEAL), 25, 10);
    }

    private static void addBiogasAndMethaneRecipes(RecipeOutput output, String name, Ingredient input,
                                                    ItemStack result, long biogasAmount, long methaneAmount) {
        addRecipe(output, name + "_biogas", input, result, GasRegistry.BIOGAS, biogasAmount, 100);
        addRecipe(output, name + "_methane", input, result, GasRegistry.METHANE, methaneAmount, 100);
    }

    private static void addRecipe(RecipeOutput output, String name, Ingredient input, ItemStack result,
                                  cn.tea.toilet.technology.gas.Gas gas, long gasAmount, int processingTime) {
        output.accept(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, name),
                new GasMeltingRecipe(input, result, gas, gasAmount, processingTime), null);
    }
}
