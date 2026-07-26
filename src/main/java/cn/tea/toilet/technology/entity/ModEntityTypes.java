package cn.tea.toilet.technology.entity;

import cn.tea.toilet.technology.ModConstants;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/** Registers projectile entities owned by the mod. */
public final class ModEntityTypes {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, ModConstants.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<FecesBallProjectile>> FECES_BALL =
            ENTITY_TYPES.register("feces_ball", () -> EntityType.Builder
                    .<FecesBallProjectile>of(FecesBallProjectile::new, MobCategory.MISC)
                    .sized(0.25F, 0.25F)
                    .clientTrackingRange(4)
                    .updateInterval(10)
                    .build("feces_ball"));

    private ModEntityTypes() {
    }

    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
