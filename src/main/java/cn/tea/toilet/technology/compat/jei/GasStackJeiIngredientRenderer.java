package cn.tea.toilet.technology.compat.jei;

import cn.tea.toilet.technology.gas.GasStack;
import com.mojang.blaze3d.systems.RenderSystem;
import mezz.jei.api.ingredients.IIngredientRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public final class GasStackJeiIngredientRenderer implements IIngredientRenderer<GasStack> {
    @Override
    public void render(GuiGraphics graphics, GasStack ingredient) {
        if (ingredient.isEmpty()) return;
        TextureAtlasSprite sprite = Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS)
                .apply(ingredient.getGas().texture());
        int tint = ingredient.getGas().tint();
        float red = (tint >>> 16 & 0xFF) / 255.0F;
        float green = (tint >>> 8 & 0xFF) / 255.0F;
        float blue = (tint & 0xFF) / 255.0F;
        RenderSystem.enableBlend();
        graphics.blit(0, 0, 0, 16, 16, sprite, red, green, blue, 1.0F);
        RenderSystem.disableBlend();
    }

    @Override
    public List<Component> getTooltip(GasStack ingredient, TooltipFlag tooltipFlag) {
        return List.of(
                ingredient.getTextComponent(),
                Component.translatable("jei.toilet_technology.biogas.amount", ingredient.getAmount())
        );
    }
}
