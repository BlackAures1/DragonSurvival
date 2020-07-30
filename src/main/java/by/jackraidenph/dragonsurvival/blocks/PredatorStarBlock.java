package by.jackraidenph.dragonsurvival.blocks;

import by.jackraidenph.dragonsurvival.entity.MagicalBeastEntity;
import by.jackraidenph.dragonsurvival.handlers.EntityTypesInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class PredatorStarBlock extends Block {
    public PredatorStarBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
        this.setRegistryName("predator_star");
    }

    public void blockBehaviour(Entity entity, World worldIn, BlockPos pos) {
        if (entity instanceof LivingEntity) {
            entity.attackEntityFrom(DamageSource.DRYOUT, ((LivingEntity) entity).getHealth());
            worldIn.destroyBlock(pos, false);
            if (new Random().nextInt(3) == 0) {
                MagicalBeastEntity beast = EntityTypesInit.MAGICAL_BEAST.get().create(worldIn);
                worldIn.addEntity(beast);
                beast.setPositionAndUpdate(pos.getX(), pos.getY(), pos.getZ());
            }
        }
    }

    @Override
    public void onEntityWalk(World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityWalk(worldIn, pos, entityIn);
        this.blockBehaviour(entityIn, worldIn, pos);
    }

    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        super.onBlockClicked(state, worldIn, pos, player);
        this.blockBehaviour(player, worldIn, pos);
    }

    public static class CallEntity extends Goal {

        LivingEntity entity;
        int distance;

        public CallEntity(LivingEntity entityIn, int distanceIn) {
            this.entity = entityIn;
            this.distance = distanceIn;
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }
    }

}
