package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.entity.MagicalPredatorEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketSyncPredatorStats implements IMessage<PacketSyncPredatorStats> {

    private float size;
    private int type;
    private int id;

    public PacketSyncPredatorStats() {
    }

    public PacketSyncPredatorStats(int type, float size, int id) {
        this.type = type;
        this.size = size;
        this.id = id;
    }

    @Override
    public void encode(PacketSyncPredatorStats m, PacketBuffer b) {
        b.writeInt(m.type);
        b.writeFloat(m.size);
        b.writeInt(m.id);
    }

    @Override
    public PacketSyncPredatorStats decode(PacketBuffer b) {
        return new PacketSyncPredatorStats(
                b.readInt(),
                b.readFloat(),
                b.readInt());
    }

    @Override
    public void handle(PacketSyncPredatorStats m, Supplier<NetworkEvent.Context> supplier) {
        World world = Minecraft.getInstance().world;
        ((MagicalPredatorEntity)world.getEntityByID(m.id)).size = m.size;
        ((MagicalPredatorEntity)world.getEntityByID(m.id)).type = m.type;
        supplier.get().setPacketHandled(true);
    }
}
