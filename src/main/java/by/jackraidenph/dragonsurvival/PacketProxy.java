package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.entity.MagicalPredatorEntity;
import by.jackraidenph.dragonsurvival.gecko.DragonEntity;
import by.jackraidenph.dragonsurvival.handlers.ClientEvents;
import by.jackraidenph.dragonsurvival.handlers.ClientFlightHandler;
import by.jackraidenph.dragonsurvival.nest.NestEntity;
import by.jackraidenph.dragonsurvival.network.*;
import by.jackraidenph.dragonsurvival.registration.EntityTypesInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
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
		 }
         context.setPacketHandled(true);
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
                	if (entity == thisPlayer)
                        dragonStateHandler.setMovementData(syncCapabilityMovement.bodyYaw, ((PlayerEntity) entity).yHeadRot, entity.xRot, syncCapabilityMovement.bite);
                    else
                        dragonStateHandler.setMovementData(syncCapabilityMovement.bodyYaw, syncCapabilityMovement.headYaw, syncCapabilityMovement.headPitch, syncCapabilityMovement.bite);
                });
            }
        }
        context.setPacketHandled(true);
    }

    public DistExecutor.SafeRunnable updateSize(SyncSize syncSize, Supplier<NetworkEvent.Context> contextSupplier) {
        return () -> {
            Minecraft minecraft = Minecraft.getInstance();
            Entity entity = minecraft.level.getEntity(syncSize.playerId);
            if (entity instanceof PlayerEntity) {
                DragonStateProvider.getCap(entity).ifPresent(dragonStateHandler -> {
                    dragonStateHandler.setSize(syncSize.size, (PlayerEntity)entity);
                });

            }
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
                        dragonStateHandler.setPassengerId(synchronizeDragonCap.passengerId);
                        if (!dragonStateHandler.hasWings() && thatPlayer == myPlayer)
                            ClientFlightHandler.wingsEnabled = false;
                    });
                    //refresh instances
                    if (thatPlayer != myPlayer) {
                        DragonEntity dragonEntity = EntityTypesInit.DRAGON.create(world);
                        dragonEntity.player = thatPlayer.getId();
                        ClientEvents.playerDragonHashMap.computeIfAbsent(thatPlayer.getId(), integer -> new AtomicReference<>(dragonEntity)).getAndSet(dragonEntity);
                    }
                    thatPlayer.setForcedPose(null);
                    thatPlayer.refreshDimensions();
                }
            }
            context.get().setPacketHandled(true);
        };
    }

    public void syncXpDevour(PacketSyncXPDevour m, Supplier<NetworkEvent.Context> supplier) {

        World world = Minecraft.getInstance().level;
        if (world != null) {
            ExperienceOrbEntity xpOrb = (ExperienceOrbEntity) (world.getEntity(m.xp));
            MagicalPredatorEntity entity = (MagicalPredatorEntity) world.getEntity(m.entity);
            if (xpOrb != null && entity != null) {
                entity.size += xpOrb.getValue() / 100.0F;
                entity.size = MathHelper.clamp(entity.size, 0.95F, 1.95F);
                world.addParticle(ParticleTypes.SMOKE, xpOrb.getX(), xpOrb.getY(), xpOrb.getZ(), 0, world.getRandom().nextFloat() / 12.5f, 0);
                xpOrb.remove();
                supplier.get().setPacketHandled(true);
            }
        }
    }

    public void syncPredatorStats(PacketSyncPredatorStats m, Supplier<NetworkEvent.Context> supplier) {

        World world = Minecraft.getInstance().level;
        if (world != null) {
            Entity entity = world.getEntity(m.id);
            if (entity != null) {
                ((MagicalPredatorEntity) entity).size = m.size;
                ((MagicalPredatorEntity) entity).type = m.type;
                supplier.get().setPacketHandled(true);
            }
        }
    }

    public void syncNest(SynchronizeNest synchronizeNest, Supplier<NetworkEvent.Context> contextSupplier) {
        PlayerEntity player = Minecraft.getInstance().player;
        ClientWorld world = Minecraft.getInstance().level;
        TileEntity entity = world.getBlockEntity(synchronizeNest.pos);
        if (entity instanceof NestEntity) {
            NestEntity nestEntity = (NestEntity) entity;
            nestEntity.energy = synchronizeNest.health;
            nestEntity.damageCooldown = synchronizeNest.cooldown;
            nestEntity.setChanged();
            if (nestEntity.energy <= 0) {
                world.playSound(player, synchronizeNest.pos, SoundEvents.METAL_BREAK, SoundCategory.BLOCKS, 1, 1);
            } else {
                world.playSound(player, synchronizeNest.pos, SoundEvents.SHIELD_BLOCK, SoundCategory.BLOCKS, 1, 1);
            }
            contextSupplier.get().setPacketHandled(true);
        }
    }
}
