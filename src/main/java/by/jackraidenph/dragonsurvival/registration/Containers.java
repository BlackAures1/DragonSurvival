package by.jackraidenph.dragonsurvival.registration;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.containers.CraftingContainer;
import by.jackraidenph.dragonsurvival.nest.NestContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Containers {

    public static ContainerType<NestContainer> nestContainer;
    public static ContainerType<CraftingContainer> craftingContainer;
    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> register) {
        nestContainer = IForgeContainerType.create(NestContainer::new);
        IForgeRegistry<ContainerType<?>> forgeRegistry = register.getRegistry();
        forgeRegistry.register(nestContainer.setRegistryName(DragonSurvivalMod.MODID, "dragon_nest"));

        craftingContainer=new ContainerType<>(CraftingContainer::new);
        forgeRegistry.register(craftingContainer.setRegistryName(DragonSurvivalMod.MODID,"extra_crafting"));
    }
}
