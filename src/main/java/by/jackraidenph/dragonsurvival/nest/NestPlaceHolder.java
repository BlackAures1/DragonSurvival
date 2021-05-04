package by.jackraidenph.dragonsurvival.nest;

import by.jackraidenph.dragonsurvival.tiles.BaseBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public class NestPlaceHolder extends BaseBlockEntity {
    public NestPlaceHolder(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public BlockPos rootPos = BlockPos.ZERO;

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putLong("Root", rootPos.asLong());
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        rootPos = BlockPos.of(compound.getLong("Root"));
    }
}
