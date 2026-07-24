package cn.tea.toilet.technology.compat.jei;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BiogasProductionJeiRecipesTest {
    @Test
    void residueOutputIsOffsetToTheRightOfTheBiogasOutput() {
        assertEquals(112, BiogasProductionJeiLayout.RESIDUE_SLOT_X);
    }

    @Test
    void septicTankUsesOneSummaryRecipeWithResidue() {
        List<BiogasProductionJeiRecipe> recipes = BiogasProductionJeiRecipes.septicTankRecipes();

        assertEquals(1, recipes.size());
        BiogasProductionJeiRecipe recipe = recipes.getFirst();
        assertEquals(10, recipe.minimumBiogasAmount());
        assertEquals(50, recipe.maximumBiogasAmount());
        assertTrue(recipe.hasResidueOutput());
        assertEquals(5, recipe.residueChancePercent());
        assertFalse(recipe.requiresGeneratorEnergy());
    }

    @Test
    void biogasPondUsesOneSummaryRecipeWithOptionalGeneratorBonus() {
        List<BiogasProductionJeiRecipe> recipes = BiogasProductionJeiRecipes.biogasPondRecipes();

        assertEquals(1, recipes.size());
        BiogasProductionJeiRecipe recipe = recipes.getFirst();
        assertEquals(10, recipe.minimumBiogasAmount());
        assertEquals(50, recipe.maximumBiogasAmount());
        assertFalse(recipe.hasResidueOutput());
        assertTrue(recipe.requiresGeneratorEnergy());
        assertEquals(128, recipe.energyPerBatch());
    }
}
