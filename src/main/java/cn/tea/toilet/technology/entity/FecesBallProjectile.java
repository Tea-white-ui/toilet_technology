package cn.tea.toilet.technology.entity;

import cn.tea.toilet.technology.item.ModItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;


/** A thrown feces ball that harms and debilitates the entity it strikes. */
public class FecesBallProjectile extends ThrowableItemProjectile {
    public FecesBallProjectile(EntityType<? extends FecesBallProjectile> entityType, Level level) {
        super(entityType, level);
    }

    public FecesBallProjectile(Level level, LivingEntity owner) {
        super(ModEntityTypes.FECES_BALL.get(), owner, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ModItems.FECES_BALL.get();
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity target = result.getEntity();
        DamageSources damageSources = this.level().damageSources();
        target.hurt(damageSources.thrown(this, this.getOwner()), 1.0F);

        if (target instanceof LivingEntity livingTarget) {
            livingTarget.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 300, 0));
            if (this.random.nextFloat() < 0.2F) {
                livingTarget.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0));
            }
        }
    }

    @Override
    protected void onHit(net.minecraft.world.phys.HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            ((ServerLevel) this.level()).sendParticles(
                    new ItemParticleOption(ParticleTypes.ITEM, this.getItem()),
                    this.getX(), this.getY(), this.getZ(),
                    8, 0.1D, 0.1D, 0.1D, 0.05D);
            this.discard();
        }
    }
}
