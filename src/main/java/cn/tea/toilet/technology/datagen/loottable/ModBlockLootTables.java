package cn.tea.toilet.technology.datagen.loottable;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.NotNull;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    protected ModBlockLootTables(HolderLookup.Provider provider) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), provider);
    }

    @Override
    protected void generate() {
        // 1. 掉落自身
        //this.dropSelf(ModBlocks.FECES_BLOCK.get());

        // 2. 掉落指定物品（如掉2个粪便）
        //this.add(ModBlocks.FECES_BLOCK.get(), block ->
        //        this.createSingleItemTable(ModItems.FECES.get(), 2));

        // 3. 随机数量掉落（1~3个）
        this.add(ModBlocks.FECES_BLOCK.get(), block ->
               this.createSingleItemTable(ModItems.FECES.get())
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(3, 4))));

        // 4. 带概率掉落（50%掉落）
        //this.add(ModBlocks.FECES_BLOCK.get(), block ->
        //        LootTable.lootTable().withPool(this.applyExplosionCondition(block,
        //                LootPool.lootPool().add(LootItem.lootTableItem(ModItems.FECES.get()))
        //                        .when(LootItemRandomChanceCondition.randomChance(0.5f)))));

        // 5. 时运加成掉落
        //this.add(ModBlocks.FECES_BLOCK.get(), block ->
        //        this.createOreDrop(ModBlocks.FECES_BLOCK.get(), ModItems.FECES.get()));


        this.dropSelf(ModBlocks.SQUAT_TOILET.get());
        this.dropSelf(ModBlocks.OAK_TOILET.get());
        this.dropSelf(ModBlocks.STONE_TOILET.get());
        this.dropSelf(ModBlocks.IRON_TOILET.get());
        this.dropSelf(ModBlocks.GOLD_TOILET.get());
        this.dropSelf(ModBlocks.DIAMOND_TOILET.get());
        this.dropSelf(ModBlocks.NETHERITE_TOILET.get());
        this.dropSelf(ModBlocks.DRYING_RACK.get());
        this.dropSelf(ModBlocks.DRYING_BOX.get());
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream()
                .map(entry -> (Block) entry.value())
                .toList();
    }
}