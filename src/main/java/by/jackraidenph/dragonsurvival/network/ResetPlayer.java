package by.jackraidenph.dragonsurvival.network;

import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class ResetPlayer implements IMessage<ResetPlayer> {
    @Override
    public void encode(ResetPlayer message, PacketBuffer buffer) {

    }

    @Override
    public ResetPlayer decode(PacketBuffer buffer) {
        return new ResetPlayer();
    }

    @Override
    public void handle(ResetPlayer message, Supplier<NetworkEvent.Context> supplier) {
        ServerPlayerEntity playerEntity = supplier.get().getSender();
        if (playerEntity != null)
            playerEntity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20);
    }
}
