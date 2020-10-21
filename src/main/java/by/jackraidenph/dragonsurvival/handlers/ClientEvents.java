package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.containers.DragonInventoryContainer;
import by.jackraidenph.dragonsurvival.gui.DragonInventoryScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvents {

    @SubscribeEvent
    public static void openPlayerInventory(GuiOpenEvent guiOpenEvent) {
        Screen screen = guiOpenEvent.getGui();
        PlayerEntity playerEntity = Minecraft.getInstance().player;
        if (screen instanceof InventoryScreen && playerEntity.container instanceof DragonInventoryContainer) {
            guiOpenEvent.setGui(new DragonInventoryScreen(Minecraft.getInstance().player));
        }
    }
}
