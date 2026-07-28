package cn.tea.toilet.technology.gui.absorptiontower;

import cn.tea.toilet.technology.ModConstants;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.gui.TankMenuData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public class AbsorptionTowerScreen extends AbstractContainerScreen<AbsorptionTowerMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ModConstants.MOD_ID, "textures/gui/container/absorption_tower.png");
    private static final int LIQUID_INPUT_X = 48;
    private static final int LIQUID_INPUT_Y = 42;
    private static final int LIQUID_INPUT_WIDTH = 79;
    private static final int LIQUID_INPUT_HEIGHT = 63;
    private static final int LIQUID_OUTPUT_LEFT_X = 22;
    private static final int LIQUID_OUTPUT_RIGHT_X = 138;
    private static final int LIQUID_OUTPUT_Y = 41;
    private static final int LIQUID_OUTPUT_WIDTH = 16;
    private static final int LIQUID_OUTPUT_HEIGHT = 65;
    private static final int GAS_INPUT_X = 64;
    private static final int GAS_INPUT_Y = 121;
    private static final int GAS_INPUT_WIDTH = 48;
    private static final int GAS_INPUT_HEIGHT = 23;
    private static final int GAS_OUTPUT_X = 63;
    private static final int GAS_OUTPUT_Y = 13;
    private static final int GAS_OUTPUT_WIDTH = 48;
    private static final int GAS_OUTPUT_HEIGHT = 24;

    public AbsorptionTowerScreen(AbsorptionTowerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 172;
        imageHeight = 231;
        titleLabelX = 5;
        titleLabelY = 5;
        inventoryLabelX = 7;
        inventoryLabelY = 138;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
        renderTankTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        drawFluidTank(graphics, menu.waterInput(), LIQUID_INPUT_X, LIQUID_INPUT_Y, LIQUID_INPUT_WIDTH, LIQUID_INPUT_HEIGHT);
        drawFluidTank(graphics, menu.liquidOutput(), LIQUID_OUTPUT_LEFT_X, LIQUID_OUTPUT_Y,
                LIQUID_OUTPUT_WIDTH, LIQUID_OUTPUT_HEIGHT);
        drawFluidTank(graphics, menu.liquidOutput(), LIQUID_OUTPUT_RIGHT_X, LIQUID_OUTPUT_Y,
                LIQUID_OUTPUT_WIDTH, LIQUID_OUTPUT_HEIGHT);
        drawGasTank(graphics, menu.gasInput(), GAS_INPUT_X, GAS_INPUT_Y, GAS_INPUT_WIDTH, GAS_INPUT_HEIGHT);
        drawGasTank(graphics, menu.gasOutput(), GAS_OUTPUT_X, GAS_OUTPUT_Y, GAS_OUTPUT_WIDTH, GAS_OUTPUT_HEIGHT);
    }

    private void drawFluidTank(GuiGraphics graphics, FluidStack stack, int x, int y, int width, int height) {
        if (stack.isEmpty()) return;
        ResourceLocation texture = IClientFluidTypeExtensions.of(stack.getFluid()).getStillTexture(stack);
        if (texture == null) return;
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
        drawTank(graphics, sprite, IClientFluidTypeExtensions.of(stack.getFluid()).getTintColor(stack), x, y, width, height,
                fillHeight(stack.getAmount(), height));
    }

    private void drawGasTank(GuiGraphics graphics, GasStack stack, int x, int y, int width, int height) {
        if (stack.isEmpty()) return;
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stack.getGas().texture());
        drawTank(graphics, sprite, stack.getGas().tint() | 0xFF000000, x, y, width, height,
                fillHeight(stack.getAmount(), height));
    }

    private int fillHeight(long amount, int height) {
        return TankMenuData.fillHeight(amount, menu.tankCapacity(), height);
    }

    private void drawTank(GuiGraphics graphics, TextureAtlasSprite sprite, int tint, int x, int y, int width, int height, int fillHeight) {
        if (fillHeight <= 0) return;
        float alpha = (tint >>> 24 & 0xFF) / 255.0F;
        float red = (tint >>> 16 & 0xFF) / 255.0F;
        float green = (tint >>> 8 & 0xFF) / 255.0F;
        float blue = (tint & 0xFF) / 255.0F;
        int fillTop = topPos + y + height - fillHeight;
        int left = leftPos + x;
        int bottom = topPos + y + height;
        RenderSystem.enableBlend();
        graphics.enableScissor(left, fillTop, left + width, bottom);
        for (int tileX = left; tileX < left + width; tileX += 16) {
            for (int tileY = bottom - 16; tileY + 16 > fillTop; tileY -= 16) {
                graphics.blit(tileX, tileY, 0, 16, 16, sprite, red, green, blue, alpha);
            }
        }
        graphics.disableScissor();
        RenderSystem.disableBlend();
    }

    private void renderTankTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (isHovering(LIQUID_INPUT_X, LIQUID_INPUT_Y, LIQUID_INPUT_WIDTH, LIQUID_INPUT_HEIGHT, mouseX, mouseY)) {
            renderFluidTooltip(graphics, menu.waterInput(), mouseX, mouseY);
        } else if (isHovering(LIQUID_OUTPUT_LEFT_X, LIQUID_OUTPUT_Y, LIQUID_OUTPUT_WIDTH, LIQUID_OUTPUT_HEIGHT, mouseX, mouseY)
                || isHovering(LIQUID_OUTPUT_RIGHT_X, LIQUID_OUTPUT_Y, LIQUID_OUTPUT_WIDTH, LIQUID_OUTPUT_HEIGHT, mouseX, mouseY)) {
            renderFluidTooltip(graphics, menu.liquidOutput(), mouseX, mouseY);
        } else if (isHovering(GAS_INPUT_X, GAS_INPUT_Y, GAS_INPUT_WIDTH, GAS_INPUT_HEIGHT, mouseX, mouseY)) {
            renderGasTooltip(graphics, menu.gasInput(), mouseX, mouseY);
        } else if (isHovering(GAS_OUTPUT_X, GAS_OUTPUT_Y, GAS_OUTPUT_WIDTH, GAS_OUTPUT_HEIGHT, mouseX, mouseY)) {
            renderGasTooltip(graphics, menu.gasOutput(), mouseX, mouseY);
        }
    }

    private void renderFluidTooltip(GuiGraphics graphics, FluidStack stack, int mouseX, int mouseY) {
        if (stack.isEmpty()) return;
        graphics.renderTooltip(font, List.of(stack.getHoverName(),
                Component.translatable("gui.toilet_technology.tank_amount", stack.getAmount(), menu.tankCapacity())),
                Optional.empty(), mouseX, mouseY);
    }

    private void renderGasTooltip(GuiGraphics graphics, GasStack stack, int mouseX, int mouseY) {
        if (stack.isEmpty()) return;
        graphics.renderTooltip(font, List.of(stack.getTextComponent(),
                Component.translatable("gui.toilet_technology.tank_amount", stack.getAmount(), menu.tankCapacity())),
                Optional.empty(), mouseX, mouseY);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }
}