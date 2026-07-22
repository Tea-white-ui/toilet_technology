package cn.tea.toilet.technology.block;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.drying.DryingBoxBlockEntity;
import cn.tea.toilet.technology.block.drying.DryingRackBlockEntity;
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
                    ModBlocks.OAK_TOILET.get(),
                    ModBlocks.STONE_TOILET.get(),
                    ModBlocks.IRON_TOILET.get(),
                    ModBlocks.GOLD_TOILET.get(),
                    ModBlocks.DIAMOND_TOILET.get(),
                    ModBlocks.NETHERITE_TOILET.get()
            ).build(null));

    // 干燥架方块实体，注册 tick 处理器
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DryingRackBlockEntity>> DRYING_RACK =
            BLOCK_ENTITIES.register("drying_rack", () -> BlockEntityType.Builder.of(
                    DryingRackBlockEntity::new,
                    ModBlocks.DRYING_RACK.get()
            ).build(null));

    // 干燥箱方块实体，16格存储，注册 tick 处理器
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DryingBoxBlockEntity>> DRYING_BOX =
            BLOCK_ENTITIES.register("drying_box", () -> BlockEntityType.Builder.of(
                    DryingBoxBlockEntity::new,
                    ModBlocks.DRYING_BOX.get()
            ).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}