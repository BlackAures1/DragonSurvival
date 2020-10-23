package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.blockentities.DragonGateBlockEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collections;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockEntities {

    public static TileEntityType<DragonGateBlockEntity> dragonGateBlockEntityEntityType;
    @SubscribeEvent
    public static void registerBlockEntities(RegistryEvent.Register<TileEntityType<?>> registryEvent)
    {
        IForgeRegistry<TileEntityType<?>> forgeRegistry=registryEvent.getRegistry();
        dragonGateBlockEntityEntityType=new TileEntityType<>(() -> new DragonGateBlockEntity(dragonGateBlockEntityEntityType), Collections.singleton(BlockInit.dragonGate),null);
        dragonGateBlockEntityEntityType.setRegistryName(BlockInit.dragonGate.getRegistryName());
        forgeRegistry.register(dragonGateBlockEntityEntityType);
    }
}
