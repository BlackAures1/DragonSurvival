package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler.DragonDebuffData;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler.DragonMovementData;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.network.SyncCapabilityDebuff;
import by.jackraidenph.dragonsurvival.network.SynchronizeDragonCap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber
@SuppressWarnings("unused")
public class SynchronizationController {

    /**
     * Synchronizes capability among players
     */
    @SubscribeEvent
    public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent loggedInEvent) {
        PlayerEntity player = loggedInEvent.getPlayer();
        if (!player.level.isClientSide) {
            // send the capability to everyone
            DragonStateProvider.getCap(player).ifPresent(cap -> {
            	DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SynchronizeDragonCap(player.getId(), cap.isHiding(), cap.getType(), cap.isDragon(), cap.getSize(), cap.hasWings()));
            	DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketSyncCapabilityMovement(player.getId(), cap.getMovementData().bodyYaw, cap.getMovementData().headYaw, cap.getMovementData().headPitch, cap.getMovementData().bite));
            	DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncCapabilityDebuff(player.getId(), cap.getDebuffData().timeWithoutWater, cap.getDebuffData().timeInDarkness));
            });
            // receive capability from others
            loggedInEvent.getPlayer().getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> {
                DragonStateProvider.getCap(serverPlayerEntity).ifPresent(dragonStateHandler -> {
                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SynchronizeDragonCap(serverPlayerEntity.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.isDragon(), dragonStateHandler.getSize(), dragonStateHandler.hasWings()));
                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new PacketSyncCapabilityMovement(player.getId(), dragonStateHandler.getMovementData().bodyYaw, dragonStateHandler.getMovementData().headYaw, dragonStateHandler.getMovementData().headPitch, dragonStateHandler.getMovementData().bite));
                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SyncCapabilityDebuff(player.getId(), dragonStateHandler.getDebuffData().timeWithoutWater, dragonStateHandler.getDebuffData().timeInDarkness));
                });
            });
        }
    }

    /**
     * Synchronizes the capability after death
     */
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent playerRespawnEvent) {
        PlayerEntity player = playerRespawnEvent.getPlayer();
        if (!player.level.isClientSide) {
            // send the capability to everyone
            DragonStateProvider.getCap(player).ifPresent(cap -> {
            	DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SynchronizeDragonCap(player.getId(), cap.isHiding(), cap.getType(), cap.isDragon(), cap.getSize(), cap.hasWings()));
            	DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketSyncCapabilityMovement(player.getId(), cap.getMovementData().bodyYaw, cap.getMovementData().headYaw, cap.getMovementData().headPitch, cap.getMovementData().bite));
            	DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncCapabilityDebuff(player.getId(), cap.getDebuffData().timeWithoutWater, cap.getDebuffData().timeInDarkness));
            });
            // receive capability from others
            playerRespawnEvent.getPlayer().getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> {
                DragonStateProvider.getCap(serverPlayerEntity).ifPresent(dragonStateHandler -> {
                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SynchronizeDragonCap(serverPlayerEntity.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.isDragon(), dragonStateHandler.getSize(), dragonStateHandler.hasWings()));
                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new PacketSyncCapabilityMovement(player.getId(), dragonStateHandler.getMovementData().bodyYaw, dragonStateHandler.getMovementData().headYaw, dragonStateHandler.getMovementData().headPitch, dragonStateHandler.getMovementData().bite));
                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SyncCapabilityDebuff(player.getId(), dragonStateHandler.getDebuffData().timeWithoutWater, dragonStateHandler.getDebuffData().timeInDarkness));
                });
            });
        }
    }

    @SubscribeEvent
    public static void onTrackingStart(PlayerEvent.StartTracking startTracking) {
        PlayerEntity trackingPlayer = startTracking.getPlayer();
        if (trackingPlayer instanceof ServerPlayerEntity) {
            Entity trackedEntity = startTracking.getTarget();
            if (trackedEntity instanceof ServerPlayerEntity) {
                DragonStateProvider.getCap(trackedEntity).ifPresent(dragonStateHandler -> {
                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)trackingPlayer), new SynchronizeDragonCap(trackedEntity.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.isDragon(), dragonStateHandler.getSize(), dragonStateHandler.hasWings()));
                    DragonMovementData mData = dragonStateHandler.getMovementData();
                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)trackingPlayer), new PacketSyncCapabilityMovement(trackedEntity.getId(), mData.bodyYaw, mData.headYaw, mData.headPitch, mData.bite));
                    DragonDebuffData dData = dragonStateHandler.getDebuffData();
                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)trackingPlayer), new SyncCapabilityDebuff(trackedEntity.getId(), dData.timeWithoutWater, dData.timeInDarkness));
                });
            }
        }
    }
}
