package cn.tea.toilet.technology.block;
import cn.tea.toilet.technology.ModConstants;

import cn.tea.toilet.technology.block.biogasgenerator.BiogasGeneratorBlock;
import cn.tea.toilet.technology.block.gasmeltingfurnace.GasMeltingFurnaceBlock;
import cn.tea.toilet.technology.block.gaspipe.GasPipeBlock;
import cn.tea.toilet.technology.block.sewagepurifier.SewagePurifierBlock;
import cn.tea.toilet.technology.block.drying.DryingBoxBlock;
import cn.tea.toilet.technology.block.drying.DryingRackBlock;
import cn.tea.toilet.technology.block.septictank.SepticTankControllerBlock;
import cn.tea.toilet.technology.block.septictank.SepticTankWallBlock;
import cn.tea.toilet.technology.block.septictank.SepticTankPortBlock;
import cn.tea.toilet.technology.block.septictank.SepticTankPortType;

import cn.tea.toilet.technology.block.biogaspond.BiogasPondControllerBlock;
import cn.tea.toilet.technology.block.biogaspond.BiogasPondPortBlock;
import cn.tea.toilet.technology.block.biogaspond.BiogasPondPortType;
import cn.tea.toilet.technology.block.biogaspond.BiogasPondWallBlock;
import cn.tea.toilet.technology.block.absorptiontower.AbsorptionTowerBodyBlock;
import cn.tea.toilet.technology.block.absorptiontower.AbsorptionTowerBottomBlock;
import cn.tea.toilet.technology.block.toilet.*;
import cn.tea.toilet.technology.fluid.ModFluids;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 方块注册类
 * 负责注册模组中的所有方块，包括：
 * - 粪便方块
 * - 各类马桶（蹲便器、木制、石制、铁制、金制、钻石制、下界合金制）
 * - 干燥设备（干燥架、干燥箱）
 * - 流体方块（粪便液体）
 */
public class ModBlocks {
    /** 方块延迟注册表，使用 NeoForge 的 DeferredRegister 系统 */
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(
            BuiltInRegistries.BLOCK,
            ModConstants.MOD_ID
    );

    /** 粪便方块：具有类似泥土的特性，可减速玩家 */
    public static final DeferredHolder<Block, Block> FECES_BLOCK = BLOCKS.register("feces_block", () -> new Block(Block.Properties.of()
            .destroyTime(1.0f)
            .explosionResistance(0.5f)
            .sound(SoundType.MUD)
            .friction(0.7f)
            .speedFactor(0.9f)
            .pushReaction(PushReaction.NORMAL)
    ));

    /** 干粪块：由粪便块干燥而成的可燃方块。 */
    public static final DeferredHolder<Block, Block> DRIED_FECES_BLOCK = BLOCKS.register("dried_feces_block", () -> new Block(Block.Properties.of()
            .destroyTime(1.0f)
            .explosionResistance(0.5f)
            .sound(SoundType.PACKED_MUD)
            .pushReaction(PushReaction.NORMAL)
    ));

    /** 防腐砖：坚固的建筑方块。 */
    public static final DeferredHolder<Block, Block> ANTISEPTIC_BRICK = BLOCKS.register("antiseptic_brick", () -> new Block(Block.Properties.of()
            .strength(2.0f, 6.0f)
            .sound(SoundType.MUD_BRICKS)
            .pushReaction(PushReaction.BLOCK)
    ));
    /** 防腐砖楼梯。 */
    public static final DeferredHolder<Block, StairBlock> ANTISEPTIC_BRICK_STAIRS = BLOCKS.register("antiseptic_brick_stairs", () ->
            new StairBlock(ANTISEPTIC_BRICK.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(ANTISEPTIC_BRICK.get())));
    /** 防腐砖半砖。 */
    public static final DeferredHolder<Block, SlabBlock> ANTISEPTIC_BRICK_SLAB = BLOCKS.register("antiseptic_brick_slab", () ->
            new SlabBlock(BlockBehaviour.Properties.ofFullCopy(ANTISEPTIC_BRICK.get())));
    /** 防腐砖墙。 */
    public static final DeferredHolder<Block, WallBlock> ANTISEPTIC_BRICK_WALL = BLOCKS.register("antiseptic_brick_wall", () ->
            new WallBlock(BlockBehaviour.Properties.ofFullCopy(ANTISEPTIC_BRICK.get())));

    // === 厕所 ===
    /** 蹲便器：基础马桶，使用时直接掉落粪便物品 */
    public static final DeferredHolder<Block, Block> SQUAT_TOILET = BLOCKS.register("squat_toilet", () -> new SquatToiletBlock(Block.Properties.of()
            .destroyTime(2.0f)
            .explosionResistance(1.0f)
            .sound(SoundType.STONE)
            .isValidSpawn((state, world, pos, type) -> false)
            .isRedstoneConductor((state, world, pos) -> false)
            .pushReaction(PushReaction.BLOCK)
    ));

