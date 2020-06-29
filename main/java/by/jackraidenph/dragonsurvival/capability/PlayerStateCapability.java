package by.jackraidenph.dragonsurvival.capability;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class PlayerStateCapability {
    public static void register() {
        CapabilityManager.INSTANCE.register(IPlayerStateHandler.class, new Capability.IStorage<IPlayerStateHandler>() {
            @Override
            public INBT writeNBT(Capability<IPlayerStateHandler> capability, IPlayerStateHandler instance, Direction side) {
                CompoundNBT tag = new CompoundNBT();

                return tag;
            }

            @Override
            public void readNBT(Capability<IPlayerStateHandler> capability, IPlayerStateHandler instance, Direction side, INBT base) {
                CompoundNBT compound = (CompoundNBT) base;

            }
        }, PlayerStateHandler::new);
    }
}
