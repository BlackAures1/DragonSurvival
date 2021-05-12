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

public class DarknessFear {
    @CapabilityInject(DarknessFear.class)
    public static Capability<DarknessFear> DARKNESSFEAR;

    int timeInDarkness;

    public void setTimeInDarkness(int timeInDarkness) {
        this.timeInDarkness = timeInDarkness;
    }

    public int getTimeInDarkness() {
        return timeInDarkness;
    }

    public void increaseTime() {
        timeInDarkness++;
    }

    public static class Provider implements ICapabilitySerializable<CompoundNBT> {
        LazyOptional<DarknessFear> darknessFearLazyOptional = LazyOptional.of(() -> Objects.requireNonNull(DARKNESSFEAR.getDefaultInstance()));

        @Nonnull
        @Override
        public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
            return cap == DARKNESSFEAR ? darknessFearLazyOptional.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundNBT serializeNBT() {
            return (CompoundNBT) DARKNESSFEAR.getStorage().writeNBT(DARKNESSFEAR, darknessFearLazyOptional.orElse(null), null);
        }

        @Override
        public void deserializeNBT(CompoundNBT nbt) {
            DARKNESSFEAR.getStorage().readNBT(DARKNESSFEAR, darknessFearLazyOptional.orElse(null), null, nbt);
        }
    }

    public static class Storage implements Capability.IStorage<DarknessFear> {

        @Nullable
        @Override
        public INBT writeNBT(Capability<DarknessFear> capability, DarknessFear instance, Direction side) {
            CompoundNBT compoundNBT = new CompoundNBT();
            compoundNBT.putInt("Time in dark", instance.timeInDarkness);
            return compoundNBT;
        }

        @Override
        public void readNBT(Capability<DarknessFear> capability, DarknessFear instance, Direction side, INBT nbt) {
            CompoundNBT compoundNBT = (CompoundNBT) nbt;
            instance.timeInDarkness = (compoundNBT.getInt("Time in dark"));
        }
    }
}
