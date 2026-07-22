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
        /** 热源方块标签（用于加速干燥过程） */
        public static final TagKey<Block> HEAT_SOURCES = tag("heat_sources");

        /**
         * 创建方块标签
         * 
         * @param name 标签名称
         * @return 方块标签键
         */
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, name));
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




        /**
         * 创建物品标签
         * 
         * @param name 标签名称
         * @return 物品标签键
         */
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, name));
        }
    }
}