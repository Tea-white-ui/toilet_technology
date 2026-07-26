package cn.tea.toilet.technology.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;

/**
 * A small fading fragment that uses the server-provided motion and unmodified sprite colors.
 */
public final class DefecationCloudParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    private DefecationCloudParticle(ClientLevel level, double x, double y, double z,
                                   double velocityX, double velocityY, double velocityZ,
                                   SpriteSet sprites) {
        super(level, x, y, z, velocityX, velocityY, velocityZ);
        // Particle's velocity constructor adds a random motion component. Replace it so the
        // server-authoritative fragment velocity (including its downward Y sign) is exact.
        this.xd = velocityX;
        this.yd = velocityY;
        this.zd = velocityZ;
        this.sprites = sprites;
        this.hasPhysics = true;
        this.friction = DefecationParticleMotion.ITEM_AIR_DRAG;
        this.gravity = (float) (DefecationParticleMotion.gravityPerTick() / 0.04D);
        this.lifetime = DefecationParticleMotion.LIFETIME_TICKS;
        this.quadSize = 0.04F + this.random.nextFloat() * 0.12F;
        this.setAlpha(1.00F);
        this.pickSprite(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed) {
            if (this.onGround && this.yd < 0.0D) {
                this.yd *= -DefecationParticleMotion.ITEM_GROUND_BOUNCE;
            }
            this.setAlpha(DefecationParticleMotion.fadeAlpha(this.age, this.lifetime));
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
