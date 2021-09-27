package by.jackraidenph.dragonsurvival.gecko;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class PrinceRenderer extends GeoEntityRenderer<Prince> {
    public PrinceRenderer(EntityRendererManager renderManager, AnimatedGeoModel<Prince> modelProvider) {
        super(renderManager, modelProvider);
    }
}
