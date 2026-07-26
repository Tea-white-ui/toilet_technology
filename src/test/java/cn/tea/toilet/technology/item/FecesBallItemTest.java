package cn.tea.toilet.technology.item;

import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

class FecesBallItemTest {
    @Test
    void usesSnowballStyleProjectileAndAppliesTheRequestedHitEffects() throws Exception {
        String itemSource = Files.readString(Path.of("src/main/java/cn/tea/toilet/technology/item/FecesBallItem.java"));
        String projectileSource = Files.readString(Path.of("src/main/java/cn/tea/toilet/technology/entity/FecesBallProjectile.java"));

        assertTrue(Item.class.isAssignableFrom(FecesBallItem.class));
        assertTrue(itemSource.contains("shootFromRotation"));
        assertTrue(projectileSource.contains("MobEffects.CONFUSION") && projectileSource.contains("300"));
        assertTrue(projectileSource.contains("MobEffects.BLINDNESS") && projectileSource.contains("200"));
        assertTrue(projectileSource.contains("nextFloat() < 0.2F"));
        assertTrue(projectileSource.contains("damageSources.thrown"));
        assertTrue(projectileSource.contains("1.0F"));
    }
}
