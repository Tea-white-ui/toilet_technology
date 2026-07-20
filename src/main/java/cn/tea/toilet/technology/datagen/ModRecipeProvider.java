package cn.tea.toilet.technology.datagen;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SmeltingRecipe;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // 示例：4个粪便合成粪便块
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.FECES_BLOCK.get())
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.FECES.get())
                .unlockedBy(getHasName(ModItems.FECES.get()), has(ModItems.FECES.get()))
                .save(output);

        // 示例：粪便块分解为4个粪便
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FECES.get(), 4)
                .requires(ModBlocks.FECES_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.FECES_BLOCK.get()), has(ModBlocks.FECES_BLOCK.get()))
                .save(output);
    }
}
