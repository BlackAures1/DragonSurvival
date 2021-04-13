package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.util.DragonLevel;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.server.TicketType;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Sets a spawn point for a player. Doesn't work in super-flat world properly
 */
@Deprecated
public class SetRespawnPosition implements IMessage<SetRespawnPosition> {
    BlockPos position;

    public SetRespawnPosition() {
    }

    public SetRespawnPosition(BlockPos position) {
        this.position = position;
    }

    @Override
    public void encode(SetRespawnPosition message, PacketBuffer buffer) {
        buffer.writeBlockPos(message.position);
    }

    @Override
    public SetRespawnPosition decode(PacketBuffer buffer) {
        return new SetRespawnPosition(buffer.readBlockPos());
    }

    @Override
    public void handle(SetRespawnPosition message, Supplier<NetworkEvent.Context> supplier) {
        ServerPlayerEntity serverPlayerEntity = supplier.get().getSender();
        BlockPos.Mutable spawnPosition = new BlockPos.Mutable(message.position);
        ServerWorld serverWorld = serverPlayerEntity.getServerWorld();
        serverWorld.getChunkProvider().registerTicket(TicketType.POST_TELEPORT, new ChunkPos(spawnPosition), 1, serverPlayerEntity.getEntityId());
        spawnPosition.setY(250);
        IChunk chunk = serverWorld.getChunk(spawnPosition);
        while (serverWorld.getBlockState(spawnPosition).isAir(serverWorld, spawnPosition)) {
            spawnPosition.setY(spawnPosition.getY() - 1);
        }
        //correct, do not change
        spawnPosition.add(0, 1, 0);
        //TODO prevent damage
        serverPlayerEntity.connection.setPlayerLocation(spawnPosition.getX() + 0.5, spawnPosition.getY() + 1, spawnPosition.getZ() + 0.5, 0, 0);
        serverPlayerEntity.setSpawnPoint(spawnPosition.up(), true, false, DimensionType.OVERWORLD);
        serverPlayerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(DragonLevel.BABY.initialHealth);
    }
}
