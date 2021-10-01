package by.jackraidenph.dragonsurvival.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class VillageRelationshipsStorage
        implements Capability.IStorage<VillageRelationShips> {
    @Nullable
    public INBT writeNBT(Capability<VillageRelationShips> capability, VillageRelationShips instance, Direction side) {
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt("Crime level", instance.crimeLevel);
        compoundNBT.putInt("Evil status duration", instance.evilStatusDuration);
        compoundNBT.putInt("Hunter spawn delay", instance.hunterSpawnDelay);
        return compoundNBT;
    }

    public void readNBT(Capability<VillageRelationShips> capability, VillageRelationShips instance, Direction side, INBT nbt) {
        CompoundNBT compoundNBT = (CompoundNBT) nbt;
        instance.crimeLevel = compoundNBT.getInt("Crime level");
        instance.evilStatusDuration = compoundNBT.getInt("Evil status duration");
        instance.hunterSpawnDelay = compoundNBT.getInt("Hunter spawn delay");
    }
}