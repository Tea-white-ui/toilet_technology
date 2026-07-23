package cn.tea.toilet.technology.gui.septictank;

import cn.tea.toilet.technology.ToiletTechnology;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class SepticTankScreen extends AbstractContainerScreen<SepticTankMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(ToiletTechnology.MOD_ID, "textures/gui/container/septic_tank.png");
    private static final float CAPACITY_TEXT_SCALE = 0.80F;

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
    }

    @Override protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        int x = leftPos;
        int y = topPos;
        graphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);
        drawTank(graphics, x + 8, y + 20, 53, 34, menu.gasAmount(), menu.gasCapacity(), 0xB088A0A8);
        drawTank(graphics, x + 8, y + 66, 53, 34, menu.liquidAmount(), menu.liquidCapacity(), 0xC04C7B32);
    }

    private void drawTank(GuiGraphics graphics, int x, int y, int width, int height, int amount, int capacity, int color) {
        if (capacity <= 0 || amount <= 0) return;
        int fill = Math.max(1, Math.min(height, amount * height / capacity));
        graphics.fill(x, y + height - fill, x + width, y + height, color);
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