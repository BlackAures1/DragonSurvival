package by.jackraidenph.dragonsurvival.handlers;

import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.IForgeRegistry;

//@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockEntities {

    @SubscribeEvent
    public static void registerBlockEntities(RegistryEvent.Register<TileEntityType<?>> registryEvent)
    {
        IForgeRegistry<TileEntityType<?>> forgeRegistry=registryEvent.getRegistry();
    }
}
