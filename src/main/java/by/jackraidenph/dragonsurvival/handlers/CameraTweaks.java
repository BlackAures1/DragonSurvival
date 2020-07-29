package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.entities.RenderViewEntity;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static java.lang.Math.abs;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CameraTweaks {

    private static Method movePosition;
    private static Method setPosition;

    static {
        try {
            movePosition = ActiveRenderInfo.class.getDeclaredMethod("movePosition", double.class, double.class, double.class);
            setPosition = ActiveRenderInfo.class.getDeclaredMethod("setPosition", double.class, double.class, double.class);

            movePosition.setAccessible(true);
            setPosition.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    private static void setCamera(EntityViewRenderEvent.CameraSetup event, double dx, double dy, double dz, Vec3d lookVec) throws InvocationTargetException, IllegalAccessException {
        ActiveRenderInfo info = event.getInfo();

        movePosition.invoke(info, dx, dy, dz);
        //setPosition.invoke(info, dx, dy, dz);
    }

    @SubscribeEvent
    public static void cameraSetup(final EntityViewRenderEvent.CameraSetup event) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Minecraft minecraft = Minecraft.getInstance();
        ActiveRenderInfo info = event.getInfo();

        float pitch = event.getPitch();

        //pitch=0 => dx=2, dy=0
        //pitch=90 => dx=0, dy=2
        //pitch=-90 => dx=0, dy=-2

        int neckLen = 10;
        double dx = (90 - abs(pitch)) / 90 * neckLen;
        double dy = pitch / 90 * neckLen;

        //System.out.println(event.getPitch());

        setCamera(event, dx, dy+1, 0, null);
        //GL11.glTranslated(0, -1, 0);

    }
}
