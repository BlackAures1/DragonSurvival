package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DragonArmorModel extends AnimatedGeoModel<DragonEntity> {
    @Override
    public ResourceLocation getModelLocation(DragonEntity object) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "geo/dragon_armor_model.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DragonEntity object) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "armor.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DragonEntity animatable) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.animations.json");
    }
}
