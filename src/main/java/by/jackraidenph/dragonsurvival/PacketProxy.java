package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class PacketProxy {

    public static void handleCapabilitySync(PacketSyncCapabilityMovement syncCapabilityMovement, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        ClientPlayerEntity thisPlayer = Minecraft.getInstance().player;
        if (thisPlayer != null) {
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
}
