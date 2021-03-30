package by.jackraidenph.dragonsurvival.capability;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class DragonStateProvider implements ICapabilitySerializable<CompoundNBT> {

    @CapabilityInject(DragonStateHandler.class)
    public static Capability<DragonStateHandler> DRAGON_CAPABILITY = null;
    private LazyOptional<DragonStateHandler> instance = LazyOptional.of(DRAGON_CAPABILITY::getDefaultInstance);

    public static LazyOptional<DragonStateHandler> getCap(Entity entity) {
        return entity.getCapability(DragonStateProvider.DRAGON_CAPABILITY);
    }

    public static boolean isDragon(Entity entity) {
        return getCap(entity).filter(DragonStateHandler::isDragon).isPresent();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == DRAGON_CAPABILITY ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT() {
        return (CompoundNBT) DRAGON_CAPABILITY.getStorage().writeNBT(DRAGON_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        DRAGON_CAPABILITY.getStorage().readNBT(DRAGON_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}
