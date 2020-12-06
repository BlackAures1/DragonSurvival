package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.ClientProxy;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncCapability implements IMessage<PacketSyncCapability> {

    public boolean isDragon;
    public DragonType type;
    public DragonLevel level;
    public boolean isHiding;

    public PacketSyncCapability() {
    }

    public PacketSyncCapability(boolean isDragon, boolean isHiding, DragonType type, DragonLevel level) {
        this.isDragon = isDragon;
        this.isHiding = isHiding;
        this.type = type;
        this.level = level;
    }

    @Override
    public void encode(PacketSyncCapability m, PacketBuffer b) {
        b.writeBoolean(m.isDragon);
        b.writeBoolean(m.isHiding);
        b.writeEnumValue(m.type);
        b.writeEnumValue(m.level);
    }

    @Override
    public PacketSyncCapability decode(PacketBuffer b) {
        return new PacketSyncCapability(
                b.readBoolean(),
                b.readBoolean(),
                b.readEnumValue(DragonType.class),
                b.readEnumValue(DragonLevel.class));
    }

    @Override
    public void handle(PacketSyncCapability packetSyncCapability, Supplier<NetworkEvent.Context> supplier) {
        System.out.println(supplier.get().getDirection());
        System.out.println(supplier.get().getSender());
        new ClientProxy().syncCapability(packetSyncCapability, supplier);
        supplier.get().setPacketHandled(true);
    }
}
