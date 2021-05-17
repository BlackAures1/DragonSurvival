package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.gecko.DragonEntity;
import by.jackraidenph.dragonsurvival.handlers.ClientEvents;
import by.jackraidenph.dragonsurvival.handlers.EntityTypesInit;
import by.jackraidenph.dragonsurvival.handlers.FlightController;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.network.SyncCapabilityDebuff;
import by.jackraidenph.dragonsurvival.network.SyncConfig;
import by.jackraidenph.dragonsurvival.network.SyncSize;
import by.jackraidenph.dragonsurvival.network.SynchronizeDragonCap;
import by.jackraidenph.dragonsurvival.util.ConfigurationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * Synchronizes client data
 */
public class PacketProxy {

	 public DistExecutor.SafeRunnable handleCapabilityDebuff(SyncCapabilityDebuff syncCapabilityDebuff, Supplier<NetworkEvent.Context> supplier) {
		 return () -> {
	            NetworkEvent.Context context = supplier.get();
	            context.enqueueWork(() -> handleDebuffs(syncCapabilityDebuff, context));
	        };
	 }
	 
	 private void handleDebuffs(SyncCapabilityDebuff syncCapabilityDebuff, NetworkEvent.Context context) {
		 PlayerEntity thisPlayer = Minecraft.getInstance().player;
		 if (thisPlayer != null) {
	 		World world = thisPlayer.level;
            Entity entity = world.getEntity(syncCapabilityDebuff.playerId);
            if (entity instanceof PlayerEntity) {
            	DragonStateProvider.getCap(entity).ifPresent(dragonStateHandler -> {
                    dragonStateHandler.setDebuffData(syncCapabilityDebuff.timeWithoutWater, syncCapabilityDebuff.timeInDarkness);
                });
            }
            context.setPacketHandled(true);
		 }
	 }
	
	
    public DistExecutor.SafeRunnable handleCapabilityMovement(PacketSyncCapabilityMovement syncCapabilityMovement, Supplier<NetworkEvent.Context> supplier) {
        return () -> {
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(() -> handleMovement(syncCapabilityMovement, context));
        };
    }

    private void handleMovement(PacketSyncCapabilityMovement syncCapabilityMovement, NetworkEvent.Context context) {
        PlayerEntity thisPlayer = Minecraft.getInstance().player;
        if (thisPlayer != null) {
            World world = thisPlayer.level;
            Entity entity = world.getEntity(syncCapabilityMovement.playerId);
            if (entity instanceof PlayerEntity) {
                DragonStateProvider.getCap(entity).ifPresent(dragonStateHandler -> {
                	if ((PlayerEntity)entity == thisPlayer)
                		dragonStateHandler.setMovementData(syncCapabilityMovement.bodyYaw, ((PlayerEntity)entity).yHeadRot, entity.xRot, syncCapabilityMovement.bite);
                	else
                		dragonStateHandler.setMovementData(syncCapabilityMovement.bodyYaw, syncCapabilityMovement.headYaw, syncCapabilityMovement.headPitch, syncCapabilityMovement.bite);
                });
            }
            context.setPacketHandled(true);
        }
    }

    public DistExecutor.SafeRunnable updateSize(SyncSize syncSize, Supplier<NetworkEvent.Context> contextSupplier) {
        return () -> {
            Minecraft minecraft = Minecraft.getInstance();
            Entity entity = minecraft.level.getEntity(syncSize.playerId);
            if (entity instanceof PlayerEntity) {
                DragonStateProvider.getCap(entity).ifPresent(dragonStateHandler -> {
                    dragonStateHandler.setSize(syncSize.size, (PlayerEntity)entity);
                });
                
                contextSupplier.get().setPacketHandled(true);
            }
        };
    }
    
    public DistExecutor.SafeRunnable saveServerConfig(SyncConfig syncConfig, Supplier<NetworkEvent.Context> contextSupplier){
    	return () -> {
    		ConfigurationHandler.NetworkedConfig.setServerConnection(true);
    		ConfigurationHandler.NetworkedConfig.saveServerConfig(
    				syncConfig.serverMaxFlightSpeed, 
    				syncConfig.serverMineStarBlock,
    				syncConfig.serverSizeChangesHitbox, 
    				syncConfig.serverHitboxGrowsPastHuman, 
    				syncConfig.serverStartWithWings,
    				syncConfig.serverDragonDebuffs);
    		contextSupplier.get().enqueueWork(() -> {
    			ClientPlayerEntity player = Minecraft.getInstance().player;
    			player.setForcedPose(null);
    			player.refreshDimensions();
    		});
    		contextSupplier.get().setPacketHandled(true);
    		
    	};
    }

    public DistExecutor.SafeRunnable refreshInstances(SynchronizeDragonCap synchronizeDragonCap, Supplier<NetworkEvent.Context> context) {
        return () -> {
            ClientPlayerEntity myPlayer = Minecraft.getInstance().player;
            if (myPlayer != null) {
                World world = myPlayer.level;

                if (ClientEvents.dummyDragon2 != null) {
                    ClientEvents.dummyDragon2.get().player = myPlayer.getId();
                }
                PlayerEntity thatPlayer = (PlayerEntity) world.getEntity(synchronizeDragonCap.playerId);

                if (thatPlayer != null) {
                    DragonStateProvider.getCap(thatPlayer).ifPresent(dragonStateHandler -> {
                        dragonStateHandler.setType(synchronizeDragonCap.dragonType);
                        dragonStateHandler.setIsHiding(synchronizeDragonCap.hiding);
                        dragonStateHandler.setHasWings(synchronizeDragonCap.hasWings);
                        dragonStateHandler.setSize(synchronizeDragonCap.size);
                        dragonStateHandler.setLavaAirSupply(synchronizeDragonCap.lavaAirSupply);
                        if (!dragonStateHandler.hasWings() && thatPlayer == myPlayer)
                            FlightController.wingsEnabled = false;
                    });
                    //refresh instances
                    if (thatPlayer != myPlayer) {
                        DragonEntity dragonEntity = EntityTypesInit.dragonEntity.create(world);
                        dragonEntity.player = thatPlayer.getId();
                        ClientEvents.playerDragonHashMap.computeIfAbsent(thatPlayer.getId(), integer -> new AtomicReference<>(dragonEntity)).getAndSet(dragonEntity);
                    }
                    thatPlayer.setForcedPose(null);
                    thatPlayer.refreshDimensions();
                }
                context.get().setPacketHandled(true);
            }
        };
    }
}
