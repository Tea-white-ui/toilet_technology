package cn.tea.toilet.technology.block.gasmeltingfurnace;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class GasMeltingFurnaceRecipeSelectionTest {
    @Test
    void selectsARecipeThatMatchesTheGasCurrentlyStored() throws IOException {
        String source = Files.readString(Path.of(
                "src/main/java/cn/tea/toilet/technology/block/gasmeltingfurnace/GasMeltingFurnaceBlockEntity.java"));

        assertTrue(source.contains("getRecipesFor(ModRecipeTypes.GAS_MELTING.get()"));
        assertTrue(source.contains("recipe.hasRequiredGas(gasStack)"));
    }
}