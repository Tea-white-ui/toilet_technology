package cn.tea.toilet.technology.compat.jei;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.recipe.GasMeltingRecipe;
import cn.tea.toilet.technology.recipe.ModRecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/** JEI definition for data-driven gas melting furnace recipes. */
public final class GasMeltingCategoryDefinition implements JeiCategoryDefinition<GasMeltingRecipe> {
    @Override
    public net.minecraft.world.item.crafting.RecipeType<GasMeltingRecipe> getMinecraftRecipeType() {
        return ModRecipeTypes.GAS_MELTING.get();
    }

    @Override
    public Class<GasMeltingRecipe> getRecipeClass() {
        return GasMeltingRecipe.class;
    }

    @Override
    public IRecipeCategory<GasMeltingRecipe> createCategory(IGuiHelper guiHelper) {
        return new GasMeltingRecipeCategory(guiHelper, getRecipeType());
    }

    @Override
    public List<ItemStack> getCatalysts() {
        return List.of(new ItemStack(ModBlocks.GAS_MELTING_FURNACE.get()));
    }
}
