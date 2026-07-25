package cn.tea.toilet.technology.client;

import cn.tea.toilet.technology.ModConstants;
import cn.tea.toilet.technology.gas.GasStack;
import cn.tea.toilet.technology.item.GasTankItem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.joml.Matrix4f;

/** Renders the gas stored in a {@link GasTankItem} inside its 16x16 item sprite. */
public final class GasTankItemRenderer extends BlockEntityWithoutLevelRenderer {
    private static final ResourceLocation FRAME_TEXTURE = texture("item/gas_tank_frame");
    private static final ResourceLocation GLASS_TEXTURE = texture("item/gas_tank_glass_50");
    private static final int WINDOW_LEFT = 4;
    private static final int WINDOW_TOP = 4;
    private static final int WINDOW_RIGHT = 12;
    private static final int WINDOW_BOTTOM = 13;
    private static final float PIXEL = 1.0F / 16.0F;
    private static final float CONTENTS_Z = 0.001F;
    private static final float GLASS_Z = 0.002F;
    private static final float FRAME_Z = 0.003F;

    public GasTankItemRenderer(BlockEntityRenderDispatcher dispatcher, EntityModelSet modelSet) {
        super(dispatcher, modelSet);
    }

    @Override
    public void renderByItem(ItemStack stack, ItemDisplayContext context, PoseStack poseStack,
            MultiBufferSource buffers, int packedLight, int packedOverlay) {
        GasStack contents = GasTankItem.getContents(stack);
        if (!contents.isEmpty()) {
            int filledHeight = Mth.clamp(Mth.ceil((float) contents.getAmount() / GasTankItem.CAPACITY
                    * (WINDOW_BOTTOM - WINDOW_TOP)), 1, WINDOW_BOTTOM - WINDOW_TOP);
            int fillTop = WINDOW_BOTTOM - filledHeight;
            drawGas(poseStack, buffers, contents, fillTop, packedLight, packedOverlay);
        }
        drawFullSprite(poseStack, buffers, GLASS_TEXTURE, GLASS_Z, packedLight, packedOverlay);
        drawFullSprite(poseStack, buffers, FRAME_TEXTURE, FRAME_Z, packedLight, packedOverlay);
    }

    private static void drawGas(PoseStack poseStack, MultiBufferSource buffers, GasStack contents, int fillTop,
            int packedLight, int packedOverlay) {
        int tint = contents.getGas().tint();
        VertexConsumer consumer = buffers.getBuffer(RenderType.entityTranslucent(contents.getGas().texture()));
        Matrix4f pose = poseStack.last().pose();
        float minX = WINDOW_LEFT * PIXEL;
        float maxX = WINDOW_RIGHT * PIXEL;
        float minY = 1.0F - WINDOW_BOTTOM * PIXEL;
        float maxY = 1.0F - fillTop * PIXEL;
        float u0 = 0.0F;
        float u1 = (WINDOW_RIGHT - WINDOW_LEFT) * PIXEL;
        float v0 = (fillTop - WINDOW_TOP) * PIXEL;
        float v1 = (WINDOW_BOTTOM - WINDOW_TOP) * PIXEL;
        int red = tint >>> 16 & 0xFF;
        int green = tint >>> 8 & 0xFF;
        int blue = tint & 0xFF;
        addQuad(consumer, pose, minX, minY, maxX, maxY, CONTENTS_Z, u0, v0, u1, v1,
                red, green, blue, 255, packedLight, packedOverlay);
    }

    private static void drawFullSprite(PoseStack poseStack, MultiBufferSource buffers, ResourceLocation texture,
            float z, int packedLight, int packedOverlay) {
        addQuad(buffers.getBuffer(RenderType.entityTranslucent(texture)), poseStack.last().pose(),
                0.0F, 0.0F, 1.0F, 1.0F, z, 0.0F, 0.0F, 1.0F, 1.0F,
                255, 255, 255, 255, packedLight, packedOverlay);
    }

    private static void addQuad(VertexConsumer consumer, Matrix4f pose, float minX, float minY, float maxX,
            float maxY, float z, float u0, float v0, float u1, float v1, int red, int green, int blue,
            int alpha, int packedLight, int packedOverlay) {
        consumer.addVertex(pose, minX, maxY, z).setColor(red, green, blue, alpha).setUv(u0, v0)
                .setOverlay(packedOverlay).setLight(packedLight).setNormal(0.0F, 0.0F, 1.0F);
        consumer.addVertex(pose, maxX, maxY, z).setColor(red, green, blue, alpha).setUv(u1, v0)
                .setOverlay(packedOverlay).setLight(packedLight).setNormal(0.0F, 0.0F, 1.0F);
        consumer.addVertex(pose, maxX, minY, z).setColor(red, green, blue, alpha).setUv(u1, v1)
                .setOverlay(packedOverlay).setLight(packedLight).setNormal(0.0F, 0.0F, 1.0F);
        consumer.addVertex(pose, minX, minY, z).setColor(red, green, blue, alpha).setUv(u0, v1)
                .setOverlay(packedOverlay).setLight(packedLight).setNormal(0.0F, 0.0F, 1.0F);
    }

    private static ResourceLocation texture(String path) {
        return ResourceLocation.fromNamespaceAndPath(ModConstants.MOD_ID, "textures/" + path + ".png");
    }
}
