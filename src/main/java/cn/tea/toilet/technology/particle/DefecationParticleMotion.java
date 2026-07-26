package cn.tea.toilet.technology.particle;

/**
 * Pure motion policy matching a dropped ItemEntity: upward toss, gravity, and air drag.
 */
public record DefecationParticleMotion(double xVelocity, double yVelocity, double zVelocity) {
    /** Minecraft runs at 20 ticks per second, so forty ticks is 2 seconds. */
    public static final int LIFETIME_TICKS = 40;
    /** At 20 TPS, fade only during the final 0.1 seconds of the particle's lifetime. */
    public static final int FADE_DURATION_TICKS = 2;
    /** Each burst contains three fragments, yielding 12 particles instead of the previous 42. */
    public static final int BURST_COUNT = 2;
    /** Scale every initial velocity component to 70% of the sampled emission velocity. */
    public static final double INITIAL_VELOCITY_SCALE = 0.70D;
    /** Every emitted cloud is split into this many small fragments. */
    public static final int FRAGMENT_COUNT = 3;
    /** Match a dropped item's default vertical gravity per tick. */
    public static final double ITEM_GRAVITY_PER_TICK = 0.04D;
    /** Match a dropped item's air drag multiplier. */
    public static final float ITEM_AIR_DRAG = 0.98F;
    /** Match the vertical damping of an item after it hits the ground. */
    public static final double ITEM_GROUND_BOUNCE = 0.00D;

    private static final double FRAGMENT_ANGLE_SPREAD = 0.9D;

    public static DefecationParticleMotion fromAngle(double angle, double horizontalSpeed, double verticalVelocity) {
        double scaledHorizontalSpeed = horizontalSpeed * INITIAL_VELOCITY_SCALE;
        return new DefecationParticleMotion(
                Math.cos(angle) * scaledHorizontalSpeed,
                verticalVelocity * INITIAL_VELOCITY_SCALE,
                Math.sin(angle) * scaledHorizontalSpeed
        );
    }

    public static double gravityPerTick() {
        return ITEM_GRAVITY_PER_TICK;
    }

    public static double fragmentAngleOffset(int fragmentIndex) {
        if (fragmentIndex < 0 || fragmentIndex >= FRAGMENT_COUNT) {
            throw new IllegalArgumentException("Fragment index out of range: " + fragmentIndex);
        }
        return (fragmentIndex - (FRAGMENT_COUNT - 1) / 2.0D) * FRAGMENT_ANGLE_SPREAD;
    }

    public static float fadeAlpha(int age, int lifetime) {
        if (lifetime <= 0) {
            return 0.0F;
        }
        int fadeStartAge = Math.max(0, lifetime - FADE_DURATION_TICKS);
        if (age <= fadeStartAge) {
            return 1.0F;
        }
        return Math.max(0.0F, (float) (lifetime - age) / FADE_DURATION_TICKS);
    }
}
