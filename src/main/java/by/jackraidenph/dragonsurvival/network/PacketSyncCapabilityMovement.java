package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.capability.PlayerStateHandler;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncCapabilityMovement implements IMessage<PacketSyncCapabilityMovement> {

    double bodyYaw;
    double headYaw;
    double headPitch;
    Vec3d headPos;
    Vec3d tailPos;

    public PacketSyncCapabilityMovement() {
    }

    public PacketSyncCapabilityMovement(double bodyYaw,
                                        double headYaw,
                                        double headPitch,
                                        Vec3d headPos,
                                        Vec3d tailPos) {
        this.bodyYaw = bodyYaw;
        this.headYaw = headYaw;
        this.headPitch = headPitch;
        this.headPos = headPos;
        this.tailPos = tailPos;
    }

    public PacketSyncCapabilityMovement(PlayerStateHandler.DragonMovementData data) {
        this.bodyYaw = data.bodyYaw;
        this.headYaw = data.headYaw;
        this.headPitch = data.headPitch;
        this.headPos = data.headPos;
        this.tailPos = data.tailPos;
    }

    @Override
    public void encode(PacketSyncCapabilityMovement m, PacketBuffer b) {
        b.writeDouble(m.bodyYaw);
        b.writeDouble(m.headYaw);
        b.writeDouble(m.headPitch);
        writeVec3d(b, m.headPos);
        writeVec3d(b, m.tailPos);
    }

    @Override
    public PacketSyncCapabilityMovement decode(PacketBuffer b) {
        return new PacketSyncCapabilityMovement(
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
    public void handle(PacketSyncCapabilityMovement m, Supplier<NetworkEvent.Context> supplier) {
        PlayerEntity player = supplier.get().getDirection().getReceptionSide().isServer() ? supplier.get().getSender() : Minecraft.getInstance().player;

        PlayerStateProvider.getCap(player).ifPresent(cap ->
                cap.setMovementData(new PlayerStateHandler.DragonMovementData(m.bodyYaw, m.headYaw, m.headPitch, m.headPos, m.tailPos), false));

        supplier.get().setPacketHandled(true);
    }
}
