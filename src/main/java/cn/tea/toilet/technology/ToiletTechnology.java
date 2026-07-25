package cn.tea.toilet.technology;

import cn.tea.toilet.technology.bootstrap.ModBootstrap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

/**
 * Mod entry point. Startup wiring lives in {@link ModBootstrap}.
 */
@Mod(ModConstants.MOD_ID)
public final class ToiletTechnology {
    public ToiletTechnology(IEventBus modEventBus, ModContainer modContainer) {
        ModBootstrap.initialize(modEventBus);
    }
}
