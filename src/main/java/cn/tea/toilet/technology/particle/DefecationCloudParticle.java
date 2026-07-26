package cn.tea.toilet.technology.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

/**
 * A small, fading brown cloud that drifts outwards and slightly upwards.
 */
public final class DefecationCloudParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    private DefecationCloudParticle(ClientLevel level, double x, double y, double z,
                                   double velocityX, double velocityY, double velocityZ,
                                   SpriteSet sprites) {
        super(level, x, y, z, velocityX, velocityY, velocityZ);
        this.sprites = sprites;
        this.hasPhysics = false;
        this.friction = 0.90F;
        this.gravity = 0.0F;
        this.lifetime = 16 + this.random.nextInt(9);
        this.quadSize = 0.12F + this.random.nextFloat() * 0.08F;
        this.setColor(0.40F, 0.27F, 0.13F);
        this.setAlpha(0.70F);
        this.pickSprite(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            this.yd += 0.0025D;
            this.setAlpha(0.70F * DefecationParticleMotion.fadeAlpha(this.age, this.lifetime));
            this.setSpriteFromAge(this.sprites);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    /** Factory registered on the client particle engine. */
    public static final class Provider implements net.minecraft.client.particle.ParticleProvider<net.minecraft.core.particles.SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public TextureSheetParticle createParticle(net.minecraft.core.particles.SimpleParticleType type, ClientLevel level,
                                                   double x, double y, double z,
                                                   double velocityX, double velocityY, double velocityZ) {
            return new DefecationCloudParticle(level, x, y, z, velocityX, velocityY, velocityZ, this.sprites);
        }
    }
}
