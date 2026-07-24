package cn.tea.toilet.technology.datagen;

import cn.tea.toilet.technology.item.ModItems;
import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.recipe.DryingRecipe;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Blocks;
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

        // 粪便块 → 干粪块 (100秒 = 2000 tick)
        createDryingRecipe(
                output,
                Ingredient.of(ModBlocks.FECES_BLOCK.get()),
                new ItemStack(ModBlocks.DRIED_FECES_BLOCK.get()),
                2000,
                "feces_block_to_dried_feces_block"
        );

        // 腐肉 → 皮革 (20秒 = 400 tick)
        createDryingRecipe(
                output,
                Ingredient.of(Items.ROTTEN_FLESH),
                new ItemStack(Items.LEATHER),
                400,
                "rotten_flesh_to_leather"
        );

        // 海带 → 干海带 (10秒 = 200 tick)
        createDryingRecipe(
                output,
                Ingredient.of(Items.KELP),
                new ItemStack(Items.DRIED_KELP),
                200,
                "kelp_to_dried_kelp"
        );

        // 湿海绵 → 干海绵 (40秒 = 800 tick)
        createDryingRecipe(
                output,
                Ingredient.of(Items.WET_SPONGE),
                new ItemStack(Items.SPONGE),
                800,
                "wet_sponge_to_sponge"
        );

        // 泥巴 → 粘土块 (20秒 = 400 tick)
        createDryingRecipe(
                output,
                Ingredient.of(Items.MUD),
                new ItemStack(Blocks.CLAY),
                400,
                "mud_to_clay"
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