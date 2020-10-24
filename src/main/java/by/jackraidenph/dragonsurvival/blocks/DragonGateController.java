package by.jackraidenph.dragonsurvival.blocks;

import by.jackraidenph.dragonsurvival.blockentities.DragonGateBlockEntity;
import by.jackraidenph.dragonsurvival.handlers.BlockEntities;
import by.jackraidenph.dragonsurvival.handlers.BlockInit;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

public class DragonGateController extends DragonGateBlock {
    public DragonGateController(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return BlockEntities.dragonGateBlockEntityEntityType.create();
    }

    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
        Direction direction=state.get(horizontal);
        worldIn.setBlockState(pos.up(), BlockInit.dragonGate.getDefaultState().with(horizontal,direction));
        worldIn.setBlockState(pos.up(2), BlockInit.dragonGate.getDefaultState().with(horizontal,direction));
        worldIn.setBlockState(pos.offset(direction), BlockInit.dragonGate.getDefaultState().with(horizontal,direction));
        worldIn.setBlockState(pos.offset(direction).up(), BlockInit.dragonGate.getDefaultState().with(horizontal,direction));
        worldIn.setBlockState(pos.offset(direction).up(2), BlockInit.dragonGate.getDefaultState().with(horizontal,direction));
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        DragonGateBlockEntity dragonGateBlockEntity= (DragonGateBlockEntity) worldIn.getTileEntity(pos);
        Direction direction = state.get(horizontal);
        if(dragonGateBlockEntity.closed) {
            worldIn.removeBlock(pos.up(), false);
            worldIn.removeBlock(pos.up(2), false);
            worldIn.removeBlock(pos.offset(direction), false);
            worldIn.removeBlock(pos.offset(direction).up(), false);
            worldIn.removeBlock(pos.offset(direction).up(2), false);
        }
        else{
            if(dragonGateBlockEntity.leftSide)
            {

            }
            else{
                worldIn.removeBlock(pos.up(),false);
                worldIn.removeBlock(pos.up(2),false);
                worldIn.removeBlock(pos.offset(direction.rotateY()),false);
                worldIn.removeBlock(pos.offset(direction.rotateY()).up(),false);
                worldIn.removeBlock(pos.offset(direction.rotateY()).up(2),false);
            }
        }
        super.onReplaced(state, worldIn, pos, newState, isMoving);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        return ActionResultType.PASS;
    }
}
