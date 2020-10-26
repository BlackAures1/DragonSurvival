package by.jackraidenph.dragonsurvival.blockentities;

import by.jackraidenph.dragonsurvival.blocks.DragonGateBlock;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;

@Deprecated
public class DragonGateBlockEntity extends TileEntity {
    public boolean closed =true;
    /**
     * If true, will open to the left. If false, will open to the right
     */
    public boolean openToLeft;

    public DragonGateBlockEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public void toggle() {
        BlockState blockState=getBlockState();
        Direction direction=blockState.get(DragonGateBlock.horizontal);
        if(closed) {
            if(openToLeft) {
                //rotate counter-clockwise
                world.removeBlock(pos.offset(direction), false);
                world.removeBlock(pos.offset(direction).up(), false);
                world.removeBlock(pos.offset(direction).up(2), false);

//                world.setBlockState(pos.up(),BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal,direction.rotateYCCW()));
//                world.setBlockState(pos.up(2),BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal,direction.rotateYCCW()));

//                world.setBlockState(pos.offset(direction.rotateYCCW()),BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal,direction.rotateYCCW()));
//                world.setBlockState(pos.offset(direction.rotateYCCW()).up(),BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal,direction.rotateYCCW()));
//                world.setBlockState(pos.offset(direction.rotateYCCW()).up(2),BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal,direction.rotateYCCW()));
            } else {
                //rotate clockwise
                //remove rear blocks
                world.removeBlock(pos.offset(direction), false);
                world.removeBlock(pos.offset(direction).up(), false);
                world.removeBlock(pos.offset(direction).up(2), false);

                //rotate near blocks
//                world.setBlockState(pos.up(), BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal, direction.rotateY()));
//                world.setBlockState(pos.up(2), BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal, direction.rotateY()));
                //TODO
                //world.setBlockState(pos, blockState.with(DragonGateBlock.horizontal,direction.rotateY()));

                //set rear blocks
//                world.setBlockState(pos.offset(direction.rotateY()), BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal, direction.rotateY()));
//                world.setBlockState(pos.offset(direction.rotateY()).up(), BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal, direction.rotateY()));
//                world.setBlockState(pos.offset(direction.rotateY()).up(2), BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal, direction.rotateY()));
            }
            closed =false;
        } else{
            if(openToLeft) {
                //rotate clockwise
                world.removeBlock(pos.offset(direction.rotateYCCW()), false);
                world.removeBlock(pos.offset(direction.rotateYCCW()).up(), false);
                world.removeBlock(pos.offset(direction.rotateYCCW()).up(2), false);

//                world.setBlockState(pos.up(),BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal,direction.rotateY()));
//                world.setBlockState(pos.up(2),BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal,direction.rotateY()));

//                world.setBlockState(pos.offset(direction),BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal,direction));
//                world.setBlockState(pos.offset(direction).up(),BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal,direction));
//                world.setBlockState(pos.offset(direction).up(2),BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal,direction));
            } else {
                //rotate counter-clockwise
                world.removeBlock(pos.offset(direction.rotateY()), false);
                world.removeBlock(pos.offset(direction.rotateY()).up(), false);
                world.removeBlock(pos.offset(direction.rotateY()).up(2), false);

//                world.setBlockState(pos.up(), BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal, direction.rotateYCCW()));
//                world.setBlockState(pos.up(2), BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal, direction.rotateYCCW()));

//                world.setBlockState(pos.offset(direction), BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal, direction));
//                world.setBlockState(pos.offset(direction).up(), BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal, direction));
//                world.setBlockState(pos.offset(direction).up(2), BlockInit.dragonGate.getDefaultState().with(DragonGateBlock.horizontal, direction));
            }
            closed =true;
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putBoolean("Closed", closed);
        compound.putBoolean("Left-sided", openToLeft);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        closed =compound.getBoolean("Closed");
        openToLeft =compound.getBoolean("Left-sided");
    }
}
