package by.jackraidenph.dragonsurvival.blocks;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.entity.MagicalPredatorEntity;
import by.jackraidenph.dragonsurvival.handlers.BlockInit;
import by.jackraidenph.dragonsurvival.handlers.EntityTypesInit;
import by.jackraidenph.dragonsurvival.handlers.ItemsInit;
import by.jackraidenph.dragonsurvival.tiles.PredatorStarTileEntity;
import by.jackraidenph.dragonsurvival.util.ConfigurationHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderType;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;

import java.util.Random;

public class PredatorStarBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D);

    public PredatorStarBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context) {
        return SHAPE;
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }

    public void blockBehaviour(Entity entity, World worldIn, BlockPos pos) {
        if (entity instanceof LivingEntity) {
            entity.hurt(DamageSource.DRY_OUT, ((LivingEntity) entity).getHealth() + 1);
            worldIn.destroyBlock(pos, false);
            if (new Random().nextInt(3) == 0) {
                MagicalPredatorEntity beast = EntityTypesInit.MAGICAL_BEAST.create(worldIn);
                worldIn.addFreshEntity(beast);
                beast.teleportTo(pos.getX(), pos.getY(), pos.getZ());
            }
        } else if (entity instanceof ItemEntity) {
            ItemEntity itemEntity = (ItemEntity) entity;
            if (itemEntity.getItem().getItem() == ItemsInit.elderDragonBone) {
                itemEntity.setItem(new ItemStack(ItemsInit.starBone));
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
    public void entityInside(BlockState state, World worldIn, BlockPos pos, Entity entityIn) {
        super.entityInside(state, worldIn, pos, entityIn);
        if (!(entityIn instanceof MagicalPredatorEntity))
            this.blockBehaviour(entityIn, worldIn, pos);
    }

    @Override
    public void attack(BlockState state, World worldIn, BlockPos pos, PlayerEntity player) {
    	// TODO Should be able to do "player.getMainHandItem().isCorrectToolForDrops(state)" but always returns false for some reason
        if (!ConfigurationHandler.mineStarBlock.get() || !(player.getMainHandItem().getToolTypes().contains(ToolType.HOE) && EnchantmentHelper.getItemEnchantmentLevel(Enchantments.SILK_TOUCH, player.getMainHandItem()) > 0)) { 
        	this.blockBehaviour(player, worldIn, pos); 
        }
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState state) {
    	return PushReaction.IGNORE;
    }
}
