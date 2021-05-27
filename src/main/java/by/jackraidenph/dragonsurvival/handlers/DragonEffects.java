package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.FoodStats;
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
                FoodStats food = playerEntity.getFoodData();
                if (food.getSaturationLevel() > 0) {
                	int oldFood = food.getFoodLevel();
                	food.eat(1, -0.5F * food.getSaturationLevel());
                	if (oldFood != 20)
                		food.setFoodLevel(food.getFoodLevel() - 1);
                }
                playerEntity.causeFoodExhaustion(1.0f);
            }
        }

        @Override
        public boolean isDurationEffectTick(int p_76397_1_, int p_76397_2_) {
        	int i = 20 >> p_76397_2_;
            if (i > 0)
               return p_76397_1_ % i == 0;
            else
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
