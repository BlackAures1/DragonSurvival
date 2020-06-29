package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.capability.IPlayerStateHandler;
import by.jackraidenph.dragonsurvival.capability.PlayerStateHandler;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class MessageSyncCapability implements IMessage<MessageSyncCapability> {

    DragonType type;
    int level;
    double bodyYaw;
    double neckLength;
    double headYaw;
    double headPitch;
    Vec3d headPos;
    Vec3d tailPos;

    public MessageSyncCapability() {
    }

    public MessageSyncCapability(DragonType type,
                                 int level,
                                 double bodyYaw,
                                 double neckLength,
                                 double headYaw,
                                 Vec3d headPos,
                                 Vec3d tailPos) {
        this.type = type;
        this.level = level;
        this.bodyYaw = bodyYaw;
        this.neckLength = neckLength;
        this.headYaw = headYaw;
        this.headPos = headPos;
        this.tailPos = tailPos;
    }

    public MessageSyncCapability(PlayerStateHandler.DragonData data) {
        this.type = data.getType();
        this.level = data.getLevel();
        this.bodyYaw = (double) data.getMovementData().get("bodyYaw");
        this.neckLength = data.getNeckLength();
        this.headYaw = (double) data.getMovementData().get("headYaw");
        this.headPos = (Vec3d) data.getMovementData().get("headPos");
        this.tailPos = (Vec3d) data.getMovementData().get("tailPos");
    }

    @Override
    public void encode(MessageSyncCapability m, PacketBuffer b) {
        b.writeEnumValue(m.type);
        b.writeInt(m.level);
        b.writeDouble(m.bodyYaw);
        b.writeDouble(m.neckLength);
        b.writeDouble(m.headYaw);
        writeVec3d(b, m.headPos);
        writeVec3d(b, m.tailPos);
    }

    @Override
    public MessageSyncCapability decode(PacketBuffer b) {
        return new MessageSyncCapability(
                b.readEnumValue(DragonType.class),
                b.readInt(),
                b.readDouble(),
                b.readDouble(),
                b.readDouble(),
                readVec3d(b),
                readVec3d(b));
    }

    private void writeVec3d(PacketBuffer buffer, Vec3d vec) {
        buffer.writeDouble(vec.x);
        buffer.writeDouble(vec.y);
        buffer.writeDouble(vec.z);
    }

    private Vec3d readVec3d(PacketBuffer buffer) {
        double x = buffer.readDouble();
        double y = buffer.readDouble();
        double z = buffer.readDouble();
        Vec3d vec = new Vec3d(x, y, z);
        return vec;
    }

    @Override
    public void handle(MessageSyncCapability m, Supplier<NetworkEvent.Context> supplier) {
        PlayerEntity player;
        player = supplier.get().getDirection().getReceptionSide().isServer() ? supplier.get().getSender() : Minecraft.getInstance().player;
        IPlayerStateHandler cap;
        if (player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).isPresent()) {
            cap = player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).orElse(null);
            cap.setData(new PlayerStateHandler.DragonData(m.type, m.level, m.bodyYaw, m.neckLength, m.headYaw, m.headPitch, m.headPos, m.tailPos));
        }
        supplier.get().setPacketHandled(true);
    }
}
