package by.jackraidenph.dragonsurvival.blocks;

import by.jackraidenph.dragonsurvival.blockentities.DragonGateBlockEntity;
import by.jackraidenph.dragonsurvival.handlers.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return getDefaultState().with(horizontal,context.getPlacementHorizontalFacing());
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        BlockPos controllerPos = getControllerPosition(worldIn, state, pos);
        if (controllerPos != null) {
            DragonGateBlockEntity blockEntity = (DragonGateBlockEntity) worldIn.getTileEntity(controllerPos);
            if (blockEntity != null)
                blockEntity.toggle();
        } else {
            worldIn.removeBlock(pos, false);
        }
        return ActionResultType.SUCCESS;
    }

    public static BlockPos getControllerPosition(World worldIn, BlockState state, BlockPos pos) {
        Direction direction = state.get(horizontal).getOpposite();
        Set<BlockPos> blockPosSet = Stream.of(pos.offset(direction), pos.offset(direction).down(), pos.offset(direction).down(2), pos.down(), pos.down(2)).filter(blockPos -> worldIn.getBlockState(blockPos).getBlock() == BlockInit.dragonGateController).collect(Collectors.toSet());
        if (blockPosSet.isEmpty())
            return null;
        return blockPosSet.iterator().next();
    }
}
