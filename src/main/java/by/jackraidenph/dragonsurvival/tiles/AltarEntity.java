package by.jackraidenph.dragonsurvival.tiles;

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
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("Cooldown", usageCooldown);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        usageCooldown = compound.getInt("Cooldown");
    }
}
