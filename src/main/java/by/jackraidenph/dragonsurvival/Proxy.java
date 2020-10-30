package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.network.PacketSyncCapability;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.network.PacketSyncPredatorStats;
import by.jackraidenph.dragonsurvival.network.PacketSyncXPDevour;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface Proxy {

    DistExecutor.SafeRunnable syncMovement(PacketSyncCapabilityMovement m, Supplier<NetworkEvent.Context> supplier);

    DistExecutor.SafeRunnable syncCapability(PacketSyncCapability m, Supplier<NetworkEvent.Context> supplier);

    DistExecutor.SafeRunnable syncXpDevour(PacketSyncXPDevour m, Supplier<NetworkEvent.Context> supplier);

    DistExecutor.SafeRunnable syncPredatorStats(PacketSyncPredatorStats m, Supplier<NetworkEvent.Context> supplier);
}

