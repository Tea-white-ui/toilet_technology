package tea.toilet.technology.item;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import tea.toilet.technology.ToiletTechnology;
import static tea.toilet.technology.ToiletTechnology.LOGGER;

public class ModFoods {


    public static void addToItemGroup(FabricItemGroupEntries entries) {
    }

    private static Item register(String id, Item item) {
        return Registry.register(BuiltInRegistries.ITEM,
                ResourceKey.create(BuiltInRegistries.ITEM.key(),
                        ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, id)), item);
    }

    public static void registerModFoods() {
        LOGGER.info("toilet_technology Foods registered");
    }
}