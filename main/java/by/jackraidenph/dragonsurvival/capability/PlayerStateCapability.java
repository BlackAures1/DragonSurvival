package by.jackraidenph.dragonsurvival.capability;

import net.minecraftforge.common.capabilities.CapabilityManager;

import java.util.concurrent.Callable;

public class PlayerStateCapability {
    public static void register() {
        CapabilityManager.INSTANCE.register(IPlayerStateHandler.class, new CapabilityStorage(), new Factory());
    }

    private static class Factory implements Callable<IPlayerStateHandler> {

        @Override
        public IPlayerStateHandler call() throws Exception {
            return new PlayerStateHandler();
        }
    }
}
