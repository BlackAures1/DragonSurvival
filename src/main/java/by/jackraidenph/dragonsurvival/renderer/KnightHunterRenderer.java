package by.jackraidenph.dragonsurvival.renderer;

import by.jackraidenph.dragonsurvival.entity.KnightHunter;
import by.jackraidenph.dragonsurvival.models.HunterModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.HeadLayer;
import net.minecraft.client.renderer.entity.layers.HeldItemLayer;
import net.minecraft.util.ResourceLocation;
@Deprecated
public class KnightHunterRenderer extends MobRenderer<KnightHunter, HunterModel<KnightHunter>> {
    private static final ResourceLocation TEXTURE = new ResourceLocation("dragonsurvival", "textures/dragon_knight.png");

    public KnightHunterRenderer(EntityRendererManager entityRendererManager) {
        super(entityRendererManager, new HunterModel<>(0.0F, 0.0F, 64, 64), 0.5F);
        addLayer(new HeadLayer<>(this));
        addLayer(new HeldItemLayer<>(this));
    }

    public ResourceLocation getTextureLocation(KnightHunter knightHunter) {
        return TEXTURE;
    }

    protected void scale(KnightHunter knightHunter, MatrixStack matrixStack, float p_225620_3_) {
        float f = 0.9375F;
        matrixStack.scale(f, f, f);
    }
}
