package cn.tea.toilet.technology.compat.jei;

import cn.tea.toilet.technology.recipe.DryingRecipe;
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
import java.util.List;

public class DryingRecipeCategory implements IRecipeCategory<DryingRecipe> {

    public static final int WIDTH = 82;
    public static final int HEIGHT = 34;

    public static final Component TITLE = Component.translatable("jei.toilet_technology.drying.title");

    private static final int INPUT_SLOT_X = 1;
    private static final int INPUT_SLOT_Y = 1;

    private static final int OUTPUT_SLOT_X = 61;
    private static final int OUTPUT_SLOT_Y = 1;

    private static final int ARROW_X = 27;
    private static final int ARROW_Y = 3;

    private static final int TIME_TEXT_X = 28;
    private static final int TIME_TEXT_Y = 25;

    private final IDrawable icon;
    private final IDrawableAnimated arrow;
    private final IDrawable slotDrawable;
    private final RecipeType<DryingRecipe> recipeType;

    public DryingRecipeCategory(IGuiHelper guiHelper, RecipeType<DryingRecipe> recipeType) {
        this.recipeType = recipeType;
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(cn.tea.toilet.technology.block.ModBlocks.DRYING_RACK.get()));

        ResourceLocation furnaceTexture = ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");
        this.arrow = guiHelper.drawableBuilder(furnaceTexture, 79, 35, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);

        this.slotDrawable = guiHelper.getSlotDrawable();
    }

    @Override
    public @NotNull RecipeType<DryingRecipe> getRecipeType() {
        return this.recipeType;
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
        return this.icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, DryingRecipe recipe, IFocusGroup focuses) {
        List<ItemStack> inputStacks = Arrays.asList(recipe.getInputIngredient().getItems());
        builder.addSlot(RecipeIngredientRole.INPUT, INPUT_SLOT_X, INPUT_SLOT_Y)
                .addItemStacks(inputStacks)
                .setStandardSlotBackground();

        builder.addSlot(RecipeIngredientRole.OUTPUT, OUTPUT_SLOT_X, OUTPUT_SLOT_Y)
                .addItemStack(recipe.getResultItem(null))
                .setStandardSlotBackground();
    }

    @Override
    public void draw(DryingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.arrow.draw(guiGraphics, ARROW_X, ARROW_Y);

        int dryingTimeSeconds = recipe.getDryingTime() / 20;
        String timeText = dryingTimeSeconds + "s";
        var font = Minecraft.getInstance().font;
        int textWidth = font.width(timeText);
        int centeredX = ARROW_X + 12 - textWidth / 2;

        guiGraphics.drawString(
                font,
                timeText,
                centeredX,
                TIME_TEXT_Y,
                0x808080,
                false
        );
    }
}