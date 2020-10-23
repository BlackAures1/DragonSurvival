package by.jackraidenph.dragonsurvival.blocks;

import by.jackraidenph.dragonsurvival.handlers.BlockEntities;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class DragonGateBlock extends Block {
    public static BooleanProperty mainBlock=BooleanProperty.create("main");
    public static DirectionProperty horizontal=DirectionProperty.create("direction", Direction.EAST,Direction.NORTH,Direction.SOUTH,Direction.WEST);
    public DragonGateBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return state.get(mainBlock);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(mainBlock,horizontal);
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return BlockEntities.dragonGateBlockEntityEntityType.create();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
    }

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return super.getStateForPlacement(context);
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {

        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }
}
