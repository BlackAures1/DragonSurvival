package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.network.SynchronizeDragonCap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber
public class SynchronizationController {

    @SubscribeEvent
    public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent loggedInEvent) {
        PlayerEntity player = loggedInEvent.getPlayer();
        //send the capability to others
        PlayerStateProvider.getCap(player).ifPresent(cap -> {
            DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SynchronizeDragonCap(player.getEntityId(), cap.isHiding(), cap.getType(), cap.getLevel(), cap.isDragon()));
        });
        System.out.println(loggedInEvent);
    }

    @SubscribeEvent
    public static void trackingStart(PlayerEvent.StartTracking startTracking) {
        PlayerEntity playerEntity = startTracking.getPlayer();
        Entity tracked = startTracking.getTarget();
        if (tracked instanceof ServerPlayerEntity) {
            PlayerStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
                DragonSurvivalMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY.with(() -> tracked), new SynchronizeDragonCap(playerEntity.getEntityId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getLevel(), dragonStateHandler.isDragon()));
            });
            System.out.println(startTracking);
        }
    }

    @SubscribeEvent
    public static void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent e) {
        PlayerStateProvider.getCap(e.getPlayer()).ifPresent(cap ->
                cap.getMovementData().ifPresent(data ->
                        DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketSyncCapabilityMovement(data))));
    }

    /**
     * Synchronizes the capability after death
     */
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent playerRespawnEvent) {
        PlayerEntity playerEntity = playerRespawnEvent.getPlayer();
        PlayerStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                dragonStateHandler.syncCapabilityData(!playerEntity.world.isRemote);
            }
        });
    }

    @SubscribeEvent
    public static void entityJoinedWorld(EntityJoinWorldEvent entityJoinWorldEvent) {
        Entity joined = entityJoinWorldEvent.getEntity();
        if (joined instanceof ServerPlayerEntity) {
            System.out.println(entityJoinWorldEvent);
        }
    }
}
