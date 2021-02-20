package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.PacketProxy;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Synchronizes dragon level
 */
public class SyncLevel implements IMessage<SyncLevel> {

    public int playerId;
    public DragonLevel level;

    public SyncLevel(int playerId, DragonLevel level) {
        this.playerId = playerId;
        this.level = level;
    }

    public SyncLevel() {

    }

    @Override
    public void encode(SyncLevel message, PacketBuffer buffer) {
        buffer.writeInt(message.playerId);
        buffer.writeEnumValue(message.level);
    }

    @Override
    public SyncLevel decode(PacketBuffer buffer) {
        return new SyncLevel(buffer.readInt(), buffer.readEnumValue(DragonLevel.class));
    }

    @Override
    public void handle(SyncLevel message, Supplier<NetworkEvent.Context> supplier) {
        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> new PacketProxy().updateLevel(message, supplier));
    }
}
