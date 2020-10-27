package by.jackraidenph.dragonsurvival.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.world.World;

public class RenderViewEntity extends Entity {
    public RenderViewEntity(World p_i48580_2_) {
        super(EntityType.PLAYER, p_i48580_2_);
        setRawPosition(0, 10, 0);
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void readAdditional(CompoundNBT compound) {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {

    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return null;
    }
}
