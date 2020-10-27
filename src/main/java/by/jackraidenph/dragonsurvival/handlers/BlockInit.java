package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.blocks.DragonAltarBlock;
import by.jackraidenph.dragonsurvival.blocks.DragonDoor;
import by.jackraidenph.dragonsurvival.items.DragonDoorItem;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(DragonSurvivalMod.MODID)
@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {
    static ItemGroup blocks=new ItemGroup("dragon.survival.blocks") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(dragon_altar);
        }
    };

    public static Block dragon_altar;

    public static DragonDoor dragonDoor;
    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        //WARNING: do not use final static initialization outside from here, because it breaks hot-swap
        dragon_altar = new DragonAltarBlock(Block.Properties
                        .create(Material.ANVIL)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(2)
                        .hardnessAndResistance(5.0f)
                        .lightValue(5)
                        .sound(SoundType.ANVIL));
        event.getRegistry().register(dragon_altar);
        IForgeRegistry<Block> forgeRegistry= event.getRegistry();
        dragonDoor = new DragonDoor(Block.Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(3.0F).sound(SoundType.WOOD).notSolid());
        forgeRegistry.register(dragonDoor.setRegistryName(DragonSurvivalMod.MODID, "dragon_gate"));
    }

    @SubscribeEvent
    public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new BlockItem(dragon_altar, new Item.Properties().group(ItemGroup.DECORATIONS)).setRegistryName("dragon_altar"));
        IForgeRegistry<Item> forgeRegistry = event.getRegistry();
        Item dragonDoorItem = new DragonDoorItem(dragonDoor, new Item.Properties().group(blocks)).setRegistryName(dragonDoor.getRegistryName());
        forgeRegistry.register(dragonDoorItem);
    }
}