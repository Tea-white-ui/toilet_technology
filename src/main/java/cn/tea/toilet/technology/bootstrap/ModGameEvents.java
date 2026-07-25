package cn.tea.toilet.technology.bootstrap;

import cn.tea.toilet.technology.event.PlayerToiletHandler;
import net.neoforged.neoforge.common.NeoForge;

/**
 * Registers gameplay event listeners on the NeoForge event bus.
 */
public final class ModGameEvents {
    private ModGameEvents() {
    }

    public static void register() {
        NeoForge.EVENT_BUS.register(new PlayerToiletHandler());
    }
}
