package cn.tea.toilet.technology.datagen;

import cn.tea.toilet.technology.item.ModItems;
import cn.tea.toilet.technology.recipe.DryingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

/**
 * 干燥配方数据生成器
 * 用于在 datagen 时生成干燥配方 JSON 文件
 */
public class ModDryingRecipeProvider {

    /**
     * 添加干燥配方
     */
    public static void addRecipes(RecipeOutput output) {
        createDryingRecipe(
                output,
                Ingredient.of(ModItems.FECES.get()), // 输入
                new ItemStack(ModItems.DRIED_FECES.get()), // 输出
                600, // 干燥时间
                "feces_to_dried_feces"
        );
    }

    /**
     * 创建干燥配方
     *
     * @param output     配方输出
     * @param input      输入物品
     * @param outputItem 输出物品
     * @param dryingTime 干燥时间（tick）
     * @param name       配方名称
     */
    private static void createDryingRecipe(
            RecipeOutput output,
            Ingredient input,
            ItemStack outputItem,
            int dryingTime,
            String name
    ) {
        DryingRecipe recipe = new DryingRecipe(input, outputItem, dryingTime);
        output.accept(
                net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("toilet_technology", name),
                recipe,
                null
        );
    }
}