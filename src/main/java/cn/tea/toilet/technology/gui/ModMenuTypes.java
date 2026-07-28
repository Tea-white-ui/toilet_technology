package cn.tea.toilet.technology.gui;
import cn.tea.toilet.technology.ModConstants;

import cn.tea.toilet.technology.gui.dryingbox.DryingBoxMenu;
import cn.tea.toilet.technology.gui.septictank.SepticTankMenu;
import cn.tea.toilet.technology.gui.biogaspond.BiogasPondMenu;
import cn.tea.toilet.technology.gui.absorptiontower.AbsorptionTowerMenu;
import cn.tea.toilet.technology.gui.gasmeltingfurnace.GasMeltingFurnaceMenu;
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
            ModConstants.MOD_ID
    );

    /** 干燥箱菜单类型：使用 vanilla 特性标志集 */
    public static final DeferredHolder<MenuType<?>, MenuType<DryingBoxMenu>> DRYING_BOX_MENU =
            MENU_TYPES.register("drying_box", () -> new MenuType<>(DryingBoxMenu::new, FeatureFlags.VANILLA_SET));
    public static final DeferredHolder<MenuType<?>, MenuType<SepticTankMenu>> SEPTIC_TANK_MENU =
            MENU_TYPES.register("septic_tank", () -> new MenuType<>(SepticTankMenu::new, FeatureFlags.VANILLA_SET));
    public static final DeferredHolder<MenuType<?>, MenuType<BiogasPondMenu>> BIOGAS_POND_MENU =
            MENU_TYPES.register("biogas_pond", () -> new MenuType<>(BiogasPondMenu::new, FeatureFlags.VANILLA_SET));
    public static final DeferredHolder<MenuType<?>, MenuType<AbsorptionTowerMenu>> ABSORPTION_TOWER_MENU =
            MENU_TYPES.register("absorption_tower", () -> new MenuType<>(AbsorptionTowerMenu::new, FeatureFlags.VANILLA_SET));
    public static final DeferredHolder<MenuType<?>, MenuType<GasMeltingFurnaceMenu>> GAS_MELTING_FURNACE_MENU =
            MENU_TYPES.register("gas_melting_furnace", () -> new MenuType<>(GasMeltingFurnaceMenu::new, FeatureFlags.VANILLA_SET));

    /**
     * 注册所有菜单类型到事件总线
     * 
     * @param bus NeoForge 事件总线
     */
    public static void register(IEventBus bus) {
        MENU_TYPES.register(bus);
    }
}