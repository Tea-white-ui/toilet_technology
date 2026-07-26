package cn.tea.toilet.technology.particle;

/**
 * Pure math for the outward drift and fade of a defecation cloud particle.
 */
public record DefecationParticleMotion(double xVelocity, double yVelocity, double zVelocity) {
    private static final double RISE_VELOCITY = 0.02D;

    public static DefecationParticleMotion fromAngle(double angle, double horizontalSpeed) {
        return new DefecationParticleMotion(
                Math.cos(angle) * horizontalSpeed,
                RISE_VELOCITY,
                Math.sin(angle) * horizontalSpeed
        );
    }

    public static float fadeAlpha(int age, int lifetime) {
        if (lifetime <= 0) {
            return 0.0F;
        }
        return Math.max(0.0F, 1.0F - (float) age / lifetime);
    }
}
