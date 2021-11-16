package by.jackraidenph.dragonsurvival.renderer;

import by.jackraidenph.dragonsurvival.entity.Squire;
import by.jackraidenph.dragonsurvival.models.HunterModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;

public class SquireHunterRenderer extends MobRenderer<Squire, HunterModel<Squire>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("dragonsurvival", "textures/dragon_squire.png");

    public SquireHunterRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new HunterModel<>(0.0F, 0.0F, 64, 64), 0.5F);
        addLayer(new HeadLayer<>(this));
        addLayer(new HeldItemLayer<>(this));
    }

    public ResourceLocation getTextureLocation(Squire squireHunter) {
        return TEXTURE;
    }

    protected void scale(Squire squire, MatrixStack matrixStack, float p_225620_3_) {
        float f = 0.9375F;
        matrixStack.scale(f, f, f);
    }
}
