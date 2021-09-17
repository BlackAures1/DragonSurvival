package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.handlers.DragonSizeHandler;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.PacketDistributor;

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
        DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SetFlyState(supplier.get().getSender().getId(), message.state));
        DragonSizeHandler.wingsStatusServer.put(supplier.get().getSender().getId(), message.state);
        supplier.get().setPacketHandled(true);
    }
}
