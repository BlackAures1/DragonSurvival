package by.jackraidenph.dragonsurvival.capability;

import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.concurrent.Callable;

public class PlayerStateCapability {
    public static void register() {
        CapabilityManager.INSTANCE.register(PlayerStateHandler.class, new CapabilityStorage(), new Factory());
    }

    private static class Factory implements Callable<PlayerStateHandler> {
        @Override
        public PlayerStateHandler call() {
            return new PlayerStateHandler();
        }
    }
}
