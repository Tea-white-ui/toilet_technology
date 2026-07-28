package cn.tea.toilet.technology.datagen;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class ModGasMeltingRecipeProviderTest {
    @Test
    void registersBiogasAndMethaneRecipesForGasFurnaceResourceRecovery() throws IOException {
        String source = Files.readString(Path.of(
                "src/main/java/cn/tea/toilet/technology/datagen/ModGasMeltingRecipeProvider.java"));

        assertTrue(source.contains("addBiogasAndMethaneRecipes(output, \"dried_feces_to_coal\""));
        assertTrue(source.contains("Ingredient.of(ModItems.DRIED_FECES.get())"));
        assertTrue(source.contains("new ItemStack(Items.COAL)"));
        assertTrue(source.contains("new ItemStack(Items.COAL), 25, 10"));

        assertTrue(source.contains("addBiogasAndMethaneRecipes(output, \"dried_feces_block_to_coal\""));
        assertTrue(source.contains("Ingredient.of(ModBlocks.DRIED_FECES_BLOCK.get())"));
        assertTrue(source.contains("new ItemStack(Items.COAL, 4)"));
        assertTrue(source.contains("new ItemStack(Items.COAL, 4), 80, 20"));

        assertTrue(source.contains("addBiogasAndMethaneRecipes(output, \"biogas_residue_to_bone_meal\""));
        assertTrue(source.contains("Ingredient.of(ModItems.BIOGAS_RESIDUE.get())"));
        assertTrue(source.contains("new ItemStack(Items.BONE_MEAL)"));
        assertTrue(source.contains("new ItemStack(Items.BONE_MEAL), 25, 10"));
        assertTrue(source.contains("GasRegistry.BIOGAS, biogasAmount, 100"));
        assertTrue(source.contains("GasRegistry.METHANE, methaneAmount, 100"));
        assertTrue(source.contains("name + \"_biogas\""));
        assertTrue(source.contains("name + \"_methane\""));
    }
}
