package by.jackraidenph.dragonsurvival.network;
import by.jackraidenph.dragonsurvival.PacketProxy;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Synchronizes dragon level and size
 */
public class SyncSize implements IMessage<SyncSize> {

    public int playerId;
    public float size;

    public SyncSize(int playerId, float size) {
        this.playerId = playerId;
        this.size = size;
    }

    public SyncSize() {

    }

    @Override
    public void encode(SyncSize message, PacketBuffer buffer) {
        buffer.writeInt(message.playerId);
        buffer.writeFloat(message.size);
    }

    @Override
    public SyncSize decode(PacketBuffer buffer) {
        return new SyncSize(buffer.readInt(), buffer.readFloat());
    }

    @Override
    public void handle(SyncSize message, Supplier<NetworkEvent.Context> supplier) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> new PacketProxy().updateSize(message, supplier));
    }
}