    /** 木制马桶：高级马桶，可存储粪便液体，效率系数 1.0 */
    public static final DeferredHolder<Block, Block> OAK_TOILET = BLOCKS.register("oak_toilet", () -> new PremiumToiletBlock(Block.Properties.of()
            .destroyTime(2.0f)
            .explosionResistance(1.0f)
            .sound(SoundType.WOOD)
            .isValidSpawn((state, world, pos, type) -> false)
            .isRedstoneConductor((state, world, pos) -> false)
            .pushReaction(PushReaction.BLOCK)
            ,1.0
    ));

    /** 石制马桶：高级马桶，效率系数 0.9 */
    public static final DeferredHolder<Block, Block> STONE_TOILET = BLOCKS.register("stone_toilet", () -> new PremiumToiletBlock(Block.Properties.of()
            .destroyTime(2.0f)
            .explosionResistance(1.0f)
            .sound(SoundType.STONE)
            .isValidSpawn((state, world, pos, type) -> false)
            .isRedstoneConductor((state, world, pos) -> false)
            .pushReaction(PushReaction.BLOCK)
            ,0.9
    ));

    /** 铁制马桶：高级马桶，效率系数 0.8 */
    public static final DeferredHolder<Block, Block> IRON_TOILET = BLOCKS.register("iron_toilet", () -> new PremiumToiletBlock(Block.Properties.of()
            .destroyTime(2.0f)
            .explosionResistance(1.0f)
            .sound(SoundType.STONE)
            .isValidSpawn((state, world, pos, type) -> false)
            .isRedstoneConductor((state, world, pos) -> false)
            .pushReaction(PushReaction.BLOCK)
            ,0.8
    ));

    /** 金制马桶：高级马桶，效率系数 0.7 */
    public static final DeferredHolder<Block, Block> GOLD_TOILET = BLOCKS.register("gold_toilet", () -> new PremiumToiletBlock(Block.Properties.of()
            .destroyTime(2.0f)
            .explosionResistance(1.0f)
            .sound(SoundType.STONE)
            .isValidSpawn((state, world, pos, type) -> false)
            .isRedstoneConductor((state, world, pos) -> false)
            .pushReaction(PushReaction.BLOCK)
            ,0.7
    ));

    /** 钻石马桶：高级马桶，效率系数 0.6 */
    public static final DeferredHolder<Block, Block> DIAMOND_TOILET = BLOCKS.register("diamond_toilet", () -> new PremiumToiletBlock(Block.Properties.of()
            .destroyTime(2.0f)
            .explosionResistance(1.0f)
            .sound(SoundType.STONE)
            .isValidSpawn((state, world, pos, type) -> false)
            .isRedstoneConductor((state, world, pos) -> false)
            .pushReaction(PushReaction.BLOCK)
            ,0.6
    ));

    /** 下界合金马桶：高级马桶，效率系数 0.6，具有防火特性 */
    public static final DeferredHolder<Block, Block> NETHERITE_TOILET = BLOCKS.register("netherite_toilet", () -> new NetheriteToiletBlock(Block.Properties.of()
            .destroyTime(2.0f)
            .explosionResistance(1.0f)
            .sound(SoundType.STONE)
            .isValidSpawn((state, world, pos, type) -> false)
            .isRedstoneConductor((state, world, pos) -> false)
            .pushReaction(PushReaction.BLOCK)
            ,0.6
    ));

    // === 干燥台/箱 ===
    /** 干燥架：4槽位干燥设备，支持右键放置/取出物品，具有3D物品渲染 */
    public static final DeferredHolder<Block, Block> DRYING_RACK = BLOCKS.register("drying_rack", () -> new DryingRackBlock(Block.Properties.of()
            .destroyTime(2.0f)
            .explosionResistance(1.0f)
            .sound(SoundType.WOOD)
            .isValidSpawn((state, world, pos, type) -> false)
            .isRedstoneConductor((state, world, pos) -> false)
            .pushReaction(PushReaction.BLOCK)
    ));

    /** 干燥箱：16格输入+16格输出的大型干燥设备，支持GUI界面和漏斗交互 */
    public static final DeferredHolder<Block, Block> DRYING_BOX = BLOCKS.register("drying_box", () -> new DryingBoxBlock(Block.Properties.of()
            .destroyTime(2.0f)
            .explosionResistance(1.0f)
            .sound(SoundType.WOOD)
            .isValidSpawn((state, world, pos, type) -> false)
            .isRedstoneConductor((state, world, pos) -> false)
            .pushReaction(PushReaction.BLOCK)
    ));

