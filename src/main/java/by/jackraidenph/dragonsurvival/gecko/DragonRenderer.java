package by.jackraidenph.dragonsurvival.gecko;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class DragonRenderer extends GeoEntityRenderer<DragonEntity> {
    public DragonRenderer(EntityRendererManager renderManager, AnimatedGeoModel<DragonEntity> modelProvider) {
        super(renderManager, modelProvider);
    }

//    @Override
//    public void render(DragonEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn) {
//        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
//    }
}
