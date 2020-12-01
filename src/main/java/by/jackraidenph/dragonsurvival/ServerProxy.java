package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.network.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerProxy implements Proxy {
    @Override
    public DistExecutor.SafeRunnable syncMovement(PacketSyncCapabilityMovement syncCapabilityMovement, Supplier<NetworkEvent.Context> supplier) {
        return () -> {
            ServerPlayerEntity playerEntity = supplier.get().getSender();
            if (playerEntity != null) {
                PlayerStateProvider.getCap(playerEntity).ifPresent(cap -> cap.setMovementData(new DragonStateHandler.DragonMovementData(syncCapabilityMovement.bodyYaw, syncCapabilityMovement.headYaw, syncCapabilityMovement.headPitch, syncCapabilityMovement.headPos, syncCapabilityMovement.tailPos), false));
            }
        };
    }

    @Override
    public DistExecutor.SafeRunnable syncCapability(PacketSyncCapability syncCapability, Supplier<NetworkEvent.Context> supplier) {
        ServerPlayerEntity playerEntity = supplier.get().getSender();
        if (playerEntity != null) {
            PlayerStateProvider.getCap(playerEntity).ifPresent(cap -> {
                cap.setIsDragon(syncCapability.isDragon);
                cap.setType(syncCapability.type);
                cap.setLevel(syncCapability.level);
            });
        }
        return null;
    }

    @Override
    public DistExecutor.SafeRunnable syncXpDevour(PacketSyncXPDevour syncXPDevour, Supplier<NetworkEvent.Context> supplier) {
        return null;
    }

    @Override
    public DistExecutor.SafeRunnable syncPredatorStats(PacketSyncPredatorStats syncPredatorStats, Supplier<NetworkEvent.Context> supplier) {
        return null;
    }

    @Override
    public DistExecutor.SafeRunnable syncNest(SynchronizeNest synchronizeNest, Supplier<NetworkEvent.Context> contextSupplier) {
        return null;
    }
}
