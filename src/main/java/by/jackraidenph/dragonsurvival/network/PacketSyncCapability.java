package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncCapability implements IMessage<PacketSyncCapability> {

    private boolean isDragon;
    private DragonType type;
    private int level;

    public PacketSyncCapability() {
    }

    public PacketSyncCapability(boolean isDragon, DragonType type, int level) {
        this.isDragon = isDragon;
        this.type = type;
        this.level = level;
    }

    @Override
    public void encode(PacketSyncCapability m, PacketBuffer b) {
        b.writeBoolean(m.isDragon);
        b.writeEnumValue(m.type);
        b.writeInt(m.level);
    }

    @Override
    public PacketSyncCapability decode(PacketBuffer b) {
        return new PacketSyncCapability(
                b.readBoolean(),
                b.readEnumValue(DragonType.class),
                b.readInt());
    }

    @Override
    public void handle(PacketSyncCapability m, Supplier<NetworkEvent.Context> supplier) {
        PlayerEntity player = supplier.get().getDirection().getReceptionSide().isServer() ? supplier.get().getSender() : Minecraft.getInstance().player;

        if (player == null)
            return;
        
        PlayerStateProvider.getCap(player).ifPresent(cap -> {
            cap.setIsDragon(m.isDragon);
            cap.setType(m.type);
            cap.setLevel(m.level);
        });

        supplier.get().setPacketHandled(true);
    }
}
