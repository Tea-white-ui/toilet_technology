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
        add(ModItems.FECES.get(), "粪便");
        add(ModItems.FECES_BLOCK_ITEM.get(), "粪便块");
        add(ModItems.DRIED_FECES.get(),"干粪");
        add(ModItems.SQUAT_TOILET_ITEM.get(),"蹲坑");
        add(ModItems.OAK_TOILET_ITEM.get(), "木制厕所");
        add(ModItems.STONE_TOILET_ITEM.get(), "石制厕所");
        add(ModItems.IRON_TOILET_ITEM.get(), "铁制厕所");
        add(ModItems.GOLD_TOILET_ITEM.get(), "金制厕所");
        add(ModItems.DIAMOND_TOILET_ITEM.get(), "钻石厕所");
        add(ModItems.NETHERITE_TOILET_ITEM.get(), "下届合金厕所");
        add(ModItems.FECES_LIQUID_BUCKET.get(), "粪桶");
        add(ModItems.BIOGAS_BUCKET.get(), "沼气桶");
        add(ModBlocks.FECES_LIQUID_BLOCK.get(), "粪液");
        add(ModBlocks.DRYING_RACK.get(), "干燥架");
        add(ModBlocks.DRYING_BOX.get(), "干燥箱");
        add("fluid_type.toilet_technology.feces_liquid","粪液");
        add("jei.toilet_technology.drying.title", "物品干燥");
        add(ModBlocks.SEPTIC_TANK_CONTROLLER.get(), "化粪池控制器");
        add(ModBlocks.SEPTIC_TANK_WALL.get(), "化粪池壁");
        add("message.toilet_technology.septic_tank_invalid", "3×4×3 化粪池结构不完整");
        add("gui.toilet_technology.gas", "气体：%s / %s mB");
        add("gui.toilet_technology.liquid", "液体：%s / %s mB");
        add("gui.toilet_technology.tank_amount", "%s / %s mB");
        add("chemical.toilet_technology.biogas", "沼气");
        add("tooltip.toilet_technology.biogas_bucket.amount", "储存：%s / %s mB 沼气");
        add("tooltip.toilet_technology.biogas_bucket.sealed", "密封容器：无法在世界中放置");

    }
}