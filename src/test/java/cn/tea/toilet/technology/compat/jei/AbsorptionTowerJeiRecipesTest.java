package cn.tea.toilet.technology.compat.jei;

import cn.tea.toilet.technology.block.absorptiontower.AbsorptionTowerOperation;
import cn.tea.toilet.technology.block.absorptiontower.AbsorptionTowerStructure;
import cn.tea.toilet.technology.fluid.ModFluids;
import cn.tea.toilet.technology.gas.GasRegistry;
import net.minecraft.world.level.material.Fluids;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AbsorptionTowerJeiRecipesTest {
    @Test
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

    @Test
    void layoutMatchesTheFourBlockTowerStructure() {
        assertEquals(AbsorptionTowerStructure.HEIGHT, AbsorptionTowerJeiLayout.STRUCTURE_BLOCK_COUNT);
        assertEquals(3, AbsorptionTowerJeiLayout.BODY_BLOCK_COUNT);
        assertEquals(1, AbsorptionTowerJeiLayout.BOTTOM_BLOCK_COUNT);
        assertEquals(176, AbsorptionTowerJeiLayout.WIDTH);
        assertEquals(96, AbsorptionTowerJeiLayout.HEIGHT);
    }
}
