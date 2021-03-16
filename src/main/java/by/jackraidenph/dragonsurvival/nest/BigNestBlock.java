package by.jackraidenph.dragonsurvival.nest;

import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class BigNestBlock extends MediumNestBlock {
    public BigNestBlock(Properties properties) {
        super(properties);
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
        }
    }
}
