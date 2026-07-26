package cn.tea.toilet.technology.particle;

import cn.tea.toilet.technology.ModConstants;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * Registers visual particle types used by Toilet Technology.
 */
public final class ModParticles {
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(BuiltInRegistries.PARTICLE_TYPE, ModConstants.MOD_ID);

    /** A short-lived brown cloud emitted whenever a player completes a toilet action. */
    public static final DeferredHolder<ParticleType<?>, SimpleParticleType> DEFECATION_CLOUD =
            PARTICLE_TYPES.register("defecation_cloud", () -> new SimpleParticleType(false));

    private ModParticles() {
    }

    public static void register(IEventBus eventBus) {
        PARTICLE_TYPES.register(eventBus);
    }
}
