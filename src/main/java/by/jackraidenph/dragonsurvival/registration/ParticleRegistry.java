package by.jackraidenph.dragonsurvival.registration;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.particles.BasicParticleType;
import net.minecraft.particles.ParticleType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ParticleRegistry {
    public static ParticleType<BasicParticleType> vetoBeaconParticle, magicBeaconParticle, peaceBeaconParticle;

    @SubscribeEvent
    public static void registerParticles(RegistryEvent.Register<ParticleType<?>> registryEvent) {
        IForgeRegistry<ParticleType<?>> particleTypes = registryEvent.getRegistry();
        vetoBeaconParticle = new BasicParticleType(false);
        vetoBeaconParticle.setRegistryName(DragonSurvivalMod.MODID, "netherite_particle");
        particleTypes.register(vetoBeaconParticle);
        peaceBeaconParticle = new BasicParticleType(false);
        peaceBeaconParticle.setRegistryName(DragonSurvivalMod.MODID, "gold_particle");
        particleTypes.register(peaceBeaconParticle);
        magicBeaconParticle = new BasicParticleType(false);
        magicBeaconParticle.setRegistryName(DragonSurvivalMod.MODID, "diamond_particle");
        particleTypes.register(magicBeaconParticle);
    }
}
