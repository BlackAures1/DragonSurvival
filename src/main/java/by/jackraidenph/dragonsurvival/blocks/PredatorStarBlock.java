package by.jackraidenph.dragonsurvival.blocks;

import by.jackraidenph.dragonsurvival.entity.MagicalBeastEntity;
import by.jackraidenph.dragonsurvival.handlers.BlockInit;
import by.jackraidenph.dragonsurvival.handlers.EntityTypesInit;
import by.jackraidenph.dragonsurvival.tiles.PredatorStarTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import java.util.List;
import java.util.Random;

public class PredatorStarBlock extends Block {
    public PredatorStarBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
        this.setRegistryName("predator_star");
    }

    @Override
    public BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
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
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {

        return new PredatorStarTileEntity();
    }

    @Override
    public void onEntityCollision(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        super.onEntityCollision(state, worldIn, pos, entityIn);
        if (!(entityIn instanceof MagicalBeastEntity))
            this.blockBehaviour(entityIn, worldIn, pos);
    }

    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        super.onBlockClicked(state, worldIn, pos, player);
        this.blockBehaviour(player, worldIn, pos);
    }

    @Override
    public int tickRate(IWorldReader worldIn) {
        return 1;
    }

    public static class CallEntity extends Goal {

        CreatureEntity entity;
        BlockPos pos;

        public CallEntity(CreatureEntity entityIn, BlockPos posIn) {
            this.entity = entityIn;
            this.pos = posIn;
        }

        @Override
        public boolean shouldContinueExecuting() {
            return entity.world.getBlockState(pos).getBlock() == BlockInit.PREDATOR_STAR_BLOCK;
        }

        @Override
        public void startExecuting() {
            super.startExecuting();
            this.entity.getNavigator().tryMoveToXYZ(pos.getX(), pos.getY(), pos.getZ(), 0.88);
        }

        @Override
        public boolean shouldExecute() {
            return entity.world.getBlockState(pos).getBlock() == BlockInit.PREDATOR_STAR_BLOCK;
        }
    }

}
