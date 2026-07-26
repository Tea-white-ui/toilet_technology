package cn.tea.toilet.technology.particle;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DefecationParticleMotionTest {
    @Test
    void fragmentUsesTheSuppliedRandomInitialVelocity() {
        DefecationParticleMotion motion = DefecationParticleMotion.fromAngle(0.0D, 0.10D, 0.20D);

        assertEquals(0.70D, DefecationParticleMotion.INITIAL_VELOCITY_SCALE, 0.000001D);
        assertEquals(0.07D, motion.xVelocity(), 0.000001D);
        assertEquals(0.0D, motion.zVelocity(), 0.000001D);
        assertEquals(0.14D, motion.yVelocity(), 0.000001D);
    }

    @Test
    void gravityMatchesDroppedItemPhysics() {
        assertEquals(0.04D, DefecationParticleMotion.gravityPerTick(), 0.000001D);
        assertEquals(0.98F, DefecationParticleMotion.ITEM_AIR_DRAG, 0.000001F);
        assertEquals(0.50D, DefecationParticleMotion.ITEM_GROUND_BOUNCE, 0.000001D);
    }

    @Test
    void alphaOnlyFadesDuringTheFinalPointOneSeconds() {
        assertEquals(2, DefecationParticleMotion.FADE_DURATION_TICKS);
        assertEquals(1.0F, DefecationParticleMotion.fadeAlpha(0, DefecationParticleMotion.LIFETIME_TICKS), 0.000001F);
        assertEquals(1.0F, DefecationParticleMotion.fadeAlpha(
                DefecationParticleMotion.LIFETIME_TICKS - DefecationParticleMotion.FADE_DURATION_TICKS,
                DefecationParticleMotion.LIFETIME_TICKS), 0.000001F);
        assertEquals(0.5F, DefecationParticleMotion.fadeAlpha(
                DefecationParticleMotion.LIFETIME_TICKS - 1,
                DefecationParticleMotion.LIFETIME_TICKS), 0.000001F);
        assertEquals(0.0F, DefecationParticleMotion.fadeAlpha(DefecationParticleMotion.LIFETIME_TICKS, DefecationParticleMotion.LIFETIME_TICKS), 0.000001F);
    }

    @Test
    void eachCloudSplitsIntoThreeSymmetricFragments() {
        assertEquals(4, DefecationParticleMotion.BURST_COUNT);
        assertEquals(12, DefecationParticleMotion.BURST_COUNT * DefecationParticleMotion.FRAGMENT_COUNT);
        assertEquals(3, DefecationParticleMotion.FRAGMENT_COUNT);
        assertEquals(-0.22D, DefecationParticleMotion.fragmentAngleOffset(0), 0.000001D);
        assertEquals(0.0D, DefecationParticleMotion.fragmentAngleOffset(1), 0.000001D);
        assertEquals(0.22D, DefecationParticleMotion.fragmentAngleOffset(2), 0.000001D);
    }
}
