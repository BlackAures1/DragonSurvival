package by.jackraidenph.dragonsurvival.nest;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BigNestBlock extends MediumNestBlock {
    public BigNestBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        BlockState superState = super.getStateForPlacement(context);
        if (superState != null) {
            BlockPos blockPos = context.getPos();
            World world = context.getWorld();
            PlayerEntity playerEntity = context.getPlayer();
            Direction direction = playerEntity.getHorizontalFacing();
            if (world.isAirBlock(blockPos.offset(direction.getOpposite())) &&
                    world.isAirBlock(blockPos.offset(direction).offset(direction.rotateY())) &&
                    world.isAirBlock(blockPos.offset(direction.rotateY())) &&
                    world.isAirBlock(blockPos.offset(direction.getOpposite()).offset(direction.rotateYCCW())) &&
                    world.isAirBlock(blockPos.offset(direction.getOpposite()).offset(direction.rotateY())) &&
                    world.isAirBlock(blockPos.offset(direction).up()) &&
                    world.isAirBlock(blockPos.offset(direction).up().offset(direction.rotateY())) &&
                    world.isAirBlock(blockPos.offset(direction).up().offset(direction.rotateYCCW()))
            )
                return superState;
        }
        return null;
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (placer != null) {
            Direction direction = placer.getHorizontalFacing();
            final BlockPos pos1 = pos.offset(direction.getOpposite());
            worldIn.setBlockState(pos1, state.with(PRIMARY_BLOCK, false));
            final BlockPos pos2 = pos.offset(direction).offset(direction.rotateY());
            worldIn.setBlockState(pos2, state.with(PRIMARY_BLOCK, false));
            final BlockPos pos3 = pos.offset(direction.rotateY());
            worldIn.setBlockState(pos3, state.with(PRIMARY_BLOCK, false));
            final BlockPos pos4 = pos.offset(direction.getOpposite()).offset(direction.rotateYCCW());
            worldIn.setBlockState(pos4, state.with(PRIMARY_BLOCK, false));
            final BlockPos pos5 = pos.offset(direction.getOpposite()).offset(direction.rotateY());
            worldIn.setBlockState(pos5, state.with(PRIMARY_BLOCK, false));
            NestPlaceHolder placeHolder1 = (NestPlaceHolder) worldIn.getTileEntity(pos1);
            placeHolder1.rootPos = pos;
            NestPlaceHolder placeHolder2 = (NestPlaceHolder) worldIn.getTileEntity(pos2);
            placeHolder2.rootPos = pos;
            NestPlaceHolder placeHolder3 = (NestPlaceHolder) worldIn.getTileEntity(pos3);
            placeHolder3.rootPos = pos;
            NestPlaceHolder placeHolder4 = (NestPlaceHolder) worldIn.getTileEntity(pos4);
            placeHolder4.rootPos = pos;
            NestPlaceHolder placeHolder5 = (NestPlaceHolder) worldIn.getTileEntity(pos5);
            placeHolder5.rootPos = pos;

            final BlockPos pos6 = pos.up().offset(direction);
            final BlockPos pos7 = pos.up().offset(direction).offset(direction.rotateYCCW());
            final BlockPos pos8 = pos.up().offset(direction).offset(direction.rotateY());
            worldIn.setBlockState(pos6, state.with(PRIMARY_BLOCK, false));
            worldIn.setBlockState(pos7, state.with(PRIMARY_BLOCK, false));
            worldIn.setBlockState(pos8, state.with(PRIMARY_BLOCK, false));
            NestPlaceHolder placeHolder6 = (NestPlaceHolder) worldIn.getTileEntity(pos6);
            placeHolder6.rootPos = pos;
            NestPlaceHolder placeHolder7 = (NestPlaceHolder) worldIn.getTileEntity(pos7);
            placeHolder7.rootPos = pos;
            NestPlaceHolder placeHolder8 = (NestPlaceHolder) worldIn.getTileEntity(pos8);
            placeHolder8.rootPos = pos;

        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onReplaced(state, worldIn, pos, newState, isMoving);
        if (state.get(PRIMARY_BLOCK)) {
            Direction direction = state.get(HORIZONTAL_FACING);
            //TODO remove redundant one
            worldIn.destroyBlock(pos.offset(direction), false);
            worldIn.destroyBlock(pos.offset(direction.getOpposite()).offset(direction.rotateY()), false);
            worldIn.destroyBlock(pos.offset(direction.getOpposite()).offset(direction.getOpposite().rotateY()), false);
            worldIn.destroyBlock(pos.offset(direction).offset(direction.rotateYCCW()), false);
            worldIn.destroyBlock(pos.offset(direction).offset(direction.rotateY()), false);
            worldIn.destroyBlock(pos.offset(direction.rotateYCCW()), false);
            //upper blocks
            worldIn.destroyBlock(pos.up().offset(direction.getOpposite()), false);
            worldIn.destroyBlock(pos.up().offset(direction.getOpposite()).offset(direction.rotateYCCW()), false);
            worldIn.destroyBlock(pos.up().offset(direction.getOpposite()).offset(direction.rotateY()), false);
        }
    }
}
