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
            BlockPos blockPos = context.getClickedPos();
            World world = context.getLevel();
            PlayerEntity playerEntity = context.getPlayer();
            Direction direction = playerEntity.getDirection();
            if (world.isEmptyBlock(blockPos.relative(direction.getOpposite())) &&
                    world.isEmptyBlock(blockPos.relative(direction).relative(direction.getClockWise())) &&
                    world.isEmptyBlock(blockPos.relative(direction.getClockWise())) &&
                    world.isEmptyBlock(blockPos.relative(direction.getOpposite()).relative(direction.getCounterClockWise())) &&
                    world.isEmptyBlock(blockPos.relative(direction.getOpposite()).relative(direction.getClockWise())) &&
                    world.isEmptyBlock(blockPos.relative(direction).above()) &&
                    world.isEmptyBlock(blockPos.relative(direction).above().relative(direction.getClockWise())) &&
                    world.isEmptyBlock(blockPos.relative(direction).above().relative(direction.getCounterClockWise()))
            )
                return superState;
        }
        return null;
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(worldIn, pos, state, placer, stack);
        if (placer != null) {
            Direction direction = placer.getDirection();
            final BlockPos pos1 = pos.relative(direction.getOpposite());
            worldIn.setBlockAndUpdate(pos1, state.setValue(PRIMARY_BLOCK, false));
            final BlockPos pos2 = pos.relative(direction).relative(direction.getClockWise());
            worldIn.setBlockAndUpdate(pos2, state.setValue(PRIMARY_BLOCK, false));
            final BlockPos pos3 = pos.relative(direction.getClockWise());
            worldIn.setBlockAndUpdate(pos3, state.setValue(PRIMARY_BLOCK, false));
            final BlockPos pos4 = pos.relative(direction.getOpposite()).relative(direction.getCounterClockWise());
            worldIn.setBlockAndUpdate(pos4, state.setValue(PRIMARY_BLOCK, false));
            final BlockPos pos5 = pos.relative(direction.getOpposite()).relative(direction.getClockWise());
            worldIn.setBlockAndUpdate(pos5, state.setValue(PRIMARY_BLOCK, false));
            NestPlaceHolder placeHolder1 = (NestPlaceHolder) worldIn.getBlockEntity(pos1);
            placeHolder1.rootPos = pos;
            NestPlaceHolder placeHolder2 = (NestPlaceHolder) worldIn.getBlockEntity(pos2);
            placeHolder2.rootPos = pos;
            NestPlaceHolder placeHolder3 = (NestPlaceHolder) worldIn.getBlockEntity(pos3);
            placeHolder3.rootPos = pos;
            NestPlaceHolder placeHolder4 = (NestPlaceHolder) worldIn.getBlockEntity(pos4);
            placeHolder4.rootPos = pos;
            NestPlaceHolder placeHolder5 = (NestPlaceHolder) worldIn.getBlockEntity(pos5);
            placeHolder5.rootPos = pos;

            final BlockPos pos6 = pos.above().relative(direction);
            final BlockPos pos7 = pos.above().relative(direction).relative(direction.getCounterClockWise());
            final BlockPos pos8 = pos.above().relative(direction).relative(direction.getClockWise());
            worldIn.setBlockAndUpdate(pos6, state.setValue(PRIMARY_BLOCK, false));
            worldIn.setBlockAndUpdate(pos7, state.setValue(PRIMARY_BLOCK, false));
            worldIn.setBlockAndUpdate(pos8, state.setValue(PRIMARY_BLOCK, false));
            NestPlaceHolder placeHolder6 = (NestPlaceHolder) worldIn.getBlockEntity(pos6);
            placeHolder6.rootPos = pos;
            NestPlaceHolder placeHolder7 = (NestPlaceHolder) worldIn.getBlockEntity(pos7);
            placeHolder7.rootPos = pos;
            NestPlaceHolder placeHolder8 = (NestPlaceHolder) worldIn.getBlockEntity(pos8);
            placeHolder8.rootPos = pos;

        }
    }

    @Override
    public void onRemove(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        super.onRemove(state, worldIn, pos, newState, isMoving);
        if (state.getValue(PRIMARY_BLOCK)) {
            Direction direction = state.getValue(FACING);
            //TODO remove redundant one
            worldIn.destroyBlock(pos.relative(direction), false);
            worldIn.destroyBlock(pos.relative(direction.getOpposite()).relative(direction.getClockWise()), false);
            worldIn.destroyBlock(pos.relative(direction.getOpposite()).relative(direction.getOpposite().getClockWise()), false);
            worldIn.destroyBlock(pos.relative(direction).relative(direction.getCounterClockWise()), false);
            worldIn.destroyBlock(pos.relative(direction).relative(direction.getClockWise()), false);
            worldIn.destroyBlock(pos.relative(direction.getCounterClockWise()), false);
            //upper blocks
            worldIn.destroyBlock(pos.above().relative(direction.getOpposite()), false);
            worldIn.destroyBlock(pos.above().relative(direction.getOpposite()).relative(direction.getCounterClockWise()), false);
            worldIn.destroyBlock(pos.above().relative(direction.getOpposite()).relative(direction.getClockWise()), false);
        }
    }
}
