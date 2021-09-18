package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
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
                        if (flightSpeed > 0.03) {
                            double damage = flightSpeed * 70 * dragonStateHandler.getSize() / 20;
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
}
