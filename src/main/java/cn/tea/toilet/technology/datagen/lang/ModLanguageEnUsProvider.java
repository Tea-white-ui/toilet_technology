package cn.tea.toilet.technology.datagen.lang;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.ModBlocks;import cn.tea.toilet.technology.datagen.ModItemTagProvider;
import cn.tea.toilet.technology.fluid.ModFluids;
import cn.tea.toilet.technology.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageEnUsProvider extends LanguageProvider {
    public ModLanguageEnUsProvider(PackOutput output, String locale) {
        super(output, ToiletTechnology.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.toilet_technology", "toilet technology");
        add(ModItems.FECES.get(), "Feces");
        add(ModItems.FECES_BLOCK_ITEM.get(), "Feces block");
        add(ModItems.DRIED_FECES.get(),"Dried feces");
        add(ModItems.SQUAT_TOILET_ITEM.get(),"Squat Toilet");
        add(ModItems.OAK_TOILET_ITEM.get(), "Oak Toilet");
        add(ModItems.STONE_TOILET_ITEM.get(),"Stone Toilet");
        add(ModItems.IRON_TOILET_ITEM.get(),"Iron Toilet");
        add(ModItems.GOLD_TOILET_ITEM.get(),"Gold Toilet");
        add(ModItems.DIAMOND_TOILET_ITEM.get(),"Diamond Toilet");
        add(ModItems.NETHERITE_TOILET_ITEM.get(),"Netherite Toilet");
        add(ModItems.FECES_LIQUID_BUCKET.get(), "Feces Liquid Bucket");
        add(ModItems.BIOGAS_BUCKET.get(), "Biogas Bucket");
        add(ModBlocks.FECES_LIQUID_BLOCK.get(), "Feces Liquid");
        add(ModBlocks.DRYING_RACK.get(), "Dryer_Rack");
        add(ModBlocks.DRYING_BOX.get(), "Drying Box");
        add("fluid_type.toilet_technology.feces_liquid","Feces Liquid");
        add(ModBlocks.SEPTIC_TANK_CONTROLLER.get(), "Septic Tank Controller");
        add(ModBlocks.SEPTIC_TANK_WALL.get(), "Septic Tank Wall");
        add(ModBlocks.BIOGAS_GENERATOR.get(), "Biogas Generator");
        add("message.toilet_technology.septic_tank_invalid", "The 3×4×3 septic tank structure is incomplete");
        add("gui.toilet_technology.gas", "Gas: %s / %s mB");
        add("gui.toilet_technology.liquid", "Liquid: %s / %s mB");
        add("gui.toilet_technology.tank_amount", "%s / %s mB");
        add("chemical.toilet_technology.biogas", "Biogas");
        add("tooltip.toilet_technology.biogas_bucket.amount", "Stores: %s / %s mB of biogas");
        add("tooltip.toilet_technology.biogas_bucket.sealed", "Sealed container: cannot be placed in the world");




    }
}