package by.jackraidenph.dragonsurvival.capability;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;

public class Capabilities {
    @CapabilityInject(VillageRelationShips.class)
    public static Capability<VillageRelationShips> VILLAGE_RELATIONSHIP;

    public static void register() {
        CapabilityManager.INSTANCE.register(DragonStateHandler.class, new DragonCapStorage(), DragonStateHandler::new);
        CapabilityManager.INSTANCE.register(VillageRelationShips.class, new VillageRelationshipsStorage(), VillageRelationShips::new);
    }
}
