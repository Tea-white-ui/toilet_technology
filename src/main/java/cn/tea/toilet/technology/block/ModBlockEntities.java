package cn.tea.toilet.technology.block;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.biogasgenerator.BiogasGeneratorBlockEntity;
import cn.tea.toilet.technology.block.drying.DryingBoxBlockEntity;
import cn.tea.toilet.technology.block.drying.DryingRackBlockEntity;
import cn.tea.toilet.technology.block.septictank.SepticTankControllerBlockEntity;
import cn.tea.toilet.technology.block.biogaspond.BiogasPondControllerBlockEntity;
import cn.tea.toilet.technology.block.toilet.NetheriteToiletBlockEntity;
import cn.tea.toilet.technology.block.toilet.PremiumToiletBlockEntity;
import cn.tea.toilet.technology.block.toilet.SquatToiletBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 方块实体注册类
 * 负责注册模组中的所有方块实体类型，包括：
 * - 蹲便器方块实体
 * - 高级马桶方块实体（适用于所有材质的高级马桶）
 * - 干燥架方块实体（带tick处理）
 * - 干燥箱方块实体（16格存储，带tick处理）
 */
public class ModBlockEntities {
    /** 方块实体延迟注册表 */
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(
            BuiltInRegistries.BLOCK_ENTITY_TYPE,
            ToiletTechnology.MOD_ID
    );

    /** 蹲便器方块实体：仅用于蹲便器方块 */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SquatToiletBlockEntity>> TOILET =
            BLOCK_ENTITIES.register("toilet", () -> BlockEntityType.Builder.of(
                    SquatToiletBlockEntity::new,
                    ModBlocks.SQUAT_TOILET.get()
            ).build(null));

    /** 高级马桶方块实体：适用于木制、石制、铁制、金制、钻石材质的高级马桶 */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PremiumToiletBlockEntity>> PREMIUM_TOILET =
            BLOCK_ENTITIES.register("premium_toilet", () -> BlockEntityType.Builder.of(
                    PremiumToiletBlockEntity::new,
                    ModBlocks.OAK_TOILET.get(),
                    ModBlocks.STONE_TOILET.get(),
                    ModBlocks.IRON_TOILET.get(),
                    ModBlocks.GOLD_TOILET.get(),
                    ModBlocks.DIAMOND_TOILET.get()
            ).build(null));

    /** 下界合金马桶方块实体：容量更大（16000mB），独立注册以支持特殊逻辑 */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<NetheriteToiletBlockEntity>> NETHERITE_TOILET =
            BLOCK_ENTITIES.register("netherite_toilet", () -> BlockEntityType.Builder.of(
                    NetheriteToiletBlockEntity::new,
                    ModBlocks.NETHERITE_TOILET.get()
            ).build(null));

    /** 干燥架方块实体：管理4个干燥槽位，注册 tick 处理器用于干燥逻辑 */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DryingRackBlockEntity>> DRYING_RACK =
            BLOCK_ENTITIES.register("drying_rack", () -> BlockEntityType.Builder.of(
                    DryingRackBlockEntity::new,
                    ModBlocks.DRYING_RACK.get()
            ).build(null));

    /** 干燥箱方块实体：管理16格输入+16格输出存储，注册 tick 处理器用于干燥逻辑 */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DryingBoxBlockEntity>> DRYING_BOX =
            BLOCK_ENTITIES.register("drying_box", () -> BlockEntityType.Builder.of(
                    DryingBoxBlockEntity::new,
                    ModBlocks.DRYING_BOX.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SepticTankControllerBlockEntity>> SEPTIC_TANK_CONTROLLER =
            BLOCK_ENTITIES.register("septic_tank_controller", () -> BlockEntityType.Builder.of(
                    SepticTankControllerBlockEntity::new, ModBlocks.SEPTIC_TANK_CONTROLLER.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BiogasPondControllerBlockEntity>> BIOGAS_POND_CONTROLLER =
            BLOCK_ENTITIES.register("biogas_pond_controller", () -> BlockEntityType.Builder.of(
                    BiogasPondControllerBlockEntity::new, ModBlocks.BIOGAS_POND_CONTROLLER.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BiogasGeneratorBlockEntity>> BIOGAS_GENERATOR =
            BLOCK_ENTITIES.register("biogas_generator", () -> BlockEntityType.Builder.of(
                    BiogasGeneratorBlockEntity::new, ModBlocks.BIOGAS_GENERATOR.get()
            ).build(null));

    /**
     * 注册所有方块实体到事件总线
     * 
     * @param bus NeoForge 事件总线
     */
    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }
}