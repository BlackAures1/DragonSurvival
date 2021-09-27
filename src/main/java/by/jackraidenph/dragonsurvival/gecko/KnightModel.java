package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class KnightModel extends AnimatedGeoModel<Knight> {
    @Override
    public ResourceLocation getModelLocation(Knight object) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "geo/horseback_rider.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Knight object) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "textures/riders/dragon_knight_black.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Knight animatable) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon_knight.animations.json");
    }
}
