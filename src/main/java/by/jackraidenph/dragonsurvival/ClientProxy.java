package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.entity.MagicalPredatorEntity;
import by.jackraidenph.dragonsurvival.nest.NestEntity;
import by.jackraidenph.dragonsurvival.network.PacketSyncPredatorStats;
import by.jackraidenph.dragonsurvival.network.PacketSyncXPDevour;
import by.jackraidenph.dragonsurvival.network.SynchronizeNest;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ExperienceOrbEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;
@Deprecated
public class ClientProxy {

    public void syncXpDevour(PacketSyncXPDevour m, Supplier<NetworkEvent.Context> supplier) {

        World world = Minecraft.getInstance().world;
        if (world != null) {
            ExperienceOrbEntity xpOrb = (ExperienceOrbEntity) (world.getEntityByID(m.xp));
            MagicalPredatorEntity entity = (MagicalPredatorEntity) world.getEntityByID(m.entity);
            if (xpOrb != null && entity != null) {
                entity.size += xpOrb.getXpValue() / 100.0F;
                entity.size = MathHelper.clamp(entity.size, 0.95F, 1.95F);
                world.addParticle(ParticleTypes.SMOKE, xpOrb.getPosX(), xpOrb.getPosY(), xpOrb.getPosZ(), 0, world.getRandom().nextFloat() / 12.5f, 0);
                xpOrb.remove();
                supplier.get().setPacketHandled(true);
            }
        }
    }

    public void syncPredatorStats(PacketSyncPredatorStats m, Supplier<NetworkEvent.Context> supplier) {

        World world = Minecraft.getInstance().world;
        if (world != null) {
            Entity entity = world.getEntityByID(m.id);
            if (entity != null) {
                ((MagicalPredatorEntity) entity).size = m.size;
                ((MagicalPredatorEntity) entity).type = m.type;
                supplier.get().setPacketHandled(true);
            }
        }
    }

    public void syncNest(SynchronizeNest synchronizeNest, Supplier<NetworkEvent.Context> contextSupplier) {
        PlayerEntity player = Minecraft.getInstance().player;
        ClientWorld world = Minecraft.getInstance().world;
        TileEntity entity = world.getTileEntity(synchronizeNest.pos);
        if (entity instanceof NestEntity) {
            NestEntity nestEntity = (NestEntity) entity;
            nestEntity.energy = synchronizeNest.health;
            nestEntity.damageCooldown = synchronizeNest.cooldown;
            nestEntity.markDirty();
            if (nestEntity.energy <= 0) {
                world.playSound(player, synchronizeNest.pos, SoundEvents.BLOCK_METAL_BREAK, SoundCategory.BLOCKS, 1, 1);
            } else {
                world.playSound(player, synchronizeNest.pos, SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.BLOCKS, 1, 1);
            }
            contextSupplier.get().setPacketHandled(true);
        }
    }
}
