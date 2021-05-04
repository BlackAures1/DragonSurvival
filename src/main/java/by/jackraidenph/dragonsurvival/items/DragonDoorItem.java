package by.jackraidenph.dragonsurvival.items;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.item.BlockItem;
import net.minecraft.item.BlockItemUseContext;

public class DragonDoorItem extends BlockItem {
    public DragonDoorItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    protected boolean placeBlock(BlockItemUseContext context, BlockState state) {
        context.getLevel().setBlock(context.getClickedPos().above(), Blocks.AIR.defaultBlockState(), 27);
        context.getLevel().setBlock(context.getClickedPos().above(2), Blocks.AIR.defaultBlockState(), 27);
        return super.placeBlock(context, state);
    }
}