    public static final DeferredHolder<Block, Block> SEPTIC_TANK_CONTROLLER = BLOCKS.register("septic_tank_controller",
            () -> new SepticTankControllerBlock(Block.Properties.of().strength(3.5f, 6.0f).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Block, Block> SEPTIC_TANK_WALL = BLOCKS.register("septic_tank_wall",
            () -> new SepticTankWallBlock(Block.Properties.of().strength(3.5f, 6.0f).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Block, Block> SEPTIC_TANK_LIQUID_INPUT_PORT = BLOCKS.register("septic_tank_liquid_input_port",
            () -> new SepticTankPortBlock(SepticTankPortType.LIQUID_INPUT, Block.Properties.of().strength(3.5f, 6.0f).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Block, Block> SEPTIC_TANK_GAS_VALVE = BLOCKS.register("septic_tank_gas_valve",
            () -> new SepticTankPortBlock(SepticTankPortType.GAS_OUTPUT, Block.Properties.of().strength(3.5f, 6.0f).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Block, Block> BIOGAS_POND_CONTROLLER = BLOCKS.register("biogas_pond_controller",
            () -> new BiogasPondControllerBlock(Block.Properties.of().strength(3.5f, 6.0f).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Block, Block> BIOGAS_POND_WALL = BLOCKS.register("biogas_pond_wall",
            () -> new BiogasPondWallBlock(Block.Properties.of().strength(3.5f, 6.0f).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Block, Block> BIOGAS_POND_GAS_VALVE = BLOCKS.register("biogas_pond_gas_valve",
            () -> new BiogasPondPortBlock(BiogasPondPortType.GAS_OUTPUT, Block.Properties.of().strength(3.5f, 6.0f).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Block, Block> BIOGAS_POND_ITEM_INPUT_PORT = BLOCKS.register("biogas_pond_item_input_port",
            () -> new BiogasPondPortBlock(BiogasPondPortType.ITEM_INPUT, Block.Properties.of().strength(3.5f, 6.0f).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Block, Block> BIOGAS_POND_FLUID_INPUT_PORT = BLOCKS.register("biogas_pond_fluid_input_port",
            () -> new BiogasPondPortBlock(BiogasPondPortType.FLUID_INPUT, Block.Properties.of().strength(3.5f, 6.0f).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Block, Block> BIOGAS_POND_ITEM_OUTPUT_PORT = BLOCKS.register("biogas_pond_item_output_port",
            () -> new BiogasPondPortBlock(BiogasPondPortType.ITEM_OUTPUT, Block.Properties.of().strength(3.5f, 6.0f).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Block, Block> BIOGAS_GENERATOR = BLOCKS.register("biogas_generator",
            () -> new BiogasGeneratorBlock(Block.Properties.of().strength(3.5f, 6.0f).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK)));
    /** 燃气熔炼炉：可储存 2 桶气体，并预留一个输入槽和一个输出槽。 */
    public static final DeferredHolder<Block, Block> GAS_MELTING_FURNACE = BLOCKS.register("gas_melting_furnace",
            () -> new GasMeltingFurnaceBlock(Block.Properties.of().strength(3.5f, 6.0f).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK)));
    /** 基础燃气管道：六向连接，以每刻 256 mB 的速率传输气体。 */
    public static final DeferredHolder<Block, Block> GAS_PIPE = BLOCKS.register("gas_pipe",
            () -> new GasPipeBlock(Block.Properties.of().strength(2.0f, 6.0f).sound(SoundType.METAL).noOcclusion().pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Block, Block> SEWAGE_PURIFIER = BLOCKS.register("sewage_purifier",
            () -> new SewagePurifierBlock(Block.Properties.of().strength(3.5f, 6.0f).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Block, Block> ABSORPTION_TOWER_BOTTOM = BLOCKS.register("absorption_tower_bottom",
            () -> new AbsorptionTowerBottomBlock(Block.Properties.of().strength(3.5f, 6.0f).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK)));
    public static final DeferredHolder<Block, Block> ABSORPTION_TOWER_BODY = BLOCKS.register("absorption_tower_body",
            () -> new AbsorptionTowerBodyBlock(Block.Properties.of().strength(3.5f, 6.0f).sound(SoundType.METAL).pushReaction(PushReaction.BLOCK)));


    // === 流体 ===
    /** 粪便液体方块：不可碰撞、不可破坏的流体方块 */
    public static final DeferredHolder<Block, LiquidBlock> FECES_LIQUID_BLOCK = BLOCKS.register("feces_liquid_block",
            () -> new LiquidBlock(ModFluids.FECES_LIQUID.get(), Block.Properties.of()
                    .noCollission()
                    .strength(100.0f)
                    .noLootTable()
                    .liquid()
                    .replaceable()
                    .sound(SoundType.EMPTY)
            ));

    /** 废水方块：不可碰撞、不可破坏的流体方块。 */
    public static final DeferredHolder<Block, LiquidBlock> WASTEWATER_BLOCK = BLOCKS.register("wastewater_block",
            () -> new LiquidBlock(ModFluids.WASTEWATER.get(), Block.Properties.of()
                    .noCollission()
                    .strength(100.0f)
                    .noLootTable()
                    .liquid()
                    .replaceable()
                    .sound(SoundType.EMPTY)
            ));

    /**
     * 注册所有方块到事件总线
     * 
     * @param bus NeoForge 事件总线
     */
    public static void register(IEventBus bus){
        BLOCKS.register(bus);

    }
}