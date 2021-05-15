package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.file.AnimationFile;
import software.bernie.geckolib3.geo.exception.GeckoLibException;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.resource.GeckoLibCache;

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
	public Animation getAnimation(String name, IAnimatable animatable) {
    	DragonEntity dragonEntity = (DragonEntity)animatable;
    	ResourceLocation animLocation = new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.stand.animation.json");
    	final PlayerEntity player = dragonEntity.getPlayer();
    	if (player != null) {
    		switch (name) {
	    		case "animation.dragon.idle":
	    			animLocation = new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.stand.animation.json");
	    			break;
	    		case "animation.dragon.walk":
	    			animLocation = new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.walk.animation.json");
	    			break;
	    		case "animation.dragon.run":
	    			animLocation = new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.run.animation.json");
	    			break;
	    		case "animation.dragon.sneak":
	    			animLocation = new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.sneaking_stand.animation.json");
	    			break;
	    		case "animation.dragon.sneak_walk":
	    			animLocation = new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.sneaking.animation.json");
	    			break;
	    		case "animation.dragon.jump":
	    			animLocation = new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.jump.animation.json");
	    			break;
	    		case "animation.dragon.fly_slow":
	    			animLocation = new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.fly.animation.json");
	    			break;
	    		case "animation.dragon.bite":
	    			animLocation = new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.bite.animation.json");
	    			break;
	    		case "animation.dragon.dig":
	    			animLocation = new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.digging.animation.json");
	    			break;
	    		case "animation.dragon.dig_sneak":
	    			animLocation = new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.digging_sneaking.animation.json");
	    			break;
	    		case "animation.dragon.swim":
	    			animLocation = new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.swim.animation.json");
	    			break;
	    		case "animation.dragon.swim_fast":
	    			animLocation = new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.swim_fast.animation.json");
	    			break;
	    		case "animation.dragon.sleep":
	    			animLocation = new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.sleep.animation.json");
	    			break;
    		}
    	}
    	AnimationFile animation = GeckoLibCache.getInstance().getAnimations().get(animLocation);
    	if (animation == null) {
			throw new GeckoLibException(animLocation, "Could not find animation file. Please double check name.");
		}
		return animation.getAnimation(name);
	}

	@Override
	public ResourceLocation getAnimationFileLocation(DragonEntity animatable) {
		return null;
	}
    
	@Override
	public void setLivingAnimations(DragonEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		
		// TODO Replace temp head turn with system that includes vertical
		PlayerEntity player = entity.getPlayer();
		if (!player.isSleeping()) {
			DragonStateProvider.getCap(player).ifPresent(playerStateHandler -> {
				IBone neck0 = this.getAnimationProcessor().getBone("NeckandHead"); // rot(0, -22.5, 0)
				IBone neck1 = this.getAnimationProcessor().getBone("NeckandMain"); // rot(0, 0, -10)
				IBone neck2 = this.getAnimationProcessor().getBone("Neckand_3"); // rot(12.5, -15, 30), mov(-0.25, 0, 0)
				IBone neck3 = this.getAnimationProcessor().getBone("Neckand_2"); // rot(9.04, -5, 35.55), mov(0, 0.75, 0)
				IBone neck4 = this.getAnimationProcessor().getBone("Neckand_1"); // rot(5, -17.5, 30), mov(0.25, -0.25, 0)
				IBone head = this.getAnimationProcessor().getBone("Head"); // rot(-39.41, -35, 55), mov(2.25, -1.25, -1.25)
				// neck0: rot(-115, 0, 0), mov(-4, 16, -15)
				// neck1: rot(25, 0, 0), mov(-4, 16, -15)
				// neck2: rot(30, 0, 0), mov(-2.01, 27.92, -12.3163)
				// neck3: rot(30, 0, 0), mov(-2, 22.7551, -13.1526)
				// neck4: rot(10, 0, 0), mov(-3, 19.4825, -13.4911)
				// head: rot(20, 0, 0), mov(2.1, 37.4402, -12.953)
				float rotation = -1F * (((float)playerStateHandler.getMovementData().bodyYaw) - player.yHeadRot) * (float)Math.PI / 180F;
				if (rotation > (float)Math.PI)
					rotation = (float)Math.PI;
				if (rotation < -(float)Math.PI)
					rotation = -(float)Math.PI;
				neck0.setRotationY(-0.125F * rotation);
				neck1.setRotationZ(-1F * -0.0555556F * rotation);
				neck2.setRotationX((rotation >= 0 ? -1F : 1F) * 0.06944F * rotation + 0.523599F);
				neck2.setRotationY(-0.083333F * rotation);
				neck2.setRotationZ(-1F * 0.166667F * rotation);
				neck2.setPositionX(-1F * -0.07957F * rotation);
				neck3.setRotationX((rotation >= 0 ? -1F : 1F) * 0.0502222F * rotation + 0.523599F);
				neck3.setRotationY(-0.0277778F * rotation);
				neck3.setRotationZ(-1F * 0.1975F * rotation);
				neck3.setPositionY(-1F * (rotation >= 0 ? -1F : 1F) * 0.2387324F * rotation);
				neck4.setRotationX((rotation >= 0 ? -1F : 1F) * 0.02777763F * rotation + 0.174533F);
				neck4.setRotationY(-0.0972222F * rotation);
				neck4.setRotationZ(-1F * 0.1666667F * rotation);
				neck4.setPositionX(-1F * 0.0795775F * rotation);
				neck4.setPositionY(-1F * (rotation >= 0 ? -1F : 1F) * -0.0795775F * rotation);
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
