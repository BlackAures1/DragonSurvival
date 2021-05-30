package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
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

	private IBone neck;
	private IBone neck4;
	private IBone neck3;
	private IBone neck2;
	private IBone neck1;
	private IBone head;

	private boolean hasBones;

	public void setupBones() {
		if (hasBones || this.getAnimationProcessor().getModelRendererList().size() == 0)
			return;

		neck = this.getAnimationProcessor().getBone("Neck");
		neck4 = this.getAnimationProcessor().getBone("Neck4");
		neck3 = this.getAnimationProcessor().getBone("Neck3");
		neck2 = this.getAnimationProcessor().getBone("Neck2");
		neck1 = this.getAnimationProcessor().getBone("Neck1");
		head = this.getAnimationProcessor().getBone("Head");

		hasBones = true;
	}

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
    	ResourceLocation animLocation = new ResourceLocation(DragonSurvivalMod.MODID, "animations/dragon.animations.json");
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

	private int biteTicks;

	@Override
	public void setLivingAnimations(DragonEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
		super.setLivingAnimations(entity, uniqueID, customPredicate);
		
		if (customPredicate.getController() == null || !hasBones)
			return;
		
		// TODO Replace temp head turn with system that includes vertical
		PlayerEntity player = entity.getPlayer();
		if (!player.isSleeping()) {
			DragonStateProvider.getCap(player).ifPresent(playerStateHandler -> {
				/*IBone neck = this.getAnimationProcessor().getBone("Neck"); // rot(0, -22.5, 0)
				IBone neck4 = this.getAnimationProcessor().getBone("Neck4"); // rot(0, 0, -10)
				IBone neck3 = this.getAnimationProcessor().getBone("Neck3"); // rot(12.5, -15, 30), mov(-0.25, 0, 0)
				IBone neck2 = this.getAnimationProcessor().getBone("Neck2"); // rot(9.04, -5, 35.55), mov(0, 0.75, 0)
				IBone neck1 = this.getAnimationProcessor().getBone("Neck1"); // rot(5, -17.5, 30), mov(0.25, -0.25, 0)
				IBone head = this.getAnimationProcessor().getBone("Head"); // rot(-39.41, -35, 55), mov(2.25, -1.25, -1.25)*/
				// neck0: rot(-115, 0, 0), mov(-4, 16, -15)
				// neck1: rot(25, 0, 0), mov(-4, 16, -15)
				// neck2: rot(30, 0, 0), mov(-2.01, 27.92, -12.3163)
				// neck3: rot(30, 0, 0), mov(-2, 22.7551, -13.1526)
				// neck4: rot(10, 0, 0), mov(-3, 19.4825, -13.4911)
				// head: rot(20, 0, 0), mov(2.1, 37.4402, -12.953)
				float rotation = -1F * (((float)playerStateHandler.getMovementData().bodyYaw) - (float)playerStateHandler.getMovementData().headYaw) * (float)Math.PI / 180F;
				if (rotation > (float)Math.PI)
					rotation = (float)Math.PI;
				if (rotation < -(float)Math.PI)
					rotation = -(float)Math.PI;
				neck.setRotationY(neck.getRotationY() + (-0.125F * rotation));
				neck4.setRotationZ(neck4.getRotationZ() + (-1F * -0.0555556F * rotation));
				neck3.setRotationX(neck3.getRotationX() + ((rotation >= 0 ? -1F : 1F) * 0.06944F * rotation));
				neck3.setRotationY(neck3.getRotationY() + (-0.083333F * rotation));
				neck3.setRotationZ(neck3.getRotationZ() + (-1F * 0.166667F * rotation));

				neck3.setPositionX(neck3.getPositionX() + (-1F * -0.07957F * rotation));

				neck2.setRotationX(neck2.getRotationX() + ((rotation >= 0 ? -1F : 1F) * 0.0502222F * rotation));
				neck2.setRotationY(neck2.getRotationY() + (-0.0277778F * rotation));
				neck2.setRotationZ(neck2.getRotationZ() + (-1F * 0.1975F * rotation));

				neck2.setPositionY(neck2.getPositionY() + (-1F * (rotation >= 0 ? -1F : 1F) * 0.2387324F * rotation));

				neck1.setRotationX(neck1.getRotationX() + ((rotation >= 0 ? -1F : 1F) * 0.02777763F * rotation));
				neck1.setRotationY(neck1.getRotationY() + (-0.0972222F * rotation));
				neck1.setRotationZ(neck1.getRotationZ() + (-1F * 0.1666667F * rotation));

				neck1.setPositionX(neck1.getPositionX() + (-1F * 0.0795775F * rotation));
				neck1.setPositionY(neck1.getPositionY() + (-1F * (rotation >= 0 ? -1F : 1F) * -0.0795775F * rotation));

				head.setRotationX(head.getRotationX() + ((rotation >= 0 ? -1F : 1F) * -0.2189445F * rotation));
				head.setRotationY(head.getRotationY() + (-0.1944444F * rotation));
				head.setRotationZ(head.getRotationZ() + (-1F * 0.3055555F * rotation));
				head.setPositionX(head.getPositionX() + (-1F * 0.716197F * rotation));
				head.setPositionY(head.getPositionY() + (-1F * (rotation >= 0 ? -1F : 1F) * -0.397887F * rotation));
				head.setPositionZ(head.getPositionZ() + ((rotation >= 0 ? 1F : -1F) * -0.397887F * rotation));

				
				if (playerStateHandler.getMovementData().bite) {
					biteTicks++;
					
					
				}
				else if (biteTicks > 0) {
					biteTicks--;
					
					
				}
					
			});
		}
		
		
		
    	
		
	}
    
}
