package by.jackraidenph.dragonsurvival.gecko;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;

public class KnightRenderer extends GeoEntityRenderer<Knight> {
    public KnightRenderer(EntityRendererManager renderManager, AnimatedGeoModel<Knight> modelProvider) {
        super(renderManager, modelProvider);
        addLayer(new GeoLayerRenderer<Knight>(this) {
            @Override
            public void render(MatrixStack matrixStackIn, IRenderTypeBuffer bufferIn, int packedLightIn, Knight entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
//                ItemStack main = entitylivingbaseIn.getMainHandItem();
//                matrixStackIn.pushPose();
//                matrixStackIn.translate(0.4,1.7,-0.7);
//                Minecraft.getInstance().getItemInHandRenderer().renderItem(entitylivingbaseIn, main, ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, true, matrixStackIn, bufferIn, packedLightIn);
//                matrixStackIn.popPose();
//                ItemStack second = entitylivingbaseIn.getOffhandItem();
//                matrixStackIn.pushPose();
//                matrixStackIn.translate(-0.5,2.8,-0.4);
//                matrixStackIn.mulPose(Vector3f.YP.rotationDegrees(90));
//                Minecraft.getInstance().getItemInHandRenderer().renderItem(entitylivingbaseIn, second, ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, true, matrixStackIn, bufferIn, packedLightIn);
//                matrixStackIn.popPose();
            }
        });
    }
}
