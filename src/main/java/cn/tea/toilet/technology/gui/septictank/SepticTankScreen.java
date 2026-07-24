package cn.tea.toilet.technology.gui.septictank;

import cn.tea.toilet.technology.ToiletTechnology;
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

public class SepticTankScreen extends AbstractContainerScreen<SepticTankMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, "textures/gui/container/septic_tank.png");
    private static final float CAPACITY_TEXT_SCALE = 0.65F;
    private static final int TANK_X = 8;
    private static final int TANK_WIDTH = 53;
    private static final int TANK_HEIGHT = 34;
    private static final int GAS_TANK_Y = 20;
    private static final int LIQUID_TANK_Y = 66;

    public SepticTankScreen(SepticTankMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = 175;
        imageHeight = 197;
        titleLabelX = 5;
        titleLabelY = 5;
        inventoryLabelX = 6;
        inventoryLabelY = 104;
    }

    @Override public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
        renderTankTooltip(graphics, mouseX, mouseY);
    }

    @Override protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        drawChemicalTank(graphics, menu.gasStack(), x + TANK_X, y + GAS_TANK_Y, TANK_WIDTH, TANK_HEIGHT, menu.gasCapacity());
        drawTank(graphics, menu.liquidStack(), x + TANK_X, y + LIQUID_TANK_Y, TANK_WIDTH, TANK_HEIGHT, menu.liquidCapacity());
    }

    private void drawTank(GuiGraphics graphics, FluidStack stack, int x, int y, int width, int height, int capacity) {
        int fillHeight = SepticTankFluidDisplay.fillHeight(stack.getAmount(), capacity, height);
        if (stack.isEmpty() || fillHeight == 0) return;

        IClientFluidTypeExtensions extensions = IClientFluidTypeExtensions.of(stack.getFluid());
        ResourceLocation texture = extensions.getStillTexture(stack);
        if (texture == null) return;
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(texture);
        int tint = extensions.getTintColor(stack);
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

    private void drawChemicalTank(GuiGraphics graphics, ChemicalStack stack, int x, int y, int width, int height, long capacity) {
        int fillHeight = SepticTankChemicalDisplay.fillHeight(stack.getAmount(), capacity, height);
        if (stack.isEmpty() || fillHeight == 0) return;
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(stack.getChemical().getIcon());
        int tint = stack.getChemical().getTint();
        float red = (tint >>> 16 & 0xFF) / 255.0F;
        float green = (tint >>> 8 & 0xFF) / 255.0F;
        float blue = (tint & 0xFF) / 255.0F;
        int fillTop = y + height - fillHeight;

        RenderSystem.enableBlend();
        graphics.enableScissor(x, fillTop, x + width, y + height);
        for (int tileX = x; tileX < x + width; tileX += 16) {
            for (int tileY = y + height - 16; tileY + 16 > fillTop; tileY -= 16) {
                graphics.blit(tileX, tileY, 0, 16, 16, sprite, red, green, blue, 1.0F);
            }
        }
        graphics.disableScissor();
        RenderSystem.disableBlend();
    }

    private void renderTankTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (isHovering(TANK_X, GAS_TANK_Y, TANK_WIDTH, TANK_HEIGHT, mouseX, mouseY)) {
            ChemicalStack gas = menu.gasStack();
            if (!gas.isEmpty()) graphics.renderTooltip(font, List.of(
                    gas.getTextComponent(),
                    Component.translatable("gui.toilet_technology.tank_amount", gas.getAmount(), menu.gasCapacity())
            ), Optional.empty(), mouseX, mouseY);
            return;
        }
        if (!isHovering(TANK_X, LIQUID_TANK_Y, TANK_WIDTH, TANK_HEIGHT, mouseX, mouseY)) return;
        FluidStack stack = menu.liquidStack();
        if (stack.isEmpty()) return;
        graphics.renderTooltip(font, List.of(
                stack.getHoverName(),
                Component.translatable("gui.toilet_technology.tank_amount", stack.getAmount(), menu.liquidCapacity())
        ), Optional.empty(), mouseX, mouseY);
    }

    @Override protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
        drawCapacityText(graphics, Component.translatable("gui.toilet_technology.gas", menu.gasAmount(), menu.gasCapacity()), 69, 14, 0x303030, false);
        drawCapacityText(graphics, Component.translatable("gui.toilet_technology.liquid", menu.liquidAmount(), menu.liquidCapacity()), 69, 28, 0xFFFFFF, true);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }

    private void drawCapacityText(GuiGraphics graphics, Component text, int x, int y, int color, boolean shadow) {
        graphics.pose().pushPose();
        graphics.pose().translate(x, y, 0);
        graphics.pose().scale(CAPACITY_TEXT_SCALE, CAPACITY_TEXT_SCALE, 1.0F);
        graphics.drawString(font, text, 0, 0, color, shadow);
        graphics.pose().popPose();
    }
}