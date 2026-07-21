package cn.tea.toilet.technology.compat.jei;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.recipe.DryingRecipe;
import cn.tea.toilet.technology.recipe.ModRecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * 干燥配方的JEI类别定义 - 自描述干燥配方的所有JEI集成信息
 * <p>
 * 通过实现JeiCategoryDefinition接口，将RecipeType、Category创建、配方收集、催化剂列表全部内聚。
 * JEI RecipeType基于Minecraft RecipeType自动构建，无需手动指定命名空间和路径。
 */
public class DryingCategoryDefinition implements JeiCategoryDefinition<DryingRecipe> {

    @Override
    public net.minecraft.world.item.crafting.RecipeType<DryingRecipe> getMinecraftRecipeType() {
        return ModRecipeTypes.DRYING.get();
    }

    @Override
    public Class<DryingRecipe> getRecipeClass() {
        return DryingRecipe.class;
    }

    @Override
    public IRecipeCategory<DryingRecipe> createCategory(IGuiHelper guiHelper) {
        return new DryingRecipeCategory(guiHelper, getRecipeType());
    }

    @Override
    public List<ItemStack> getCatalysts() {
        return List.of(ModBlocks.DRYING_RACK.get().asItem().getDefaultInstance());
    }
}