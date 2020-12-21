package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncCapabilityMovement implements IMessage<PacketSyncCapabilityMovement> {

    public int playerId;
    public double bodyYaw;
    public double headYaw;
    public double headPitch;
    public Vec3d headPos;
    public Vec3d tailPos;

    public PacketSyncCapabilityMovement() {
    }

    public PacketSyncCapabilityMovement(int playerId, double bodyYaw,
                                        double headYaw,
                                        double headPitch,
                                        Vec3d headPos,
                                        Vec3d tailPos) {
        this.bodyYaw = bodyYaw;
        this.headYaw = headYaw;
        this.headPitch = headPitch;
        this.headPos = headPos;
        this.tailPos = tailPos;
        this.playerId = playerId;
    }

//    public PacketSyncCapabilityMovement(DragonStateHandler.DragonMovementData data) {
//        this.bodyYaw = data.bodyYaw;
//        this.headYaw = data.headYaw;
//        this.headPitch = data.headPitch;
//        this.headPos = data.headPos;
//        this.tailPos = data.tailPos;
//    }

    @Override
    public void encode(PacketSyncCapabilityMovement m, PacketBuffer b) {
        b.writeInt(m.playerId);
        b.writeDouble(m.bodyYaw);
        b.writeDouble(m.headYaw);
        b.writeDouble(m.headPitch);
        writeVec3d(b, m.headPos);
        writeVec3d(b, m.tailPos);
    }

    @Override
    public PacketSyncCapabilityMovement decode(PacketBuffer b) {
        return new PacketSyncCapabilityMovement(b.readInt(),
                b.readDouble(),
                b.readDouble(),
                b.readDouble(),
                readVec3d(b), readVec3d(b));
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
        return new Vec3d(x, y, z);
    }

    @Override
    public void handle(PacketSyncCapabilityMovement syncCapabilityMovement, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        ClientPlayerEntity thisPlayer = Minecraft.getInstance().player;
        World world = thisPlayer.world;
        Entity entity = world.getEntityByID(syncCapabilityMovement.playerId);
        if (entity instanceof PlayerEntity) {
            AbstractClientPlayerEntity otherPlayer = (AbstractClientPlayerEntity) entity;
            if (otherPlayer.getMotion().x != 0 || otherPlayer.getMotion().z != 0) {
                DragonStateProvider.getCap(otherPlayer).ifPresent(dragonStateHandler -> {
                    dragonStateHandler.setMovementData(syncCapabilityMovement.bodyYaw, syncCapabilityMovement.headYaw, syncCapabilityMovement.headPitch, syncCapabilityMovement.headPos, syncCapabilityMovement.tailPos);
                });
            }
            context.setPacketHandled(true);
        }

    }
}
