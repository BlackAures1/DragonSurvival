package by.jackraidenph.dragonsurvival.capability;

import net.minecraftforge.common.capabilities.CapabilityManager;

public class Capabilities {
    public static void register() {
        CapabilityManager.INSTANCE.register(DragonStateHandler.class, new CapabilityStorage(), DragonStateHandler::new);
    }
}
