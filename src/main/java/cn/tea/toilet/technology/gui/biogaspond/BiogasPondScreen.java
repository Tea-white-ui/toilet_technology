package cn.tea.toilet.technology.gui.biogaspond;

import cn.tea.toilet.technology.ToiletTechnology;
import cn.tea.toilet.technology.block.biogaspond.BiogasPondControllerBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class BiogasPondScreen extends AbstractContainerScreen<BiogasPondMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ToiletTechnology.MOD_ID, "textures/gui/container/biogas_pond.png");

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
    }

    @Override protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        drawTank(graphics, 44, 23, 88, 31, menu.gasAmount(), BiogasPondControllerBlockEntity.GAS_CAPACITY, 0xFF7DBB6A);
        drawTank(graphics, 44, 68, 88, 34, menu.liquidAmount(), BiogasPondControllerBlockEntity.LIQUID_CAPACITY, 0xFF4A91C8);
    }

    private void drawTank(GuiGraphics graphics, int x, int y, int width, int height, long amount, long capacity, int color) {
        if (amount <= 0) return;
        int fill = Math.max(1, (int) (height * Math.min(amount, capacity) / capacity));
        graphics.fill(leftPos + x, topPos + y + height - fill, leftPos + x + width, topPos + y + height, color);
    }

    @Override protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
        graphics.drawString(font, Component.translatable("gui.toilet_technology.gas", menu.gasAmount(), BiogasPondControllerBlockEntity.GAS_CAPACITY), 44, 13, 0x303030, false);
        graphics.drawString(font, Component.translatable("gui.toilet_technology.liquid", menu.liquidAmount(), BiogasPondControllerBlockEntity.LIQUID_CAPACITY), 44, 56, 0x404040, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }
}
