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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class CameraTweaks {

    private static Method movePosition;

    static {
        try {
            movePosition = ActiveRenderInfo.class.getDeclaredMethod("movePosition", double.class, double.class, double.class);
            movePosition.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @SubscribeEvent
    public static void cameraSetup(final EntityViewRenderEvent.CameraSetup event) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        Minecraft minecraft = Minecraft.getInstance();
        ActiveRenderInfo info = event.getInfo();
        movePosition.invoke(info, 0, 1, 0);
        //GL11.glTranslated(0, -1, 0);

    }
}
