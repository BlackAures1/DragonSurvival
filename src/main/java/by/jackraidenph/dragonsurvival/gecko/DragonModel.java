package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.handlers.ClientEvents;
import net.minecraft.entity.Pose;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DragonModel extends AnimatedGeoModel<DragonEntity> {

    private ResourceLocation currentTexture = new ResourceLocation(DragonSurvivalMod.MODID, "textures/dragon/cave_newborn.png");

    @Override
    public ResourceLocation getModelLocation(DragonEntity dragonEntity) {
        return new ResourceLocation(DragonSurvivalMod.MODID, "geo/dragon_model.geo.json");
    }

    public void setCurrentTexture(ResourceLocation currentTexture) {
        this.currentTexture = currentTexture;
    }

    @Override
    public ResourceLocation getTextureLocation(DragonEntity dragonEntity) {
        return currentTexture;
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DragonEntity dragonEntity) {
        Vec3d vec3d = dragonEntity.player.getMotion();
        if (dragonEntity.player.getPose() == Pose.SWIMMING)
            return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.swim_fast.animation.json");
        if (dragonEntity.player.isInWaterOrBubbleColumn() && (vec3d.x != 0 || vec3d.z != 0))
            return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.swim.animation.json");
        if (dragonEntity.player.isSneaking()) {
            return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.sneaking.animation.json");
        }
        //TODO jumping
//        if ()
//            return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.jump.animation.json");
        boolean flyingEnabled = ClientEvents.dragonsFlying.getOrDefault(dragonEntity.player.getEntityId(), false);
        if (flyingEnabled && !dragonEntity.player.isInWater() && !dragonEntity.player.onGround && dragonEntity.player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).hasWings())
            return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.fly.animation.json");

        if (dragonEntity.player.isSprinting())
            return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.run.animation.json");
        //todo too slow animation
        if (dragonEntity.player.getCooledAttackStrength(-3f) != 1) {
            return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.bite.animation.json");
        }
        if (vec3d.z != 0 || vec3d.x != 0)
            return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.walk.animation.json");
        if (dragonEntity.player.isSleeping())
            return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.sleep.animation.json");
        return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.stand.animation.json");
    }
}
