package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.blocks.DragonAltarBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
@Deprecated
//@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlocksInit {

    public static Block DRAGON_ALTAR_BLOCK;

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        DRAGON_ALTAR_BLOCK = new DragonAltarBlock(Block.Properties
                        .create(Material.ANVIL)
                        .harvestTool(ToolType.PICKAXE)
                        .harvestLevel(2)
                        .hardnessAndResistance(5.0f)
                        .lightValue(5)
                        .sound(SoundType.STONE));
        event.getRegistry().registerAll(
                DRAGON_ALTAR_BLOCK,
                BlockInit.PREDATOR_STAR_BLOCK);
    }

    @SubscribeEvent
    public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new BlockItem(DRAGON_ALTAR_BLOCK, new Item.Properties().group(ItemGroup.DECORATIONS)).setRegistryName("dragon_altar"));
    }
}