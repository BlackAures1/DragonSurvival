package by.jackraidenph.dragonsurvival.handlers;

import java.util.concurrent.ConcurrentHashMap;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.network.ToggleWings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
@SuppressWarnings("unused")
public class FlightController {
	
    public static boolean wingsEnabled;
    static double ax, ay, az;

    @SubscribeEvent
    public static void flightControl(TickEvent.PlayerTickEvent playerTickEvent) {
        PlayerEntity playerEntity = playerTickEvent.player;
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                if (wingsEnabled) {
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
                            motion = motion.add(lookVec.x * d3 / d9, d3, lookVec.z * d3 / d9);
                        }

                        if (f6 < 0.0F && d9 > 0.0D) {
                            double d13 = d11 * (double) (-MathHelper.sin(f6)) * 0.04D;
                            motion = motion.add(-lookVec.x * d13 / d9, d13 * 3.2D, -lookVec.z * d13 / d9);
                        }

                        if (d9 > 0.0D) {
                            motion = motion.add((lookVec.x / d9 * d11 - motion.x) * 0.1D, 0.0D, (lookVec.z / d9 * d11 - motion.z) * 0.1D);
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
                        if (lookY < 0) {
                            motion = motion.add(ax, 0, az);
                        } else {
                            motion = motion.add(ax, ay, az);
                        }
                        playerEntity.setDeltaMovement(motion.multiply(0.99F, 0.98F, 0.99F));
                    } else {
                        ax = 0;
                        az = 0;
                        ay = 0;
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void toggleWings(InputEvent.KeyInputEvent keyInputEvent) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player != null && ClientModEvents.TOGGLE_WINGS.consumeClick()) {
            DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
                if (dragonStateHandler.hasWings()) {
                    wingsEnabled = !wingsEnabled;
                    DragonSurvivalMod.CHANNEL.sendToServer(new ToggleWings(wingsEnabled));
                    if (wingsEnabled)
                        player.sendMessage(new TranslationTextComponent("ds.wings.enabled"), player.getUUID());
                    else
                        player.sendMessage(new TranslationTextComponent("ds.wings.disabled"), player.getUUID());
                } else {
                    player.sendMessage(new TranslationTextComponent("ds.you.have.no.wings"), player.getUUID());
                }
            });
        }
    }
}
