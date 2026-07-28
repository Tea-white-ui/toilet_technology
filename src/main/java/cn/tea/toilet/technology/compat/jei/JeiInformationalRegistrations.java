package cn.tea.toilet.technology.compat.jei;

import cn.tea.toilet.technology.block.ModBlocks;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.world.item.ItemStack;

/**
 * Registers JEI displays backed by machine rules rather than Minecraft recipe types.
 * Recipe-backed categories remain in {@link JeiCategoryDefinition}.
 */
final class JeiInformationalRegistrations {
    private JeiInformationalRegistrations() {
    }

    static void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                BiogasProductionJeiCategory.septicTank(guiHelper),
                BiogasProductionJeiCategory.biogasPond(guiHelper),
                new AbsorptionTowerJeiCategory(guiHelper)
        );
    }

    static void registerRecipes(IRecipeRegistration registration) {
        registration.addRecipes(BiogasProductionJeiCategory.SEPTIC_TANK_RECIPE_TYPE,
                BiogasProductionJeiRecipes.septicTankRecipes());
        registration.addRecipes(BiogasProductionJeiCategory.BIOGAS_POND_RECIPE_TYPE,
                BiogasProductionJeiRecipes.biogasPondRecipes());
        registration.addRecipes(AbsorptionTowerJeiCategory.RECIPE_TYPE, AbsorptionTowerJeiRecipes.recipes());
    }

    static void registerCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.SEPTIC_TANK_CONTROLLER.get()),
                BiogasProductionJeiCategory.SEPTIC_TANK_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BIOGAS_POND_CONTROLLER.get()),
                BiogasProductionJeiCategory.BIOGAS_POND_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.BIOGAS_GENERATOR.get()),
                BiogasProductionJeiCategory.BIOGAS_POND_RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ABSORPTION_TOWER_BOTTOM.get()),
                AbsorptionTowerJeiCategory.RECIPE_TYPE);
        registration.addRecipeCatalyst(new ItemStack(ModBlocks.ABSORPTION_TOWER_BODY.get()),
                AbsorptionTowerJeiCategory.RECIPE_TYPE);
    }
}
