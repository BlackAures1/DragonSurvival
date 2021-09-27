package by.jackraidenph.dragonsurvival.gecko;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.util.math.vector.Vector3f;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.util.RenderUtils;

public class KnightRenderer extends GeoEntityRenderer<Knight> {
    public KnightRenderer(EntityRendererManager renderManager, AnimatedGeoModel<Knight> modelProvider) {
        super(renderManager, modelProvider);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, IVertexBuilder bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("left_item")) {
            stack.pushPose();
            RenderUtils.moveToPivot(bone, stack);
            stack.mulPose(Vector3f.ZP.rotationDegrees(180));
            stack.translate(0.0, -0.3, -0.5);
            Minecraft.getInstance().getItemRenderer().renderStatic(mainHand, ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, packedLightIn, packedOverlayIn, stack, rtb);
            stack.popPose();
            bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
        } else if (bone.getName().equals("right_item")) {
            stack.pushPose();
            RenderUtils.moveToPivot(bone, stack);
            stack.translate(-0.05, 0.1, 0.1);
            stack.mulPose(Vector3f.XP.rotationDegrees(90));
            Minecraft.getInstance().getItemRenderer().renderStatic(offHand, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, packedLightIn, packedOverlayIn, stack, rtb);
            stack.popPose();
            bufferIn = rtb.getBuffer(RenderType.entityTranslucent(whTexture));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
