package by.jackraidenph.dragonsurvival.registration;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Sounds {
    public static SoundEvent activateBeacon, deactivateBeacon, upgradeBeacon, applyEffect;

    @SubscribeEvent
    public static void registerSounds(RegistryEvent.Register<SoundEvent> registryEvent) {
        IForgeRegistry<SoundEvent> forgeRegistry = registryEvent.getRegistry();
        activateBeacon = register("activate_beacon", forgeRegistry);
        deactivateBeacon = register("deactivate_beacon", forgeRegistry);
        upgradeBeacon = register("upgrade_beacon", forgeRegistry);
        applyEffect = register("apply_effect", forgeRegistry);
    }

    private static SoundEvent register(String name, IForgeRegistry<SoundEvent> forgeRegistry) {
        SoundEvent soundEvent = new SoundEvent(new ResourceLocation(DragonSurvivalMod.MODID, name));
        soundEvent.setRegistryName(DragonSurvivalMod.MODID, name);
        forgeRegistry.register(soundEvent);
        return soundEvent;
    }
}
