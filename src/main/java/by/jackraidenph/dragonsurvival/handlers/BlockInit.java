package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.blocks.DragonAltarBlock;
import by.jackraidenph.dragonsurvival.blocks.PredatorStarBlock;
import by.jackraidenph.dragonsurvival.tiles.PredatorStarTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {

    public static final Block DRAGON_ALTAR_BLOCK = new DragonAltarBlock(
            Block.Properties
                    .create(Material.ANVIL)
                    .harvestTool(ToolType.PICKAXE)
                    .harvestLevel(2)
                    .hardnessAndResistance(5.0f)
                    .lightValue(5)
                    .sound(SoundType.ANVIL));

    public static final Block PREDATOR_STAR_BLOCK = new PredatorStarBlock(
            Block.Properties
                    .create(Material.DRAGON_EGG)
                    .doesNotBlockMovement()
                    .hardnessAndResistance(9999)
                    .tickRandomly()
                    .noDrops()
                    .sound(SoundType.STONE));

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().registerAll(
                DRAGON_ALTAR_BLOCK,
                PREDATOR_STAR_BLOCK);
    }

    @SubscribeEvent
    public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                new BlockItem(DRAGON_ALTAR_BLOCK, new Item.Properties().group(ItemGroup.DECORATIONS)).setRegistryName("dragon_altar"),
                new BlockItem(PREDATOR_STAR_BLOCK, new Item.Properties().group(ItemGroup.DECORATIONS)).setRegistryName("predator_star"));
    }
}