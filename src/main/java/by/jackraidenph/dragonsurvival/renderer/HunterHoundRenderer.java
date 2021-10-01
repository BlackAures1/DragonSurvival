package by.jackraidenph.dragonsurvival.renderer;

import by.jackraidenph.dragonsurvival.entity.HunterHound;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.util.ResourceLocation;

import java.util.Arrays;
import java.util.List;

public class HunterHoundRenderer extends WolfRenderer {
    static final List<ResourceLocation> SKINS = Arrays.asList(new ResourceLocation("dragonsurvival", "textures/hounds/dragon_hound_1.png"), new ResourceLocation("dragonsurvival", "textures/hounds/dragon_hound_2.png"), new ResourceLocation("dragonsurvival", "textures/hounds/dragon_hound_3.png"), new ResourceLocation("dragonsurvival", "textures/hounds/dragon_hound_4.png"), new ResourceLocation("dragonsurvival", "textures/hounds/dragon_hound_5.png"), new ResourceLocation("dragonsurvival", "textures/hounds/dragon_hound_6.png"), new ResourceLocation("dragonsurvival", "textures/hounds/dragon_hound_7.png"), new ResourceLocation("dragonsurvival", "textures/hounds/dragon_hound_8.png"));

    static final ResourceLocation HECTOR_SKIN = new ResourceLocation("dragonsurvival", "textures/hounds/dragon_hound_hector.png");

    public HunterHoundRenderer(EntityRendererManager rendererManager) {
        super(rendererManager);
    }

    public ResourceLocation getTextureLocation(WolfEntity entity) {
        if (entity.getDisplayName().getString().equals("Hector") || entity.getDisplayName().getString().equals("Гектор"))
            return HECTOR_SKIN;
        return SKINS.get(entity.getEntityData().get(HunterHound.variety));
    }
}

