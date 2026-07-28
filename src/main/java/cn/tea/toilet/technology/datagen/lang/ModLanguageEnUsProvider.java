package cn.tea.toilet.technology.datagen.lang;
import cn.tea.toilet.technology.ModConstants;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageEnUsProvider extends LanguageProvider {
    public ModLanguageEnUsProvider(PackOutput output, String locale) {
        super(output, ModConstants.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.toilet_technology", "toilet technology");
        add(ModItems.FECES.get(), "Feces");
        add(ModItems.FECES_BLOCK_ITEM.get(), "Feces block");
        add(ModItems.DRIED_FECES.get(),"Dried feces");
        add(ModItems.FECES_BALL.get(), "Feces Ball");
        add(ModItems.DRIED_FECES_BLOCK_ITEM.get(), "Dried feces block");
        add(ModItems.ANTISEPTIC_BRICK_ITEM.get(), "Antiseptic Brick");
        add(ModItems.ANTISEPTIC_BRICK_STAIRS_ITEM.get(), "Antiseptic Brick Stairs");
        add(ModItems.ANTISEPTIC_BRICK_SLAB_ITEM.get(), "Antiseptic Brick Slab");
        add(ModItems.ANTISEPTIC_BRICK_WALL_ITEM.get(), "Antiseptic Brick Wall");
        add(ModItems.BIOGAS_RESIDUE.get(), "Biogas Residue");
        add(ModItems.SEALING_COMPONENT.get(), "Sealing Component");
        add(ModItems.METAL_MESH.get(), "Metal Mesh");
        add(ModItems.MULTI_LAYER_SINTERED_METAL_MESH.get(), "Multi-Layer Sintered Metal Mesh");
        add(ModItems.SQUAT_TOILET_ITEM.get(),"Squat Toilet");
        add(ModItems.OAK_TOILET_ITEM.get(), "Oak Toilet");
        add(ModItems.STONE_TOILET_ITEM.get(),"Stone Toilet");
        add(ModItems.IRON_TOILET_ITEM.get(),"Iron Toilet");
        add(ModItems.GOLD_TOILET_ITEM.get(),"Gold Toilet");
        add(ModItems.DIAMOND_TOILET_ITEM.get(),"Diamond Toilet");
        add(ModItems.NETHERITE_TOILET_ITEM.get(),"Netherite Toilet");
        add(ModItems.FECES_LIQUID_BUCKET.get(), "Feces Liquid Bucket");
        add(ModItems.WASTEWATER_BUCKET.get(), "Wastewater Bucket");
        add(ModItems.GAS_TANK.get(), "Gas Tank");
        add(ModBlocks.FECES_LIQUID_BLOCK.get(), "Feces Liquid");
        add(ModBlocks.WASTEWATER_BLOCK.get(), "Wastewater");
        add(ModBlocks.DRYING_RACK.get(), "Dryer_Rack");
        add(ModBlocks.DRYING_BOX.get(), "Drying Box");
        add("fluid_type.toilet_technology.feces_liquid","Feces Liquid");
        add("fluid_type.toilet_technology.wastewater", "Wastewater");
        add("jei.toilet_technology.biogas.amount", "%s mB");
        add("jei.toilet_technology.septic_tank_biogas.title", "Septic Tank Fermentation");
        add("jei.toilet_technology.biogas_pond_biogas.title", "Biogas Pond Fermentation");
        add("jei.toilet_technology.biogas.output_scales_with_liquid", "More feces liquid produces more gas: %s–%s mB / batch");
        add("jei.toilet_technology.biogas.duration", "Duration per batch: %s s");
        add("jei.toilet_technology.biogas.residue_probability", "%s%% chance");
        add("jei.toilet_technology.absorption_tower.title", "Absorption Tower");
        add("jei.toilet_technology.absorption_tower.per_batch", "Conversion per batch");
        add("jei.toilet_technology.absorption_tower.structure", "Structure (%s blocks high)");
        add("jei.toilet_technology.absorption_tower.same_facing", "All blocks face the same direction");
        add("jei.toilet_technology.gas_melting.title", "Gas Melting");
        add("jei.toilet_technology.gas_melting.duration", "Processing time: %s s");
        add("jei.toilet_technology.gas_melting.gas_usage", "Gas consumed: %s mB");
        add(ModBlocks.SEPTIC_TANK_CONTROLLER.get(), "Septic Tank Controller");
        add(ModBlocks.SEPTIC_TANK_WALL.get(), "Septic Tank Wall");
        add(ModBlocks.SEPTIC_TANK_LIQUID_INPUT_PORT.get(), "Septic Tank Liquid Input Port");
        add(ModBlocks.SEPTIC_TANK_GAS_VALVE.get(), "Septic Tank Gas Valve");
        add(ModBlocks.BIOGAS_POND_CONTROLLER.get(), "Biogas Pond Controller");
        add(ModBlocks.BIOGAS_POND_WALL.get(), "Biogas Pond Wall");
        add(ModBlocks.BIOGAS_POND_GAS_VALVE.get(), "Biogas Pond Gas Valve");
        add(ModBlocks.BIOGAS_POND_ITEM_INPUT_PORT.get(), "Biogas Pond Item Input Port");
        add(ModBlocks.BIOGAS_POND_FLUID_INPUT_PORT.get(), "Biogas Pond Fluid Input Port");
        add(ModBlocks.BIOGAS_POND_ITEM_OUTPUT_PORT.get(), "Biogas Pond Item Output Port");
        add(ModBlocks.BIOGAS_GENERATOR.get(), "Biogas Generator");
        add(ModBlocks.GAS_MELTING_FURNACE.get(), "Gas Melting Furnace");
        add(ModBlocks.GAS_PIPE.get(), "Gas Pipe");
        add(ModBlocks.SEWAGE_PURIFIER.get(), "Sewage Purifier");
        add(ModBlocks.ABSORPTION_TOWER_BOTTOM.get(), "Absorption Tower Bottom");
        add(ModBlocks.ABSORPTION_TOWER_BODY.get(), "Absorption Tower Body");
        add("message.toilet_technology.septic_tank_invalid", "The 3×4×3 septic tank structure is incomplete");
        add("message.toilet_technology.biogas_pond_invalid", "The 5×5×5 biogas pond structure is incomplete (the 3×3×3 interior must be hollow)");
        add("gui.toilet_technology.gas", "Gas: %s / %s mB");
        add("gui.toilet_technology.liquid", "Liquid: %s / %s mB");
        add("gui.toilet_technology.absorption_tower", "Absorption Tower");
        add("gui.toilet_technology.pressure", "Pressure");
        add("gui.toilet_technology.tank_amount", "%s / %s mB");
        add("gas.toilet_technology.biogas", "Biogas");
        add("gas.toilet_technology.methane", "Methane");
        add("chemical.toilet_technology.biogas", "Biogas");
        add("tooltip.toilet_technology.gas_tank.empty", "Empty");
        add("tooltip.toilet_technology.gas_tank.amount", "Stores: %s %s / %s mB");
        add("tooltip.toilet_technology.gas_tank.sealed", "Sealed container: cannot be placed in the world");
        add("tooltip.toilet_technology.feces", "You can't eat this...");
        add("tooltip.toilet_technology.dried_feces", "An efficient fuel");
        add("tooltip.toilet_technology.biogas_residue", "Can be used as fertilizer");
        add("tooltip.toilet_technology.multi_layer_sintered_metal_mesh", "A high-strength material");

        add("advancements.toilet_technology.root.title", "Toilet Technology: Resource Cycle");
        add("advancements.toilet_technology.root.description", "Turn waste into a sustainable cycle of processing and reuse.");
        add("advancements.toilet_technology.obtain_feces.title", "A Convenient Beginning");
        add("advancements.toilet_technology.obtain_feces.description", "Collect feces. Crouch on a toilet to produce it over time.");
        add("advancements.toilet_technology.dry_feces.title", "Drying Done Right");
        add("advancements.toilet_technology.dry_feces.description", "Process feces in a drying rack or drying box to make dried feces.");
        add("advancements.toilet_technology.build_septic_tank.title", "Sealed Fermentation");
        add("advancements.toilet_technology.build_septic_tank.description", "Build a 3x4x3 septic tank with a controller, walls, and ports to ferment feces liquid into biogas.");
        add("advancements.toilet_technology.build_biogas_pond.title", "Biogas Engineering");
        add("advancements.toilet_technology.build_biogas_pond.description", "Build a 5x5x5 biogas pond with a hollow 3x3x3 interior for larger-scale processing.");
        add("advancements.toilet_technology.use_biogas_residue.title", "Nothing Goes to Waste");
        add("advancements.toilet_technology.use_biogas_residue.description", "Biogas residue from fermentation can fertilize crops like bone meal.");
        add("advancements.toilet_technology.purify_sewage.title", "Clean Recovery");
        add("advancements.toilet_technology.purify_sewage.description", "Use a sewage purifier to turn feces liquid into water and feces for a reusable cycle.");
        add("advancements.toilet_technology.refine_methane.title", "Source of Blue Flame");
        add("advancements.toilet_technology.refine_methane.description", "Send biogas and water through an absorption tower to separate and produce methane.");




    }
}