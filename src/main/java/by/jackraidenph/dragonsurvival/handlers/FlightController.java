package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.Functions;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(Dist.CLIENT)
public class FlightController {

    static double velX;
    static double velY;
    static double velZ;

    @SubscribeEvent
    public static void controlVelocity(TickEvent.PlayerTickEvent playerTickEvent) {
        PlayerEntity playerEntity = playerTickEvent.player;
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                if (!playerEntity.onGround) {
                    //my code
//                    double verticalSpeed = playerEntity.rotationPitch;
//                    float radians = Math.min(Functions.degreesToRadians(30)+90,Functions.degreesToRadians(playerEntity.rotationYawHead+90));
//                    if(verticalSpeed<0) {
//                        playerEntity.addVelocity(MathHelper.cos(radians)/10,.1,MathHelper.sin(radians)/10);
//                    }
//                    else{
//                        playerEntity.addVelocity(MathHelper.cos(radians)/16,0,MathHelper.sin(radians)/16);
//                    }

                    double yaw = Functions.degreesToRadians(playerEntity.rotationYawHead);
                    double pitch = Functions.degreesToRadians(playerEntity.rotationPitch);
                    double yawcos = Math.cos(-yaw - Math.PI);
                    double yawsin = Math.sin(-yaw - Math.PI);
                    double pitchcos = Math.cos(pitch);
                    double pitchsin = Math.sin(pitch);

                    double lookX = yawsin * -pitchcos;
                    double lookZ = yawcos * -pitchcos;

                    double hvel = Math.sqrt(velX * velX + velZ * velZ);
                    double sqrpitchcos = pitchcos * pitchcos;

                    velY += -0.08 + sqrpitchcos * 0.06;

                    if (velY < 0 && pitchcos > 0) {
                        double yacc = velY * -0.1 * sqrpitchcos;
                        velY += yacc;
                        velX += lookX * yacc / pitchcos;
                        velZ += lookZ * yacc / pitchcos;
                    }
                    if (pitch < 0) {
                        double yacc = hvel * -pitchsin * 0.04;
                        velY += yacc * 3.5;
                        velX -= lookX * yacc / pitchcos;
                        velZ -= lookZ * yacc / pitchcos;
                    }
                    if (pitchcos > 0) {
                        velX += (lookX / pitchcos * hvel - velX) * 0.1;
                        velZ += (lookZ / pitchcos * hvel - velZ) * 0.1;
                    }

                    velX *= 0.99;
                    velY *= 0.98;
                    velZ *= 0.99;
                    playerEntity.addVelocity(Math.min(0.5, velX / 5), MathHelper.clamp(velY, 0, 0.7), Math.min(0.5, velZ / 5));
//                    System.out.println(velY);
                }
            }
        });
    }
}
