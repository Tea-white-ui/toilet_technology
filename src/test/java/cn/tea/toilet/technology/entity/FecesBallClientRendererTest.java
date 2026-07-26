package cn.tea.toilet.technology.entity;

import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FecesBallClientRendererTest {
    @Test
    void registersThrownItemRendererForTheFecesBallProjectile() throws Exception {
        String clientSource = Files.readString(Path.of("src/main/java/cn/tea/toilet/technology/ToiletTechnologyClient.java"));

        assertTrue(clientSource.contains("EntityRenderers.register(ModEntityTypes.FECES_BALL.get(), ThrownItemRenderer::new)"),
                "The feces-ball projectile must have a client renderer before it can enter a rendered level");
    }
}
