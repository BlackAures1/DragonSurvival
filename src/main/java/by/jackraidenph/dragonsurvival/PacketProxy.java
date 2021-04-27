package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.gecko.DragonEntity;
import by.jackraidenph.dragonsurvival.handlers.ClientEvents;
import by.jackraidenph.dragonsurvival.handlers.EntityTypesInit;
import by.jackraidenph.dragonsurvival.handlers.FlightController;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.network.SyncLevel;
import by.jackraidenph.dragonsurvival.network.SynchronizeDragonCap;
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

    public DistExecutor.SafeRunnable handleCapabilityMovement(PacketSyncCapabilityMovement syncCapabilityMovement, Supplier<NetworkEvent.Context> supplier) {
        return () -> {
            NetworkEvent.Context context = supplier.get();
            context.enqueueWork(() -> handleMovement(syncCapabilityMovement, context));
        };
    }

    private void handleMovement(PacketSyncCapabilityMovement syncCapabilityMovement, NetworkEvent.Context context) {
        PlayerEntity thisPlayer = Minecraft.getInstance().player;
        if (thisPlayer != null) {
            World world = thisPlayer.world;
            Entity entity = world.getEntityByID(syncCapabilityMovement.playerId);
            if (entity instanceof PlayerEntity) {
                if (entity != thisPlayer) {
                    DragonStateProvider.getCap(entity).ifPresent(dragonStateHandler -> {
                        dragonStateHandler.setMovementData(syncCapabilityMovement.bodyYaw, syncCapabilityMovement.headYaw, syncCapabilityMovement.headPitch);
                    });
                }
            }
            context.setPacketHandled(true);
        }
    }

    public DistExecutor.SafeRunnable updateLevel(SyncLevel syncLevel, Supplier<NetworkEvent.Context> contextSupplier) {
        return () -> {
            Minecraft minecraft = Minecraft.getInstance();
            Entity entity = minecraft.world.getEntityByID(syncLevel.playerId);
            if (entity instanceof PlayerEntity) {
                DragonStateProvider.getCap(entity).ifPresent(dragonStateHandler -> dragonStateHandler.setLevel(syncLevel.level));
                contextSupplier.get().setPacketHandled(true);
            }
        };
    }

    public DistExecutor.SafeRunnable refreshInstances(SynchronizeDragonCap synchronizeDragonCap, Supplier<NetworkEvent.Context> context) {
        return () -> {
            ClientPlayerEntity myPlayer = Minecraft.getInstance().player;
            if (myPlayer != null) {
                World world = myPlayer.world;

                if (ClientEvents.dummyDragon2 != null) {
                    ClientEvents.dummyDragon2.get().player = myPlayer.getEntityId();
                }
                PlayerEntity thatPlayer = (PlayerEntity) world.getEntityByID(synchronizeDragonCap.playerId);

                if (thatPlayer != null) {
                    DragonStateProvider.getCap(thatPlayer).ifPresent(dragonStateHandler -> {
                        dragonStateHandler.setIsDragon(synchronizeDragonCap.isDragon);
                        dragonStateHandler.setLevel(synchronizeDragonCap.dragonLevel);
                        dragonStateHandler.setType(synchronizeDragonCap.dragonType);
                        dragonStateHandler.setIsHiding(synchronizeDragonCap.hiding);
                        dragonStateHandler.setHasWings(synchronizeDragonCap.hasWings);
                        if (!dragonStateHandler.hasWings())
                            FlightController.wingsEnabled = false;
                    });
                    //refresh instances
                    if (thatPlayer != myPlayer) {
                        DragonEntity dragonEntity = EntityTypesInit.dragonEntity.create(world);
                        dragonEntity.player = thatPlayer.getEntityId();
                        ClientEvents.playerDragonHashMap.computeIfAbsent(thatPlayer.getEntityId(), integer -> new AtomicReference<>(dragonEntity)).getAndSet(dragonEntity);
                    }
                }
                context.get().setPacketHandled(true);
            }
        };
    }
}
