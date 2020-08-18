package by.jackraidenph.dragonsurvival.renderer;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.entity.MagicalPredatorEntity;
import by.jackraidenph.dragonsurvival.models.MagicalPredatorModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MagicalPredatorRenderer extends MobRenderer<MagicalPredatorEntity, MagicalPredatorModel> {

    public static List<ResourceLocation> MAGICAL_BEAST_TEXTURES = new ArrayList<>(Arrays.asList(
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_dark.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_dark_broken.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_grass.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_gray.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_green.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_jungle.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_jungle_flowers.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_light.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_sand.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_zombie.png")
    ));

    public MagicalPredatorRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new MagicalPredatorModel(RenderType::getEntityTranslucent), 0.66F);
    }

    @Override
    protected void preRenderCallback(MagicalPredatorEntity entitylivingbaseIn, MatrixStack matrixStackIn, float partialTickTime) {
        this.shadowSize = entitylivingbaseIn.size / entitylivingbaseIn.getHeight() / 1.44F;
        float scale = entitylivingbaseIn.size / entitylivingbaseIn.getHeight();
        matrixStackIn.scale(scale, scale, scale);
    }


    @Override
    public ResourceLocation getEntityTexture(MagicalPredatorEntity entity) {
        return MAGICAL_BEAST_TEXTURES.get(entity.type);
    }
}
