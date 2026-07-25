package cn.tea.toilet.technology.datagen;
import cn.tea.toilet.technology.ModConstants;

import cn.tea.toilet.technology.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ModConstants.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // === 简单方块 ===
        simpleBlock(ModBlocks.FECES_BLOCK.get(), models().cubeAll("feces_block", modLoc("block/feces_block")));
        simpleBlock(ModBlocks.DRIED_FECES_BLOCK.get(), models().cubeAll("dried_feces_block", modLoc("block/dried_feces_block")));
        simpleBlock(ModBlocks.ANTISEPTIC_BRICK.get(), models().cubeAll("antiseptic_brick", modLoc("block/antiseptic_brick")));
        simpleBlock(ModBlocks.BIOGAS_POND_WALL.get(), models().cubeAll("biogas_pond_wall", modLoc("block/biogas_pond_wall")));
        simpleBlock(ModBlocks.BIOGAS_POND_CONTROLLER.get(), models().cube(
                "biogas_pond_controller",
                modLoc("block/biogas_pond_controller"), modLoc("block/biogas_pond_controller_top"),
                modLoc("block/biogas_pond_controller"), modLoc("block/biogas_pond_controller"),
                modLoc("block/biogas_pond_controller"), modLoc("block/biogas_pond_controller"))
                .texture("particle", modLoc("block/biogas_pond_controller")));
        horizontalBlock(ModBlocks.BIOGAS_POND_GAS_VALVE.get(), models().cube(
                "biogas_pond_gas_valve",
                modLoc("block/biogas_pond_wall"), modLoc("block/biogas_pond_gas_valve_front"),
                modLoc("block/biogas_pond_wall"), modLoc("block/biogas_pond_wall"),
                modLoc("block/biogas_pond_wall"), modLoc("block/biogas_pond_wall"))
                .texture("particle", modLoc("block/biogas_pond_wall")));
        biogasPondPort(ModBlocks.BIOGAS_POND_ITEM_INPUT_PORT.get(), "biogas_pond_item_input_port");
        biogasPondPort(ModBlocks.BIOGAS_POND_FLUID_INPUT_PORT.get(), "biogas_pond_fluid_input_port");
        biogasPondPort(ModBlocks.BIOGAS_POND_ITEM_OUTPUT_PORT.get(), "biogas_pond_item_output_port");
        septicTankPort(ModBlocks.SEPTIC_TANK_LIQUID_INPUT_PORT.get(), "septic_tank_liquid_input_port", false);
        septicTankPort(ModBlocks.SEPTIC_TANK_GAS_VALVE.get(), "septic_tank_gas_valve", true);
        // === 自定义模型 ===
        horizontalBlock(ModBlocks.SQUAT_TOILET.get(), models().getExistingFile(modLoc("block/squat_toilet")));
        horizontalBlock(ModBlocks.OAK_TOILET.get(), models().getExistingFile(modLoc("block/oak_toilet")));
        horizontalBlock(ModBlocks.STONE_TOILET.get(), models().getExistingFile(modLoc("block/stone_toilet")));
        horizontalBlock(ModBlocks.IRON_TOILET.get(), models().getExistingFile(modLoc("block/iron_toilet")));
        horizontalBlock(ModBlocks.GOLD_TOILET.get(), models().getExistingFile(modLoc("block/gold_toilet")));
        horizontalBlock(ModBlocks.DIAMOND_TOILET.get(), models().getExistingFile(modLoc("block/diamond_toilet")));
        horizontalBlock(ModBlocks.NETHERITE_TOILET.get(), models().getExistingFile(modLoc("block/netherite_toilet")));
        horizontalBlock(ModBlocks.DRYING_RACK.get(), models().getExistingFile(modLoc("block/drying_rack")));
        horizontalBlock(ModBlocks.DRYING_BOX.get(), models().getExistingFile(modLoc("block/drying_box")));
        horizontalBlock(ModBlocks.BIOGAS_GENERATOR.get(), models().getExistingFile(modLoc("block/biogas_generator")));
        horizontalBlock(ModBlocks.SEWAGE_PURIFIER.get(), models().getExistingFile(modLoc("block/sewage_purifier")));
        horizontalBlock(ModBlocks.ABSORPTION_TOWER_BOTTOM.get(), models().getExistingFile(modLoc("block/absorption_tower_bottom")));
        horizontalBlock(ModBlocks.ABSORPTION_TOWER_BODY.get(), models().getExistingFile(modLoc("block/absorption_tower_body")));

    }

    private void septicTankPort(net.minecraft.world.level.block.Block block, String name, boolean topFunctionFace) {
        net.minecraft.resources.ResourceLocation side = modLoc("block/" + name);
        net.minecraft.resources.ResourceLocation functionFace = modLoc("block/" + name + "_front");
        ModelFile model = models().cube(name,
                side, topFunctionFace ? functionFace : side,
                topFunctionFace ? side : functionFace, side, side, side)
                .texture("particle", side);
        horizontalBlock(block, model);
    }

    private void biogasPondPort(net.minecraft.world.level.block.Block block, String name) {
        ModelFile model = models().cube(name,
                modLoc("block/biogas_pond_wall"), modLoc("block/biogas_pond_wall"),
                modLoc("block/" + name + "_front"), modLoc("block/biogas_pond_wall"),
                modLoc("block/biogas_pond_wall"), modLoc("block/biogas_pond_wall"))
                .texture("particle", modLoc("block/biogas_pond_wall"));
        horizontalBlock(block, model);
    }
}