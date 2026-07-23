package cn.tea.toilet.technology.block.toilet;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

/**
 * 马桶方块实体渲染器（合并版）
 *
 * 替代旧的 PremiumToiletBlockEntityBER / NetheriteToiletBlockEntityBER（两份代码逐字节相同），
 * 通过泛型上界 AbstractToiletBlockEntity 复用同一份渲染逻辑：
 * - 渲染流体静水面（y=0.85f）
 * - 颜色取自 IClientFluidTypeExtensions 的 tint
 * - 使用 RenderType.translucentMovingBlock 支持半透明流体
 *
 * 注册方式见 ToiletTechnologyClient：
 *   BlockEntityRenderers.register(ModBlockEntities.PREMIUM_TOILET.get(), ToiletBlockEntityBER::new);
 *   BlockEntityRenderers.register(ModBlockEntities.NETHERITE_TOILET.get(), ToiletBlockEntityBER::new);
 */
public class ToiletBlockEntityBER<T extends AbstractToiletBlockEntity> implements BlockEntityRenderer<T> {

    public ToiletBlockEntityBER(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(@NotNull T blockEntity, float partialTick, @NotNull PoseStack poseStack,
                       @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        FluidStack fluidStack = blockEntity.getFluid();
        if (fluidStack.isEmpty()) return;

        Fluid fluid = fluidStack.getFluid();
        IClientFluidTypeExtensions clientExtensions = IClientFluidTypeExtensions.of(fluid);

        ResourceLocation stillTexture = clientExtensions.getStillTexture();

        TextureAtlasSprite sprite = Minecraft.getInstance().getModelManager()
                .getAtlas(InventoryMenu.BLOCK_ATLAS)
                .getSprite(stillTexture);

        int color = clientExtensions.getTintColor();
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        float a = 1.0f;

        float y = 0.85f;

        poseStack.pushPose();
        VertexConsumer buffer = bufferSource.getBuffer(RenderType.translucentMovingBlock());
        Matrix4f matrix = poseStack.last().pose();

        float uMin = sprite.getU0();
        float uMax = sprite.getU1();
        float vMin = sprite.getV0();
        float vMax = sprite.getV1();

        buffer.addVertex(matrix, 0, y, 0).setColor(r, g, b, a).setUv(uMin, vMin).setOverlay(packedOverlay).setLight(packedLight).setNormal(poseStack.last(), 0, 1, 0);
        buffer.addVertex(matrix, 0, y, 1).setColor(r, g, b, a).setUv(uMin, vMax).setOverlay(packedOverlay).setLight(packedLight).setNormal(poseStack.last(), 0, 1, 0);
        buffer.addVertex(matrix, 1, y, 1).setColor(r, g, b, a).setUv(uMax, vMax).setOverlay(packedOverlay).setLight(packedLight).setNormal(poseStack.last(), 0, 1, 0);
        buffer.addVertex(matrix, 1, y, 0).setColor(r, g, b, a).setUv(uMax, vMin).setOverlay(packedOverlay).setLight(packedLight).setNormal(poseStack.last(), 0, 1, 0);

        poseStack.popPose();
    }
}
