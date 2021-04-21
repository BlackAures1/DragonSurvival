package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.network.PacketSyncPredatorStats;
import by.jackraidenph.dragonsurvival.network.PacketSyncXPDevour;
import by.jackraidenph.dragonsurvival.network.SynchronizeNest;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;
@Deprecated
public interface Proxy {


    void syncXpDevour(PacketSyncXPDevour m, Supplier<NetworkEvent.Context> supplier);

    void syncPredatorStats(PacketSyncPredatorStats m, Supplier<NetworkEvent.Context> supplier);

    void syncNest(SynchronizeNest synchronizeNest, Supplier<NetworkEvent.Context> contextSupplier);
}

