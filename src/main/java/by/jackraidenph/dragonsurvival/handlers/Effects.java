package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Effects {

    public static class Stress extends Effect {

        protected Stress(int color) {
            super(EffectType.HARMFUL, color);
        }
    }

    public static Effect STRESS;

    @SubscribeEvent
    public static void registerEffects(RegistryEvent.Register<Effect> effectRegister) {
        STRESS = new Stress(0xf4a2e8).setRegistryName(DragonSurvivalMod.MODID, "stress");
        IForgeRegistry<Effect> forgeRegistry = effectRegister.getRegistry();
        forgeRegistry.register(STRESS);
    }
}
