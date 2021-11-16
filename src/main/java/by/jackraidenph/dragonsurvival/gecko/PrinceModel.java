package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PrinceModel extends AnimatedGeoModel<Prince> {
    @Override
    public ResourceLocation getModelLocation(Prince object) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "geo/horseback_rider.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Prince object) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "textures/riders/dragon_prince.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Prince animatable) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "animations/horseback_rider.animations.json");
    }
}
