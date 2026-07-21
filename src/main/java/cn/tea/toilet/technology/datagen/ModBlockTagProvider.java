package cn.tea.toilet.technology.datagen;


import cn.tea.toilet.technology.ModTags;
import cn.tea.toilet.technology.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {


    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(ModTags.Blocks.BUILDING_BLOCKS)
                .add(ModBlocks.FECES_BLOCK.get());

        this.tag(ModTags.Blocks.TOILETS)
                .add(ModBlocks.SQUAT_TOILET.get())
                .add(ModBlocks.OAK_TOILET.get());

        this.tag(ModTags.Blocks.ADVANCED_TOILETS)
                .add(ModBlocks.OAK_TOILET.get())
                .add(ModBlocks.STONE_TOILET.get())
                .add(ModBlocks.IRON_TOILET.get())
                .add(ModBlocks.GOLD_TOILET.get())
                .add(ModBlocks.DIAMOND_TOILET.get())
                .add(ModBlocks.NETHERITE_TOILET.get());

        this.tag(ModTags.Blocks.DRYERS)
                .add(ModBlocks.DRYING_RACK.get());


        // === 挖掘加速 ===
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.FECES_BLOCK.get());

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.OAK_TOILET.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.SQUAT_TOILET.get())
                .add(ModBlocks.STONE_TOILET.get())
                .add(ModBlocks.IRON_TOILET.get())
                .add(ModBlocks.GOLD_TOILET.get())
                .add(ModBlocks.DIAMOND_TOILET.get())
                .add(ModBlocks.NETHERITE_TOILET.get());

        // === 挖掘等级 ===
        this.tag(Tags.Blocks.NEEDS_WOOD_TOOL)
                .add(ModBlocks.SQUAT_TOILET.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.STONE_TOILET.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.IRON_TOILET.get())
                .add(ModBlocks.GOLD_TOILET.get())
                .add(ModBlocks.DIAMOND_TOILET.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.NETHERITE_TOILET.get());


    }
}