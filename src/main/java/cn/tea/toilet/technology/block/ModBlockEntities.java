package cn.tea.toilet.technology.block;
import cn.tea.toilet.technology.ModConstants;

import cn.tea.toilet.technology.block.biogasgenerator.BiogasGeneratorBlockEntity;
import cn.tea.toilet.technology.block.gasmeltingfurnace.GasMeltingFurnaceBlockEntity;
import cn.tea.toilet.technology.block.gaspipe.GasPipeBlockEntity;
import cn.tea.toilet.technology.block.sewagepurifier.SewagePurifierBlockEntity;
import cn.tea.toilet.technology.block.drying.DryingBoxBlockEntity;
import cn.tea.toilet.technology.block.drying.DryingRackBlockEntity;
import cn.tea.toilet.technology.block.septictank.SepticTankControllerBlockEntity;
import cn.tea.toilet.technology.block.septictank.SepticTankPortBlockEntity;
import cn.tea.toilet.technology.block.biogaspond.BiogasPondControllerBlockEntity;
import cn.tea.toilet.technology.block.biogaspond.BiogasPondPortBlockEntity;
import cn.tea.toilet.technology.block.absorptiontower.AbsorptionTowerBottomBlockEntity;
import cn.tea.toilet.technology.block.absorptiontower.AbsorptionTowerBodyBlockEntity;
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
            ModConstants.MOD_ID
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

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SepticTankPortBlockEntity>> SEPTIC_TANK_PORT =
            BLOCK_ENTITIES.register("septic_tank_port", () -> BlockEntityType.Builder.of(
                    (pos, state) -> new SepticTankPortBlockEntity(pos, state,
                            ((cn.tea.toilet.technology.block.septictank.SepticTankPortBlock) state.getBlock()).getPortType()),
                    ModBlocks.SEPTIC_TANK_LIQUID_INPUT_PORT.get(),
                    ModBlocks.SEPTIC_TANK_GAS_VALVE.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BiogasPondControllerBlockEntity>> BIOGAS_POND_CONTROLLER =
            BLOCK_ENTITIES.register("biogas_pond_controller", () -> BlockEntityType.Builder.of(
                    BiogasPondControllerBlockEntity::new, ModBlocks.BIOGAS_POND_CONTROLLER.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BiogasPondPortBlockEntity>> BIOGAS_POND_PORT =
            BLOCK_ENTITIES.register("biogas_pond_port", () -> BlockEntityType.Builder.of(
                    (pos, state) -> new BiogasPondPortBlockEntity(pos, state,
                            ((cn.tea.toilet.technology.block.biogaspond.BiogasPondPortBlock) state.getBlock()).getPortType()),
                    ModBlocks.BIOGAS_POND_GAS_VALVE.get(),
                    ModBlocks.BIOGAS_POND_ITEM_INPUT_PORT.get(),
                    ModBlocks.BIOGAS_POND_FLUID_INPUT_PORT.get(),
                    ModBlocks.BIOGAS_POND_ITEM_OUTPUT_PORT.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BiogasGeneratorBlockEntity>> BIOGAS_GENERATOR =
            BLOCK_ENTITIES.register("biogas_generator", () -> BlockEntityType.Builder.of(
                    BiogasGeneratorBlockEntity::new, ModBlocks.BIOGAS_GENERATOR.get()
            ).build(null));

    /** 燃气熔炼炉方块实体：保存气体储罐及输入/输出物品槽。 */
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GasMeltingFurnaceBlockEntity>> GAS_MELTING_FURNACE =
            BLOCK_ENTITIES.register("gas_melting_furnace", () -> BlockEntityType.Builder.of(
                    GasMeltingFurnaceBlockEntity::new, ModBlocks.GAS_MELTING_FURNACE.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GasPipeBlockEntity>> GAS_PIPE =
            BLOCK_ENTITIES.register("gas_pipe", () -> BlockEntityType.Builder.of(
                    GasPipeBlockEntity::new, ModBlocks.GAS_PIPE.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SewagePurifierBlockEntity>> SEWAGE_PURIFIER =
            BLOCK_ENTITIES.register("sewage_purifier", () -> BlockEntityType.Builder.of(
                    SewagePurifierBlockEntity::new, ModBlocks.SEWAGE_PURIFIER.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AbsorptionTowerBottomBlockEntity>> ABSORPTION_TOWER_BOTTOM =
            BLOCK_ENTITIES.register("absorption_tower_bottom", () -> BlockEntityType.Builder.of(
                    AbsorptionTowerBottomBlockEntity::new, ModBlocks.ABSORPTION_TOWER_BOTTOM.get()
            ).build(null));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AbsorptionTowerBodyBlockEntity>> ABSORPTION_TOWER_BODY =
            BLOCK_ENTITIES.register("absorption_tower_body", () -> BlockEntityType.Builder.of(
                    AbsorptionTowerBodyBlockEntity::new, ModBlocks.ABSORPTION_TOWER_BODY.get()
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