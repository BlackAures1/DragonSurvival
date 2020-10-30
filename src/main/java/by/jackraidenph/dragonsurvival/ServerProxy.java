package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.capability.PlayerStateHandler;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapability;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.network.PacketSyncPredatorStats;
import by.jackraidenph.dragonsurvival.network.PacketSyncXPDevour;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ServerProxy implements Proxy {
    @Override
    public DistExecutor.SafeRunnable syncMovement(PacketSyncCapabilityMovement m, Supplier<NetworkEvent.Context> supplier) {
        return () -> {
            ServerPlayerEntity playerEntity = supplier.get().getSender();
            if (playerEntity != null) {
                PlayerStateProvider.getCap(playerEntity).ifPresent(cap -> cap.setMovementData(new PlayerStateHandler.DragonMovementData(m.bodyYaw, m.headYaw, m.headPitch, m.headPos, m.tailPos), false));
            }
        };
    }

    @Override
    public DistExecutor.SafeRunnable syncCapability(PacketSyncCapability m, Supplier<NetworkEvent.Context> supplier) {
        return () -> {
            ServerPlayerEntity playerEntity = supplier.get().getSender();
            if (playerEntity != null) {
                PlayerStateProvider.getCap(playerEntity).ifPresent(cap -> {
                    cap.setIsDragon(m.isDragon);
                    cap.setType(m.type);
                    cap.setLevel(m.level);
                });
            }
        };
    }

    @Override
    public DistExecutor.SafeRunnable syncXpDevour(PacketSyncXPDevour m, Supplier<NetworkEvent.Context> supplier) {
        return null;
    }

    @Override
    public DistExecutor.SafeRunnable syncPredatorStats(PacketSyncPredatorStats m, Supplier<NetworkEvent.Context> supplier) {
        return null;
    }
}
