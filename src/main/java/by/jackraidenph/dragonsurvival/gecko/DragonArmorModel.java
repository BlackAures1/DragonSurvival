package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.AnimationProcessor;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import javax.annotation.Nullable;

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

    @Override
    public void setLivingAnimations(DragonEntity entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        PlayerEntity player = entity.getPlayer();
        if (!player.isSleeping() && !player.isPassenger()) {
            AnimationProcessor animationProcessor = getAnimationProcessor();
            IBone neck = animationProcessor.getBone("Neck");
            IBone neck1 = animationProcessor.getBone("Neck1");
            IBone neck2 = animationProcessor.getBone("Neck2");
            IBone neck3 = animationProcessor.getBone("Neck3");
            IBone neck4 = animationProcessor.getBone("Neck4");
            IBone head = animationProcessor.getBone("Head");
            DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
                float rotation = -1F * (((float) dragonStateHandler.getMovementData().bodyYaw) - (float) dragonStateHandler.getMovementData().headYaw) * (float) Math.PI / 180F;
                if (rotation > (float) Math.PI)
                    rotation = (float) Math.PI;
                if (rotation < -(float) Math.PI)
                    rotation = -(float) Math.PI;
                neck.setRotationY(-0.125F * rotation);
                neck4.setRotationZ(-1F * -0.0555556F * rotation);
                neck3.setRotationX((rotation >= 0 ? -1F : 1F) * 0.06944F * rotation + 0.523599F);
                neck3.setRotationY(-0.083333F * rotation);
                neck3.setRotationZ(-1F * 0.166667F * rotation);
                neck3.setPositionX(-1F * -0.07957F * rotation);
                neck2.setRotationX((rotation >= 0 ? -1F : 1F) * 0.0502222F * rotation + 0.523599F);
                neck2.setRotationY(-0.0277778F * rotation);
                neck2.setRotationZ(-1F * 0.1975F * rotation);
                neck2.setPositionY(-1F * (rotation >= 0 ? -1F : 1F) * 0.2387324F * rotation);
                neck1.setRotationX((rotation >= 0 ? -1F : 1F) * 0.02777763F * rotation + 0.174533F);
                neck1.setRotationY(-0.0972222F * rotation);
                neck1.setRotationZ(-1F * 0.1666667F * rotation);
                neck1.setPositionX(-1F * 0.0795775F * rotation);
                neck1.setPositionY(-1F * (rotation >= 0 ? -1F : 1F) * -0.0795775F * rotation);
                head.setRotationX((rotation >= 0 ? -1F : 1F) * -0.2189445F * rotation + 0.349066F);
                head.setRotationY(-0.1944444F * rotation);
                head.setRotationZ(-1F * 0.3055555F * rotation);
                head.setPositionX(-1F * 0.716197F * rotation);
                head.setPositionY(-1F * (rotation >= 0 ? -1F : 1F) * -0.397887F * rotation);
                head.setPositionZ((rotation >= 0 ? 1F : -1F) * -0.397887F * rotation);
            });
        }
    }
}
