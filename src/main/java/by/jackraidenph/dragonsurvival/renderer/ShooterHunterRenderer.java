package by.jackraidenph.dragonsurvival.renderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PillagerRenderer;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.util.ResourceLocation;

public class ShooterHunterRenderer extends PillagerRenderer {
    private static final ResourceLocation TEXTURE = new ResourceLocation("dragonsurvival", "textures/dragon_hunter.png");

    public ShooterHunterRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    public ResourceLocation getTextureLocation(PillagerEntity p_110775_1_) {
        return TEXTURE;
    }
}
