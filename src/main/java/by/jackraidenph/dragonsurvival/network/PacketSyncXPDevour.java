package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.ClientProxy;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncXPDevour implements IMessage<PacketSyncXPDevour> {

    public int entity;
    public int xp;

    public PacketSyncXPDevour() {
    }

    public PacketSyncXPDevour(int entity, int xp) {
        this.entity = entity;
        this.xp = xp;
    }

    @Override
    public void encode(PacketSyncXPDevour m, PacketBuffer b) {
        b.writeInt(m.entity);
        b.writeInt(m.xp);
    }

    @Override
    public PacketSyncXPDevour decode(PacketBuffer b) {
        return new PacketSyncXPDevour(
                b.readInt(),
                b.readInt());
    }

    @Override
    public void handle(PacketSyncXPDevour m, Supplier<NetworkEvent.Context> supplier) {
        new ClientProxy().syncXpDevour(m, supplier);
        supplier.get().setPacketHandled(true);
    }
}
