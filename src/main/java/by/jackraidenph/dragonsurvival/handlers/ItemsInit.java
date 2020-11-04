package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.items.HeartElement;
import net.minecraft.item.Item;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemsInit {
    public static HeartElement heartElement;
    @SubscribeEvent
    public static void register(final RegistryEvent.Register<Item> event) {
        heartElement = new HeartElement(new Item.Properties().group(BlockInit.blocks).maxStackSize(16));
        heartElement.setRegistryName(DragonSurvivalMod.MODID, "heart_element");
        event.getRegistry().registerAll(heartElement);
    }
}
