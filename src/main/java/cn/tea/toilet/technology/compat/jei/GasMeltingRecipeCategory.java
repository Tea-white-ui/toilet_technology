package cn.tea.toilet.technology.compat.jei;

import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.recipe.GasMeltingRecipe;
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

import java.util.Arrays;

/** JEI category for recipes processed by the gas melting furnace. */
public final class GasMeltingRecipeCategory implements IRecipeCategory<GasMeltingRecipe> {
    private static final int WIDTH = 124;
    private static final int HEIGHT = 48;
    private static final int GAS_INPUT_X = 1;
    private static final int GAS_INPUT_Y = 1;
    private static final int ITEM_INPUT_X = 25;
    private static final int ITEM_INPUT_Y = 1;
    private static final int ARROW_X = 51;
    private static final int ARROW_Y = 2;
    private static final int OUTPUT_X = 99;
    private static final int OUTPUT_Y = 1;
    private static final Component TITLE = Component.translatable("jei.toilet_technology.gas_melting.title");

    private final RecipeType<GasMeltingRecipe> recipeType;
    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public GasMeltingRecipeCategory(IGuiHelper guiHelper, RecipeType<GasMeltingRecipe> recipeType) {
        this.recipeType = recipeType;
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.GAS_MELTING_FURNACE.get()));
        ResourceLocation furnaceTexture = ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");
        this.arrow = guiHelper.drawableBuilder(furnaceTexture, 79, 35, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public @NotNull RecipeType<GasMeltingRecipe> getRecipeType() {
        return recipeType;
    }

    @Override
    public @NotNull Component getTitle() {
        return TITLE;
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
    public void setRecipe(IRecipeLayoutBuilder builder, GasMeltingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, GAS_INPUT_X, GAS_INPUT_Y)
                .addIngredient(GasStackJeiIngredient.TYPE, new cn.tea.toilet.technology.api.gas.GasStack(
                        recipe.gas(), recipe.gasAmount()))
                .setStandardSlotBackground();
        builder.addSlot(RecipeIngredientRole.INPUT, ITEM_INPUT_X, ITEM_INPUT_Y)
                .addItemStacks(Arrays.asList(recipe.input().getItems()))
                .setStandardSlotBackground();
        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_X, OUTPUT_Y)
                .addItemStack(recipe.output())
                .setStandardSlotBackground();
    }

    @Override
    public void draw(GasMeltingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics,
            double mouseX, double mouseY) {
        arrow.draw(graphics, ARROW_X, ARROW_Y);
        var font = Minecraft.getInstance().font;
        graphics.drawString(font, Component.translatable("jei.toilet_technology.gas_melting.duration",
                recipe.processingTime() / 20.0D), 1, 27, 0x404040, false);
        graphics.drawString(font, Component.translatable("jei.toilet_technology.gas_melting.gas_usage",
                recipe.gasAmount()), 1, 38, 0x404040, false);
    }
}
