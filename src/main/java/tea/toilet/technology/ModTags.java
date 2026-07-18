package tea.toilet.technology;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> MATERIAL_TAG = createTag("material");
        public static final TagKey<Item> BLOCK_TAG = createTag("block");


        private static TagKey<Item> createTag(String name) {
            return TagKey.create(Registries.ITEM,
                    ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> BUILDING_BLOCKS = createTag("building_blocks");
        public static final TagKey<Block> FUNCTIONAL_BLOCKS = createTag("functional_blocks");

        private static TagKey<Block> createTag(String name) {
            return TagKey.create(Registries.BLOCK,
                    ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, name));
        }
    }

}