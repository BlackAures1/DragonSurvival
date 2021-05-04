package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.handlers.ClientEvents;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
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
            Vector3d playerMotion = player.getDeltaMovement();
            if (player.getPose() == Pose.SWIMMING)
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.swim_fast.animation.json");
            if (player.isInWaterOrBubble() && (playerMotion.x != 0 || playerMotion.z != 0))
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.swim.animation.json");
            if (player.isShiftKeyDown()) {
                if ((playerMotion.z() != 0 || playerMotion.x() != 0) && player.animationSpeed != 0f) {
                    return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.sneaking.animation.json");
                } else if (ClientEvents.dragonsDigging.getOrDefault(dragonEntity.player, false))
                    return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.digging_sneaking.animation.json");
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.sneaking_stand.animation.json");
            }

            boolean flyingEnabled = ClientEvents.dragonsFlying.getOrDefault(player.getId(), false);
            if ((flyingEnabled || player.abilities.flying) && !player.isInWater() && !player.isOnGround() && player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).hasWings())
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.fly.animation.json");

            if (player.isSprinting())
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.run.animation.json");
            if (player.swinging && player.getAttackStrengthScale(-3f) != 1) {
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.bite.animation.json");
            }
            if (ClientEvents.dragonsJumpingTicks.getOrDefault(dragonEntity.player, 0) > 0)
                return new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.jump.animation.json");
            //motion variables alone are not reliable
            if ((playerMotion.z != 0 || playerMotion.x != 0) && player.animationSpeed != 0f) {
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
