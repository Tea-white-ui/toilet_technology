package cn.tea.toilet.technology.bootstrap;

import net.neoforged.bus.api.IEventBus;

/**
 * Composes the mod's registration and lifecycle modules.
 */
public final class ModBootstrap {
    private ModBootstrap() {
    }

    public static void initialize(IEventBus modEventBus) {
        ModRegistries.register(modEventBus);
        ModLifecycleEvents.register(modEventBus);
        ModCapabilities.register(modEventBus);
        ModGameEvents.register();
        OptionalCompatBootstrap.register(modEventBus);
    }
}
