package cn.tea.toilet.technology.item;
import cn.tea.toilet.technology.ModConstants;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.fluid.ModFluids;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 物品注册类
 * 负责注册模组中的所有物品，包括：
 * - 材料物品（粪便、干燥粪便）
 * - 方块对应的物品形式
 * - 流体桶物品
 */
public class ModItems {


    /** 物品延迟注册表 */
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            BuiltInRegistries.ITEM,
            ModConstants.MOD_ID
    );

    // === 材料 ===
        /** 粪便物品：可食用但会产生负面效果（失明、减速、反胃） */
        public static final DeferredHolder<Item, Item> FECES = ITEMS.register("feces", () -> new TooltipItem(
                new Item.Properties().food(new FoodProperties
                        .Builder()
                        .alwaysEdible()
                        .nutrition(1)
                        .saturationModifier(0.5f)
                        .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 20, 5), 1.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1), 1.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200, 1), 1.0f)
                        .build()), "tooltip.toilet_technology.feces"));

        /** 干燥粪便物品：用于干燥配方或其他用途的基础材料 */
        public static final DeferredHolder<Item, Item> DRIED_FECES = ITEMS.register("dried_feces",
                () -> new TooltipItem(new Item.Properties(), "tooltip.toilet_technology.dried_feces"));

        /** 粪球：可像雪球一样投掷，命中生物时造成伤害并施加负面效果。 */
        public static final DeferredHolder<Item, Item> FECES_BALL = ITEMS.register("feces_ball",
                () -> new FecesBallItem(new Item.Properties()));

        /** 沼渣物品：沼气生产的副产物，可作为骨粉催熟作物。 */
        public static final DeferredHolder<Item, Item> BIOGAS_RESIDUE = ITEMS.register("biogas_residue",
                () -> new BiogasResidueItem(new Item.Properties(), "tooltip.toilet_technology.biogas_residue"));

        /** 密封组件：用于合成化粪池壁的密封材料 */
        public static final DeferredHolder<Item, Item> SEALING_COMPONENT = ITEMS.register("sealing_component",
                () -> new Item(new Item.Properties()));

        /** 金属网：用于制作机器和其他结构的材料 */
        public static final DeferredHolder<Item, Item> METAL_MESH = ITEMS.register("metal_mesh",
                () -> new Item(new Item.Properties()));

        /** 多层烧结金属网：由金属网熔炼制成的高级材料 */
        public static final DeferredHolder<Item, Item> MULTI_LAYER_SINTERED_METAL_MESH = ITEMS.register("multi_layer_sintered_metal_mesh",
                () -> new TooltipItem(new Item.Properties(), "tooltip.toilet_technology.multi_layer_sintered_metal_mesh"));

    // === 方块物品 ===
    /** 粪便方块物品 */
    public static final DeferredHolder<Item, BlockItem> FECES_BLOCK_ITEM = ITEMS.register("feces_block",
            () -> new BlockItem(ModBlocks.FECES_BLOCK.get(), new Item.Properties()));
    /** 干粪块方块物品 */
    public static final DeferredHolder<Item, BlockItem> DRIED_FECES_BLOCK_ITEM = ITEMS.register("dried_feces_block",
            () -> new BlockItem(ModBlocks.DRIED_FECES_BLOCK.get(), new Item.Properties()));
    /** 防腐砖方块物品 */
    public static final DeferredHolder<Item, BlockItem> ANTISEPTIC_BRICK_ITEM = ITEMS.register("antiseptic_brick",
            () -> new BlockItem(ModBlocks.ANTISEPTIC_BRICK.get(), new Item.Properties()));
    /** 防腐砖楼梯方块物品 */
    public static final DeferredHolder<Item, BlockItem> ANTISEPTIC_BRICK_STAIRS_ITEM = ITEMS.register("antiseptic_brick_stairs",
            () -> new BlockItem(ModBlocks.ANTISEPTIC_BRICK_STAIRS.get(), new Item.Properties()));
    /** 防腐砖半砖方块物品 */
    public static final DeferredHolder<Item, BlockItem> ANTISEPTIC_BRICK_SLAB_ITEM = ITEMS.register("antiseptic_brick_slab",
            () -> new BlockItem(ModBlocks.ANTISEPTIC_BRICK_SLAB.get(), new Item.Properties()));
    /** 防腐砖墙方块物品 */
    public static final DeferredHolder<Item, BlockItem> ANTISEPTIC_BRICK_WALL_ITEM = ITEMS.register("antiseptic_brick_wall",
            () -> new BlockItem(ModBlocks.ANTISEPTIC_BRICK_WALL.get(), new Item.Properties()));
    // === 厕所 ===
    /** 蹲便器物品 */
    public static final DeferredHolder<Item, BlockItem> SQUAT_TOILET_ITEM = ITEMS.register("squat_toilet",
            () -> new BlockItem(ModBlocks.SQUAT_TOILET.get(), new Item.Properties()));
    /** 木制马桶物品 */
    public static final DeferredHolder<Item, BlockItem> OAK_TOILET_ITEM = ITEMS.register("oak_toilet",
            () -> new BlockItem(ModBlocks.OAK_TOILET.get(), new Item.Properties()));
    /** 石制马桶物品 */
    public static final DeferredHolder<Item, BlockItem> STONE_TOILET_ITEM = ITEMS.register("stone_toilet",
            () -> new BlockItem(ModBlocks.STONE_TOILET.get(), new Item.Properties()));
    /** 铁制马桶物品 */
    public static final DeferredHolder<Item, BlockItem> IRON_TOILET_ITEM = ITEMS.register("iron_toilet",
            () -> new BlockItem(ModBlocks.IRON_TOILET.get(), new Item.Properties()));
    /** 金制马桶物品 */
    public static final DeferredHolder<Item, BlockItem> GOLD_TOILET_ITEM = ITEMS.register("gold_toilet",
            () -> new BlockItem(ModBlocks.GOLD_TOILET.get(), new Item.Properties()));
    /** 钻石马桶物品 */
    public static final DeferredHolder<Item, BlockItem> DIAMOND_TOILET_ITEM = ITEMS.register("diamond_toilet",
            () -> new BlockItem(ModBlocks.DIAMOND_TOILET.get(), new Item.Properties()));
    /** 下界合金马桶物品：具有防火特性 */
    public static final DeferredHolder<Item, BlockItem> NETHERITE_TOILET_ITEM = ITEMS.register("netherite_toilet",
            () -> new BlockItem(ModBlocks.NETHERITE_TOILET.get(), new Item.Properties().fireResistant()));
    // === 干燥台/箱 ===
    /** 干燥架物品 */
    public static final DeferredHolder<Item, BlockItem> DRYING_RACK_ITEM = ITEMS.register("drying_rack",
            () -> new BlockItem(ModBlocks.DRYING_RACK.get(), new Item.Properties()));
    /** 干燥箱物品 */
    public static final DeferredHolder<Item, BlockItem> DRYING_BOX_ITEM = ITEMS.register("drying_box",
            () -> new BlockItem(ModBlocks.DRYING_BOX.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> SEPTIC_TANK_CONTROLLER_ITEM = ITEMS.register("septic_tank_controller",
            () -> new BlockItem(ModBlocks.SEPTIC_TANK_CONTROLLER.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SEPTIC_TANK_WALL_ITEM = ITEMS.register("septic_tank_wall",
            () -> new BlockItem(ModBlocks.SEPTIC_TANK_WALL.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SEPTIC_TANK_LIQUID_INPUT_PORT_ITEM = ITEMS.register("septic_tank_liquid_input_port",
            () -> new BlockItem(ModBlocks.SEPTIC_TANK_LIQUID_INPUT_PORT.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SEPTIC_TANK_GAS_VALVE_ITEM = ITEMS.register("septic_tank_gas_valve",
            () -> new BlockItem(ModBlocks.SEPTIC_TANK_GAS_VALVE.get(), new Item.Properties()));

    public static final DeferredHolder<Item, BlockItem> BIOGAS_POND_CONTROLLER_ITEM = ITEMS.register("biogas_pond_controller",
            () -> new BlockItem(ModBlocks.BIOGAS_POND_CONTROLLER.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> BIOGAS_POND_WALL_ITEM = ITEMS.register("biogas_pond_wall",
            () -> new BlockItem(ModBlocks.BIOGAS_POND_WALL.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> BIOGAS_POND_GAS_VALVE_ITEM = ITEMS.register("biogas_pond_gas_valve",
            () -> new BlockItem(ModBlocks.BIOGAS_POND_GAS_VALVE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> BIOGAS_POND_ITEM_INPUT_PORT_ITEM = ITEMS.register("biogas_pond_item_input_port",
            () -> new BlockItem(ModBlocks.BIOGAS_POND_ITEM_INPUT_PORT.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> BIOGAS_POND_FLUID_INPUT_PORT_ITEM = ITEMS.register("biogas_pond_fluid_input_port",
            () -> new BlockItem(ModBlocks.BIOGAS_POND_FLUID_INPUT_PORT.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> BIOGAS_POND_ITEM_OUTPUT_PORT_ITEM = ITEMS.register("biogas_pond_item_output_port",
            () -> new BlockItem(ModBlocks.BIOGAS_POND_ITEM_OUTPUT_PORT.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> BIOGAS_GENERATOR_ITEM = ITEMS.register("biogas_generator",
            () -> new BlockItem(ModBlocks.BIOGAS_GENERATOR.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> GAS_MELTING_FURNACE_ITEM = ITEMS.register("gas_melting_furnace",
            () -> new BlockItem(ModBlocks.GAS_MELTING_FURNACE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> GAS_GENERATORS_ITEM = ITEMS.register("gas_generators",
            () -> new BlockItem(ModBlocks.GAS_GENERATORS.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> GAS_PIPE_ITEM = ITEMS.register("gas_pipe",
            () -> new BlockItem(ModBlocks.GAS_PIPE.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> SEWAGE_PURIFIER_ITEM = ITEMS.register("sewage_purifier",
            () -> new BlockItem(ModBlocks.SEWAGE_PURIFIER.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> ABSORPTION_TOWER_BOTTOM_ITEM = ITEMS.register("absorption_tower_bottom",
            () -> new BlockItem(ModBlocks.ABSORPTION_TOWER_BOTTOM.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> ABSORPTION_TOWER_BODY_ITEM = ITEMS.register("absorption_tower_body",
            () -> new BlockItem(ModBlocks.ABSORPTION_TOWER_BODY.get(), new Item.Properties()));

    // === 罐 ===
    /** 粪便液体桶：用于存储和运输粪便液体，使用后返回空桶 */
    public static final DeferredHolder<Item, Item> FECES_LIQUID_BUCKET = ITEMS.register("feces_liquid_bucket",
            () -> new BucketItem(ModFluids.FECES_LIQUID.get(), new Item.Properties()
                    .craftRemainder(Items.BUCKET)
                    .stacksTo(1)
            ));

    /** 废水桶：用于存储和运输废水，使用后返回空桶。 */
    public static final DeferredHolder<Item, Item> WASTEWATER_BUCKET = ITEMS.register("wastewater_bucket",
            () -> new BucketItem(ModFluids.WASTEWATER.get(), new Item.Properties()
                    .craftRemainder(Items.BUCKET)
                    .stacksTo(1)
            ));

    /** A reusable gas transport tank that can hold any one registered gas type. */
    public static final DeferredHolder<Item, Item> GAS_TANK = ITEMS.register("gas_tank",
            () -> new GasTankItem(new Item.Properties().stacksTo(4)));




    /**
     * 注册所有物品到事件总线
     * 
     * @param bus NeoForge 事件总线
     */
    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}