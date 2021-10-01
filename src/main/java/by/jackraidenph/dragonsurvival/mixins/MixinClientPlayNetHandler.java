package by.jackraidenph.dragonsurvival.mixins;

import by.jackraidenph.dragonsurvival.handlers.EntityTypesInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.play.ClientPlayNetHandler;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketThreadUtil;
import net.minecraft.network.play.server.SSpawnObjectPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Deprecated
@Mixin({ClientPlayNetHandler.class})
public abstract class MixinClientPlayNetHandler {
    @Inject(at = {@At("HEAD")}, method = {"(Lnet/minecraft/network/play/server/SSpawnObjectPacket;)V"})
    private void addCustomEntitySpawn(SSpawnObjectPacket spawnObjectPacket, CallbackInfo callbackInfo) {
        EntityType<?> entityType = spawnObjectPacket.getType();

        if (entityType == EntityTypesInit.BOLAS_ENTITY) {
            ClientPlayNetHandler clientPlayNetHandler = (ClientPlayNetHandler) (Object) this;
            PacketThreadUtil.ensureRunningOnSameThread(spawnObjectPacket, clientPlayNetHandler, Minecraft.getInstance());
            double d0 = spawnObjectPacket.getX();
            double d1 = spawnObjectPacket.getY();
            double d2 = spawnObjectPacket.getZ();
            Entity entity = EntityTypesInit.BOLAS_ENTITY.create(clientPlayNetHandler.getLevel());
            int i = spawnObjectPacket.getId();
            entity.setPacketCoordinates(d0, d1, d2);
            entity.moveTo(d0, d1, d2);
            entity.xRot = (spawnObjectPacket.getxRot() * 360) / 256.0F;
            entity.yRot = (spawnObjectPacket.getyRot() * 360) / 256.0F;
            entity.setId(i);
            entity.setUUID(spawnObjectPacket.getUUID());
            clientPlayNetHandler.getLevel().putNonPlayerEntity(i, entity);
        }
    }
}
