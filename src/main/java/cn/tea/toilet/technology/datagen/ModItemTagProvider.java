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
        this.tag(ModTags.Items.FECES)
                .add(ModItems.FECES.get())
                .add(ModItems.DRIED_FECES.get());

        // 建筑方块物品
        this.tag(ModTags.Items.BUILDING_BLOCKS_ITEM)
                .add(ModItems.FECES_BLOCK_ITEM.get())
                .add(ModItems.DRIED_FECES_BLOCK_ITEM.get());
        this.tag(ModTags.Items.TOILETS)
                .add(ModItems.SQUAT_TOILET_ITEM.get())
                .add(ModItems.OAK_TOILET_ITEM.get())
                .add(ModItems.STONE_TOILET_ITEM.get())
                .add(ModItems.IRON_TOILET_ITEM.get())
                .add(ModItems.GOLD_TOILET_ITEM.get())
                .add(ModItems.DIAMOND_TOILET_ITEM.get())
                .add(ModItems.NETHERITE_TOILET_ITEM.get());
        this.tag(ModTags.Items.DRYERS)
                .add(ModItems.DRYING_RACK_ITEM.get())
                .add(ModItems.DRYING_BOX_ITEM.get());

        this.tag(ModTags.Items.SEPTIC_TANKS)
                .add(ModItems.SEPTIC_TANK_CONTROLLER_ITEM.get())
                .add(ModItems.SEPTIC_TANK_WALL_ITEM.get());

        this.tag(ModTags.Items.BIOGAS_PONDS)
                .add(ModItems.BIOGAS_POND_CONTROLLER_ITEM.get())
                .add(ModItems.BIOGAS_POND_WALL_ITEM.get())
                .add(ModItems.BIOGAS_POND_GAS_VALVE_ITEM.get())
                .add(ModItems.BIOGAS_POND_ITEM_INPUT_PORT_ITEM.get())
                .add(ModItems.BIOGAS_POND_FLUID_INPUT_PORT_ITEM.get())
                .add(ModItems.BIOGAS_POND_ITEM_OUTPUT_PORT_ITEM.get());

        this.tag(ModTags.Items.MACHINES)
                .add(ModItems.BIOGAS_GENERATOR_ITEM.get())
                .add(ModItems.SEWAGE_PURIFIER_ITEM.get());

        this.tag(ModTags.Items.FLUID_CONTAINERS)
                .add(ModItems.FECES_LIQUID_BUCKET.get())
                .add(ModItems.BIOGAS_BUCKET.get());

        // === 标签嵌套 ===
        this.tag(ModTags.Items.MATERIALS)
                .addTag(ModTags.Items.FECES)
                .add(ModItems.BIOGAS_RESIDUE.get());
    }
}