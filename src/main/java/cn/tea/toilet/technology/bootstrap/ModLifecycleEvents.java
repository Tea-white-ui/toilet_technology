package cn.tea.toilet.technology.bootstrap;

import cn.tea.toilet.technology.network.ModNetworkRegistry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;

/**
 * Registers mod lifecycle listeners that are not tied to a DeferredRegister.
 */
public final class ModLifecycleEvents {
    private static final String NETWORK_PROTOCOL_VERSION = "1.0.0";

    private ModLifecycleEvents() {
    }

    public static void register(IEventBus modEventBus) {
        modEventBus.addListener(ModLifecycleEvents::registerPayloadHandlers);
    }

    private static void registerPayloadHandlers(RegisterPayloadHandlersEvent event) {
        ModNetworkRegistry.register(event.registrar(NETWORK_PROTOCOL_VERSION));
    }
}
