package cn.tea.toilet.technology.datagen;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        // 示例：4个粪便合成粪便块
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.FECES_BLOCK.get())
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.FECES.get())
                .unlockedBy(getHasName(ModItems.FECES.get()), has(ModItems.FECES.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SQUAT_TOILET.get())
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .define('#', Items.COBBLESTONE)
                .unlockedBy(getHasName(Items.COBBLESTONE), has(Items.COBBLESTONE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.OAK_TOILET.get())
                .pattern("#D#")
                .pattern("###")
                .pattern("###")
                .define('#', Ingredient.of(ItemTags.PLANKS))
                .define('D', ModItems.FECES.get())
                .unlockedBy(getHasName(ModItems.FECES.get()), has(ModItems.FECES.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STONE_TOILET.get())
                .pattern("#D#")
                .pattern("###")
                .pattern("###")
                .define('#', Items.STONE)
                .define('D', ModItems.FECES.get())
                .unlockedBy(getHasName(ModItems.FECES.get()), has(ModItems.FECES.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.IRON_TOILET.get())
                .pattern("#D#")
                .pattern("###")
                .define('#', Items.IRON_INGOT)
                .define('D', ModItems.STONE_TOILET_ITEM.get())
                .unlockedBy(getHasName(ModBlocks.STONE_TOILET.get()), has(ModBlocks.STONE_TOILET.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TOILET.get())
                .pattern("#D#")
                .pattern("###")
                .define('#', Items.GOLD_INGOT)
                .define('D', ModItems.IRON_TOILET_ITEM.get())
                .unlockedBy(getHasName(ModBlocks.IRON_TOILET.get()), has(ModBlocks.IRON_TOILET.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TOILET.get())
                .pattern("#D#")
                .define('#', Items.DIAMOND)
                .define('D', ModItems.GOLD_TOILET_ITEM.get())
                .unlockedBy(getHasName(ModBlocks.GOLD_TOILET.get()), has(ModBlocks.GOLD_TOILET.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DRYING_RACK.get())
                .pattern("#D#")
                .pattern("# #")
                .pattern("# #")
                .define('D', Ingredient.of(ItemTags.PLANKS))
                .define('#', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(output);

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.EMPTY,
                        Ingredient.of(ModBlocks.DIAMOND_TOILET.get()),
                        Ingredient.of(Items.NETHERITE_INGOT),
                        RecipeCategory.BUILDING_BLOCKS,
                        ModBlocks.NETHERITE_TOILET.get().asItem()
                ).unlocks(getHasName(ModBlocks.DIAMOND_TOILET.get()), has(ModBlocks.DIAMOND_TOILET.get()))
                .save(output, "netherite_toilet_smithing");






        // 示例：粪便块分解为4个粪便
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FECES.get(), 4)
                .requires(ModBlocks.FECES_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.FECES_BLOCK.get()), has(ModBlocks.FECES_BLOCK.get()))
                .save(output);
    }
}
