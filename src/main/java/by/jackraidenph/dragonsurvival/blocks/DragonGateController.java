package by.jackraidenph.dragonsurvival.blocks;

import by.jackraidenph.dragonsurvival.blockentities.DragonGateBlockEntity;
import by.jackraidenph.dragonsurvival.handlers.BlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
@Deprecated
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
        Direction direction = state.get(horizontal);
//        worldIn.setBlockState(pos.up(), BlockInit.dragonGate.getDefaultState().with(horizontal,direction));
//        worldIn.setBlockState(pos.up(2), BlockInit.dragonGate.getDefaultState().with(horizontal,direction));
//        worldIn.setBlockState(pos.offset(direction), BlockInit.dragonGate.getDefaultState().with(horizontal,direction));
//        worldIn.setBlockState(pos.offset(direction).up(), BlockInit.dragonGate.getDefaultState().with(horizontal,direction));
//        worldIn.setBlockState(pos.offset(direction).up(2), BlockInit.dragonGate.getDefaultState().with(horizontal,direction));
        DragonGateBlockEntity blockEntity = (DragonGateBlockEntity) worldIn.getTileEntity(pos);
        Vec3d playerPosvec = placer.getPositionVec();
        Vec3d targetVector = new Vec3d(pos.getX(), pos.getY(), pos.getZ());
        Vec3d placement = targetVector.subtract(playerPosvec);
        boolean leftSide = false;
        switch (direction) {
            case WEST:
                if (placement.z > -0.5) {
                    leftSide=true;
                }
                break;
            case NORTH:
                if(placement.x<-0.5)
                {
                    leftSide=true;
                }
                break;
            case EAST:
                if(placement.z<-0.5)
                {
                    leftSide=true;
                }
                break;
            case SOUTH:
                if (placement.x > -0.5) {
                    leftSide = true;
                }
                break;
        }
        blockEntity.openToLeft = leftSide;
        if (worldIn.isRemote) {
            if (leftSide)
                placer.sendMessage(new TranslationTextComponent("ds.opens.to.left"));
            else
                placer.sendMessage(new TranslationTextComponent("ds.opens.to.right"));
        }
    }

    @Override
    public void onReplaced(BlockState state, World worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        DragonGateBlockEntity dragonGateBlockEntity = (DragonGateBlockEntity) worldIn.getTileEntity(pos);
        Direction direction = state.get(horizontal);
        worldIn.removeBlock(pos.up(), false);
        worldIn.removeBlock(pos.up(2), false);
        if (dragonGateBlockEntity.closed) {
            worldIn.removeBlock(pos.offset(direction), false);
            worldIn.removeBlock(pos.offset(direction).up(), false);
            worldIn.removeBlock(pos.offset(direction).up(2), false);
        } else {
            if (dragonGateBlockEntity.openToLeft) {
                worldIn.removeBlock(pos.offset(direction.rotateYCCW()), false);
                worldIn.removeBlock(pos.offset(direction.rotateYCCW()).up(), false);
                worldIn.removeBlock(pos.offset(direction.rotateYCCW()).up(2), false);
            }
            else{
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
