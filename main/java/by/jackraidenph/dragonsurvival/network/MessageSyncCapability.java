package by.jackraidenph.dragonsurvival.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSyncCapability implements IMessage<MessageSyncCapability> {

    private boolean isDragon = false;
    private String dragonType = "";
    private int dragonLevel = 0;

    public MessageSyncCapability() {
    }

    public MessageSyncCapability(boolean isDragon, String dragonType, int dragonLevel) {
        this.isDragon = isDragon;
        this.dragonType = dragonType;
        this.dragonLevel = dragonLevel;
    }

    @Override
    public void encode(MessageSyncCapability message, PacketBuffer buffer) {
        buffer.writeBoolean(isDragon);
        buffer.writeString(dragonType);
        buffer.writeInt(dragonLevel);
    }

    @Override
    public MessageSyncCapability decode(PacketBuffer buffer) {
        return new MessageSyncCapability(buffer.readBoolean(), buffer.readString(), buffer.readInt());
    }

    @Override
    public void handle(MessageSyncCapability message, Supplier<NetworkEvent.Context> supplier) {

        supplier.get().setPacketHandled(true);
    }
}
