package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.handlers.ClientEvents;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
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
        final PlayerEntity player = dragonEntity.getPlayer();
        if (player != null) {
            Vec3d playerMotion = player.getMotion();
            if (player.getPose() == Pose.SWIMMING)
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.swim_fast.animation.json");
            if (player.isInWaterOrBubbleColumn() && (playerMotion.x != 0 || playerMotion.z != 0))
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.swim.animation.json");
            if (player.isSneaking()) {
                if ((playerMotion.getZ() != 0 || playerMotion.getX() != 0) && player.limbSwingAmount != 0f) {
                    return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.sneaking.animation.json");
                } else if (ClientEvents.dragonsDigging.getOrDefault(dragonEntity.player, false))
                    return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.digging_sneaking.animation.json");
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.sneaking_stand.animation.json");
            }

            boolean flyingEnabled = ClientEvents.dragonsFlying.getOrDefault(player.getEntityId(), false);
            if ((flyingEnabled || player.abilities.isFlying) && !player.isInWater() && !player.onGround && player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).hasWings())
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.fly.animation.json");

            if (player.isSprinting())
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.run.animation.json");
            if (player.isSwingInProgress && player.getCooledAttackStrength(-3f) != 1) {
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.bite.animation.json");
            }
            if (ClientEvents.dragonsJumpingTicks.getOrDefault(dragonEntity.player, 0) > 0)
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.jump.animation.json");
            //motion variables alone are not reliable
            if ((playerMotion.z != 0 || playerMotion.x != 0) && player.limbSwingAmount != 0f) {
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.walk.animation.json");
            }
            if (player.isSleeping())
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.sleep.animation.json");
            if (ClientEvents.dragonsDigging.getOrDefault(dragonEntity.player, false))
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.digging.animation.json");
        }
        return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.stand.animation.json");
    }
}
