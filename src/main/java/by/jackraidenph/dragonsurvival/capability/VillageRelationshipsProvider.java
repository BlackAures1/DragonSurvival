package by.jackraidenph.dragonsurvival.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

public class VillageRelationshipsProvider implements ICapabilitySerializable<CompoundNBT> {
    private final LazyOptional<VillageRelationShips> instance = LazyOptional.of(() -> (VillageRelationShips) Objects.<Object>requireNonNull(Capabilities.VILLAGE_RELATIONSHIP.getDefaultInstance()));

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return (cap == Capabilities.VILLAGE_RELATIONSHIP) ? this.instance.cast() : LazyOptional.empty();
    }

    public CompoundNBT serializeNBT() {
        return (CompoundNBT) Capabilities.VILLAGE_RELATIONSHIP.getStorage().writeNBT(Capabilities.VILLAGE_RELATIONSHIP, this.instance.orElse(null), null);
    }

    public void deserializeNBT(CompoundNBT nbt) {
        Capabilities.VILLAGE_RELATIONSHIP.getStorage().readNBT(Capabilities.VILLAGE_RELATIONSHIP, this.instance.orElse(null), null, (INBT) nbt);
    }
}
