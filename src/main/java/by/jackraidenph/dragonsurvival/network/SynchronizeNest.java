package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.ClientProxy;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class SynchronizeNest implements IMessage<SynchronizeNest> {
    public BlockPos pos;
    public int health;
    public int cooldown;

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
        new ClientProxy().syncNest(message, supplier);
    }
}
