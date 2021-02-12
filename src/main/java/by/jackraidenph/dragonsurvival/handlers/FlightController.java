package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
@SuppressWarnings("unused")
public class FlightController {

    static boolean wingsEnabled;

    @SubscribeEvent
    public static void flightControl(TickEvent.PlayerTickEvent playerTickEvent) {
        PlayerEntity playerEntity = playerTickEvent.player;
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                if (!playerEntity.onGround && !playerEntity.isInWater() && wingsEnabled) {

                    {
                        Vec3d motion = playerEntity.getMotion();
                        if (motion.y > -0.5D) {
                            playerEntity.fallDistance = 1.0F;
                        }

                        Vec3d lookVec = playerEntity.getLookVec();
                        float f6 = playerEntity.rotationPitch * ((float) Math.PI / 180F);
                        double d9 = Math.sqrt(lookVec.x * lookVec.x + lookVec.z * lookVec.z);
                        double d11 = Math.sqrt(LivingEntity.horizontalMag(motion));
                        double d12 = lookVec.length();
                        float f3 = MathHelper.cos(f6);
                        f3 = (float) ((double) f3 * (double) f3 * Math.min(1.0D, d12 / 0.4D));
                        IAttributeInstance gravity = playerEntity.getAttribute(LivingEntity.ENTITY_GRAVITY);
                        double g = gravity.getValue();
                        motion = playerEntity.getMotion().add(0.0D, g * (-1.0D + (double) f3 * 0.75D), 0.0D);
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
                        playerEntity.setMotion(motion.mul(0.99F, 0.98F, 0.99F));
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void toggleWings(InputEvent.KeyInputEvent keyInputEvent) {
        if (Minecraft.getInstance().player != null && ClientModEvents.TOGGLE_WINGS.isPressed()) {
            wingsEnabled = !wingsEnabled;
            if (wingsEnabled)
                Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("ds.wings.enabled"));
            else
                Minecraft.getInstance().player.sendMessage(new TranslationTextComponent("ds.wings.disabled"));
        }
    }
}
