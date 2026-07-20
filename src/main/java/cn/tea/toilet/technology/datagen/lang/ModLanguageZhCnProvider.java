package cn.tea.toilet.technology.datagen.lang;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageZhCnProvider extends LanguageProvider {
    public ModLanguageZhCnProvider(PackOutput output, String locale) {
        super(output, ToiletTechnology.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.toilet_technology", "厕所技艺");
        // add(ModBlocks.EXAMPLE_BLOCK.get(), "Example Block");
        add(ModItems.FECES.get(), "粪便");
        add(ModItems.FECES_BLOCK_ITEM.get(), "粪便块");
        add(ModItems.DRIED_FECES.get(),"干粪");
        add(ModItems.SQUAT_TOILET_ITEM.get(),"蹲坑");
        add(ModItems.OAK_TOILET_ITEM.get(), "木制厕所");
        add(ModItems.FECES_LIQUID_BUCKET.get(), "粪桶");
        add(ModBlocks.FECES_LIQUID_BLOCK.get(), "粪液");

    }
}
