package by.jackraidenph.dragonsurvival.tiles;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class AltarEntity extends BaseBlockEntity implements ITickableTileEntity {
    public int usageCooldown;

    public AltarEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }


    @Override
    public void tick() {
        if (usageCooldown > 0)
            usageCooldown--;
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putInt("Cooldown", usageCooldown);
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        usageCooldown = compound.getInt("Cooldown");
    }
}