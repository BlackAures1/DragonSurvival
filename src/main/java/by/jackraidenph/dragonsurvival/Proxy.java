package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.network.*;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public interface Proxy {

    void syncMovement(PacketSyncCapabilityMovement m, Supplier<NetworkEvent.Context> supplier);

    void syncCapability(PacketSyncCapability m, Supplier<NetworkEvent.Context> supplier);

    void syncXpDevour(PacketSyncXPDevour m, Supplier<NetworkEvent.Context> supplier);

    void syncPredatorStats(PacketSyncPredatorStats m, Supplier<NetworkEvent.Context> supplier);

    void syncNest(SynchronizeNest synchronizeNest, Supplier<NetworkEvent.Context> contextSupplier);
}

