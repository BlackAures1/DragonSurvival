package by.jackraidenph.dragonsurvival.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class Hydration {
    @CapabilityInject(Hydration.class)
    public static Capability<Hydration> HYDRATION;

    int timeWithoutWater;

    public void setTimeWithoutWater(int timeWithoutWater) {
        this.timeWithoutWater = timeWithoutWater;
    }

    public int getTimeWithoutWater() {
        return timeWithoutWater;
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        LazyOptional<Hydration> hydrationLazyOptional = LazyOptional.of(() -> Objects.requireNonNull(HYDRATION.getDefaultInstance()));

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return cap == HYDRATION ? hydrationLazyOptional.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT() {
            return (CompoundNBT) HYDRATION.getStorage().writeNBT(HYDRATION, hydrationLazyOptional.orElseThrow(() -> new IllegalStateException("Instance not present")), null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            HYDRATION.getStorage().readNBT(HYDRATION, hydrationLazyOptional.orElseThrow(() -> new IllegalStateException("Instance not present")), null, nbt);
        }
    }

    public static class Storage implements Capability.IStorage<Hydration> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<Hydration> capability, Hydration instance, Direction side) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putInt("Time without water", instance.timeWithoutWater);
            return compoundNBT;
        }

        @Override
        public void readNBT(Capability<Hydration> capability, Hydration instance, Direction side, INBT nbt) {
            CompoundNBT compoundNBT = (CompoundNBT) nbt;
            instance.timeWithoutWater = compoundNBT.getInt("Time without water");
        }
    }
}
