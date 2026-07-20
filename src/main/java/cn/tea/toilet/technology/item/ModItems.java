package cn.tea.toilet.technology.item;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.ModBlocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(
            BuiltInRegistries.ITEM,
            ToiletTechnology.MOD_ID
    );

    // === 材料 ===
        // === 粪便 ===
        public static final DeferredHolder<Item, Item> FECES = ITEMS.register("feces", () -> new Item(
                new Item.Properties().food(new FoodProperties
                        .Builder()
                        .alwaysEdible()
                        .nutrition(1)
                        .saturationModifier(0f)
                        .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 20, 5), 1.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1), 1.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200, 1), 1.0f)
                        .build())));

        public static final DeferredHolder<Item, Item> DRIED_FECES = ITEMS.register("dried_feces",() -> new ModFuelItem(
                new Item.Properties(),200
                )
        );

    // === 方块物品 ===
    public static final DeferredHolder<Item, BlockItem> FECES_BLOCK_ITEM = ITEMS.register("feces_block",
            () -> new BlockItem(ModBlocks.FECES_BLOCK.get(), new Item.Properties()));
    // === 厕所 ===
    public static final DeferredHolder<Item, BlockItem> SQUAT_TOILET_ITEM = ITEMS.register("squat_toilet",
            () -> new BlockItem(ModBlocks.SQUAT_TOILET.get(), new Item.Properties()));




    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
