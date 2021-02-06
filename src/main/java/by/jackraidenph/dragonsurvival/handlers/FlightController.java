package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
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
                        Vec3d vec3d3 = playerEntity.getMotion();
                        if (vec3d3.y > -0.5D) {
                            playerEntity.fallDistance = 1.0F;
                        }

                        Vec3d vec3d = playerEntity.getLookVec();
                        float f6 = playerEntity.rotationPitch * ((float) Math.PI / 180F);
                        double d9 = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
                        double d11 = Math.sqrt(LivingEntity.horizontalMag(vec3d3));
                        double d12 = vec3d.length();
                        float f3 = MathHelper.cos(f6);
                        f3 = (float) ((double) f3 * (double) f3 * Math.min(1.0D, d12 / 0.4D));
                        IAttributeInstance gravity = playerEntity.getAttribute(LivingEntity.ENTITY_GRAVITY);
                        double g = gravity.getValue();
                        vec3d3 = playerEntity.getMotion().add(0.0D, g * (-1.0D + (double) f3 * 0.75D), 0.0D);
                        if (vec3d3.y < 0.0D && d9 > 0.0D) {
                            double d3 = vec3d3.y * -0.1D * (double) f3;
                            vec3d3 = vec3d3.add(vec3d.x * d3 / d9, d3, vec3d.z * d3 / d9);
                        }

                        if (f6 < 0.0F && d9 > 0.0D) {
                            double d13 = d11 * (double) (-MathHelper.sin(f6)) * 0.04D;
                            vec3d3 = vec3d3.add(-vec3d.x * d13 / d9, d13 * 3.2D, -vec3d.z * d13 / d9);
                        }

                        if (d9 > 0.0D) {
                            vec3d3 = vec3d3.add((vec3d.x / d9 * d11 - vec3d3.x) * 0.1D, 0.0D, (vec3d.z / d9 * d11 - vec3d3.z) * 0.1D);
                        }

                        playerEntity.setMotion(vec3d3.mul(0.99F, 0.98F, 0.99F));
                        playerEntity.move(MoverType.SELF, playerEntity.getMotion());
//                        if (playerEntity.collidedHorizontally && !playerEntity.world.isRemote) {
//                            double d14 = Math.sqrt(LivingEntity.horizontalMag(playerEntity.getMotion()));
//                            double d4 = d11 - d14;
//                            float f4 = (float)(d4 * 10.0D - 3.0D);
//                            if (f4 > 0.0F) {
//                                playerEntity.playSound(playerEntity.getFallSound((int)f4), 1.0F, 1.0F);
//                                playerEntity.attackEntityFrom(DamageSource.FLY_INTO_WALL, f4);
//                            }
//                        }
//
//                        if (playerEntity.onGround && !playerEntity.world.isRemote) {
//                            playerEntity.setFlag(7, false);
//                        }
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
