package tea.toilet.technology;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Items {
        // === 原材料 ===
        public static final TagKey<Item> MATERIAL = createTag("material");
            // === 粪便 ===
            public static final TagKey<Item> FECES = createTag("feces");

        // === 建筑方块物品 ===
        public static final TagKey<Item> BUILDING_BLOCK = createTag("building_blocks");
        public static final TagKey<Item> TOILET = createTag("toilets");


        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, name));
        }
    }

    public static class Blocks {
        // === 建筑方块 ===
        public static final TagKey<Block> BUILDING_BLOCKS = createTag("building_blocks");
        // === 厕所 ===
        public static final TagKey<Block> TOILETS = createTag("toilets");

        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, name));
        }
    }

}