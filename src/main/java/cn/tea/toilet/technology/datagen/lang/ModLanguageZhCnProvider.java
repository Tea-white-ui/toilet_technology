package cn.tea.toilet.technology.datagen.lang;
import cn.tea.toilet.technology.ModConstants;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ModLanguageZhCnProvider extends LanguageProvider {
    public ModLanguageZhCnProvider(PackOutput output, String locale) {
        super(output, ModConstants.MOD_ID, locale);
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.toilet_technology", "厕所技艺");
        add(ModItems.FECES.get(), "粪便");
        add(ModItems.FECES_BLOCK_ITEM.get(), "粪便块");
        add(ModItems.DRIED_FECES.get(),"干粪");
        add(ModItems.FECES_BALL.get(), "粪球");
        add(ModItems.DRIED_FECES_BLOCK_ITEM.get(), "干粪块");
        add(ModItems.ANTISEPTIC_BRICK_ITEM.get(), "防腐砖");
        add(ModItems.ANTISEPTIC_BRICK_STAIRS_ITEM.get(), "防腐砖楼梯");
        add(ModItems.ANTISEPTIC_BRICK_SLAB_ITEM.get(), "防腐砖台阶");
        add(ModItems.ANTISEPTIC_BRICK_WALL_ITEM.get(), "防腐砖墙");
        add(ModItems.BIOGAS_RESIDUE.get(), "沼渣");
        add(ModItems.SEALING_COMPONENT.get(), "密封组件");
        add(ModItems.METAL_MESH.get(), "金属网");
        add(ModItems.MULTI_LAYER_SINTERED_METAL_MESH.get(), "多层烧结金属网");
        add(ModItems.SQUAT_TOILET_ITEM.get(),"蹲坑");
        add(ModItems.OAK_TOILET_ITEM.get(), "木制厕所");
        add(ModItems.STONE_TOILET_ITEM.get(), "石制厕所");
        add(ModItems.IRON_TOILET_ITEM.get(), "铁制厕所");
        add(ModItems.GOLD_TOILET_ITEM.get(), "金制厕所");
        add(ModItems.DIAMOND_TOILET_ITEM.get(), "钻石厕所");
        add(ModItems.NETHERITE_TOILET_ITEM.get(), "下届合金厕所");
        add(ModItems.FECES_LIQUID_BUCKET.get(), "粪桶");
        add(ModItems.WASTEWATER_BUCKET.get(), "废水桶");
        add(ModItems.GAS_TANK.get(), "气罐");
        add(ModBlocks.FECES_LIQUID_BLOCK.get(), "粪液");
        add(ModBlocks.WASTEWATER_BLOCK.get(), "废水");
        add(ModBlocks.DRYING_RACK.get(), "干燥架");
        add(ModBlocks.DRYING_BOX.get(), "干燥箱");
        add("fluid_type.toilet_technology.feces_liquid","粪液");
        add("fluid_type.toilet_technology.wastewater", "废水");
        add("jei.toilet_technology.drying.title", "物品干燥");
        add("jei.toilet_technology.biogas.amount", "%s mB");
        add("jei.toilet_technology.septic_tank_biogas.title", "化粪池发酵");
        add("jei.toilet_technology.biogas_pond_biogas.title", "沼气池发酵");
        add("jei.toilet_technology.biogas.output_scales_with_liquid", "粪液越多，产气越多：%s–%s mB / 批");
        add("jei.toilet_technology.biogas.duration", "每批耗时：%s 秒");
        add("jei.toilet_technology.biogas.residue_probability", "%s%% 概率");
        add("jei.toilet_technology.absorption_tower.title", "吸收塔吸收");
        add("jei.toilet_technology.absorption_tower.per_batch", "每批转化");
        add("jei.toilet_technology.absorption_tower.structure", "结构（%s 格高）");
        add("jei.toilet_technology.absorption_tower.same_facing", "所有方块朝向一致");
        add("jei.toilet_technology.gas_melting.title", "燃气熔炼");
        add("jei.toilet_technology.gas_melting.duration", "处理时间：%s 秒");
        add("jei.toilet_technology.gas_melting.gas_usage", "消耗气体：%s mB");
        add(ModBlocks.SEPTIC_TANK_CONTROLLER.get(), "化粪池控制器");
        add(ModBlocks.SEPTIC_TANK_WALL.get(), "化粪池壁");
        add(ModBlocks.SEPTIC_TANK_LIQUID_INPUT_PORT.get(), "化粪池液体输入接口");
        add(ModBlocks.SEPTIC_TANK_GAS_VALVE.get(), "化粪池气阀");
        add(ModBlocks.BIOGAS_POND_CONTROLLER.get(), "沼气池控制器");
        add(ModBlocks.BIOGAS_POND_WALL.get(), "沼气池壁");
        add(ModBlocks.BIOGAS_POND_GAS_VALVE.get(), "沼气池气阀");
        add(ModBlocks.BIOGAS_POND_ITEM_INPUT_PORT.get(), "沼气池物品输入端口");
        add(ModBlocks.BIOGAS_POND_FLUID_INPUT_PORT.get(), "沼气池液体输入端口");
        add(ModBlocks.BIOGAS_POND_ITEM_OUTPUT_PORT.get(), "沼气池物品输出端口");
        add(ModBlocks.BIOGAS_GENERATOR.get(), "沼气发生机");
        add(ModBlocks.GAS_MELTING_FURNACE.get(), "燃气熔炼炉");
        add(ModBlocks.SEWAGE_PURIFIER.get(), "污水净化机");
        add(ModBlocks.ABSORPTION_TOWER_BOTTOM.get(), "吸收塔底");
        add(ModBlocks.ABSORPTION_TOWER_BODY.get(), "吸收塔体");
        add("message.toilet_technology.septic_tank_invalid", "3×4×3 化粪池结构不完整");
        add("message.toilet_technology.biogas_pond_invalid", "5×5×5 沼气池结构不完整（内部必须为 3×3×3 空心）");
        add("gui.toilet_technology.gas", "气体：%s / %s mB");
        add("gui.toilet_technology.liquid", "液体：%s / %s mB");
        add("gui.toilet_technology.absorption_tower", "吸收塔");
        add("gui.toilet_technology.pressure", "压力");
        add("gui.toilet_technology.tank_amount", "%s / %s mB");
        add("gas.toilet_technology.biogas", "沼气");
        add("gas.toilet_technology.methane", "甲烷");
        add("chemical.toilet_technology.biogas", "沼气");
        add("chemical.toilet_technology.methane", "甲烷");
        add("tooltip.toilet_technology.gas_tank.empty", "空");
        add("tooltip.toilet_technology.gas_tank.amount", "储存：%s %s / %s mB");
        add("tooltip.toilet_technology.gas_tank.sealed", "密封容器：无法在世界中放置");
        add("tooltip.toilet_technology.feces", "这东西不能吃...");
        add("tooltip.toilet_technology.dried_feces", "高效的燃料");
        add("tooltip.toilet_technology.biogas_residue", "可当作肥料");
        add("tooltip.toilet_technology.multi_layer_sintered_metal_mesh", "高强度材料");

        add("advancements.toilet_technology.root.title", "厕所技艺：资源循环");
        add("advancements.toilet_technology.root.description", "从排泄物出发，建立可持续的处理与利用体系。");
        add("advancements.toilet_technology.obtain_feces.title", "方便之门");
        add("advancements.toilet_technology.obtain_feces.description", "收集粪便。蹲在厕所上按住潜行可持续产出。");
        add("advancements.toilet_technology.dry_feces.title", "晾晒有方");
        add("advancements.toilet_technology.dry_feces.description", "用干燥架或干燥箱处理粪便，制得干粪。");
        add("advancements.toilet_technology.build_septic_tank.title", "密封发酵");
        add("advancements.toilet_technology.build_septic_tank.description", "用控制器、池壁与接口搭建 3×4×3 化粪池，将粪液发酵为沼气。");
        add("advancements.toilet_technology.build_biogas_pond.title", "沼气工程");
        add("advancements.toilet_technology.build_biogas_pond.description", "搭建 5×5×5 沼气池，保持内部 3×3×3 空心，以更大规模处理有机物。");
        add("advancements.toilet_technology.use_biogas_residue.title", "物尽其用");
        add("advancements.toilet_technology.use_biogas_residue.description", "发酵产生的沼渣可以像骨粉一样催熟作物。");
        add("advancements.toilet_technology.purify_sewage.title", "清洁回收");
        add("advancements.toilet_technology.purify_sewage.description", "污水净化机将粪液处理为水与粪便，形成可再利用的循环。");
        add("advancements.toilet_technology.refine_methane.title", "蓝焰之源");
        add("advancements.toilet_technology.refine_methane.description", "将沼气与水送入吸收塔，分离并制取甲烷。");

    }
}