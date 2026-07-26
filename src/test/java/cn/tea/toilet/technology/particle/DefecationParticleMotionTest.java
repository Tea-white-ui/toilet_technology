package cn.tea.toilet.technology.particle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefecationParticleMotionTest {
    @Test
    void radialVelocityPointsOutwardAndRisesSlightly() {
        DefecationParticleMotion motion = DefecationParticleMotion.fromAngle(0.0D, 0.10D);

        assertEquals(0.10D, motion.xVelocity(), 0.000001D);
        assertEquals(0.0D, motion.zVelocity(), 0.000001D);
        assertTrue(motion.yVelocity() > 0.0D);
    }

    @Test
    void alphaFadesFromOpaqueToTransparentOverLifetime() {
        assertEquals(1.0F, DefecationParticleMotion.fadeAlpha(0, 20), 0.000001F);
        assertTrue(DefecationParticleMotion.fadeAlpha(19, 20) < 0.1F);
    }
}
