package cn.tea.toilet.technology.gui.gasmeltingfurnace;

import cn.tea.toilet.technology.ModConstants;
import cn.tea.toilet.technology.block.gasmeltingfurnace.GasMeltingFurnaceBlockEntity;
import cn.tea.toilet.technology.api.gas.GasStack;
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

public final class GasMeltingFurnaceScreen extends AbstractContainerScreen<GasMeltingFurnaceMenu> {
    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ModConstants.MOD_ID, "textures/gui/container/gas_melting_furnace.png");
    private static final ResourceLocation LIT_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace(
            "container/furnace/lit_progress");
    private static final ResourceLocation BURN_PROGRESS_SPRITE = ResourceLocation.withDefaultNamespace(
            "container/furnace/burn_progress");
    private static final int TANK_X = 8;
    private static final int TANK_Y = 26;
    private static final int TANK_WIDTH = 31;
    private static final int TANK_HEIGHT = 45;

    public GasMeltingFurnaceScreen(GasMeltingFurnaceMenu menu, Inventory inventory, Component title) {
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
        renderGasTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        renderGasTank(graphics, menu.gasStack());
        renderProcessingIndicators(graphics);
    }

    private void renderProcessingIndicators(GuiGraphics graphics) {
        if (!menu.isProcessing()) return;
        graphics.blitSprite(LIT_PROGRESS_SPRITE, 14, 14, 0, 0, leftPos + 63, topPos + 45, 14, 14);
        int progressWidth = menu.getProcessingProgressScaled(24);
        if (progressWidth > 0) {
            graphics.blitSprite(BURN_PROGRESS_SPRITE, 24, 16, 0, 0, leftPos + 86, topPos + 26,
                    progressWidth, 16);
        }
    }

    private void renderGasTank(GuiGraphics graphics, GasStack stack) {
        if (stack.isEmpty()) return;
        int filledHeight = Math.clamp((int) Math.ceil((double) stack.getAmount() * TANK_HEIGHT
                / GasMeltingFurnaceBlockEntity.GAS_CAPACITY), 0, TANK_HEIGHT);
        if (filledHeight == 0) return;
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(stack.getGas().texture());
        int tint = stack.getGas().tint() | 0xFF000000;
        float red = (tint >>> 16 & 0xFF) / 255.0F;
        float green = (tint >>> 8 & 0xFF) / 255.0F;
        float blue = (tint & 0xFF) / 255.0F;
        int fillTop = topPos + TANK_Y + TANK_HEIGHT - filledHeight;
        int x = leftPos + TANK_X;
        int bottom = topPos + TANK_Y + TANK_HEIGHT;

        RenderSystem.enableBlend();
        graphics.enableScissor(x, fillTop, x + TANK_WIDTH, bottom);
        for (int tileX = x; tileX < x + TANK_WIDTH; tileX += 16) {
            for (int tileY = bottom - 16; tileY + 16 > fillTop; tileY -= 16) {
                graphics.blit(tileX, tileY, 0, 16, 16, sprite, red, green, blue, 1.0F);
            }
        }
        graphics.disableScissor();
        RenderSystem.disableBlend();
    }

    private void renderGasTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        if (!isHovering(TANK_X, TANK_Y, TANK_WIDTH, TANK_HEIGHT, mouseX, mouseY)) return;
        GasStack gas = menu.gasStack();
        if (gas.isEmpty()) return;
        graphics.renderTooltip(font, List.of(gas.getTextComponent(), Component.translatable(
                "gui.toilet_technology.tank_amount", gas.getAmount(), GasMeltingFurnaceBlockEntity.GAS_CAPACITY
        )), Optional.empty(), mouseX, mouseY);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics graphics, int mouseX, int mouseY) {
        graphics.drawString(font, title, titleLabelX, titleLabelY, 0x404040, false);
        graphics.drawString(font, playerInventoryTitle, inventoryLabelX, inventoryLabelY, 0x404040, false);
    }
}
