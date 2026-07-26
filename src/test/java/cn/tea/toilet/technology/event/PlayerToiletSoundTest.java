package cn.tea.toilet.technology.event;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlayerToiletSoundTest {

    @Test
    void fartSoundPitchIsWithinTheConfiguredVariationRange() {
        for (float randomValue : new float[]{0.0F, 0.2F, 0.5F, 0.8F, 1.0F}) {
            float pitch = PlayerToiletSound.getFartSoundPitch(randomValue);

            assertTrue(pitch >= 0.8F && pitch <= 1.2F,
                    "Expected pitch within [0.8, 1.2], but was " + pitch);
        }
    }
}
