package cn.tea.toilet.technology.recipe;

import cn.tea.toilet.technology.api.gas.Gas;
import net.minecraft.world.level.material.Fluid;

/**
 * Defines one absorption-tower conversion.
 * Inputs and outputs are measured in mB and processed as one atomic batch.
 */
public record AbsorptionTowerRecipe(
        Gas inputGas,
        int inputGasAmount,
        Fluid inputFluid,
        int inputFluidAmount,
        Gas outputGas,
        int outputGasAmount,
        Fluid outputFluid,
        int outputFluidAmount) {

    public AbsorptionTowerRecipe {
        if (inputGas == null || inputFluid == null || outputGas == null || outputFluid == null) {
            throw new IllegalArgumentException("Absorption tower recipe resources cannot be null");
        }
        if (inputGasAmount <= 0 || inputFluidAmount <= 0 || outputGasAmount <= 0 || outputFluidAmount <= 0) {
            throw new IllegalArgumentException("Absorption tower recipe amounts must be positive");
        }
    }
}
