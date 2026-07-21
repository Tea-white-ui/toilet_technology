package cn.tea.toilet.technology.datagen;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ToiletTechnology.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // === 普通物品 ===
        basicItem(ModItems.FECES.get());
        basicItem(ModItems.DRIED_FECES.get());
        basicItem(ModItems.FECES_LIQUID_BUCKET.get());
        // === 方块物品 ===
        getBuilder("feces_block")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/feces_block")));
        getBuilder("squat_toilet")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/squat_toilet")));
        getBuilder("oak_toilet")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/oak_toilet")));
        getBuilder("stone_toilet")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/stone_toilet")));
        getBuilder("iron_toilet")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/iron_toilet")));
        getBuilder("gold_toilet")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/gold_toilet")));
        getBuilder("diamond_toilet")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/diamond_toilet")));
        getBuilder("netherite_toilet")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/netherite_toilet")));
        getBuilder("drying_rack")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/drying_rack")));

    }
}