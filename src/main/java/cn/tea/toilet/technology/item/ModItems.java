package cn.tea.toilet.technology.item;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.fluid.ModFluids;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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
                        .saturationModifier(0.5f)
                        .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 20, 5), 1.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 80, 1), 1.0f)
                        .effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 200, 1), 1.0f)
                        .build())));

        public static final DeferredHolder<Item, Item> DRIED_FECES = ITEMS.register("dried_feces",() -> new Item(
 new Item.Properties()
 )
 );

    // === 方块物品 ===
    public static final DeferredHolder<Item, BlockItem> FECES_BLOCK_ITEM = ITEMS.register("feces_block",
            () -> new BlockItem(ModBlocks.FECES_BLOCK.get(), new Item.Properties()));
    // === 厕所 ===
    public static final DeferredHolder<Item, BlockItem> SQUAT_TOILET_ITEM = ITEMS.register("squat_toilet",
            () -> new BlockItem(ModBlocks.SQUAT_TOILET.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> OAK_TOILET_ITEM = ITEMS.register("oak_toilet",
            () -> new BlockItem(ModBlocks.OAK_TOILET.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> STONE_TOILET_ITEM = ITEMS.register("stone_toilet",
            () -> new BlockItem(ModBlocks.STONE_TOILET.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> IRON_TOILET_ITEM = ITEMS.register("iron_toilet",
            () -> new BlockItem(ModBlocks.IRON_TOILET.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> GOLD_TOILET_ITEM = ITEMS.register("gold_toilet",
            () -> new BlockItem(ModBlocks.GOLD_TOILET.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> DIAMOND_TOILET_ITEM = ITEMS.register("diamond_toilet",
            () -> new BlockItem(ModBlocks.DIAMOND_TOILET.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> NETHERITE_TOILET_ITEM = ITEMS.register("netherite_toilet",
            () -> new BlockItem(ModBlocks.NETHERITE_TOILET.get(), new Item.Properties().fireResistant()));
    // === 干燥台/箱 ===
    public static final DeferredHolder<Item, BlockItem> DRYING_RACK_ITEM = ITEMS.register("drying_rack",
            () -> new BlockItem(ModBlocks.DRYING_RACK.get(), new Item.Properties()));
    public static final DeferredHolder<Item, BlockItem> DRYING_BOX_ITEM = ITEMS.register("drying_box",
            () -> new BlockItem(ModBlocks.DRYING_BOX.get(), new Item.Properties()));

    // === 桶 ===
    public static final DeferredHolder<Item, Item> FECES_LIQUID_BUCKET = ITEMS.register("feces_liquid_bucket",
            () -> new BucketItem(ModFluids.FECES_LIQUID.get(), new Item.Properties()
                    .craftRemainder(Items.BUCKET)
                    .stacksTo(1)
            ));




    public static void register(IEventBus bus){
        ITEMS.register(bus);
    }
}
