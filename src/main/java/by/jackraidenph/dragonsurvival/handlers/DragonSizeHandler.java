package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.util.ConfigurationHandler;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
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
    		float height = ((float)health + 4.0F) / 20.0F; // 0.9 -> 2.2 (Config limit to 1.8)
    		float width = (3.0F * (float)health + 62.0F) / 260.0F; // 0.4 -> 0.7 (Config limit to 0.6)
    		float eyeHeight = (11.0F * (float)health + 54.0F) / 260.0F; // 0.8 -> 1.9 (Config limit to 1.62)
    		// Limit Values
    		if (!ConfigurationHandler.hitboxGrowsPastHuman.get()) {
    			if (height > 1.8F)
    				height = 1.8F;
    			if (width > 0.6F)
    				width = 0.6F;
    			if (eyeHeight > 1.62F)
    				eyeHeight = 1.62F;
    		}
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
    
    private static float calculateModifiedHeight(float height, Pose pose) {
    	if (pose == Pose.CROUCHING) {
			height *= 5.0F / 6.0F; 
		} else if (pose == Pose.SWIMMING || pose == Pose.FALL_FLYING || pose == Pose.SPIN_ATTACK) {
			height *= 2.0F / 3.0F;
		}
    	return height;
    }
    
    private static float calculateModifiedEyeHeight(float eyeHeight, Pose pose) {
    	if (pose == Pose.CROUCHING) {
    		eyeHeight *= 5.0F / 6.0F;
		} else if (pose == Pose.SWIMMING || pose == Pose.FALL_FLYING || pose == Pose.SPIN_ATTACK) {
			eyeHeight *= 2.0F / 3.0F;
		}
    	return eyeHeight;
    }
    
    private static Pose overridePose(PlayerEntity player) {
    	Pose overridePose = getOverridePose(player);
    	if (player.getForcedPose() != overridePose)
			player.setForcedPose(overridePose);
    	return overridePose;
    }
    
    private static Pose getOverridePose(PlayerEntity player) {
    	boolean swim = player.isInWaterOrBubble() && player.isAffectedByFluids(); // TODO This needs more work to avoid bobbing
		boolean fly = !player.isOnGround() && !player.isInWater() && player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).hasWings();
		boolean spin = player.isAutoSpinAttack();
		boolean crouch = player.isShiftKeyDown(); // TODO This might need more work
		if (fly)
			return Pose.FALL_FLYING;
		else if (swim)
			return Pose.SWIMMING;
		else if (spin)
			return Pose.SPIN_ATTACK;
		else if (crouch)
			return Pose.CROUCHING; // TODO Set crouching pose if block is above your head (going to need to do calculations based on pose sizes)
		return Pose.STANDING;
    }
    
    private static boolean wasDragon;
    
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void playerTick(TickEvent.PlayerTickEvent event) {
    	if (event.player == null || event.phase == TickEvent.Phase.END)
    		return;
    	DragonStateProvider.getCap(event.player).ifPresent(dragonStateHandler -> {
    		if (dragonStateHandler.isDragon()) {
    			overridePose(event.player);
    			if (!wasDragon) {
    				event.player.refreshDimensions();
    				wasDragon = true;
    			}
    		} else if (wasDragon) {
				event.player.setForcedPose(null);
				event.player.refreshDimensions();
				wasDragon = false;
			}
    	});
    }
}
