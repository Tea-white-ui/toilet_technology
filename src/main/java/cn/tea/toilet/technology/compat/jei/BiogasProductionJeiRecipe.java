package cn.tea.toilet.technology.compat.jei;

/** Static JEI documentation for a biogas machine's variable-output fermentation process. */
public record BiogasProductionJeiRecipe(
        int displayedLiquidAmount,
        long minimumBiogasAmount,
        long maximumBiogasAmount,
        int durationTicks,
        boolean requiresGeneratorEnergy,
        int energyPerBatch,
        int residueChancePercent
) {
    public boolean hasResidueOutput() {
        return residueChancePercent > 0;
    }
}
