package cn.tea.toilet.technology;

import cn.tea.toilet.technology.item.ModItems;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeTabs {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(
            Registries.CREATIVE_MODE_TAB,
            ToiletTechnology.MOD_ID
    );

    public static final Supplier<CreativeModeTab> TOILET_TAB = CREATIVE_TABS.register("toilet_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.toilet_technology"))
                    .icon(() -> ModItems.FECES.get().getDefaultInstance())
                    .displayItems((parameters, output) -> {
                        // === 材料 ===
                        output.accept(ModItems.FECES.get());
                        // === 方块物品 ===
                        output.accept(ModItems.FECES_BLOCK_ITEM.get());
                        output.accept(ModItems.SQUAT_TOILET_ITEM.get());
                        output.accept(ModItems.OAK_TOILET_ITEM.get());
                        output.accept(ModItems.STONE_TOILET_ITEM.get());
                        output.accept(ModItems.IRON_TOILET_ITEM.get());
                        output.accept(ModItems.GOLD_TOILET_ITEM.get());
                        output.accept(ModItems.DIAMOND_TOILET_ITEM.get());
                        output.accept(ModItems.NETHERITE_TOILET_ITEM.get());
                        output.accept(ModItems.FECES_LIQUID_BUCKET.get());
                    })
                    .build());

    public static void register(IEventBus bus) {
        CREATIVE_TABS.register(bus);
    }
}
