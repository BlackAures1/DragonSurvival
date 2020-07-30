package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.blocks.DragonAltarBlock;
import by.jackraidenph.dragonsurvival.blocks.PredatorStarBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.InvocationTargetException;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {

    public static final Block dragon_altar = new DragonAltarBlock(
            Block.Properties
                    .create(Material.ANVIL)
                    .harvestTool(ToolType.PICKAXE)
                    .harvestLevel(2)
                    .hardnessAndResistance(5.0f)
                    .lightValue(5)
                    .sound(SoundType.ANVIL));

    public static final Block predator_star = new PredatorStarBlock(
            Block.Properties
                    .create(Material.DRAGON_EGG)
                    .doesNotBlockMovement()
                    .noDrops()
                    .sound(SoundType.STONE));

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                dragon_altar,
                predator_star);
    }

    @SubscribeEvent
    public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new BlockItem(dragon_altar, new Item.Properties().group(ItemGroup.DECORATIONS)).setRegistryName("dragon_altar"),
                new BlockItem(predator_star, new Item.Properties().group(ItemGroup.DECORATIONS)).setRegistryName("predator_star"));
    }
}