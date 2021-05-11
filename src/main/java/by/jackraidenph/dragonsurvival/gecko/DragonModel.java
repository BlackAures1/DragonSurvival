package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.handlers.ClientEvents;
import by.jackraidenph.dragonsurvival.handlers.DragonSizeHandler;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3d;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.builder.Animation;
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
    
    
}
