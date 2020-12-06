package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.network.*;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerProxy implements Proxy {
    @Override
    public void syncMovement(PacketSyncCapabilityMovement syncCapabilityMovement, Supplier<NetworkEvent.Context> supplier) {
    }

    @Override
    public void syncCapability(PacketSyncCapability syncCapability, Supplier<NetworkEvent.Context> supplier) {
        ServerPlayerEntity playerEntity = supplier.get().getSender();
        if (playerEntity != null) {
            PlayerStateProvider.getCap(playerEntity).ifPresent(cap -> {
                cap.setIsDragon(syncCapability.isDragon);
                cap.setType(syncCapability.type);
                cap.setLevel(syncCapability.level);
            });
        }
    }

    @Override
    public void syncXpDevour(PacketSyncXPDevour syncXPDevour, Supplier<NetworkEvent.Context> supplier) {
    }

    @Override
    public void syncPredatorStats(PacketSyncPredatorStats syncPredatorStats, Supplier<NetworkEvent.Context> supplier) {
    }

    @Override
    public void syncNest(SynchronizeNest synchronizeNest, Supplier<NetworkEvent.Context> contextSupplier) {
    }
}
