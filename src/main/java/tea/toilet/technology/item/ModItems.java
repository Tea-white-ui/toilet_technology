package tea.toilet.technology.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import static tea.toilet.technology.ToiletTechnology.LOGGER;
import tea.toilet.technology.ToiletTechnology;



public class ModItems {


    public static final Item FECES = register("feces", new Item(new Item.Properties()));


    /**
     * 添加物品到物品栏入口
     */
    public static void addItemToIG(FabricItemGroupEntries fabricItemGroupEntries){
        fabricItemGroupEntries.accept(new ItemStack(FECES));
    }


    /**
     * 注册物品入口
     */
    private static Item register(String id, Item item){
        return Registry.register(BuiltInRegistries.ITEM, ResourceKey.create(BuiltInRegistries.ITEM.key(), ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, id)), item);
    }


    public static void registerModItems(){
        LOGGER.info("toilet_technology Items registered");
    }

}
