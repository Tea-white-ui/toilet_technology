package cn.tea.toilet.technology.gui;

import cn.tea.toilet.technology.ToiletTechnology;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenuTypes {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(
            BuiltInRegistries.MENU,
            ToiletTechnology.MOD_ID
    );

    public static final DeferredHolder<MenuType<?>, MenuType<DryingBoxMenu>> DRYING_BOX_MENU =
            MENU_TYPES.register("drying_box", () -> new MenuType<>(DryingBoxMenu::new, FeatureFlags.VANILLA_SET));

    public static void register(IEventBus bus) {
        MENU_TYPES.register(bus);
    }
}
