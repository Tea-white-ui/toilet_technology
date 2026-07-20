package cn.tea.toilet.technology.datagen;


import cn.tea.toilet.technology.ModTags;
import cn.tea.toilet.technology.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
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

        // 铲子加速挖掘
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.FECES_BLOCK.get());


    }
}