package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SpawnEggItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(DragonSurvivalMod.MODID)
@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemInit {
    public static final Item BEAST_SPAWN = new SpawnEggItem(
            EntityTypesInit.MAGICAL_BEAST.get(),
            0x2b2b2b,
            0x5a5a5a,
            new Item.Properties().group(ItemGroup.FOOD));

    @SubscribeEvent
    public static void registerEntitySpawnEggs(final RegistryEvent.Register<Item> event) {
        //event.getRegistry().register(BEAST_SPAWN);
    }
}
