package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.ClientProxy;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncPredatorStats implements IMessage<PacketSyncPredatorStats> {

    public float size;
    public int type;
    public int id;

    public PacketSyncPredatorStats() {
    }

    public PacketSyncPredatorStats(int type, float size, int id) {
        this.type = type;
        this.size = size;
        this.id = id;
    }

    @Override
    public void encode(PacketSyncPredatorStats m, PacketBuffer b) {
        b.writeInt(m.type);
        b.writeFloat(m.size);
        b.writeInt(m.id);
    }

    @Override
    public PacketSyncPredatorStats decode(PacketBuffer b) {
        return new PacketSyncPredatorStats(
                b.readInt(),
                b.readFloat(),
                b.readInt());
    }

    @Override
    public void handle(PacketSyncPredatorStats m, Supplier<NetworkEvent.Context> supplier) {
        new ClientProxy().syncPredatorStats(m, supplier);
        supplier.get().setPacketHandled(true);
    }
}
