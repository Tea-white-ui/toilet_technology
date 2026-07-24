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
        basicItem(ModItems.BIOGAS_RESIDUE.get());
        basicItem(ModItems.FECES_LIQUID_BUCKET.get());
        basicItem(ModItems.BIOGAS_TANK.get());
        basicItem(ModItems.EMPTY_BIOGAS_TANK.get());
        // === 方块物品 ===
        getBuilder("feces_block")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/feces_block")));
        getBuilder("dried_feces_block")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/dried_feces_block")));
        getBuilder("antiseptic_brick")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/antiseptic_brick")));
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
        getBuilder("drying_box")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/drying_box")));
        getBuilder("biogas_generator")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/biogas_generator")));
        getBuilder("sewage_purifier")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/sewage_purifier")));
        getBuilder("septic_tank_controller")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/septic_tank_controller")));
        getBuilder("septic_tank_wall")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/septic_tank_wall")));
        getBuilder("biogas_pond_controller")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/biogas_pond_controller")));
        getBuilder("biogas_pond_wall")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/biogas_pond_wall")));
        getBuilder("biogas_pond_gas_valve")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/biogas_pond_gas_valve")));
        getBuilder("biogas_pond_item_input_port")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/biogas_pond_item_input_port")));
        getBuilder("biogas_pond_fluid_input_port")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/biogas_pond_fluid_input_port")));
        getBuilder("biogas_pond_item_output_port")
                .parent(new ModelFile.UncheckedModelFile(modLoc("block/biogas_pond_item_output_port")));

    }
}