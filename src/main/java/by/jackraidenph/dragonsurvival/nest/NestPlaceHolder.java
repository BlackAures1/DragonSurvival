package by.jackraidenph.dragonsurvival.nest;

import by.jackraidenph.dragonsurvival.tiles.BaseBlockEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

public class NestPlaceHolder extends BaseBlockEntity {
    public NestPlaceHolder(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    public BlockPos rootPos = BlockPos.ZERO;

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putLong("Root", rootPos.toLong());
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        rootPos = BlockPos.fromLong(compound.getLong("Root"));
    }
}
