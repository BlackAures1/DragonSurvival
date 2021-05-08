package by.jackraidenph.dragonsurvival.handlers;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.util.ConfigurationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.StringTextComponent;
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
    		player.sendMessage(new StringTextComponent(("Tick1 for " + player.getDisplayName().getString() + ": hp(" + health + "), p(" + overridePose.toString() + ")")), player.getUUID());
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
    	if (player.getForcedPose() != overridePose) {
    		player.setForcedPose(overridePose);
    		player.sendMessage(new StringTextComponent(("Tick2 for " + player.getDisplayName().getString() + ": p(" + overridePose.toString() + ")")), player.getUUID());
    		if (player.level.isClientSide() && Minecraft.getInstance().cameraEntity != player)
    			player.refreshDimensions();
    	}
			
    	return overridePose;
    }
    
    // Server Only
    public static ConcurrentHashMap<Integer, Boolean> serverWingsEnabled = new ConcurrentHashMap<>(20);
    
    private static Pose getOverridePose(PlayerEntity player) {
    	boolean swimming = player.isInWaterOrBubble() && player.isAffectedByFluids(); // TODO This needs more work to avoid bobbing
    	boolean flying = (player.level.isClientSide && ClientEvents.dragonsFlying.getOrDefault(player.getId(), false) && !player.isInWater() && !player.isOnGround() && player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).hasWings())
				|| (!player.level.isClientSide && !player.isOnGround() && serverWingsEnabled.getOrDefault(player.getId(), false) && !player.isInWater() && player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).hasWings());
    	boolean spinning = player.isAutoSpinAttack();
		boolean crouching = player.isShiftKeyDown();
		if (flying)
			return Pose.FALL_FLYING;
		else if (swimming)
			return Pose.SWIMMING;
		else if (spinning)
			return Pose.SPIN_ATTACK;
		else if (crouching || (!canPoseFit(player, Pose.STANDING) && canPoseFit(player, Pose.CROUCHING)))
				return Pose.CROUCHING;
		return Pose.STANDING;
    }
    
    private static ConcurrentHashMap<Integer, Boolean> wasDragon = new ConcurrentHashMap<Integer, Boolean>(20);
    
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
    	PlayerEntity player = event.player;
    	if (player == null || event.phase == TickEvent.Phase.END)
    		return;
    	DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
    		if (dragonStateHandler.isDragon()) {
    			Pose pose = overridePose(player);
    			if (!wasDragon.getOrDefault(player.getId(), false)) {
    				player.refreshDimensions();
    				player.sendMessage(new StringTextComponent(("Tick3 for " + player.getDisplayName().getString() + ": p(" + pose.toString() + ")")), player.getUUID());
    				wasDragon.put(player.getId(), true);
    			}
    		} else if (wasDragon.getOrDefault(player.getId(), false)) {
    			player.setForcedPose(null);
    			player.refreshDimensions();
    			player.sendMessage(new StringTextComponent(("Tick4 for " + player.getDisplayName().getString())), player.getUUID());
    			wasDragon.put(player.getId(), false);
			}
    	});
    }
}
