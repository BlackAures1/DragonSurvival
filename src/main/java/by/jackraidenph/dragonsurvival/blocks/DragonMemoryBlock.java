package by.jackraidenph.dragonsurvival.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.BlockStateProperties;

public class DragonMemoryBlock extends Block {
    public static BooleanProperty LIT = BlockStateProperties.LIT;

    public DragonMemoryBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
        registerDefaultState(getStateDefinition().any().setValue(LIT, false));
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> p_206840_1_) {
        super.createBlockStateDefinition(p_206840_1_);
        p_206840_1_.add(LIT);
    }

}
