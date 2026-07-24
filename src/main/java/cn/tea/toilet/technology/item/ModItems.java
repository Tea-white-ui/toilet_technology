package cn.tea.toilet.technology.item;

import cn.tea.toilet.technology.ToiletTechnology;
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
            ToiletTechnology.MOD_ID
    );

    // === 材料 ===
        /** 粪便物品：可食用但会产生负面效果（失明、减速、反胃） */
        public static final DeferredHolder<Item, Item> FECES = ITEMS.register("feces", () -> new Item(
                new Item.Properties().food(new FoodProperties
                        .Builder()
                        .alwaysEdible()
                        .nutrition(1)
                        .saturationModifier(0.5f)
                        .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 20, 5), 1.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1), 1.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200, 1), 1.0f)
                        .build())));

        /** 干燥粪便物品：用于干燥配方或其他用途的基础材料 */
        public static final DeferredHolder<Item, Item> DRIED_FECES = ITEMS.register("dried_feces",
                () -> new Item(new Item.Properties()));

        /** 沼渣物品：沼气生产的副产物 */
        public static final DeferredHolder<Item, Item> BIOGAS_RESIDUE = ITEMS.register("biogas_residue",
                () -> new Item(new Item.Properties()));

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
    public static final DeferredHolder<Item, BlockItem> SEWAGE_PURIFIER_ITEM = ITEMS.register("sewage_purifier",
            () -> new BlockItem(ModBlocks.SEWAGE_PURIFIER.get(), new Item.Properties()));

    // === 罐 ===
    /** 粪便液体桶：用于存储和运输粪便液体，使用后返回空桶 */
    public static final DeferredHolder<Item, Item> FECES_LIQUID_BUCKET = ITEMS.register("feces_liquid_bucket",
            () -> new BucketItem(ModFluids.FECES_LIQUID.get(), new Item.Properties()
                    .craftRemainder(Items.BUCKET)
                    .stacksTo(1)
            ));

    /**
     * A sealed 1,000 mB transport tank. It deliberately is not a BucketItem, so using it can never
     * place gas as a world fluid. Optional compatibility lets Mekanism automation drain it.
     */
    public static final DeferredHolder<Item, Item> BIOGAS_TANK = ITEMS.register("biogas_tank",
            () -> new BiogasTankItem(new Item.Properties().stacksTo(1).durability((int) BiogasTankItem.CAPACITY)));

    /** 空沼气罐：用于后续装填沼气。 */
    public static final DeferredHolder<Item, Item> EMPTY_BIOGAS_TANK = ITEMS.register("empty_biogas_tank",
            () -> new Item(new Item.Properties().stacksTo(1)));




    /**
     * 注册所有物品到事件总线
     * 
     * @param bus NeoForge 事件总线
     */
    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}