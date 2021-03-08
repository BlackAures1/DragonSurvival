package by.jackraidenph.dragonsurvival.network;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ToggleWings implements IMessage<ToggleWings> {
    public boolean state;

    public ToggleWings() {
    }

    public ToggleWings(boolean state) {
        this.state = state;
    }

    @Override
    public void encode(ToggleWings message, PacketBuffer buffer) {
        buffer.writeBoolean(message.state);
    }

    @Override
    public ToggleWings decode(PacketBuffer buffer) {
        return new ToggleWings(buffer.readBoolean());
    }

    @Override
    public void handle(ToggleWings message, Supplier<NetworkEvent.Context> supplier) {
        supplier.get().setPacketHandled(true);
    }
}
