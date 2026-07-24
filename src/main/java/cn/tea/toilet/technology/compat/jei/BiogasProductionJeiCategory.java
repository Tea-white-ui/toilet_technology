package cn.tea.toilet.technology.compat.jei;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.fluid.ModFluids;
import cn.tea.toilet.technology.gas.GasRegistry;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.item.ModItems;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public final class BiogasProductionJeiCategory implements IRecipeCategory<BiogasProductionJeiRecipe> {
    public static final RecipeType<BiogasProductionJeiRecipe> SEPTIC_TANK_RECIPE_TYPE = RecipeType.create(
            ToiletTechnology.MOD_ID, "septic_tank_biogas_production", BiogasProductionJeiRecipe.class);
    public static final RecipeType<BiogasProductionJeiRecipe> BIOGAS_POND_RECIPE_TYPE = RecipeType.create(
            ToiletTechnology.MOD_ID, "biogas_pond_biogas_production", BiogasProductionJeiRecipe.class);
    private static final int WIDTH = 150;
    private static final int HEIGHT = 60;

    private final RecipeType<BiogasProductionJeiRecipe> recipeType;
    private final Component title;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public BiogasProductionJeiCategory(IGuiHelper guiHelper, RecipeType<BiogasProductionJeiRecipe> recipeType,
            String titleKey, ItemStack iconStack) {
        this.recipeType = recipeType;
        this.title = Component.translatable(titleKey);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, iconStack);
        ResourceLocation furnaceTexture = ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");
        this.arrow = guiHelper.drawableBuilder(furnaceTexture, 79, 35, 24, 17)
                .buildAnimated(1000, IDrawableAnimated.StartDirection.LEFT, false);
    }

    public static BiogasProductionJeiCategory septicTank(IGuiHelper guiHelper) {
        return new BiogasProductionJeiCategory(guiHelper, SEPTIC_TANK_RECIPE_TYPE,
                "jei.toilet_technology.septic_tank_biogas.title",
                new ItemStack(ModBlocks.SEPTIC_TANK_CONTROLLER.get()));
    }

    public static BiogasProductionJeiCategory biogasPond(IGuiHelper guiHelper) {
        return new BiogasProductionJeiCategory(guiHelper, BIOGAS_POND_RECIPE_TYPE,
                "jei.toilet_technology.biogas_pond_biogas.title",
                new ItemStack(ModBlocks.BIOGAS_POND_CONTROLLER.get()));
    }

    @Override
    public @NotNull RecipeType<BiogasProductionJeiRecipe> getRecipeType() {
        return recipeType;
    }

    @Override
    public @NotNull Component getTitle() {
        return title;
    }

    @Override
    public int getWidth() {
        return WIDTH;
    }

    @Override
    public int getHeight() {
        return HEIGHT;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, BiogasProductionJeiRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1)
                .addFluidStack(ModFluids.FECES_LIQUID.get(), recipe.displayedLiquidAmount())
                .setFluidRenderer(64_000, true, 16, 16)
                .setStandardSlotBackground();
        builder.addSlot(RecipeIngredientRole.OUTPUT, 62, 1)
                .addIngredient(GasStackJeiIngredient.TYPE, new GasStack(GasRegistry.BIOGAS, recipe.maximumBiogasAmount()))
                .setStandardSlotBackground();
        if (recipe.hasResidueOutput()) {
            builder.addSlot(RecipeIngredientRole.OUTPUT, BiogasProductionJeiLayout.RESIDUE_SLOT_X,
                    BiogasProductionJeiLayout.RESIDUE_SLOT_Y)
                    .addItemStack(new ItemStack(ModItems.BIOGAS_RESIDUE.get()))
                    .setStandardSlotBackground();
        }
    }

    @Override
    public void draw(BiogasProductionJeiRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics,
            double mouseX, double mouseY) {
        arrow.draw(graphics, 32, 1);
        var font = Minecraft.getInstance().font;
        int summaryTextOffsetY = recipe.hasResidueOutput() ? 10 : 0;
        graphics.drawString(font, Component.translatable("jei.toilet_technology.biogas.output_scales_with_liquid",
                recipe.minimumBiogasAmount(), recipe.maximumBiogasAmount()), 1, 25 + summaryTextOffsetY, 0x404040, false);
        graphics.drawString(font, Component.translatable("jei.toilet_technology.biogas.duration",
                recipe.durationTicks() / 20.0D), 1, 37 + summaryTextOffsetY, 0x404040, false);
        if (recipe.requiresGeneratorEnergy()) {
            graphics.drawString(font, Component.translatable("jei.toilet_technology.biogas.generator_energy",
                    recipe.energyPerBatch()), 1, 49, 0x404040, false);
        }
        if (recipe.hasResidueOutput()) {
            graphics.drawString(font, Component.translatable("jei.toilet_technology.biogas.residue_probability",
                    recipe.residueChancePercent()), BiogasProductionJeiLayout.RESIDUE_SLOT_X,
                    BiogasProductionJeiLayout.RESIDUE_PROBABILITY_Y, 0x404040, false);
        }
    }
}
