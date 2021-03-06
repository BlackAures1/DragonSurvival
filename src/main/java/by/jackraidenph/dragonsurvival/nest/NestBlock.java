package by.jackraidenph.dragonsurvival.nest;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.handlers.TileEntityTypesInit;
import by.jackraidenph.dragonsurvival.network.SynchronizeNest;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;

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
        if (!worldIn.isRemote()) {
//        if (nestEntity.damageCooldown <= 0)
            {
                double damage = player.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getValue();
                nestEntity.health -= Math.min(damage, 10);
                DragonSurvivalMod.CHANNEL.send(PacketDistributor.NEAR.with(() -> new PacketDistributor.TargetPoint(pos.getX(), pos.getY(), pos.getZ(), 40, worldIn.getDimension().getType())), new SynchronizeNest(nestEntity.getPos(), nestEntity.health, nestEntity.damageCooldown));
                if (nestEntity.health <= 0) {
                    worldIn.playSound(player, pos, SoundEvents.BLOCK_ANVIL_DESTROY, SoundCategory.BLOCKS, 1, 1);
                    worldIn.destroyBlock(pos, false);
                } else {
                    worldIn.playSound(player, pos, SoundEvents.ITEM_SHIELD_BLOCK, SoundCategory.BLOCKS, 1, 1);
                    nestEntity.damageCooldown = NestEntity.COOLDOWN_TIME;
                }
                nestEntity.markDirty();
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

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (player instanceof ServerPlayerEntity) {
            NetworkHooks.openGui((ServerPlayerEntity) player, getBlockEntity(worldIn, pos), packetBuffer -> packetBuffer.writeBlockPos(pos));
        }
        return ActionResultType.SUCCESS;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        NestEntity nestEntity = getBlockEntity(worldIn, pos);
        if (nestEntity.ownerUUID == null && placer instanceof PlayerEntity)
            nestEntity.ownerUUID = placer.getUniqueID();
    }
}
