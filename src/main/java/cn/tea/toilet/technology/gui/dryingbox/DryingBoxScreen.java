package cn.tea.toilet.technology.gui.dryingbox;
import cn.tea.toilet.technology.ModConstants;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * 干燥箱 GUI 界面
 * 左边 4×4 输入槽位，右边 4×4 输出槽位，中间箭头
 */
public class DryingBoxScreen extends AbstractContainerScreen<DryingBoxMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(
            ModConstants.MOD_ID, "textures/gui/container/drying_box.png");

    public DryingBoxScreen(DryingBoxMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth = 175;
        this.imageHeight = 176;
        this.titleLabelX = 7;
        this.titleLabelY = 3;
        this.inventoryLabelX = 7;
        this.inventoryLabelY = 85;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTick);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        // 在物品渲染之后渲染进度条，确保进度条显示在物品上方
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        renderDryingProgressBars(guiGraphics, x, y);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        guiGraphics.blit(TEXTURE, x, y, 0, 0, this.imageWidth, this.imageHeight);
    }

    /**
     * 渲染所有正在干燥的槽位的进度条
     */
    private void renderDryingProgressBars(GuiGraphics guiGraphics, int x, int y) {
        // 遍历16个输入槽位
        for (int i = 0; i < 16; i++) {
            int progress = this.menu.getDryingProgress(i);
            int totalTime = this.menu.getDryingTotalTime(i);
            
            // 只有当有干燥任务时才渲染进度条
            if (totalTime > 0 && progress > 0) {
                // 计算进度百分比（0.0 - 1.0）
                float percent = (float) progress / totalTime;
                
                // 计算该槽位在GUI中的位置（4x4网格）
                int row = i / 4;
                int col = i % 4;
                int slotX = x + 8 + col * 18;
                int slotY = y + 13 + row * 18;
                
                // 渲染进度条
                renderProgressBar(guiGraphics, slotX, slotY, percent);
            }
        }
    }

    /**
     * 在指定槽位上方渲染进度条
     */
    private void renderProgressBar(GuiGraphics guiGraphics, int slotX, int slotY, float percent) {
        int barWidth = 16;  // 进度条宽度（与槽位同宽）
        int barHeight = 2;  // 进度条高度（更细）
        int barY = slotY; // 进度条Y坐标（槽位内部偏上位置）
        
        // 绘制半透明黑色背景（让进度条更清晰）
        guiGraphics.fill(slotX, barY, slotX + barWidth, barY + barHeight, 0x80000000);
        
        // 绘制进度条填充（根据进度渐变颜色）
        int filledWidth = (int) (barWidth * Math.min(1.0f, percent));
        if (filledWidth > 0) {
            int fillColor = getProgressColor(percent);
            guiGraphics.fill(slotX, barY, slotX + filledWidth, barY + barHeight, fillColor);
        }
    }

    /**
     * 根据进度百分比获取进度条颜色
     * 0% = 橙色（刚开始），100% = 绿色（即将完成）
     */
    private int getProgressColor(float percent) {
        if (percent < 0.5f) {
            // 0-50%: 橙色 #FFA500
            return 0xFFFFA500;
        } else if (percent < 0.8f) {
            // 50-80%: 黄绿色 #9ACD32
            return 0xFF9ACD32;
        } else {
            // 80-100%: 绿色 #00FF00
            return 0xFF00CC00;
        }
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        // 标题
        guiGraphics.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 0x404040, false);
        // 玩家背包标签
        guiGraphics.drawString(this.font, this.playerInventoryTitle,
                this.inventoryLabelX, this.inventoryLabelY, 0x404040, false);
    }

}