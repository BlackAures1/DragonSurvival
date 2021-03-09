package by.jackraidenph.dragonsurvival.nest;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class MediumNestBlock extends NestBlock {
    static final BooleanProperty PRIMARYBLOCK = BooleanProperty.create("primary");

    public MediumNestBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(PRIMARYBLOCK);
    }

//    @Override
//    public BlockState getStateForPlacement(BlockState state, Direction facing, BlockState state2, IWorld world, BlockPos pos1, BlockPos pos2, Hand hand) {
//        return null;
//    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(PRIMARYBLOCK);
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        if (placer != null) {
            Direction direction = placer.getHorizontalFacing();
            worldIn.setBlockState(pos.offset(direction), state.with(PRIMARYBLOCK, false));
            worldIn.setBlockState(pos.offset(direction.rotateYCCW()), state.with(PRIMARYBLOCK, false));
            worldIn.setBlockState(pos.offset(direction).offset(direction.rotateYCCW()), state.with(PRIMARYBLOCK, false));
        }
    }
}
