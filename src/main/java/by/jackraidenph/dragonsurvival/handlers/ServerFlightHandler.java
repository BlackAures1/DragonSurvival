package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Used in pair with {@link ClientFlightHandler}
 */
@Mod.EventBusSubscriber()
@SuppressWarnings("unused")
public class ServerFlightHandler {

    /**
     * Sets the fall damage based on flight speed and dragon's size
     */
    @SubscribeEvent
    public static void changeFlightFallDamage(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        if (livingEntity.level.isClientSide())
            return;
        DamageSource damageSource = event.getSource();
        if (damageSource == DamageSource.FALL) {
            final double flightSpeed = livingEntity.getDeltaMovement().length();
            if (livingEntity.isPassenger() && DragonStateProvider.isDragon(livingEntity.getVehicle())) {
                event.setCanceled(true);
            } else if (ConfigHandler.SERVER.enableFlightFallDamage.get()) {
                DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
                    if (dragonStateHandler.isDragon() && DragonSizeHandler.wingsStatusServer.containsKey(livingEntity.getId()) && DragonSizeHandler.wingsStatusServer.get(livingEntity.getId())) {
                        if (flightSpeed > 0.08) {
                            DragonSurvivalMod.LOGGER.info(flightSpeed);
                            double damage = flightSpeed * 35 * dragonStateHandler.getSize() / 20;
                            damage = MathHelper.clamp(damage, 0, livingEntity.getHealth() - 1);
                            event.setAmount((float) (damage));
                        } else {
                            event.setCanceled(true);
                        }
                    }
                });
            }
        }
    }
    
    @SubscribeEvent
    public static void playerFoodExhaustion(TickEvent.PlayerTickEvent playerTickEvent) {
        DragonStateProvider.getCap(playerTickEvent.player).ifPresent(dragonStateHandler -> {
            if(dragonStateHandler.isDragon()) {
                boolean wingsSpread = DragonSizeHandler.wingsStatusServer.containsKey(playerTickEvent.player.getId()) && DragonSizeHandler.wingsStatusServer.get(playerTickEvent.player.getId());
                if(ConfigHandler.SERVER.creativeFlight.get()){
                    if(playerTickEvent.player.abilities.flying != wingsSpread){
                        playerTickEvent.player.abilities.flying = wingsSpread;
                    }
                }
                
                if (wingsSpread) {
                    if (ConfigHandler.SERVER.flyingUsesHunger.get()) {
                        if (!playerTickEvent.player.isOnGround()) {
                            Vector3d delta = playerTickEvent.player.getDeltaMovement();
                            float l = Math.round(MathHelper.sqrt(delta.x * delta.x + delta.y * delta.y + delta.z * delta.z));
                            float exhaustion = ConfigHandler.SERVER.creativeFlight.get() ? (playerTickEvent.player.abilities.flying ?  playerTickEvent.player.flyingSpeed : 0F) : (0.005F * l);
                            playerTickEvent.player.causeFoodExhaustion(exhaustion);
                        }
                    }
                }
            }
        });
    }
}
