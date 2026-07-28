package cn.tea.toilet.technology;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

/**
 * 模组标签定义类
 * 定义所有自定义标签（Tags），用于方块和物品的分类和匹配
 * 标签可用于配方、事件处理、能力注册等场景
 */
public class ModTags {

    /** 方块标签集合 */
    public static class Blocks {
        /** 建筑方块标签 */
        public static final TagKey<Block> BUILDING_BLOCKS = tag("building_blocks");
        /** 所有马桶方块标签 */
        public static final TagKey<Block> TOILETS = tag("toilets");
        /** 高级马桶方块标签（可存储流体的马桶） */
        public static final TagKey<Block> ADVANCED_TOILETS = tag("advanced_toilets");
        /** 干燥设备方块标签（干燥架、干燥箱） */
        public static final TagKey<Block> DRYERS = tag("dryers");
        /** 污水处理系统方块标签（化粪池控制器和墙体） */
        public static final TagKey<Block> SEPTIC_TANKS = tag("septic_tanks");
        /** 沼气池组成方块标签（控制器、墙体及各类端口） */
        public static final TagKey<Block> BIOGAS_PONDS = tag("biogas_ponds");
        /** 吸收塔组成方块标签（塔底与塔体） */
        public static final TagKey<Block> ABSORPTION_TOWERS = tag("absorption_towers");
        /** 沼气生产与污水处理机器方块标签 */
        public static final TagKey<Block> MACHINES = tag("machines");
        /** 传输模组内置气体的燃气管道。 */
        public static final TagKey<Block> GAS_PIPES = tag("gas_pipes");
        /** 热源方块标签（用于加速干燥过程） */
        public static final TagKey<Block> HEAT_SOURCES = tag("heat_sources");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, name));
        }
    }

    /** 物品标签集合 */
    public static class Items {
        /** 材料物品标签 */
        public static final TagKey<Item> MATERIALS = tag("materials");
        /** 粪便相关物品标签 */
        public static final TagKey<Item> FECES = tag("feces");
        /** 建筑方块物品标签 */
        public static final TagKey<Item> BUILDING_BLOCKS_ITEM = tag("building_blocks_item");
        /** 马桶物品标签 */
        public static final TagKey<Item> TOILETS = tag("toilets");
        /** 干燥设备物品标签 */
        public static final TagKey<Item> DRYERS = tag("dryers");
        /** 污水处理系统方块物品标签 */
        public static final TagKey<Item> SEPTIC_TANKS = tag("septic_tanks");
        /** 沼气池组成方块物品标签 */
        public static final TagKey<Item> BIOGAS_PONDS = tag("biogas_ponds");
        /** 吸收塔组成方块物品标签 */
        public static final TagKey<Item> ABSORPTION_TOWERS = tag("absorption_towers");
        /** 沼气生产与污水处理机器物品标签 */
        public static final TagKey<Item> MACHINES = tag("machines");
        /** 燃气管道方块物品。 */
        public static final TagKey<Item> GAS_PIPES = tag("gas_pipes");
        /** 液体运输容器物品标签 */
        public static final TagKey<Item> FLUID_CONTAINERS = tag("fluid_containers");
        /** 气体运输容器物品标签 */
        public static final TagKey<Item> GAS_CONTAINERS = tag("gas_containers");
        /** 可投掷物品标签 */
        public static final TagKey<Item> THROWABLES = tag("throwables");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, name));
        }
    }
}