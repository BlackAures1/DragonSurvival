package by.jackraidenph.dragonsurvival.handlers;

import java.util.concurrent.ConcurrentHashMap;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.util.ConfigurationHandler;
import by.jackraidenph.dragonsurvival.util.DragonMovementFromOptions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
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
    		float size = dragonStateHandler.getSize();
    		// Calculate base values
    		float height = calculateDragonHeight(size, ConfigurationHandler.NetworkedConfig.getHitboxGrowsPastHuman());
    		float width = calculateDragonWidth(size, ConfigurationHandler.NetworkedConfig.getHitboxGrowsPastHuman());
    		float eyeHeight = calculateDragonEyeHeight(size, ConfigurationHandler.NetworkedConfig.getHitboxGrowsPastHuman());
    		// Handle Pose stuff
    		if (ConfigurationHandler.NetworkedConfig.getSizeChangesHitbox()) {
    		Pose overridePose = overridePose(player);
    		height = calculateModifiedHeight(height, overridePose, true);
    		eyeHeight = calculateModifiedEyeHeight(eyeHeight, overridePose);
    		// Apply changes
			event.setNewEyeHeight(eyeHeight);
    		event.setNewSize(new EntitySize(width, height, false));
    		}
    		
        });
    }
    
	private static float calculateDragonHeight(float size, boolean growsPastHuman) {
		float height = (size + 4.0F) / 20.0F; // 0.9 -> 2.2
		if (!growsPastHuman)
			height = 9F * (size + 12F) / 260F; // 0.9 -> 1.8
		return height;
	}
	
	private static float calculateDragonWidth(float size, boolean growsPastHuman) {
		float width = (3.0F * size + 62.0F) / 260.0F; // 0.4 -> 0.7
		if (!growsPastHuman)
			width = (size + 38) / 130F; // 0.4 -> 0.6
		return width;
	}
	
	private static float calculateDragonEyeHeight(float size, boolean growsPastHuman) {
		float eyeHeight = (11.0F * size + 54.0F) / 260.0F; // 0.8 -> 1.9
		if (!growsPastHuman)
			eyeHeight = (41F * size + 466F) / 1300F; // 14, 0.8 -> 40, 1.62
		return eyeHeight;
	}
	
    private static float calculateModifiedHeight(float height, Pose pose, boolean sizeChangesHitbox) {
    	if (pose == Pose.CROUCHING) {
    		if (sizeChangesHitbox)
    			height *= 5.0F / 6.0F;
    		else
    			height = 1.5F;
		} else if (pose == Pose.SWIMMING || pose == Pose.FALL_FLYING || pose == Pose.SPIN_ATTACK) {
			if (sizeChangesHitbox)
				height *= 7.0F / 12.0F;
			else
				height = 0.6F;
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
    	if (!DragonStateProvider.getCap(player).isPresent())
    		return false;
		float size = player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).getSize();
		float height = calculateModifiedHeight(calculateDragonHeight(size, ConfigurationHandler.NetworkedConfig.getHitboxGrowsPastHuman()), pose,  ConfigurationHandler.NetworkedConfig.getSizeChangesHitbox());
		float width = calculateDragonWidth(size, ConfigurationHandler.NetworkedConfig.getHitboxGrowsPastHuman());
		return (player.level.getBlockCollisions(null, new AxisAlignedBB(
				player.position().subtract(width * 0.5D, 0.0D, width * 0.5D), 
				player.position().add(width * 0.5D, height, width * 0.5D)))
		.count() == 0);
    }
    
    private static Pose overridePose(PlayerEntity player) {
    	Pose overridePose = getOverridePose(player);
    	if (player.getForcedPose() != overridePose) {
    		player.setForcedPose(overridePose);
    		if (player.level.isClientSide() && Minecraft.getInstance().cameraEntity != player)
    			player.refreshDimensions();
    	}
    	
			
    	return overridePose;
    }
    
    // Server Only
    public static ConcurrentHashMap<Integer, Boolean> serverWingsEnabled = new ConcurrentHashMap<>(20);
    
    private static Pose getOverridePose(PlayerEntity player) {
    	boolean swimming = (player.isInWaterOrBubble() || player.isInLava()) && player.isSprinting() && !player.isPassenger();
    	boolean flying = (player.level.isClientSide && ClientEvents.dragonsFlying.getOrDefault(player.getId(), false) && !player.isInWater() && !player.isOnGround() && player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).hasWings())
				|| (!player.level.isClientSide && !player.isOnGround() && serverWingsEnabled.getOrDefault(player.getId(), false) && !player.isInWater() && player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).hasWings());
    	boolean spinning = player.isAutoSpinAttack();
		boolean crouching = player.isShiftKeyDown();
		if (flying && !player.isSleeping())
			return Pose.FALL_FLYING;
		else if (swimming || (player.isInWaterOrBubble() && !canPoseFit(player, Pose.STANDING) && canPoseFit(player, Pose.SWIMMING)))
			return Pose.SWIMMING;
		else if (spinning)
			return Pose.SPIN_ATTACK;
		else if (crouching || (!canPoseFit(player, Pose.STANDING) && canPoseFit(player, Pose.CROUCHING)))
				return Pose.CROUCHING;
		return Pose.STANDING;
    }
    
    private static ConcurrentHashMap<Integer, Boolean> wasDragon = new ConcurrentHashMap<Integer, Boolean>(20);
    public static ConcurrentHashMap<Integer, Float> lastSize = new ConcurrentHashMap<Integer, Float>(20);
    
    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent event) {
    	PlayerEntity player = event.player;
    	if (player == null || event.phase == TickEvent.Phase.END || !ConfigurationHandler.NetworkedConfig.getSizeChangesHitbox())
    		return;
    	DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
    		if (dragonStateHandler.isDragon()) {
    			overridePose(player);
    			if (!wasDragon.getOrDefault(player.getId(), false)) {
    				player.refreshDimensions();
    				wasDragon.put(player.getId(), true);
    			}
    			else if (lastSize.getOrDefault(player.getId(), 20.0F) != dragonStateHandler.getSize()) {
    				player.refreshDimensions();
    				lastSize.put(player.getId(), dragonStateHandler.getSize());
    			}
    		} else if (wasDragon.getOrDefault(player.getId(), false)) {
    			player.setForcedPose(null);
    			player.refreshDimensions();
    			wasDragon.put(player.getId(), false);
			}
    	});
    }
    
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void clientPlayerTick(TickEvent.PlayerTickEvent event) { // SOMEONE PLEASE FIND A BETTER WAY THIS PHYSICALLY HURTS ME
    	PlayerEntity player = event.player;
    	if (player == Minecraft.getInstance().cameraEntity && ConfigurationHandler.NetworkedConfig.getSizeChangesHitbox()) {
    		DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
    			if (player instanceof ClientPlayerEntity && dragonStateHandler.isDragon() && !(((ClientPlayerEntity)player).input instanceof DragonMovementFromOptions)) {
        			((ClientPlayerEntity)player).input = new DragonMovementFromOptions(Minecraft.getInstance().options, (ClientPlayerEntity)player, ConfigurationHandler.NetworkedConfig.getSizeChangesHitbox());
        		}
    		});
    	}
    }
    
}


