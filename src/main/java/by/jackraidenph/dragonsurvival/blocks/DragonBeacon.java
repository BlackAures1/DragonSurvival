package by.jackraidenph.dragonsurvival.blocks;

import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.registration.BlockInit;
import by.jackraidenph.dragonsurvival.registration.TileEntityTypesInit;
import by.jackraidenph.dragonsurvival.tiles.DragonBeaconEntity;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

public class DragonBeacon extends Block {
    public DragonBeacon(Properties p_i48440_1_) {
        super(p_i48440_1_);
    }

    @Override
    public ActionResultType use(BlockState blockState, World world, BlockPos pos, PlayerEntity playerEntity, Hand hand, BlockRayTraceResult p_225533_6_) {
        ItemStack itemStack = playerEntity.getItemInHand(hand);
        Item item = itemStack.getItem();
        //upgrading
        if (item == Items.GOLD_BLOCK) {
            world.setBlockAndUpdate(pos, BlockInit.peaceDragonBeacon.defaultBlockState());
            DragonBeaconEntity dragonBeaconEntity = (DragonBeaconEntity) world.getBlockEntity(pos);
            dragonBeaconEntity.type = DragonBeaconEntity.Type.PEACE;
            itemStack.shrink(1);
            return ActionResultType.SUCCESS;
        } else if (item == Items.DIAMOND_BLOCK) {
            world.setBlockAndUpdate(pos, BlockInit.magicDragonBeacon.defaultBlockState());
            DragonBeaconEntity dragonBeaconEntity = (DragonBeaconEntity) world.getBlockEntity(pos);
            dragonBeaconEntity.type = DragonBeaconEntity.Type.MAGIC;
            itemStack.shrink(1);
            return ActionResultType.SUCCESS;
        } else if (item == Items.NETHERITE_INGOT) {
            world.setBlockAndUpdate(pos, BlockInit.vetoDragonBeacon.defaultBlockState());
            DragonBeaconEntity dragonBeaconEntity = (DragonBeaconEntity) world.getBlockEntity(pos);
            dragonBeaconEntity.type = DragonBeaconEntity.Type.VETO;
            itemStack.shrink(1);
            return ActionResultType.SUCCESS;
        }
        //apply temporary benefits
        if (itemStack.isEmpty()) {
            LazyOptional<DragonStateHandler> dragonState = DragonStateProvider.getCap(playerEntity);
            if (dragonState.isPresent()) {
                DragonStateHandler dragonStateHandler = dragonState.orElse(null);
                if (dragonStateHandler.isDragon()) {
                    if (this == BlockInit.peaceDragonBeacon && dragonStateHandler.getType() == DragonType.SEA) {
                        //apply effect
                        return ActionResultType.SUCCESS;
                    } else if (this == BlockInit.magicDragonBeacon && dragonStateHandler.getType() == DragonType.FOREST) {
                        //apply effect
                        return ActionResultType.SUCCESS;
                    } else if (this == BlockInit.vetoDragonBeacon && dragonStateHandler.getType() == DragonType.CAVE) {
                        //apply effect
                        return ActionResultType.SUCCESS;
                    }
                }
            }

        }
        playerEntity.hurt(DamageSource.GENERIC, 1);
        return ActionResultType.SUCCESS;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return TileEntityTypesInit.dragonBeacon.create();
    }
}
