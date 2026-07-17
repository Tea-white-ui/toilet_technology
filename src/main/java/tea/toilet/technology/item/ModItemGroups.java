package tea.toilet.technology.item;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import tea.toilet.technology.ToiletTechnology;
import static tea.toilet.technology.ToiletTechnology.LOGGER;


public class ModItemGroups {


    // 创建一个厕所工艺物品栏
    public static final ResourceKey<CreativeModeTab> TOILET_TECHNOLOGY_GROUP_KEY = ResourceKey.create(BuiltInRegistries.CREATIVE_MODE_TAB.key(), ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, ToiletTechnology.MOD_ID));
    public static final CreativeModeTab TOILET_TECHNOLOGY_GROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack(ModItems.FECES))// 标签页图标物品
            .title(Component.translatable("itemGroup.toilet_technology"))
            .build();

    /**
     * 创建物品标签页
     */
    private static ResourceKey<CreativeModeTab> createKey(String string) {
        return ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.withDefaultNamespace(string));
    }

    /**
     * 注册物品标签页
     */
    public static void registerModItemsGroups(){
        LOGGER.info("toilet_technology Items Group registered");
        // 注册物品栏
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, TOILET_TECHNOLOGY_GROUP_KEY, TOILET_TECHNOLOGY_GROUP);
        // 将物品添加到厕所工艺标签页里
        ItemGroupEvents.modifyEntriesEvent(TOILET_TECHNOLOGY_GROUP_KEY)
                .register(ModItems::addItemToIG);
    }

}
