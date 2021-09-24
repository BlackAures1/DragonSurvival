package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.handlers.EntityTypesInit;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.WorldEntitySpawner;

import javax.annotation.Nullable;

public class Functions {

    public static int secondsToTicks(int seconds)
    {
        return seconds * 20;
    }

    public static int minutesToTicks(int minutes)
    {
        return secondsToTicks(minutes)*60;
    }

    public static int ticksToSeconds(int ticks) {
        return ticks / 20;
    }

    public static int ticksToMinutes(int ticks) {
        return ticksToSeconds(ticks) / 60;
    }

    @Nullable
    public static BlockPos findRandomSpawnPosition(PlayerEntity playerEntity, int p_221298_1_, int timesToCheck, float distance) {
        int i = (p_221298_1_ == 0) ? 2 : (2 - p_221298_1_);
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable();

        for (int i1 = 0; i1 < timesToCheck; i1++) {
            float f = playerEntity.level.random.nextFloat() * 6.2831855F;
            double xRandom = playerEntity.getX() + MathHelper.floor(MathHelper.cos(f) * distance * i) + playerEntity.level.random.nextInt(5);
            double zRandom = playerEntity.getZ() + MathHelper.floor(MathHelper.sin(f) * distance * i) + playerEntity.level.random.nextInt(5);
            int y = playerEntity.level.getHeight(Heightmap.Type.WORLD_SURFACE, (int) xRandom, (int) zRandom);
            blockpos$mutable.set(xRandom, y, zRandom);
            if (playerEntity.level.hasChunksAt(blockpos$mutable.getX() - 10, blockpos$mutable.getY() - 10, blockpos$mutable.getZ() - 10, blockpos$mutable.getX() + 10, blockpos$mutable.getY() + 10, blockpos$mutable.getZ() + 10) && playerEntity.level.getChunkSource().isEntityTickingChunk(new ChunkPos((BlockPos) blockpos$mutable)) && (WorldEntitySpawner.canSpawnAtBody(EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, playerEntity.level, (BlockPos) blockpos$mutable, EntityTypesInit.HUNTER_HOUND) || (playerEntity.level.getBlockState(blockpos$mutable).is(Blocks.SNOW) && playerEntity.level.getBlockState((BlockPos) blockpos$mutable).isAir()))) {
                return (BlockPos) blockpos$mutable;
            }
        }
        return null;
    }

    public static void spawn(MobEntity mobEntity, BlockPos blockPos, ServerWorld serverWorld) {
        mobEntity.setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
        mobEntity.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(blockPos), SpawnReason.NATURAL, null, null);
        serverWorld.addFreshEntity(mobEntity);
    }

    public static boolean isAirOrFluid(BlockPos blockPos, World world) {
        return !world.getFluidState(blockPos).isEmpty() || world.isEmptyBlock(blockPos);
    }

}
