package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.item.DyeColor;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class PrincessModel extends AnimatedGeoModel<Princess> {
    @Override
    public ResourceLocation getModelLocation(Princess object) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "geo/horseback_rider.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Princess object) {
        DyeColor dyeColor = DyeColor.byId(object.getColor());
        switch (dyeColor) {
            case RED:
                return new ResourceLocation(DragonSurvivalMod.MODID, "textures/riders/princess_red.png");
            case BLUE:
                return new ResourceLocation(DragonSurvivalMod.MODID, "textures/riders/princess_blue.png");
            case PURPLE:
                return new ResourceLocation(DragonSurvivalMod.MODID, "textures/riders/princess_purple.png");
            case WHITE:
                return new ResourceLocation(DragonSurvivalMod.MODID, "textures/riders/princess_white.png");
            case YELLOW:
                return new ResourceLocation(DragonSurvivalMod.MODID, "textures/riders/princess_yellow.png");
            case BLACK:
                return new ResourceLocation(DragonSurvivalMod.MODID, "textures/riders/princess_black.png");
        }
        return null;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Princess animatable) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "animations/horseback_rider.animations.json");
    }
}
