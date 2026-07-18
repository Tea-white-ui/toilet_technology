package tea.toilet.technology.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroupEntries;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import static net.minecraft.world.item.Items.registerItem;
import static tea.toilet.technology.ToiletTechnology.LOGGER;

import net.minecraft.world.level.block.Block;
import tea.toilet.technology.ToiletTechnology;
import tea.toilet.technology.block.ModBlocks;


public class ModItems {

    // === 物品 ===
    public static final Item FECES = register("feces", new Item(new Item.Properties()));


    // === 方块物品 ===
    public static final Item FECES_BLOCK = registerBlockItem(ModBlocks.FECES_BLOCK, new BlockItem(ModBlocks.FECES_BLOCK, new Item.Properties()));


    /**
     * 方块物品注册入口
     */
    public static Item registerBlockItem(Block block, Item item) {
        return registerItem(BuiltInRegistries.BLOCK.getKey(block), item);
    }

    /**
     * 添加物品到物品栏入口
     */
    public static void addItemToIG(FabricItemGroupEntries fabricItemGroupEntries){
        fabricItemGroupEntries.accept(new ItemStack(FECES));
        fabricItemGroupEntries.accept(new ItemStack(FECES_BLOCK));
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
