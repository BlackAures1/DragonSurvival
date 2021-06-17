package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler.DragonDebuffData;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler.DragonMovementData;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.network.SyncCapabilityDebuff;
import by.jackraidenph.dragonsurvival.network.SynchronizeDragonCap;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SSetPassengersPacket;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
            	DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SynchronizeDragonCap(player.getId(), cap.isHiding(), cap.getType(), cap.getSize(), cap.hasWings(), cap.getLavaAirSupply(), 0));
            	DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketSyncCapabilityMovement(player.getId(), cap.getMovementData().bodyYaw, cap.getMovementData().headYaw, cap.getMovementData().headPitch, cap.getMovementData().bite));
            	DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncCapabilityDebuff(player.getId(), cap.getDebuffData().timeWithoutWater, cap.getDebuffData().timeInDarkness));
            });
            // receive capability from others
            loggedInEvent.getPlayer().getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> {
                DragonStateProvider.getCap(serverPlayerEntity).ifPresent(dragonStateHandler -> {
                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SynchronizeDragonCap(serverPlayerEntity.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getSize(), dragonStateHandler.hasWings(), dragonStateHandler.getLavaAirSupply(), 0));
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
            	DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SynchronizeDragonCap(player.getId(), cap.isHiding(), cap.getType(), cap.getSize(), cap.hasWings(), cap.getLavaAirSupply(), 0));
            	DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketSyncCapabilityMovement(player.getId(), cap.getMovementData().bodyYaw, cap.getMovementData().headYaw, cap.getMovementData().headPitch, cap.getMovementData().bite));
            	DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncCapabilityDebuff(player.getId(), cap.getDebuffData().timeWithoutWater, cap.getDebuffData().timeInDarkness));
            });
            // receive capability from others
            playerRespawnEvent.getPlayer().getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> {
                DragonStateProvider.getCap(serverPlayerEntity).ifPresent(dragonStateHandler -> {
                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SynchronizeDragonCap(serverPlayerEntity.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getSize(), dragonStateHandler.hasWings(), dragonStateHandler.getLavaAirSupply(), 0));
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
                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)trackingPlayer), new SynchronizeDragonCap(trackedEntity.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getSize(), dragonStateHandler.hasWings(), dragonStateHandler.getLavaAirSupply(), dragonStateHandler.getPassengerId()));
                    DragonMovementData mData = dragonStateHandler.getMovementData();
                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)trackingPlayer), new PacketSyncCapabilityMovement(trackedEntity.getId(), mData.bodyYaw, mData.headYaw, mData.headPitch, mData.bite));
                    DragonDebuffData dData = dragonStateHandler.getDebuffData();
                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)trackingPlayer), new SyncCapabilityDebuff(trackedEntity.getId(), dData.timeWithoutWater, dData.timeInDarkness));
                });
            }
        }
    }

    @SubscribeEvent
    public static void onDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
        PlayerEntity player = event.getPlayer();
        if (player.level.isClientSide())
            return;
        DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
            DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), new SynchronizeDragonCap(player.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getSize(), dragonStateHandler.hasWings(), dragonStateHandler.getLavaAirSupply(), 0));
            DragonMovementData mData = dragonStateHandler.getMovementData();
            DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), new PacketSyncCapabilityMovement(player.getId(), mData.bodyYaw, mData.headYaw, mData.headPitch, mData.bite));
            DragonDebuffData dData = dragonStateHandler.getDebuffData();
            DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity)player), new SyncCapabilityDebuff(player.getId(), dData.timeWithoutWater, dData.timeInDarkness));
        });
    }

    @SubscribeEvent
    public static void onServerPlayerTick(TickEvent.PlayerTickEvent event) { // TODO: Find a better way of doing this.
        if (!(event.player instanceof ServerPlayerEntity))
            return;
        ServerPlayerEntity player = (ServerPlayerEntity)event.player;
        DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
            int passengerId = dragonStateHandler.getPassengerId();
            Entity passenger = player.level.getEntity(passengerId);
            boolean flag = false;
            if (!dragonStateHandler.isDragon() && player.isVehicle()){
                flag = true;
                player.getPassengers().get(0).stopRiding();
                player.connection.send(new SSetPassengersPacket(player));
            } else if (player.isSpectator() && passenger != null){
                flag = true;
                player.getPassengers().get(0).stopRiding();
                player.connection.send(new SSetPassengersPacket(player));
            } else if (dragonStateHandler.isDragon() && dragonStateHandler.getSize() != 40 && player.isVehicle()){
                flag = true;
                player.getPassengers().get(0).stopRiding();
                player.connection.send(new SSetPassengersPacket(player));
            }
            if (passenger != null) {
                DragonStateHandler passengerCap = DragonStateProvider.getCap(passenger).orElseGet(null);
                if (passengerCap != null){
                    if (passengerCap.isDragon() && passengerCap.getLevel() != DragonLevel.BABY){
                        flag = true;
                        passenger.stopRiding();
                        player.connection.send(new SSetPassengersPacket(player));
                    }
                }
            }
            if (flag || passenger == null || !player.hasPassenger(passenger) || passenger.isSpectator() || player.isSpectator()){
                dragonStateHandler.setPassengerId(0);
                DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SynchronizeDragonCap(player.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getSize(), dragonStateHandler.hasWings(), dragonStateHandler.getLavaAirSupply(), 0));
            }

        });
    }


}
