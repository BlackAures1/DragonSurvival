package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.nest.NestEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SynchronizeNest implements IMessage<SynchronizeNest> {
    private BlockPos pos;
    private int health;
    private int cooldown;

    public SynchronizeNest(BlockPos nestPos, int nestHealth, int cooldown) {
        pos = nestPos;
        health = nestHealth;
        this.cooldown = cooldown;
    }

    public SynchronizeNest() {

    }

    @Override
    public void encode(SynchronizeNest message, PacketBuffer buffer) {
        buffer.writeBlockPos(message.pos);
        buffer.writeInt(message.health);
        buffer.writeInt(message.cooldown);
    }

    @Override
    public SynchronizeNest decode(PacketBuffer buffer) {
        return new SynchronizeNest(buffer.readBlockPos(), buffer.readInt(), buffer.readInt());
    }

    @Override
    public void handle(SynchronizeNest message, Supplier<NetworkEvent.Context> supplier) {
        PlayerEntity player = Minecraft.getInstance().player;
        ClientWorld world = Minecraft.getInstance().world;
        TileEntity entity = world.getTileEntity(message.pos);
        if (entity instanceof NestEntity) {
            NestEntity nestEntity = (NestEntity) entity;
            nestEntity.health = message.health;
            nestEntity.damageCooldown = message.cooldown;
            nestEntity.markDirty();
            if (nestEntity.health <= 0) {
                world.playSound(player, message.pos, SoundEvents.BLOCK_METAL_BREAK, SoundCategory.BLOCKS, 1, 1);
            } else {
                world.playSound(player, message.pos, SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.BLOCKS, 1, 1);
            }
        }
        supplier.get().setPacketHandled(true);
    }
}
