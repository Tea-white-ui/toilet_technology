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
        // === 方块物品 ===
        getBuilder("feces_block")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/feces_block")));
    }
}