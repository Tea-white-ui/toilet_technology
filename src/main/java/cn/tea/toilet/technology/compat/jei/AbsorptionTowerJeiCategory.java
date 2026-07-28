package cn.tea.toilet.technology.compat.jei;

import cn.tea.toilet.technology.ModConstants;
import cn.tea.toilet.technology.block.ModBlocks;
import cn.tea.toilet.technology.recipe.AbsorptionTowerRecipe;
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

public final class AbsorptionTowerJeiCategory implements IRecipeCategory<AbsorptionTowerRecipe> {
    public static final RecipeType<AbsorptionTowerRecipe> RECIPE_TYPE = RecipeType.create(
            ModConstants.MOD_ID, "absorption_tower", AbsorptionTowerRecipe.class);
    private static final Component TITLE = Component.translatable("jei.toilet_technology.absorption_tower.title");

    private final IDrawable icon;
    private final IDrawableAnimated arrow;

    public AbsorptionTowerJeiCategory(IGuiHelper guiHelper) {
        icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK,
                new ItemStack(ModBlocks.ABSORPTION_TOWER_BOTTOM.get()));
        ResourceLocation furnaceTexture = ResourceLocation.withDefaultNamespace("textures/gui/container/furnace.png");
        arrow = guiHelper.drawableBuilder(furnaceTexture, 79, 35, 24, 17)
                .buildAnimated(200, IDrawableAnimated.StartDirection.LEFT, false);
    }

    @Override
    public @NotNull RecipeType<AbsorptionTowerRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public @NotNull Component getTitle() {
        return TITLE;
    }

    @Override
    public int getWidth() {
        return AbsorptionTowerJeiLayout.WIDTH;
    }

    @Override
    public int getHeight() {
        return AbsorptionTowerJeiLayout.HEIGHT;
    }

    @Override
    public @NotNull IDrawable getIcon() {
        return icon;
    }

    @Override
    public void setRecipe(IRecipeLayoutBuilder builder, AbsorptionTowerRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, AbsorptionTowerJeiLayout.GAS_INPUT_X, AbsorptionTowerJeiLayout.GAS_INPUT_Y)
                .addIngredient(GasStackJeiIngredient.TYPE, new cn.tea.toilet.technology.api.gas.GasStack(recipe.inputGas(), recipe.inputGasAmount()))
                .setStandardSlotBackground();
        builder.addSlot(RecipeIngredientRole.INPUT, AbsorptionTowerJeiLayout.FLUID_INPUT_X, AbsorptionTowerJeiLayout.FLUID_INPUT_Y)
                .addFluidStack(recipe.inputFluid(), recipe.inputFluidAmount())
                .setFluidRenderer(recipe.inputFluidAmount(), true, 16, 16)
                .setStandardSlotBackground();
        builder.addSlot(RecipeIngredientRole.OUTPUT, AbsorptionTowerJeiLayout.GAS_OUTPUT_X, AbsorptionTowerJeiLayout.GAS_OUTPUT_Y)
                .addIngredient(GasStackJeiIngredient.TYPE, new cn.tea.toilet.technology.api.gas.GasStack(recipe.outputGas(), recipe.outputGasAmount()))
                .setStandardSlotBackground();
        builder.addSlot(RecipeIngredientRole.OUTPUT, AbsorptionTowerJeiLayout.FLUID_OUTPUT_X, AbsorptionTowerJeiLayout.FLUID_OUTPUT_Y)
                .addFluidStack(recipe.outputFluid(), recipe.outputFluidAmount())
                .setFluidRenderer(recipe.outputFluidAmount(), true, 16, 16)
                .setStandardSlotBackground();
    }

    @Override
    public void draw(AbsorptionTowerRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics graphics,
            double mouseX, double mouseY) {
        arrow.draw(graphics, AbsorptionTowerJeiLayout.ARROW_X, AbsorptionTowerJeiLayout.ARROW_Y);
        var font = Minecraft.getInstance().font;
        graphics.drawString(font, Component.translatable("jei.toilet_technology.absorption_tower.per_batch"),
                26, 65, 0x404040, false);
        for (int bodyIndex = 0; bodyIndex < AbsorptionTowerJeiLayout.BODY_BLOCK_COUNT; bodyIndex++) {
            graphics.renderItem(new ItemStack(ModBlocks.ABSORPTION_TOWER_BODY.get()), AbsorptionTowerJeiLayout.STRUCTURE_X,
                    AbsorptionTowerJeiLayout.STRUCTURE_TOP_Y + bodyIndex * AbsorptionTowerJeiLayout.STRUCTURE_BLOCK_SPACING);
        }
        graphics.renderItem(new ItemStack(ModBlocks.ABSORPTION_TOWER_BOTTOM.get()), AbsorptionTowerJeiLayout.STRUCTURE_X,
                AbsorptionTowerJeiLayout.STRUCTURE_TOP_Y + AbsorptionTowerJeiLayout.BODY_BLOCK_COUNT
                        * AbsorptionTowerJeiLayout.STRUCTURE_BLOCK_SPACING);
    }
}
