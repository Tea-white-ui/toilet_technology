package cn.tea.toilet.technology.item;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import org.junit.jupiter.api.Test;

class GasTankItemModelTest {
    private static final Path MODEL = Path.of("src/generated/resources/assets/toilet_technology/models/item/gas_tank.json");
    private static final Path RENDERER = Path.of("src/main/java/cn/tea/toilet/technology/client/GasTankItemRenderer.java");

    @Test
    void usesBuiltinEntityModelToInvokeDynamicGasRenderer() throws IOException {
        String modelJson = Files.readString(MODEL);

        assertTrue(modelJson.contains("\"parent\": \"minecraft:builtin/entity\""),
                "Gas tanks must use the builtin entity model so their custom renderer draws stored gas.");
    }

    @Test
    void mapsTextureTopToTheTopOfTheCustomRenderedSpriteAndKeepsFullGasOpaque() throws IOException {
        String rendererSource = Files.readString(RENDERER);

        assertTrue(rendererSource.contains("1.0F - WINDOW_BOTTOM * PIXEL"),
                "The bottom texture pixel must map to the lower world-space Y coordinate.");
        assertTrue(rendererSource.contains("1.0F - fillTop * PIXEL"),
                "The top fill pixel must map to the upper world-space Y coordinate.");
        assertTrue(rendererSource.contains("red, green, blue, 255, packedLight, packedOverlay"),
                "Gas must remain opaque beneath the semi-transparent glass overlay.");
        assertTrue(rendererSource.contains("minX, maxY, z).setColor(red, green, blue, alpha).setUv(u0, v0)"),
                "The texture top must be drawn at the upper world-space vertex.");
    }
}
