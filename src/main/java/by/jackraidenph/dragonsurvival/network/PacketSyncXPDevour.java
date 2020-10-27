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

public class PacketSyncXPDevour implements IMessage<PacketSyncXPDevour> {

    private int entity;
    private int xp;

    public PacketSyncXPDevour() {
    }

    public PacketSyncXPDevour(int entity, int xp) {
        this.entity = entity;
        this.xp = xp;
    }

    @Override
    public void encode(PacketSyncXPDevour m, PacketBuffer b) {
        b.writeInt(m.entity);
        b.writeInt(m.xp);
    }

    @Override
    public PacketSyncXPDevour decode(PacketBuffer b) {
        return new PacketSyncXPDevour(
                b.readInt(),
                b.readInt());
    }

    @Override
    public void handle(PacketSyncXPDevour m, Supplier<NetworkEvent.Context> supplier) {
        World world = Minecraft.getInstance().world;
        ExperienceOrbEntity xpOrb = (ExperienceOrbEntity) (world.getEntityByID(m.xp));
        MagicalPredatorEntity entity = (MagicalPredatorEntity) world.getEntityByID(m.entity);
        if (xpOrb != null && entity != null) {
            entity.size += xpOrb.getXpValue() / 100.0F;
            entity.size = MathHelper.clamp(entity.size, 0.95F, 1.95F);
            world.addParticle(ParticleTypes.SMOKE, xpOrb.getPosX(), xpOrb.getPosY(), xpOrb.getPosZ(), 0, world.getRandom().nextFloat() / 12.5f, 0);
            xpOrb.remove();
        }
        supplier.get().setPacketHandled(true);
    }
}
