package by.jackraidenph.dragonsurvival.capability;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;

public class PlayerStateProvider implements ICapabilitySerializable {

    @CapabilityInject(DragonStateHandler.class)
    public static Capability<DragonStateHandler> PLAYER_STATE_HANDLER_CAPABILITY = null;
    private LazyOptional<DragonStateHandler> instance = LazyOptional.of(PLAYER_STATE_HANDLER_CAPABILITY::getDefaultInstance);

    public static LazyOptional<DragonStateHandler> getCap(Entity entity) {
        return entity.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == PLAYER_STATE_HANDLER_CAPABILITY ? instance.cast() : LazyOptional.empty();
    }

    @Override
    public INBT serializeNBT() {
        return PLAYER_STATE_HANDLER_CAPABILITY.getStorage().writeNBT(PLAYER_STATE_HANDLER_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null);
    }

    @Override
    public void deserializeNBT(INBT nbt) {
        PLAYER_STATE_HANDLER_CAPABILITY.getStorage().readNBT(PLAYER_STATE_HANDLER_CAPABILITY, this.instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional must not be empty!")), null, nbt);
    }
}
