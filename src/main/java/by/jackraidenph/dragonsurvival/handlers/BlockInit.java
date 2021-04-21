package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.blocks.DragonAltarBlock;
import by.jackraidenph.dragonsurvival.blocks.DragonDoor;
import by.jackraidenph.dragonsurvival.blocks.PredatorStarBlock;
import by.jackraidenph.dragonsurvival.items.DragonDoorItem;
import by.jackraidenph.dragonsurvival.nest.BigNestBlock;
import by.jackraidenph.dragonsurvival.nest.MediumNestBlock;
import by.jackraidenph.dragonsurvival.nest.NestBlock;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

@ObjectHolder(DragonSurvivalMod.MODID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {
    public static Block PREDATOR_STAR_BLOCK;

    public static Block dragon_altar;
    public static Block dragon_altar2;
    public static Block dragon_altar3;
    public static Block dragon_altar4;
    public static NestBlock smallCaveNest, smallForestNest, smallSeaNest;
    public static DragonDoor dragonDoor;
    public static MediumNestBlock mediumSeaNest, mediumCaveNest, mediumForestNest;
    public static BigNestBlock bigForestNest, bigCaveNest, bigSeaNest;

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        //WARNING: do not use final static initialization outside from here, because it breaks hot-swap
        IForgeRegistry<Block> forgeRegistry = event.getRegistry();
        dragon_altar = new DragonAltarBlock(Block.Properties
                .create(Material.ANVIL).harvestTool(ToolType.PICKAXE).harvestLevel(2)
                .hardnessAndResistance(5.0f).lightValue(5).sound(SoundType.ANVIL));
        forgeRegistry.register(dragon_altar.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar0"));
        dragon_altar2 = new DragonAltarBlock(Block.Properties.from(dragon_altar));
        forgeRegistry.register(dragon_altar2.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar1"));
        dragon_altar3 = new DragonAltarBlock(Block.Properties.from(dragon_altar));
        forgeRegistry.register(dragon_altar3.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar2"));
        dragon_altar4 = new DragonAltarBlock(Block.Properties.from(dragon_altar));
        forgeRegistry.register(dragon_altar4.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar3"));
        PREDATOR_STAR_BLOCK = new PredatorStarBlock(Block.Properties
                .create(Material.DRAGON_EGG)
                .doesNotBlockMovement()
                .hardnessAndResistance(9999)
                .tickRandomly()
                .noDrops()
                .sound(SoundType.NETHER_WART));
        forgeRegistry.register(PREDATOR_STAR_BLOCK.setRegistryName(DragonSurvivalMod.MODID, "predator_star"));
        dragonDoor = new DragonDoor(Block.Properties.create(Material.WOOD, MaterialColor.BROWN).hardnessAndResistance(3.0F).sound(SoundType.WOOD).notSolid());

        smallCaveNest = new NestBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3, 100).notSolid());
        smallSeaNest = new NestBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3, 100).notSolid());
        smallForestNest = new NestBlock(Block.Properties.create(Material.ROCK).hardnessAndResistance(3, 100).notSolid());
        forgeRegistry.register(smallCaveNest.setRegistryName(DragonSurvivalMod.MODID, "cave_nest_small"));
        forgeRegistry.register(smallForestNest.setRegistryName(DragonSurvivalMod.MODID, "forest_nest_small"));
        forgeRegistry.register(smallSeaNest.setRegistryName(DragonSurvivalMod.MODID, "water_nest_small"));

        forgeRegistry.register(dragonDoor.setRegistryName(DragonSurvivalMod.MODID, "dragon_gate"));

        mediumSeaNest = new MediumNestBlock(Block.Properties.from(smallSeaNest));
        forgeRegistry.register(mediumSeaNest.setRegistryName(DragonSurvivalMod.MODID, "medium_sea_nest"));
        mediumCaveNest = new MediumNestBlock(Block.Properties.from(smallCaveNest));
        forgeRegistry.register(mediumCaveNest.setRegistryName(DragonSurvivalMod.MODID, "medium_cave_nest"));
        mediumForestNest = new MediumNestBlock(Block.Properties.from(smallForestNest));
        forgeRegistry.register(mediumForestNest.setRegistryName(DragonSurvivalMod.MODID, "medium_forest_nest"));

        bigCaveNest = new BigNestBlock(Block.Properties.from(smallCaveNest));
        forgeRegistry.register(bigCaveNest.setRegistryName(DragonSurvivalMod.MODID, "big_cave_nest"));
        bigForestNest = new BigNestBlock(Block.Properties.from(smallForestNest));
        forgeRegistry.register(bigForestNest.setRegistryName(DragonSurvivalMod.MODID, "big_forest_nest"));
        bigSeaNest = new BigNestBlock(Block.Properties.from(smallSeaNest));
        forgeRegistry.register(bigSeaNest.setRegistryName(DragonSurvivalMod.MODID, "big_sea_nest"));
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> forgeRegistry = event.getRegistry();
        Item dragonDoorItem = new DragonDoorItem(dragonDoor, new Item.Properties().group(ItemsInit.items)).setRegistryName(dragonDoor.getRegistryName());
        forgeRegistry.register(dragonDoorItem);
        forgeRegistry.register(new BlockItem(PREDATOR_STAR_BLOCK, new Item.Properties().group(ItemsInit.items)).setRegistryName("predator_star"));

        registerSingleItem(smallCaveNest, new Item.Properties(), forgeRegistry);
        registerSingleItem(smallForestNest, new Item.Properties(), forgeRegistry);
        registerSingleItem(smallSeaNest, new Item.Properties(), forgeRegistry);

        registerSingleItem(mediumSeaNest, new Item.Properties(), forgeRegistry);
        registerSingleItem(mediumForestNest, new Item.Properties(), forgeRegistry);
        registerSingleItem(mediumCaveNest, new Item.Properties(), forgeRegistry);

        registerSingleItem(bigSeaNest, new Item.Properties(), forgeRegistry);
        registerSingleItem(bigForestNest, new Item.Properties(), forgeRegistry);
        registerSingleItem(bigCaveNest, new Item.Properties(), forgeRegistry);

        registerItem(dragon_altar, new Item.Properties(), forgeRegistry);
        registerItem(dragon_altar2, new Item.Properties(), forgeRegistry);
        registerItem(dragon_altar3, new Item.Properties(), forgeRegistry);
        registerItem(dragon_altar4, new Item.Properties(), forgeRegistry);
    }

    @SuppressWarnings("ConstantConditions")
    private static void registerItem(Block block, Item.Properties itemProperties, IForgeRegistry<Item> forgeRegistry) {
        forgeRegistry.register(new BlockItem(block, itemProperties.group(ItemsInit.items)).setRegistryName(block.getRegistryName()));
    }

    private static void registerSingleItem(Block block, Item.Properties properties, IForgeRegistry<Item> forgeRegistry) {
        registerItem(block, properties.maxStackSize(1), forgeRegistry);
    }
}