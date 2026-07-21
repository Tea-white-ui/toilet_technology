package cn.tea.toilet.technology.datagen;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.ModBlocks;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, ToiletTechnology.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        // === 简单方块 ===
        simpleBlock(ModBlocks.FECES_BLOCK.get(), models().cubeAll("feces_block", modLoc("block/feces_block")));
        // === 自定义模型 ===
        //simpleBlock(ModBlocks.YOUR_BLOCK.get(), models().cubeAll("your_block", modLoc("block/your_block")));
        horizontalBlock(ModBlocks.SQUAT_TOILET.get(), models().getExistingFile(modLoc("block/squat_toilet")));
        horizontalBlock(ModBlocks.OAK_TOILET.get(), models().getExistingFile(modLoc("block/oak_toilet")));
        horizontalBlock(ModBlocks.STONE_TOILET.get(), models().getExistingFile(modLoc("block/stone_toilet")));
        horizontalBlock(ModBlocks.IRON_TOILET.get(), models().getExistingFile(modLoc("block/iron_toilet")));
        horizontalBlock(ModBlocks.GOLD_TOILET.get(), models().getExistingFile(modLoc("block/gold_toilet")));
        horizontalBlock(ModBlocks.DIAMOND_TOILET.get(), models().getExistingFile(modLoc("block/diamond_toilet")));
        horizontalBlock(ModBlocks.NETHERITE_TOILET.get(), models().getExistingFile(modLoc("block/netherite_toilet")));
        horizontalBlock(ModBlocks.DRYING_RACK.get(), models().getExistingFile(modLoc("block/drying_rack")));

    }
}