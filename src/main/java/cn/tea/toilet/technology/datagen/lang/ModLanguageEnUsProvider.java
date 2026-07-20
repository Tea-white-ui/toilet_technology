package cn.tea.toilet.technology.datagen.lang;

import cn.tea.toilet.technology.ToiletTechnology;
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
        // add(ModBlocks.EXAMPLE_BLOCK.get(), "Example Block");
        add(ModItems.FECES.get(), "feces");
        add(ModItems.FECES_BLOCK_ITEM.get(), "feces block");
    }
}
