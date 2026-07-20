package cn.tea.toilet.technology.datagen;

import cn.tea.toilet.technology.ModTags;
import cn.tea.toilet.technology.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // 材料
            // 粪便
            this.tag(ModTags.Items.FECES)
                    .add(ModItems.FECES.get())
                    .add(ModItems.DRIED_FECES.get()
                    );

        // 建筑方块物品
        this.tag(ModTags.Items.BUILDING_BLOCKS_ITEM)
                .add(ModItems.FECES_BLOCK_ITEM.get());
        this.tag(ModTags.Items.TOILETS)
                .add(ModItems.SQUAT_TOILET_ITEM.get());


        // === 标签嵌套 ===
        this.tag(ModTags.Items.MATERIALS)
                .addTag(ModTags.Items.FECES);

    }
}