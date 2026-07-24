package cn.tea.toilet.technology.compat.jei;

import cn.tea.toilet.technology.block.biogaspond.BiogasPondBiogasProduction;
import cn.tea.toilet.technology.block.biogaspond.BiogasPondControllerBlockEntity;
import cn.tea.toilet.technology.block.septictank.SepticTankBiogasProduction;

import java.util.List;

/** Creates one concise JEI summary for each machine from its actual production rules. */
public final class BiogasProductionJeiRecipes {
    private static final int REPRESENTATIVE_LIQUID_AMOUNT = 32_001;
    private static final long GAS_CAPACITY = Long.MAX_VALUE;

    private BiogasProductionJeiRecipes() {
    }

    public static List<BiogasProductionJeiRecipe> septicTankRecipes() {
        return List.of(new BiogasProductionJeiRecipe(
                REPRESENTATIVE_LIQUID_AMOUNT,
                gasProducedBySepticTank(9_999),
                gasProducedBySepticTank(REPRESENTATIVE_LIQUID_AMOUNT),
                SepticTankBiogasProduction.INTERVAL_TICKS,
                false,
                0,
                SepticTankBiogasProduction.RESIDUE_PRODUCTION_CHANCE_PERCENT
        ));
    }

    public static List<BiogasProductionJeiRecipe> biogasPondRecipes() {
        return List.of(new BiogasProductionJeiRecipe(
                REPRESENTATIVE_LIQUID_AMOUNT,
                gasProducedByBiogasPond(9_999),
                gasProducedByBiogasPond(REPRESENTATIVE_LIQUID_AMOUNT),
                BiogasPondBiogasProduction.INTERVAL_TICKS,
                true,
                BiogasPondControllerBlockEntity.GENERATOR_ENERGY_PER_BATCH,
                0
        ));
    }

    private static long gasProducedBySepticTank(int liquidAmount) {
        return SepticTankBiogasProduction.planBatch(true, liquidAmount, 0, GAS_CAPACITY).gasProduced();
    }

    private static long gasProducedByBiogasPond(int liquidAmount) {
        return BiogasPondBiogasProduction.planBatch(true, liquidAmount, 0, GAS_CAPACITY).gasProduced();
    }
}
