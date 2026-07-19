package tea.toilet.technology.client.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import tea.toilet.technology.ModTags;
import tea.toilet.technology.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagsProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider wrapperLookup) {
        // === 建筑方块 ===
        getOrCreateTagBuilder(ModTags.Blocks.BUILDING_BLOCKS)
                .add(ModBlocks.FECES_BLOCK);
        // === 厕所 ===
        getOrCreateTagBuilder(ModTags.Blocks.TOILETS)
                .add(ModBlocks.SQUAT_TOILET);
    }
}
