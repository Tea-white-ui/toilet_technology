package cn.tea.toilet.technology;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static class Blocks {
        // === 建筑方块 ===
        public static final TagKey<Block> BUILDING_BLOCKS = tag("building_blocks");
        // === 厕所 ===
        public static final TagKey<Block> TOILETS = tag("toilets");

        private static TagKey<Block> tag(String name) {
            return BlockTags.create(ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, name));
        }
    }

    public static class Items {
        // === 材料 ===
        public static final TagKey<Item> MATERIALS = tag("materials");
            // === 粪便 ===
            public static final TagKey<Item> FECES = tag("feces");

        // === 建筑方块物品 ===
        public static final TagKey<Item> BUILDING_BLOCKS_ITEM = tag("building_blocks_item");
        // === 厕所 ===
        public static final TagKey<Item> TOILETS = tag("toilets");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, name));
        }
    }
}