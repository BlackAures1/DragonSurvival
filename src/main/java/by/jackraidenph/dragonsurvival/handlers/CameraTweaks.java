package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLLoader;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import static java.lang.Math.abs;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CameraTweaks {

    private static Method movePosition;
//    private static Method setPosition;

    static {
        try {
            if(FMLLoader.isProduction())
            {
                DragonSurvivalMod.LOGGER.info("Found methods: "+Arrays.toString(ActiveRenderInfo.class.getDeclaredMethods()));
                movePosition=ActiveRenderInfo.class.getDeclaredMethod("func_216782_a",double.class,double.class,double.class);
            }
            else{

                movePosition =ActiveRenderInfo.class.getDeclaredMethod("movePosition", double.class, double.class, double.class);
//                setPosition = ActiveRenderInfo.class.getDeclaredMethod("setPosition", double.class, double.class, double.class);
            }
            movePosition.setAccessible(true);
//            setPosition.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * Synchronizes the camera to dragon head
     */
    private static void setCamera(EntityViewRenderEvent.CameraSetup event, double dx, double dy, double dz, Vec3d lookVec) {
        ActiveRenderInfo info = event.getInfo();

        try {
            movePosition.invoke(info, dx, dy, dz);
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        //setPosition.invoke(info, dx, dy, dz);
    }

    public static final int maxNeckLen = 1;

    private static double neckLen = 0;

    @SubscribeEvent
    public static void cameraSetup(final EntityViewRenderEvent.CameraSetup event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (DragonStateProvider.isDragon(minecraft.player)) {
            ActiveRenderInfo info = event.getInfo();

            float pitch = event.getPitch();

            //pitch=0 => dx=2, dy=0
            //pitch=90 => dx=0, dy=2
            //pitch=-90 => dx=0, dy=-2

            double dx = (90 - abs(pitch)) / 90 * neckLen;
            double dy = pitch / 90 * neckLen;
            //TODO
//            setCamera(event, dx, dy + 1, 0, null);
        }

    }

    @SubscribeEvent
    public static void onPlayerEnterToWorld(EntityJoinWorldEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (event.getEntity() instanceof PlayerEntity && event.getEntity() == minecraft.player)
            if (DragonStateProvider.isDragon(minecraft.player))
                neckLen = maxNeckLen;
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.ClientTickEvent event) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && neckLen < maxNeckLen)
            if (DragonStateProvider.isDragon(minecraft.player))
                neckLen += 0.02;
    }

}
