package cn.tea.toilet.technology.compat.jei;

import cn.tea.toilet.technology.recipe.AbsorptionTowerRecipe;
import cn.tea.toilet.technology.recipe.AbsorptionTowerRecipeRegistry;

import java.util.List;

/** Supplies JEI with the absorption tower's built-in conversion definitions. */
final class AbsorptionTowerJeiRecipes {
    private AbsorptionTowerJeiRecipes() {
    }

    static List<AbsorptionTowerRecipe> recipes() {
        return AbsorptionTowerRecipeRegistry.recipes();
    }
}
