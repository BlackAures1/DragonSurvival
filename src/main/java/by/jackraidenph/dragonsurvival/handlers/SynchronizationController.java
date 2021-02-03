package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.network.SynchronizeDragonCap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber
public class SynchronizationController {

    /**
     * Synchronizes capability among players
     */
    @SubscribeEvent
    public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent loggedInEvent) {
        PlayerEntity player = loggedInEvent.getPlayer();
        //send the capability to everyone
        DragonStateProvider.getCap(player).ifPresent(cap -> {
            DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SynchronizeDragonCap(player.getEntityId(), cap.isHiding(), cap.getType(), cap.getLevel(), cap.isDragon(), cap.getHealth()));
            DragonSurvivalMod.LOGGER.info("{} {}", player.getDisplayName().getString(), cap.getLevel());
        });
        //receive capability from others
        loggedInEvent.getPlayer().getServer().getPlayerList().getPlayers().forEach(serverPlayerEntity -> {
            DragonStateProvider.getCap(serverPlayerEntity).ifPresent(dragonStateHandler -> {
                DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) player), new SynchronizeDragonCap(serverPlayerEntity.getEntityId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getLevel(), dragonStateHandler.isDragon(), dragonStateHandler.getHealth()));
            });
        });
    }

    /**
     * Synchronizes the capability after death
     */
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent playerRespawnEvent) {
        PlayerEntity playerEntity = playerRespawnEvent.getPlayer();
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                dragonStateHandler.syncCapabilityData(!playerEntity.world.isRemote);
            }
        });
    }

    /**
     * Synchronizes dragon model rotations among players
     */
    @SubscribeEvent
    public static void onTick(TickEvent.PlayerTickEvent playerTickEvent) {
        PlayerEntity playerEntity = playerTickEvent.player;
        if (playerEntity instanceof ServerPlayerEntity) {
            ServerPlayerEntity serverPlayerEntity = (ServerPlayerEntity) playerEntity;
            serverPlayerEntity.getServer().getPlayerList().getPlayers().forEach(otherPlayerEntity -> {
                if (otherPlayerEntity != playerEntity) {
                    DragonStateProvider.getCap(otherPlayerEntity).ifPresent(dragonStateHandler -> {
                        DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> serverPlayerEntity), new PacketSyncCapabilityMovement(otherPlayerEntity.getEntityId(), otherPlayerEntity.getYaw(1), otherPlayerEntity.rotationYawHead, otherPlayerEntity.rotationPitch, Vec3d.ZERO, Vec3d.ZERO));
                    });
                }
            });
        }

    }
}
