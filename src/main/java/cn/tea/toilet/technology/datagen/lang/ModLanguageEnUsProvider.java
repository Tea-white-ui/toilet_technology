package cn.tea.toilet.technology.datagen.lang;
import cn.tea.toilet.technology.ModConstants;

import cn.tea.toilet.technology.block.ModBlocks;import cn.tea.toilet.technology.datagen.ModItemTagProvider;
import cn.tea.toilet.technology.fluid.ModFluids;
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
        add(ModItems.DRIED_FECES_BLOCK_ITEM.get(), "Dried feces block");
        add(ModItems.ANTISEPTIC_BRICK_ITEM.get(), "Antiseptic Brick");
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
        add(ModItems.BIOGAS_TANK.get(), "Biogas Tank");
        add(ModItems.EMPTY_BIOGAS_TANK.get(), "Empty Biogas Tank");
        add(ModBlocks.FECES_LIQUID_BLOCK.get(), "Feces Liquid");
        add(ModBlocks.DRYING_RACK.get(), "Dryer_Rack");
        add(ModBlocks.DRYING_BOX.get(), "Drying Box");
        add("fluid_type.toilet_technology.feces_liquid","Feces Liquid");
        add("jei.toilet_technology.biogas.amount", "%s mB");
        add("jei.toilet_technology.septic_tank_biogas.title", "Septic Tank Fermentation");
        add("jei.toilet_technology.biogas_pond_biogas.title", "Biogas Pond Fermentation");
        add("jei.toilet_technology.biogas.output_scales_with_liquid", "More feces liquid produces more gas: %s–%s mB / batch");
        add("jei.toilet_technology.biogas.duration", "Duration per batch: %s s");
        add("jei.toilet_technology.biogas.generator_energy", "Biogas generator: %s FE / batch (output ×2)");
        add("jei.toilet_technology.biogas.residue_probability", "%s%% chance");
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
        add(ModBlocks.SEWAGE_PURIFIER.get(), "Sewage Purifier");
        add(ModBlocks.ABSORPTION_TOWER_BOTTOM.get(), "Absorption Tower Bottom");
        add(ModBlocks.ABSORPTION_TOWER_BODY.get(), "Absorption Tower Body");
        add("message.toilet_technology.septic_tank_invalid", "The 3×4×3 septic tank structure is incomplete");
        add("message.toilet_technology.biogas_pond_invalid", "The 5×5×5 biogas pond structure is incomplete (the 3×3×3 interior must be hollow)");
        add("gui.toilet_technology.gas", "Gas: %s / %s mB");
        add("gui.toilet_technology.liquid", "Liquid: %s / %s mB");
        add("gui.toilet_technology.pressure", "Pressure");
        add("gui.toilet_technology.tank_amount", "%s / %s mB");
        add("gas.toilet_technology.biogas", "Biogas");
        add("chemical.toilet_technology.biogas", "Biogas");
        add("tooltip.toilet_technology.biogas_tank.amount", "Stores: %s / %s mB of biogas");
        add("tooltip.toilet_technology.biogas_tank.sealed", "Sealed container: cannot be placed in the world");




    }
}