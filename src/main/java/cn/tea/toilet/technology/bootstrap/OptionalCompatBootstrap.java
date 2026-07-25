package cn.tea.toilet.technology.bootstrap;
import cn.tea.toilet.technology.ModConstants;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;

/**
 * Loads optional compatibility modules without linking their optional APIs into core startup.
 */
public final class OptionalCompatBootstrap {
    private static final String MEKANISM_MOD_ID = "mekanism";
    private static final String MEKANISM_COMPAT_CLASS =
            "cn.tea.toilet.technology.compat.mekanism.MekanismCompat";

    private OptionalCompatBootstrap() {
    }

    public static void register(IEventBus modEventBus) {
        if (!ModList.get().isLoaded(MEKANISM_MOD_ID)) {
            return;
        }

        try {
            Class<?> compat = Class.forName(MEKANISM_COMPAT_CLASS);
            compat.getMethod("register", IEventBus.class).invoke(null, modEventBus);
        } catch (ReflectiveOperationException exception) {
            ModConstants.LOGGER.error("Failed to initialize optional Mekanism compatibility", exception);
        }
    }
}
