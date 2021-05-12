package by.jackraidenph.dragonsurvival.capability;

import net.minecraftforge.common.capabilities.CapabilityManager;

public class Capabilities {
    public static void register() {
        CapabilityManager.INSTANCE.register(DragonStateHandler.class, new DragonCapStorage(), DragonStateHandler::new);
        CapabilityManager.INSTANCE.register(Hydration.class, new Hydration.Storage(), Hydration::new);
        CapabilityManager.INSTANCE.register(DarknessFear.class, new DarknessFear.Storage(), DarknessFear::new);
    }
}
