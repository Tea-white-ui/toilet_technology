package cn.tea.toilet.technology.datagen;
import cn.tea.toilet.technology.ModConstants;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, lookupProvider);
    }

    @Override
    protected void buildRecipes(@NotNull RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.SEPTIC_TANK_CONTROLLER.get())
                .pattern("IOI")
                .pattern("RBR")
                .pattern("CSC")
                .define('I', Items.IRON_INGOT)
                .define('O', Items.OBSERVER)
                .define('R', Items.REDSTONE)
                .define('B', ModBlocks.SEPTIC_TANK_WALL.get())
                .define('C', Items.COPPER_INGOT)
                .define('S', Items.SLIME_BALL)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.BIOGAS_POND_CONTROLLER.get())
                .pattern("IOI")
                .pattern("RBR")
                .pattern("CEC")
                .define('I', Items.IRON_INGOT)
                .define('O', Items.OBSERVER)
                .define('R', Items.REDSTONE)
                .define('B', ModBlocks.BIOGAS_POND_WALL.get())
                .define('C', Items.COPPER_INGOT)
                .define('E', Items.ENDER_PEARL)
                .unlockedBy(getHasName(ModBlocks.BIOGAS_POND_WALL.get()), has(ModBlocks.BIOGAS_POND_WALL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.SEALING_COMPONENT.get(), 4)
                .pattern("SCS")
                .pattern("CRC")
                .pattern("SCS")
                .define('S', Items.SLIME_BALL)
                .define('C', Items.COPPER_INGOT)
                .define('R', Items.REDSTONE)
                .unlockedBy(getHasName(Items.SLIME_BALL), has(Items.SLIME_BALL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.METAL_MESH.get(), 4)
                .pattern("I I")
                .pattern(" I ")
                .pattern("I I")
                .define('I', Items.IRON_INGOT)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.GAS_TANK.get(), 4)
                .pattern("I I")
                .pattern("PPP")
                .pattern("PPP")
                .define('I', Items.IRON_INGOT)
                .define('P', Items.GLASS_PANE)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output);

        SimpleCookingRecipeBuilder.smelting(
                        Ingredient.of(ModItems.METAL_MESH.get()),
                        RecipeCategory.MISC,
                        ModItems.MULTI_LAYER_SINTERED_METAL_MESH.get(),
                        0.1F,
                        200
                )
                .unlockedBy(getHasName(ModItems.METAL_MESH.get()), has(ModItems.METAL_MESH.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(
                        ModConstants.MOD_ID, "multi_layer_sintered_metal_mesh_from_smelting"));

        SimpleCookingRecipeBuilder.blasting(
                        Ingredient.of(ModItems.METAL_MESH.get()),
                        RecipeCategory.MISC,
                        ModItems.MULTI_LAYER_SINTERED_METAL_MESH.get(),
                        0.1F,
                        100
                )
                .unlockedBy(getHasName(ModItems.METAL_MESH.get()), has(ModItems.METAL_MESH.get()))
                .save(output, ResourceLocation.fromNamespaceAndPath(
                        ModConstants.MOD_ID, "multi_layer_sintered_metal_mesh_from_blasting"));

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ANTISEPTIC_BRICK.get(), 4)
                .pattern("CNC")
                .pattern("NCN")
                .pattern("CNC")
                .define('C', Items.CLAY_BALL)
                .define('N', Items.IRON_NUGGET)
                .unlockedBy(getHasName(Items.CLAY_BALL), has(Items.CLAY_BALL))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SEPTIC_TANK_WALL.get(), 8)
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', ModItems.ANTISEPTIC_BRICK_ITEM.get())
                .define('B', ModItems.SEALING_COMPONENT.get())
                .unlockedBy(getHasName(ModItems.ANTISEPTIC_BRICK_ITEM.get()), has(ModItems.ANTISEPTIC_BRICK_ITEM.get()))
                .unlockedBy(getHasName(ModItems.SEALING_COMPONENT.get()), has(ModItems.SEALING_COMPONENT.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.BIOGAS_POND_WALL.get(), 6)
                .pattern("AMA")
                .pattern("ASA")
                .pattern("AMA")
                .define('A', ModItems.ANTISEPTIC_BRICK_ITEM.get())
                .define('M', ModItems.MULTI_LAYER_SINTERED_METAL_MESH.get())
                .define('S', ModItems.SEALING_COMPONENT.get())
                .unlockedBy(getHasName(ModItems.ANTISEPTIC_BRICK_ITEM.get()), has(ModItems.ANTISEPTIC_BRICK_ITEM.get()))
                .unlockedBy(getHasName(ModItems.MULTI_LAYER_SINTERED_METAL_MESH.get()), has(ModItems.MULTI_LAYER_SINTERED_METAL_MESH.get()))
                .unlockedBy(getHasName(ModItems.SEALING_COMPONENT.get()), has(ModItems.SEALING_COMPONENT.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.FECES_BLOCK.get())
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.FECES.get())
                .unlockedBy(getHasName(ModItems.FECES.get()), has(ModItems.FECES.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DRIED_FECES_BLOCK.get())
                .pattern("##")
                .pattern("##")
                .define('#', ModItems.DRIED_FECES.get())
                .unlockedBy(getHasName(ModItems.DRIED_FECES.get()), has(ModItems.DRIED_FECES.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.SQUAT_TOILET.get())
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .define('#', Items.COBBLESTONE)
                .unlockedBy(getHasName(Items.COBBLESTONE), has(Items.COBBLESTONE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.OAK_TOILET.get())
                .pattern("#D#")
                .pattern("###")
                .pattern("###")
                .define('#', Ingredient.of(ItemTags.PLANKS))
                .define('D', ModItems.FECES.get())
                .unlockedBy(getHasName(ModItems.FECES.get()), has(ModItems.FECES.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.STONE_TOILET.get())
                .pattern("#D#")
                .pattern("###")
                .pattern("###")
                .define('#', Items.STONE)
                .define('D', ModItems.FECES.get())
                .unlockedBy(getHasName(ModItems.FECES.get()), has(ModItems.FECES.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.IRON_TOILET.get())
                .pattern("#D#")
                .pattern("###")
                .define('#', Items.IRON_INGOT)
                .define('D', ModItems.STONE_TOILET_ITEM.get())
                .unlockedBy(getHasName(ModBlocks.STONE_TOILET.get()), has(ModBlocks.STONE_TOILET.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.GOLD_TOILET.get())
                .pattern("#D#")
                .pattern("###")
                .define('#', Items.GOLD_INGOT)
                .define('D', ModItems.IRON_TOILET_ITEM.get())
                .unlockedBy(getHasName(ModBlocks.IRON_TOILET.get()), has(ModBlocks.IRON_TOILET.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DIAMOND_TOILET.get())
                .pattern("#D#")
                .define('#', Items.DIAMOND)
                .define('D', ModItems.GOLD_TOILET_ITEM.get())
                .unlockedBy(getHasName(ModBlocks.GOLD_TOILET.get()), has(ModBlocks.GOLD_TOILET.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DRYING_RACK.get())
                .pattern("#D#")
                .pattern("# #")
                .pattern("# #")
                .define('D', Ingredient.of(ItemTags.PLANKS))
                .define('#', Items.STICK)
                .unlockedBy(getHasName(Items.STICK), has(Items.STICK))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.DRYING_BOX.get())
                .pattern("DDD")
                .pattern("# #")
                .pattern("DDD")
                .define('D', Items.IRON_INGOT)
                .define('#', Ingredient.of(ItemTags.PLANKS))
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output);

        SmithingTransformRecipeBuilder.smithing(
                        Ingredient.EMPTY,
                        Ingredient.of(ModBlocks.DIAMOND_TOILET.get()),
                        Ingredient.of(Items.NETHERITE_INGOT),
                        RecipeCategory.BUILDING_BLOCKS,
                        ModBlocks.NETHERITE_TOILET.get().asItem()
                ).unlocks(getHasName(ModBlocks.DIAMOND_TOILET.get()), has(ModBlocks.DIAMOND_TOILET.get()))
                .save(output, "netherite_toilet_smithing");






        // 示例：粪便块分解为4个粪便
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.FECES.get(), 4)
                .requires(ModBlocks.FECES_BLOCK.get())
                .unlockedBy(getHasName(ModBlocks.FECES_BLOCK.get()), has(ModBlocks.FECES_BLOCK.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.SEPTIC_TANK_LIQUID_INPUT_PORT.get())
                .pattern(" I ")
                .pattern("IWI")
                .pattern(" B ")
                .define('I', Items.IRON_INGOT)
                .define('W', ModBlocks.SEPTIC_TANK_WALL.get())
                .define('B', Items.BUCKET)
                .unlockedBy(getHasName(ModBlocks.SEPTIC_TANK_WALL.get()), has(ModBlocks.SEPTIC_TANK_WALL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.SEPTIC_TANK_GAS_VALVE.get())
                .pattern(" I ")
                .pattern("IWI")
                .pattern(" I ")
                .define('I', Items.IRON_INGOT)
                .define('W', ModBlocks.SEPTIC_TANK_WALL.get())
                .unlockedBy(getHasName(ModBlocks.SEPTIC_TANK_WALL.get()), has(ModBlocks.SEPTIC_TANK_WALL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.BIOGAS_POND_GAS_VALVE.get())
                .pattern(" I ")
                .pattern("IWI")
                .pattern(" I ")
                .define('I', Items.IRON_INGOT)
                .define('W', ModBlocks.BIOGAS_POND_WALL.get())
                .unlockedBy(getHasName(ModBlocks.BIOGAS_POND_WALL.get()), has(ModBlocks.BIOGAS_POND_WALL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.BIOGAS_POND_ITEM_INPUT_PORT.get())
                .pattern(" I ")
                .pattern("IWI")
                .pattern(" H ")
                .define('I', Items.IRON_INGOT)
                .define('W', ModBlocks.BIOGAS_POND_WALL.get())
                .define('H', Items.HOPPER)
                .unlockedBy(getHasName(ModBlocks.BIOGAS_POND_WALL.get()), has(ModBlocks.BIOGAS_POND_WALL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.BIOGAS_POND_FLUID_INPUT_PORT.get())
                .pattern(" I ")
                .pattern("IWI")
                .pattern(" B ")
                .define('I', Items.IRON_INGOT)
                .define('W', ModBlocks.BIOGAS_POND_WALL.get())
                .define('B', Items.BUCKET)
                .unlockedBy(getHasName(ModBlocks.BIOGAS_POND_WALL.get()), has(ModBlocks.BIOGAS_POND_WALL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.BIOGAS_POND_ITEM_OUTPUT_PORT.get())
                .pattern(" C ")
                .pattern("IWI")
                .pattern(" I ")
                .define('C', Items.CHEST)
                .define('I', Items.IRON_INGOT)
                .define('W', ModBlocks.BIOGAS_POND_WALL.get())
                .unlockedBy(getHasName(ModBlocks.BIOGAS_POND_WALL.get()), has(ModBlocks.BIOGAS_POND_WALL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.BIOGAS_GENERATOR.get())
                .pattern("IPI")
                .pattern("MGM")
                .pattern("IWI")
                .define('I', Items.IRON_INGOT)
                .define('P', Items.PISTON)
                .define('M', ModItems.MULTI_LAYER_SINTERED_METAL_MESH.get())
                .define('G', ModItems.GAS_TANK.get())
                .define('W', ModBlocks.BIOGAS_POND_WALL.get())
                .unlockedBy(getHasName(ModItems.GAS_TANK.get()), has(ModItems.GAS_TANK.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.SEWAGE_PURIFIER.get())
                .pattern("IPI")
                .pattern("CBC")
                .pattern("IHI")
                .define('I', Items.IRON_INGOT)
                .define('P', Items.PISTON)
                .define('C', Items.COPPER_INGOT)
                .define('B', Items.BUCKET)
                .define('H', Items.HOPPER)
                .unlockedBy(getHasName(Items.IRON_INGOT), has(Items.IRON_INGOT))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.ABSORPTION_TOWER_BODY.get(), 4)
                .pattern("I I")
                .pattern("MWM")
                .pattern("I I")
                .define('I', Items.IRON_INGOT)
                .define('M', ModItems.MULTI_LAYER_SINTERED_METAL_MESH.get())
                .define('W', ModBlocks.BIOGAS_POND_WALL.get())
                .unlockedBy(getHasName(ModBlocks.BIOGAS_POND_WALL.get()), has(ModBlocks.BIOGAS_POND_WALL.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.ABSORPTION_TOWER_BOTTOM.get())
                .pattern("IGI")
                .pattern("MBM")
                .pattern("IWI")
                .define('I', Items.IRON_INGOT)
                .define('G', ModItems.GAS_TANK.get())
                .define('M', ModItems.MULTI_LAYER_SINTERED_METAL_MESH.get())
                .define('B', ModBlocks.ABSORPTION_TOWER_BODY.get())
                .define('W', ModBlocks.BIOGAS_POND_WALL.get())
                .unlockedBy(getHasName(ModItems.GAS_TANK.get()), has(ModItems.GAS_TANK.get()))
                .save(output);

        // 添加干燥配方
        ModDryingRecipeProvider.addRecipes(output);

    }
}