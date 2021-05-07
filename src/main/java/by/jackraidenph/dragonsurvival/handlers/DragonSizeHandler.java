package by.jackraidenph.dragonsurvival.handlers;

import java.util.HashMap;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.util.ConfigurationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class DragonSizeHandler {
	@SubscribeEvent
    public static void getDragonSize(EntityEvent.Size event) {
    	if (!(event.getEntity() instanceof PlayerEntity))
    		return;
    	PlayerEntity player = (PlayerEntity)event.getEntity();
    	DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
    		if (!dragonStateHandler.isDragon())
    			return;
    		float health = (float)player.getAttribute(Attributes.MAX_HEALTH).getValue();
    		// Calculate base values
    		float height = calculateDragonHeight(health, ConfigurationHandler.hitboxGrowsPastHuman.get());
    		float width = calculateDragonWidth(health, ConfigurationHandler.hitboxGrowsPastHuman.get());
    		float eyeHeight = calculateDragonEyeHeight(health, ConfigurationHandler.hitboxGrowsPastHuman.get());
    		// Handle Pose stuff
    		Pose overridePose = overridePose(player);
    		height = calculateModifiedHeight(height, overridePose);
    		eyeHeight = calculateModifiedEyeHeight(eyeHeight, overridePose);
    		// Apply changes
    		if (ConfigurationHandler.sizeChangesHitbox.get()) {
    			event.setNewEyeHeight(eyeHeight);
        		event.setNewSize(new EntitySize(width, height, false));
    		}
        });
    }
    
	private static float calculateDragonHeight(float health, boolean growsPastHuman) {
		float height = ((float)health + 4.0F) / 20.0F; // 0.9 -> 2.2
		if (!growsPastHuman && height > 1.8F)
			height = 1.8F;
		return height;
	}
	
	private static float calculateDragonWidth(float health, boolean growsPastHuman) {
		float width = (3.0F * (float)health + 62.0F) / 260.0F; // 0.4 -> 0.7
		if (!growsPastHuman && width > 0.6F)
			width = 0.6F;
		return width;
	}
	
	private static float calculateDragonEyeHeight(float health, boolean growsPastHuman) {
		float eyeHeight = (11.0F * (float)health + 54.0F) / 260.0F; // 0.8 -> 1.9
		if (!growsPastHuman && eyeHeight > 1.62F)
			eyeHeight = 1.62F;
		return eyeHeight;
	}
	
    private static float calculateModifiedHeight(float height, Pose pose) {
    	if (pose == Pose.CROUCHING) {
			height *= 5.0F / 6.0F; 
		} else if (pose == Pose.SWIMMING || pose == Pose.FALL_FLYING || pose == Pose.SPIN_ATTACK) {
			height *= 7.0F / 12.0F;
		}
    	return height;
    }
    
    private static float calculateModifiedEyeHeight(float eyeHeight, Pose pose) {
    	if (pose == Pose.CROUCHING) {
    		eyeHeight *= 5.0F / 6.0F;
		} else if (pose == Pose.SWIMMING || pose == Pose.FALL_FLYING || pose == Pose.SPIN_ATTACK) {
			eyeHeight *= 7.0F / 12.0F;
		}
    	return eyeHeight;
    }
    
    public static boolean canPoseFit(PlayerEntity player, Pose pose) {
    	float health = (float)player.getAttribute(Attributes.MAX_HEALTH).getValue();
		float height = calculateModifiedHeight(calculateDragonHeight(health, ConfigurationHandler.hitboxGrowsPastHuman.get()), pose);
		float width = calculateDragonWidth(health, ConfigurationHandler.hitboxGrowsPastHuman.get());
		return player.level.getBlockCollisions(null, new AxisAlignedBB(
				player.position().subtract(width * 0.5D, 0.0D, width * 0.5D), 
				player.position().add(width * 0.5D, height, width * 0.5D)))
		.count() == 0;
    }
    
    private static Pose overridePose(PlayerEntity player) {
    	Pose overridePose = getOverridePose(player);
    	if (player.getForcedPose() != overridePose)
			player.setForcedPose(overridePose);
    	return overridePose;
    }
    
    private static Pose getOverridePose(PlayerEntity player) {
    	boolean swimming = player.isInWaterOrBubble() && player.isAffectedByFluids(); // TODO This needs more work to avoid bobbing
		boolean flying = !player.isOnGround() && !player.isInWater() && FlightController.wingsEnabled && player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).hasWings() ;
		boolean spinning = player.isAutoSpinAttack();
		boolean crouching = player.isShiftKeyDown(); // TODO This might need more work
		if (flying)
			return Pose.FALL_FLYING;
		else if (swimming)
			return Pose.SWIMMING;
		else if (spinning)
			return Pose.SPIN_ATTACK;
		else {
			if (crouching || (!canPoseFit(player, Pose.STANDING) && canPoseFit(player, Pose.CROUCHING)))
				return Pose.CROUCHING;
		}
		return Pose.STANDING;
    }
    
    private static HashMap<Integer, Boolean> wasDragon = new HashMap<Integer, Boolean>();
    
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void playerTick(TickEvent.PlayerTickEvent event) {
    	if (event.player == null || event.phase == TickEvent.Phase.END || Minecraft.getInstance().getCameraEntity() != event.player)
    		return;
    	DragonStateProvider.getCap(event.player).ifPresent(dragonStateHandler -> {
    		if (dragonStateHandler.isDragon()) {
    			overridePose(event.player);
    			event.player.refreshDimensions(); // FIXME Yowch is there a way to implement this without running it every tick?
    			if (!wasDragon.getOrDefault(event.player.getId(), false)) {
    				wasDragon.put(event.player.getId(), true);
    				
    			}
    		} else if (wasDragon.getOrDefault(event.player.getId(), false)) {
				event.player.setForcedPose(null);
				event.player.refreshDimensions();
				wasDragon.put(event.player.getId(), false);
			}
    	});
    }
}
