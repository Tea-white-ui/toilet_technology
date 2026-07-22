package cn.tea.toilet.technology.gui;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.gui.dryingbox.DryingBoxMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

/**
 * 菜单类型注册类
 * 负责注册模组中的所有GUI菜单类型
 * 目前仅包含干燥箱菜单
 */
public class ModMenuTypes {

    /** 菜单类型延迟注册表 */
    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(
            BuiltInRegistries.MENU,
            ToiletTechnology.MOD_ID
    );

    /** 干燥箱菜单类型：使用 vanilla 特性标志集 */
    public static final DeferredHolder<MenuType<?>, MenuType<DryingBoxMenu>> DRYING_BOX_MENU =
            MENU_TYPES.register("drying_box", () -> new MenuType<>(DryingBoxMenu::new, FeatureFlags.VANILLA_SET));

    /**
     * 注册所有菜单类型到事件总线
     * 
     * @param bus NeoForge 事件总线
     */
    public static void register(IEventBus bus) {
        MENU_TYPES.register(bus);
    }
}