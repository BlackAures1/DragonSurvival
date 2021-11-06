package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DragonArmorModel extends AnimatedGeoModel<DragonEntity> {
    private ResourceLocation armorTexture = new ResourceLocation(DragonSurvivalMod.MODID, "textures/armor/empty_armor.png");

    @Override
    public ResourceLocation getModelLocation(DragonEntity object) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "geo/dragon_armor_model.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DragonEntity object) {
        return armorTexture;
    }

    public void setArmorTexture(ResourceLocation armorTexture) {
        this.armorTexture = armorTexture;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DragonEntity animatable) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.animations.json");
    }
}
