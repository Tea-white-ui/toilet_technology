package tea.toilet.technology;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import tea.toilet.technology.item.ModItems;

public class ModFuels {
    public static void registerFuels() {
        FuelRegistry.INSTANCE.add(ModItems.DRIED_FECES, 100);// 100tick
    }
}
