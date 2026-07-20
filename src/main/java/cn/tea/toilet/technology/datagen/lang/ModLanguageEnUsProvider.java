package cn.tea.toilet.technology.datagen.lang;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.ModBlocks;import cn.tea.toilet.technology.datagen.ModItemTagProvider;import cn.tea.toilet.technology.item.ModItems;
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
    }
}
