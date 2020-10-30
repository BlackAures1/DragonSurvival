package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.capability.PlayerStateHandler;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.entity.MagicalPredatorEntity;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapability;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.network.PacketSyncPredatorStats;
import by.jackraidenph.dragonsurvival.network.PacketSyncXPDevour;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientProxy implements Proxy {

    @Override
    public DistExecutor.SafeRunnable syncMovement(PacketSyncCapabilityMovement m, Supplier<NetworkEvent.Context> supplier) {
        return () -> {
            if (Minecraft.getInstance().player != null)
                PlayerStateProvider.getCap(Minecraft.getInstance().player).ifPresent(cap -> cap.setMovementData(new PlayerStateHandler.DragonMovementData(m.bodyYaw, m.headYaw, m.headPitch, m.headPos, m.tailPos), false));
        };
    }

    @Override
    public DistExecutor.SafeRunnable syncCapability(PacketSyncCapability m, Supplier<NetworkEvent.Context> supplier) {
        return () -> {
            if (Minecraft.getInstance().player != null) {
                PlayerStateProvider.getCap(Minecraft.getInstance().player).ifPresent(cap -> {
                    cap.setIsDragon(m.isDragon);
                    cap.setType(m.type);
                    cap.setLevel(m.level);
                });
            }
        };
    }

    @Override
    public DistExecutor.SafeRunnable syncXpDevour(PacketSyncXPDevour m, Supplier<NetworkEvent.Context> supplier) {
        return () -> {
            World world = Minecraft.getInstance().world;
            if (world != null) {
                ExperienceOrbEntity xpOrb = (ExperienceOrbEntity) (world.getEntityByID(m.xp));
                MagicalPredatorEntity entity = (MagicalPredatorEntity) world.getEntityByID(m.entity);
                if (xpOrb != null && entity != null) {
                    entity.size += xpOrb.getXpValue() / 100.0F;
                    entity.size = MathHelper.clamp(entity.size, 0.95F, 1.95F);
                    world.addParticle(ParticleTypes.SMOKE, xpOrb.getPosX(), xpOrb.getPosY(), xpOrb.getPosZ(), 0, world.getRandom().nextFloat() / 12.5f, 0);
                    xpOrb.remove();
                }
            }
        };
    }

    @Override
    public DistExecutor.SafeRunnable syncPredatorStats(PacketSyncPredatorStats m, Supplier<NetworkEvent.Context> supplier) {
        return () -> {
            World world = Minecraft.getInstance().world;
            if (world != null) {
                Entity entity = world.getEntityByID(m.id);
                ((MagicalPredatorEntity) entity).size = m.size;
                ((MagicalPredatorEntity) entity).type = m.type;
            }
        };
    }
}
