package cn.tea.toilet.technology.block;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.toilet.PremiumToiletBlockEntity;
import cn.tea.toilet.technology.block.toilet.SquatToiletBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ToiletTechnology.MOD_ID
    );

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SquatToiletBlockEntity>> TOILET =
            BLOCK_ENTITIES.register("toilet", () -> BlockEntityType.Builder.of(
                    SquatToiletBlockEntity::new,
                    ModBlocks.SQUAT_TOILET.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PremiumToiletBlockEntity>> PREMIUM_TOILET =
            BLOCK_ENTITIES.register("premium_toilet", () -> BlockEntityType.Builder.of(
                    PremiumToiletBlockEntity::new,
                    ModBlocks.OAK_TOILET.get()
            ).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}