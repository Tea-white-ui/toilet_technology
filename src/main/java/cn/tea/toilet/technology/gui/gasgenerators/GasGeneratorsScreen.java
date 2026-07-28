package cn.tea.toilet.technology.gui.gasgenerators;

import cn.tea.toilet.technology.ModConstants;
import cn.tea.toilet.technology.api.gas.GasStack;
import cn.tea.toilet.technology.block.gasgenerators.GasGeneratorOperation;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Optional;

public final class GasGeneratorsScreen extends AbstractContainerScreen<GasGeneratorsMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ModConstants.MOD_ID, "textures/gui/container/gas_generators.png");
    private static final int GAS_X = 25;
    private static final int GAS_Y = 20;
    private static final int GAS_WIDTH = 88;
    private static final int GAS_HEIGHT = 50;
    private static final int ENERGY_X = 143;
    private static final int ENERGY_Y = 20;
    private static final int ENERGY_WIDTH = 24;
    private static final int ENERGY_HEIGHT = 50;

    public GasGeneratorsScreen(GasGeneratorsMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 175;
        imageHeight = 165;
        titleLabelX = 7;
        titleLabelY = 6;
        inventoryLabelX = 7;
        inventoryLabelY = 74;
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
        renderTankTooltips(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        renderGasTank(graphics, menu.gasStack());
        renderEnergyTank(graphics);
    }

    private void renderGasTank(GuiGraphics graphics, GasStack stack) {
        if (stack.isEmpty()) return;
        int filledHeight = Math.clamp((int) Math.ceil((double) stack.getAmount() * GAS_HEIGHT / GasGeneratorOperation.GAS_CAPACITY), 0, GAS_HEIGHT);
        if (filledHeight == 0) return;
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stack.getGas().texture());
        int tint = stack.getGas().tint() | 0xFF000000;
        float red = (tint >>> 16 & 0xFF) / 255.0F;
        float green = (tint >>> 8 & 0xFF) / 255.0F;
        float blue = (tint & 0xFF) / 255.0F;
        int x = leftPos + GAS_X;
        int bottom = topPos + GAS_Y + GAS_HEIGHT;
        int fillTop = bottom - filledHeight;
        RenderSystem.enableBlend();
        graphics.enableScissor(x, fillTop, x + GAS_WIDTH, bottom);
        for (int tileX = x; tileX < x + GAS_WIDTH; tileX += 16) {
            for (int tileY = bottom - 16; tileY + 16 > fillTop; tileY -= 16) {
                graphics.blit(tileX, tileY, 0, 16, 16, sprite, red, green, blue, 1.0F);
            }
        }
        graphics.disableScissor();
        RenderSystem.disableBlend();
    }

    private void renderEnergyTank(GuiGraphics graphics) {
        int filledHeight = Math.clamp((int) Math.ceil((double) menu.energyStored() * ENERGY_HEIGHT / GasGeneratorOperation.ENERGY_CAPACITY), 0, ENERGY_HEIGHT);
        if (filledHeight == 0) return;
        int x = leftPos + ENERGY_X;
        int bottom = topPos + ENERGY_Y + ENERGY_HEIGHT;
        int fillTop = bottom - filledHeight;
        graphics.fill(x, fillTop, x + ENERGY_WIDTH, bottom, menu.isGenerating() ? 0xFF39D353 : 0xFFB8A000);
    }

    private void renderTankTooltips(GuiGraphics graphics, int mouseX, int mouseY) {
        if (isHovering(GAS_X, GAS_Y, GAS_WIDTH, GAS_HEIGHT, mouseX, mouseY)) {
            GasStack gas = menu.gasStack();
            if (!gas.isEmpty()) graphics.renderTooltip(font, List.of(gas.getTextComponent(), Component.translatable(
                    "gui.toilet_technology.tank_amount", gas.getAmount(), GasGeneratorOperation.GAS_CAPACITY)), Optional.empty(), mouseX, mouseY);
        } else if (isHovering(ENERGY_X, ENERGY_Y, ENERGY_WIDTH, ENERGY_HEIGHT, mouseX, mouseY)) {
            graphics.renderTooltip(font, Component.translatable("gui.toilet_technology.energy_amount",
                    menu.energyStored(), GasGeneratorOperation.ENERGY_CAPACITY), mouseX, mouseY);
        }
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }
}
