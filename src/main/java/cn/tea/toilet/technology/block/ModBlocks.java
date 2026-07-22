package cn.tea.toilet.technology.block;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.drying.DryingBoxBlock;
import cn.tea.toilet.technology.block.drying.DryingRackBlock;
import cn.tea.toilet.technology.block.toilet.PremiumToiletBlock;
import cn.tea.toilet.technology.block.toilet.PremiumToiletBlockEntity;
import cn.tea.toilet.technology.block.toilet.SquatToiletBlock;
import cn.tea.toilet.technology.fluid.ModFluids;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.SoundType;
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
            ToiletTechnology.MOD_ID
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
    public static final DeferredHolder<Block, Block> NETHERITE_TOILET = BLOCKS.register("netherite_toilet", () -> new PremiumToiletBlock(Block.Properties.of()
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

    /**
     * 注册所有方块到事件总线
     * 
     * @param bus NeoForge 事件总线
     */
    public static void register(IEventBus bus){
        BLOCKS.register(bus);

    }
}