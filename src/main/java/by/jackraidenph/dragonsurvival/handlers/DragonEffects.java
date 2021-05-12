package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DragonEffects {

    public static class Stress extends Effect {

        protected Stress(int color) {
            super(EffectType.HARMFUL, color);
        }

        @Override
        public void applyEffectTick(LivingEntity livingEntity, int p_76394_2_) {
            if (livingEntity instanceof PlayerEntity) {
                PlayerEntity playerEntity = (PlayerEntity) livingEntity;
                playerEntity.getFoodData().eat(0, 0);
                playerEntity.causeFoodExhaustion(0.1f);
            }
        }

        @Override
        public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
            return true;
        }
    }

    public static Effect STRESS;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerEffects(RegistryEvent.Register<Effect> effectRegister) {
        STRESS = new Stress(0xf4a2e8).setRegistryName(DragonSurvivalMod.MODID, "stress");
        IForgeRegistry<Effect> forgeRegistry = effectRegister.getRegistry();
        forgeRegistry.register(STRESS);
    }
}
