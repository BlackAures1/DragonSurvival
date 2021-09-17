package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
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
     * Acceleration
     */
    static double ax, ay, az;

    /**
     * Mirrors flight speed variables in {@link ClientFlightHandler}
     */
    @SubscribeEvent
    public static void flightControl(TickEvent.PlayerTickEvent playerTickEvent) {
        PlayerEntity playerEntity = playerTickEvent.player;
        if (!playerEntity.level.isClientSide && !playerEntity.isPassenger()) {
            DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
                if (dragonStateHandler.isDragon()) {
                    if (DragonSizeHandler.wingsStatusServer.containsKey(playerEntity.getId()) && DragonSizeHandler.wingsStatusServer.get(playerEntity.getId())) {
                        if (!playerEntity.isOnGround() && !playerEntity.isInWater() && !playerEntity.isInLava()) {
                            Vector3d motion = playerEntity.getDeltaMovement();

                            Vector3d lookVec = playerEntity.getLookAngle();
                            float f6 = playerEntity.xRot * ((float) Math.PI / 180F);
                            double d9 = Math.sqrt(lookVec.x * lookVec.x + lookVec.z * lookVec.z);
                            double d11 = Math.sqrt(LivingEntity.getHorizontalDistanceSqr(motion));
                            double d12 = lookVec.length();
                            float f3 = MathHelper.cos(f6);
                            f3 = (float) ((double) f3 * (double) f3 * Math.min(1.0D, d12 / 0.4D));
                            ModifiableAttributeInstance gravity = playerEntity.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
                            double g = gravity.getValue();
                            motion = playerEntity.getDeltaMovement().add(0.0D, g * (-1.0D + (double) f3 * 0.75D), 0.0D);
                            if (motion.y < 0.0D && d9 > 0.0D) {
                                double d3 = motion.y * -0.1D * (double) f3;
                            }

                            if (f6 < 0.0F && d9 > 0.0D) {
                                double d13 = d11 * (double) (-MathHelper.sin(f6)) * 0.04D;
                            }

                            double lookY = lookVec.y;
                            double yaw = Math.toRadians(playerEntity.yHeadRot + 90);
                            if (lookY < 0) {
                                ax += Math.cos(yaw) / 1000;
                                az += Math.sin(yaw) / 1000;
                            } else {
                                ax *= 0.99;
                                az *= 0.99;
                                ay = lookVec.y / 8;
                            }
                            double speedLimit = ConfigHandler.SERVER.maxFlightSpeed.get();
                            ax = MathHelper.clamp(ax, -0.2 * speedLimit, 0.2 * speedLimit);
                            az = MathHelper.clamp(az, -0.2 * speedLimit, 0.2 * speedLimit);
                        }
                    }
                }
            });
        }
    }

    /**
     * Sets the fall damage based on flight speed
     */
    @SubscribeEvent
    public static void changeFlightFallDamage(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        if (livingEntity.level.isClientSide())
            return;
        DamageSource damageSource = event.getSource();
        if (damageSource == DamageSource.FALL) {
            final double flightSpeed = Math.sqrt(ax * ax + ay * ay + az * az);
            if (livingEntity.isPassenger() && DragonStateProvider.isDragon(livingEntity.getVehicle())) {
                event.setCanceled(true);
                ax = 0;
                az = 0;
                ay = 0;
            } else if (ConfigHandler.SERVER.enableFlightFallDamage.get()) {
                DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
                    if (dragonStateHandler.isDragon() && DragonSizeHandler.wingsStatusServer.containsKey(livingEntity.getId()) && DragonSizeHandler.wingsStatusServer.get(livingEntity.getId())) {
                        ax = 0;
                        az = 0;
                        ay = 0;
                        if (flightSpeed > 0.03) {
                            event.setAmount((float) (flightSpeed * 100));
                        } else {
                            event.setCanceled(true);
                        }
                    }
                });
            }
        }
    }
}
