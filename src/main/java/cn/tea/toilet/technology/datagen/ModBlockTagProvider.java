package cn.tea.toilet.technology.datagen;

import cn.tea.toilet.technology.ModTags;
import cn.tea.toilet.technology.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
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
                .add(ModBlocks.FECES_BLOCK.get())
                .add(ModBlocks.DRIED_FECES_BLOCK.get())
                .add(ModBlocks.ANTISEPTIC_BRICK.get());

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
                .add(ModBlocks.DRYING_RACK.get())
                .add(ModBlocks.DRYING_BOX.get());

        this.tag(ModTags.Blocks.SEPTIC_TANKS)
                .add(ModBlocks.SEPTIC_TANK_CONTROLLER.get())
                .add(ModBlocks.SEPTIC_TANK_WALL.get())
                .add(ModBlocks.SEPTIC_TANK_LIQUID_INPUT_PORT.get())
                .add(ModBlocks.SEPTIC_TANK_GAS_VALVE.get());

        this.tag(ModTags.Blocks.BIOGAS_PONDS)
                .add(ModBlocks.BIOGAS_POND_CONTROLLER.get())
                .add(ModBlocks.BIOGAS_POND_WALL.get())
                .add(ModBlocks.BIOGAS_POND_GAS_VALVE.get())
                .add(ModBlocks.BIOGAS_POND_ITEM_INPUT_PORT.get())
                .add(ModBlocks.BIOGAS_POND_FLUID_INPUT_PORT.get())
                .add(ModBlocks.BIOGAS_POND_ITEM_OUTPUT_PORT.get());

        this.tag(ModTags.Blocks.MACHINES)
                .add(ModBlocks.BIOGAS_GENERATOR.get())
                .add(ModBlocks.SEWAGE_PURIFIER.get());

        this.tag(ModTags.Blocks.HEAT_SOURCES)
                .addTag(BlockTags.FIRE)
                .addTag(BlockTags.CAMPFIRES)
                .add(Blocks.MAGMA_BLOCK)
                .add(Blocks.LAVA);

        // === 挖掘加速 ===
        this.tag(BlockTags.MINEABLE_WITH_SHOVEL)
                .add(ModBlocks.FECES_BLOCK.get())
                .add(ModBlocks.DRIED_FECES_BLOCK.get());

        this.tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.OAK_TOILET.get())
                .add(ModBlocks.DRYING_RACK.get())
                .add(ModBlocks.DRYING_BOX.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.ANTISEPTIC_BRICK.get())
                .add(ModBlocks.SQUAT_TOILET.get())
                .add(ModBlocks.STONE_TOILET.get())
                .add(ModBlocks.IRON_TOILET.get())
                .add(ModBlocks.GOLD_TOILET.get())
                .add(ModBlocks.DIAMOND_TOILET.get())
                .add(ModBlocks.NETHERITE_TOILET.get())
                .add(ModBlocks.SEPTIC_TANK_CONTROLLER.get())
                .add(ModBlocks.SEPTIC_TANK_WALL.get())
                .add(ModBlocks.SEPTIC_TANK_LIQUID_INPUT_PORT.get())
                .add(ModBlocks.SEPTIC_TANK_GAS_VALVE.get())
                .add(ModBlocks.BIOGAS_POND_CONTROLLER.get())
                .add(ModBlocks.BIOGAS_POND_WALL.get())
                .add(ModBlocks.BIOGAS_POND_GAS_VALVE.get())
                .add(ModBlocks.BIOGAS_POND_ITEM_INPUT_PORT.get())
                .add(ModBlocks.BIOGAS_POND_FLUID_INPUT_PORT.get())
                .add(ModBlocks.BIOGAS_POND_ITEM_OUTPUT_PORT.get())
                .add(ModBlocks.BIOGAS_GENERATOR.get())
                .add(ModBlocks.SEWAGE_PURIFIER.get());

        // === 挖掘等级 ===
        this.tag(Tags.Blocks.NEEDS_WOOD_TOOL)
                .add(ModBlocks.SQUAT_TOILET.get());

        this.tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.STONE_TOILET.get())
                .add(ModBlocks.ANTISEPTIC_BRICK.get());

        this.tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.IRON_TOILET.get())
                .add(ModBlocks.GOLD_TOILET.get())
                .add(ModBlocks.DIAMOND_TOILET.get())
                .add(ModBlocks.SEPTIC_TANK_CONTROLLER.get())
                .add(ModBlocks.SEPTIC_TANK_WALL.get())
                .add(ModBlocks.SEPTIC_TANK_LIQUID_INPUT_PORT.get())
                .add(ModBlocks.SEPTIC_TANK_GAS_VALVE.get())
                .add(ModBlocks.BIOGAS_POND_CONTROLLER.get())
                .add(ModBlocks.BIOGAS_POND_WALL.get())
                .add(ModBlocks.BIOGAS_POND_GAS_VALVE.get())
                .add(ModBlocks.BIOGAS_POND_ITEM_INPUT_PORT.get())
                .add(ModBlocks.BIOGAS_POND_FLUID_INPUT_PORT.get())
                .add(ModBlocks.BIOGAS_POND_ITEM_OUTPUT_PORT.get())
                .add(ModBlocks.BIOGAS_GENERATOR.get())
                .add(ModBlocks.SEWAGE_PURIFIER.get());

        this.tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.NETHERITE_TOILET.get());
    }
}