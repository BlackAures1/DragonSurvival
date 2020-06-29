package by.jackraidenph.dragonsurvival.models;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

public class DragonRenderer extends LivingRenderer<PlayerEntity, Dragon<PlayerEntity>> {
    public DragonRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new Dragon<>(), 1.0F);
    }

    public DragonRenderer() {
        super(Minecraft.getInstance().getRenderManager(), new Dragon<>(), 1.0F);
    }

    @Override
    public ResourceLocation getEntityTexture(PlayerEntity entity) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "textures/dragon.png");
    }


}
