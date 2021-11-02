package by.jackraidenph.dragonsurvival.registration;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.entity.BolasEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.FoodStats;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collections;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class DragonEffects {


    public static Effect STRESS;
    public static Effect TRAPPED;
    public static Effect EVIL_DRAGON;
    public static Effect PEACE, MAGIC, FIRE;
    public static Effect ANIMAL_PEACE;
    public static Effect PREDATOR_ANTI_SPAWN;

    @SuppressWarnings("unused")
    @SubscribeEvent
    public static void registerEffects(RegistryEvent.Register<Effect> effectRegister) {
        IForgeRegistry<Effect> forgeRegistry = effectRegister.getRegistry();
        STRESS = new Stress(0xf4a2e8).setRegistryName(DragonSurvivalMod.MODID, "stress");
        forgeRegistry.register(STRESS);
        TRAPPED = new Trapped(EffectType.NEUTRAL, 0xdddddd).setRegistryName(DragonSurvivalMod.MODID, "trapped");
        forgeRegistry.register(TRAPPED);
        EVIL_DRAGON = new EvilDragon(EffectType.NEUTRAL).setRegistryName(DragonSurvivalMod.MODID, "evil_dragon");
        forgeRegistry.register(EVIL_DRAGON);
        PEACE = new Effect2(EffectType.BENEFICIAL, 0x0, false).setRegistryName(DragonSurvivalMod.MODID, "peace");
        forgeRegistry.register(PEACE);
        MAGIC = new Effect2(EffectType.BENEFICIAL, 0x0, false).setRegistryName(DragonSurvivalMod.MODID, "magic");
        forgeRegistry.register(MAGIC);
        FIRE = new Effect2(EffectType.BENEFICIAL, 0x0, false).setRegistryName(DragonSurvivalMod.MODID, "fire");
        forgeRegistry.register(FIRE);
        ANIMAL_PEACE = new Effect2(EffectType.BENEFICIAL, 0x0, false).setRegistryName(DragonSurvivalMod.MODID, "animal_peace");
        forgeRegistry.register(ANIMAL_PEACE);
        PREDATOR_ANTI_SPAWN = new Effect2(EffectType.BENEFICIAL, 0x0, false).setRegistryName(DragonSurvivalMod.MODID, "predator_anti_spawn");
        forgeRegistry.register(PREDATOR_ANTI_SPAWN);
    }

    private static class Effect2 extends Effect {
        private final boolean uncurable;

        protected Effect2(EffectType type, int color, boolean uncurable) {
            super(type, color);
            this.uncurable = uncurable;
        }

        @Override
        public List<ItemStack> getCurativeItems() {
            return uncurable ? Collections.emptyList() : super.getCurativeItems();
        }
    }

    private static class Stress extends Effect {

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

    private static class Trapped extends Effect {
        protected Trapped(EffectType effectType, int color) {
            super(effectType, color);
        }

        public List<ItemStack> getCurativeItems() {
            return Collections.emptyList();
        }

        public boolean isDurationEffectTick(int timeLeft, int p_76397_2_) {
            return (timeLeft == 1);
        }

        public void applyEffectTick(LivingEntity livingEntity, int strength) {
            livingEntity.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(BolasEntity.DISABLE_MOVEMENT);
            livingEntity.getAttribute(ForgeMod.ENTITY_GRAVITY.get()).removeModifier(BolasEntity.DISABLE_JUMP);
        }
    }

    private static class EvilDragon extends Effect {

        protected EvilDragon(EffectType p_i50391_1_) {
            super(p_i50391_1_, 13700608);
        }

        @Override
        public List<ItemStack> getCurativeItems() {
            return Collections.emptyList();
        }

    }
}
