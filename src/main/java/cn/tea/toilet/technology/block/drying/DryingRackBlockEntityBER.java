package cn.tea.toilet.technology.block.drying;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
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

    private static final float ITEM_HEIGHT = 1.05f;
    private static final float ITEM_SCALE = 0.45f;

    private static final float[][] ITEM_POSITIONS = {
            {0.25f, 0.25f},
            {0.25f, 0.75f},
            {0.75f, 0.25f},
            {0.75f, 0.75f}
    };

    private static final float[] ITEM_ROTATIONS = {
            45.0f,
            -45.0f,
            135.0f,
            -135.0f
    };

    @Override
    public void render(@NotNull DryingRackBlockEntity blockEntity, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        BlockState state = blockEntity.getBlockState();
        DirectionProperty facingProperty = HorizontalDirectionalBlock.FACING;
        if (!state.hasProperty(facingProperty)) {
            return;
        }

        Direction facing = state.getValue(facingProperty);
        int facingIndex = facing.get2DDataValue();

        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

        for (int i = 0; i < 4; i++) {
            ItemStack itemStack = blockEntity.itemHandler.getStackInSlot(i);
            if (itemStack.isEmpty()) {
                continue;
            }

            float[] pos = ITEM_POSITIONS[i];
            float x = pos[0];
            float z = pos[1];

            float rotatedX = x;
            float rotatedZ = z;

            switch (facingIndex) {
                case 1:
                    rotatedX = z;
                    rotatedZ = 1.0f - x;
                    break;
                case 2:
                    rotatedX = 1.0f - x;
                    rotatedZ = 1.0f - z;
                    break;
                case 3:
                    rotatedX = 1.0f - z;
                    rotatedZ = x;
                    break;
            }

            poseStack.pushPose();
            poseStack.translate(rotatedX, ITEM_HEIGHT, rotatedZ);

            poseStack.mulPose(Axis.YP.rotationDegrees(ITEM_ROTATIONS[i]));
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0f));

            poseStack.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);

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