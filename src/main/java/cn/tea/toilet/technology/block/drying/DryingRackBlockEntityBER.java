package cn.tea.toilet.technology.block.drying;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.NotNull;

public class DryingRackBlockEntityBER implements BlockEntityRenderer<DryingRackBlockEntity> {

    private final ItemRenderer itemRenderer;

    public DryingRackBlockEntityBER(BlockEntityRendererProvider.Context context) {
        this.itemRenderer = context.getItemRenderer();
    }

    @Override
    public void render(@NotNull DryingRackBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = blockEntity.getBlockState();
        DirectionProperty facingProperty = HorizontalDirectionalBlock.FACING;
        if (!state.hasProperty(facingProperty)) {
            return;
        }

        Direction facing = state.getValue(facingProperty);

        for (int i = 0; i < DryingRackConfig.SLOT_COUNT; i++) {
            ItemStack itemStack = blockEntity.itemHandler.getStackInSlot(i);
            if (itemStack.isEmpty()) {
                continue;
            }

            float[] pos = DryingRackConfig.ITEM_POSITIONS[i];
            double[] rotatedPos = DryingRackConfig.rotateTo(pos[0], pos[1], facing);
            float rotatedX = (float) rotatedPos[0];
            float rotatedZ = (float) rotatedPos[1];

            poseStack.pushPose();
            poseStack.translate(rotatedX, DryingRackConfig.ITEM_HEIGHT, rotatedZ);

            poseStack.mulPose(Axis.YP.rotationDegrees(DryingRackConfig.ITEM_ROTATIONS[i]));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));

            poseStack.scale(DryingRackConfig.ITEM_SCALE, DryingRackConfig.ITEM_SCALE, DryingRackConfig.ITEM_SCALE);

            BakedModel model = itemRenderer.getModel(itemStack, null, null, 0);
            itemRenderer.render(
                    itemStack,
                    ItemDisplayContext.NONE,
                    false,
                    poseStack,
                    bufferSource,
                    packedLight,
                    packedOverlay,
                    model
            );

            poseStack.popPose();
        }
    }
}