package by.jackraidenph.dragonsurvival.nest;

import by.jackraidenph.dragonsurvival.handlers.TileEntityTypesInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class NestBlock extends Block {
    public NestBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityTypesInit.nestEntityTile.create();
    }

    @Override
    public void onBlockClicked(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
        NestEntity nestEntity = getBlockEntity(worldIn, pos);
        if (nestEntity.damageCooldown <= 0) {
            double damage = player.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
            nestEntity.health -= Math.min(damage, 10);
            if (nestEntity.health <= 0) {
                worldIn.playSound(player, pos, SoundEvents.BLOCK_ANVIL_DESTROY, SoundCategory.BLOCKS, 1, 1);
                onReplaced(state, worldIn, pos, Blocks.AIR.getDefaultState(), false);
//                harvestBlock(worldIn,player,pos,state,nestEntity,player.getHeldItemMainhand());
            } else {
                worldIn.playSound(player, pos, SoundEvents.BLOCK_ANVIL_HIT, SoundCategory.BLOCKS, 1, 1);
                nestEntity.damageCooldown = NestEntity.cooldownTime;
            }
        }
        super.onBlockClicked(state, worldIn, pos, player);
    }

    public NestEntity getBlockEntity(World world, BlockPos pos) {
        return (NestEntity) world.getTileEntity(pos);
    }

    @Override
    public float getPlayerRelativeBlockHardness(BlockState state, PlayerEntity player, IBlockReader worldIn, BlockPos pos) {
        return 0;
    }
}
