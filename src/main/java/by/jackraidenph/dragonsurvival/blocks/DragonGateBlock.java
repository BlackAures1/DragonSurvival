package by.jackraidenph.dragonsurvival.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;

public class DragonGateBlock extends Block {
    public static DirectionProperty horizontal=DirectionProperty.create("facing", Direction.EAST,Direction.NORTH,Direction.SOUTH,Direction.WEST);
    public DragonGateBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        super.fillStateContainer(builder);
        builder.add(horizontal);
    }
}
