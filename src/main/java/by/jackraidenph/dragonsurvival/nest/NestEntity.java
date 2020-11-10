package by.jackraidenph.dragonsurvival.nest;

import by.jackraidenph.dragonsurvival.tiles.BaseBlockEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

public class NestEntity extends BaseBlockEntity implements ITickableTileEntity {
    int health = 100;
    final static int cooldownTime = 10 * 20;
    int damageCooldown;

    public NestEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        if (damageCooldown > 0) {
            damageCooldown--;
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("Health", health);
        compound.putInt("Damage cooldown", damageCooldown);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        health = compound.getInt("Health");
        damageCooldown = compound.getInt("Damage cooldown");
    }
}
