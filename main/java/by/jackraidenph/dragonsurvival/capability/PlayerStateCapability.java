package by.jackraidenph.dragonsurvival.capability;

import net.minecraftforge.common.capabilities.CapabilityManager;

public class PlayerStateCapability {
    public static void register() {
        CapabilityManager.INSTANCE.register(IPlayerStateHandler.class, new CapabilityStorage(), PlayerStateHandler::new);
    }
}
