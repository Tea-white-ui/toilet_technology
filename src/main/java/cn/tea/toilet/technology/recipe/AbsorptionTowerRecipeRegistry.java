package cn.tea.toilet.technology.recipe;

import cn.tea.toilet.technology.block.absorptiontower.AbsorptionTowerOperation;
import cn.tea.toilet.technology.fluid.ModFluids;
import cn.tea.toilet.technology.gas.Gas;
import cn.tea.toilet.technology.gas.GasRegistry;
import java.util.List;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

/**
 * Central registry for the built-in absorption-tower conversions.
 * Add future conversions here; the controller resolves them by input gas and fluid.
 */
public final class AbsorptionTowerRecipeRegistry {
    private static final List<AbsorptionTowerRecipe> RECIPES = List.of(
            new AbsorptionTowerRecipe(
                    GasRegistry.BIOGAS,
                    AbsorptionTowerOperation.BATCH_AMOUNT,
                    Fluids.WATER,
                    AbsorptionTowerOperation.BATCH_AMOUNT,
                    GasRegistry.METHANE,
                    AbsorptionTowerOperation.BATCH_AMOUNT,
                    ModFluids.WASTEWATER.get(),
                    AbsorptionTowerOperation.BATCH_AMOUNT)
    );

    private AbsorptionTowerRecipeRegistry() {
    }

    public static List<AbsorptionTowerRecipe> recipes() {
        return RECIPES;
    }

    public static @Nullable AbsorptionTowerRecipe find(Gas inputGas, net.minecraft.world.level.material.Fluid inputFluid) {
        return RECIPES.stream()
                .filter(recipe -> recipe.inputGas().equals(inputGas) && recipe.inputFluid().isSame(inputFluid))
                .findFirst()
                .orElse(null);
    }
}
