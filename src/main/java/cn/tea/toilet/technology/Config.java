package cn.tea.toilet.technology;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

/**
 * 模组配置类
 * 使用 NeoForge 的 ModConfigSpec 系统定义可配置参数
 * 配置文件类型：COMMON（服务端和客户端共享）
 * 
 * 配置项说明：
 * - logDirtBlock: 是否在初始化时记录泥土方块信息（调试用）
 * - magicNumber: 示例魔法数字配置
 * - magicNumberIntroduction: 魔法数字的前缀介绍文本
 * - items: 需要在初始化时记录日志的物品列表
 */
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    /** 是否在通用设置阶段记录泥土方块信息到日志 */
    public static final ModConfigSpec.BooleanValue LOG_DIRT_BLOCK = BUILDER
            .comment("Whether to log the dirt block on common setup")
            .define("logDirtBlock", true);

    /** 示例配置项：一个魔法数字（范围：0 到 Integer.MAX_VALUE） */
    public static final ModConfigSpec.IntValue MAGIC_NUMBER = BUILDER
            .comment("A magic number")
            .defineInRange("magicNumber", 42, 0, Integer.MAX_VALUE);

    /** 魔法数字介绍文本的前缀 */
    public static final ModConfigSpec.ConfigValue<String> MAGIC_NUMBER_INTRODUCTION = BUILDER
            .comment("What you want the introduction message to be for the magic number")
            .define("magicNumberIntroduction", "The magic number is... ");

    /** 物品资源位置列表，在初始化时记录到日志 */
    public static final ModConfigSpec.ConfigValue<List<? extends String>> ITEM_STRINGS = BUILDER
            .comment("A list of items to log on common setup.")
            .defineListAllowEmpty("items", List.of("minecraft:iron_ingot"), () -> "", Config::validateItemName);

    /** 配置规范对象，用于注册到模组容器 */
    static final ModConfigSpec SPEC = BUILDER.build();

    /**
     * 验证物品名称是否为有效的资源位置
     * 
     * @param obj 待验证的对象
     * @return 如果是有效的物品资源位置则返回 true
     */
    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }
}