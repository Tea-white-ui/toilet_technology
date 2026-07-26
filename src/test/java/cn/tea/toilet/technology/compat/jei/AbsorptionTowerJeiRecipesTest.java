package cn.tea.toilet.technology.compat.jei;

import cn.tea.toilet.technology.block.absorptiontower.AbsorptionTowerOperation;
import cn.tea.toilet.technology.fluid.ModFluids;
import cn.tea.toilet.technology.gas.GasRegistry;
import net.minecraft.world.level.material.Fluids;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbsorptionTowerJeiRecipesTest {
    @Test
    @Disabled("The conversion contains vanilla and mod fluids, which require NeoForge's game bootstrap; Gradle unit tests do not provide an FML loading context.")
    void exposesTheRegisteredBiogasPurificationConversion() {
        var recipes = AbsorptionTowerJeiRecipes.recipes();

        assertEquals(1, recipes.size());
        var recipe = recipes.getFirst();
        assertEquals(GasRegistry.BIOGAS, recipe.inputGas());
        assertEquals(AbsorptionTowerOperation.BATCH_AMOUNT, recipe.inputGasAmount());
        assertEquals(Fluids.WATER, recipe.inputFluid());
        assertEquals(AbsorptionTowerOperation.BATCH_AMOUNT, recipe.inputFluidAmount());
        assertEquals(GasRegistry.METHANE, recipe.outputGas());
        assertEquals(AbsorptionTowerOperation.BATCH_AMOUNT, recipe.outputGasAmount());
        assertEquals(ModFluids.WASTEWATER.get(), recipe.outputFluid());
        assertEquals(AbsorptionTowerOperation.BATCH_AMOUNT, recipe.outputFluidAmount());
    }
}
