package cn.tea.toilet.technology.gui.biogaspond;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.biogaspond.BiogasPondControllerBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import mekanism.api.chemical.ChemicalStack;
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

public class BiogasPondScreen extends AbstractContainerScreen<BiogasPondMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ToiletTechnology.MOD_ID, "textures/gui/container/biogas_pond.png");
    private static final int TANK_X = 44;
    private static final int TANK_WIDTH = 88;
    private static final int GAS_TANK_Y = 23;
    private static final int GAS_TANK_HEIGHT = 31;
    private static final int LIQUID_TANK_Y = 68;
    private static final int LIQUID_TANK_HEIGHT = 34;

    public BiogasPondScreen(BiogasPondMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 175;
        imageHeight = 198;
        titleLabelX = 5;
        titleLabelY = 5;
        inventoryLabelX = 7;
        inventoryLabelY = 105;
    }

    @Override public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
        renderTankTooltip(graphics, mouseX, mouseY);
    }

    @Override protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        drawChemicalTank(graphics, menu.gasStack(), leftPos + TANK_X, topPos + GAS_TANK_Y,
                TANK_WIDTH, GAS_TANK_HEIGHT, BiogasPondControllerBlockEntity.GAS_CAPACITY);
        drawFluidTank(graphics, menu.liquidStack(), leftPos + TANK_X, topPos + LIQUID_TANK_Y,
                TANK_WIDTH, LIQUID_TANK_HEIGHT, BiogasPondControllerBlockEntity.LIQUID_CAPACITY);
    }

    private void drawFluidTank(GuiGraphics graphics, FluidStack stack, int x, int y, int width, int height, int capacity) {
        int fillHeight = BiogasPondFluidDisplay.fillHeight(stack.getAmount(), capacity, height);
        if (stack.isEmpty() || fillHeight == 0) return;

        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(stack.getFluid());
        ResourceLocation texture = extensions.getStillTexture(stack);
        if (texture == null) return;
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
        int tint = extensions.getTintColor(stack);
        drawTiledTank(graphics, sprite, tint, x, y, width, height, fillHeight);
    }

    private void drawChemicalTank(GuiGraphics graphics, ChemicalStack stack, int x, int y, int width, int height, long capacity) {
        int fillHeight = BiogasPondChemicalDisplay.fillHeight(stack.getAmount(), capacity, height);
        if (stack.isEmpty() || fillHeight == 0) return;

        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(stack.getChemical().getIcon());
        drawTiledTank(graphics, sprite, stack.getChemical().getTint() | 0xFF000000, x, y, width, height, fillHeight);
    }

    private void drawTiledTank(GuiGraphics graphics, TextureAtlasSprite sprite, int tint, int x, int y, int width, int height, int fillHeight) {
        float alpha = (tint >>> 24 & 0xFF) / 255.0F;
        float red = (tint >>> 16 & 0xFF) / 255.0F;
        float green = (tint >>> 8 & 0xFF) / 255.0F;
        float blue = (tint & 0xFF) / 255.0F;
        int fillTop = y + height - fillHeight;

        RenderSystem.enableBlend();
        graphics.enableScissor(x, fillTop, x + width, y + height);
        for (int tileX = x; tileX < x + width; tileX += 16) {
            for (int tileY = y + height - 16; tileY + 16 > fillTop; tileY -= 16) {
                graphics.blit(tileX, tileY, 0, 16, 16, sprite, red, green, blue, alpha);
            }
        }
        graphics.disableScissor();
        RenderSystem.disableBlend();
    }

    private void renderTankTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (isHovering(TANK_X, GAS_TANK_Y, TANK_WIDTH, GAS_TANK_HEIGHT, mouseX, mouseY)) {
            ChemicalStack gas = menu.gasStack();
            if (!gas.isEmpty()) graphics.renderTooltip(font, List.of(
                    gas.getTextComponent(),
                    Component.translatable("gui.toilet_technology.tank_amount", gas.getAmount(), BiogasPondControllerBlockEntity.GAS_CAPACITY)
            ), Optional.empty(), mouseX, mouseY);
            return;
        }
        if (!isHovering(TANK_X, LIQUID_TANK_Y, TANK_WIDTH, LIQUID_TANK_HEIGHT, mouseX, mouseY)) return;
        FluidStack liquid = menu.liquidStack();
        if (liquid.isEmpty()) return;
        graphics.renderTooltip(font, List.of(
                liquid.getHoverName(),
                Component.translatable("gui.toilet_technology.tank_amount", liquid.getAmount(), BiogasPondControllerBlockEntity.LIQUID_CAPACITY)
        ), Optional.empty(), mouseX, mouseY);
    }

    @Override protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
        graphics.drawString(font, Component.translatable("gui.toilet_technology.gas", menu.gasAmount(), BiogasPondControllerBlockEntity.GAS_CAPACITY), 44, 13, 0x303030, false);
        graphics.drawString(font, Component.translatable("gui.toilet_technology.liquid", menu.liquidAmount(), BiogasPondControllerBlockEntity.LIQUID_CAPACITY), 44, 56, 0x404040, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }
}
