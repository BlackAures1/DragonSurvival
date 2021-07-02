package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.blocks.DragonAltarBlock;
import by.jackraidenph.dragonsurvival.blocks.DragonDoor;
import by.jackraidenph.dragonsurvival.blocks.PredatorStarBlock;
import by.jackraidenph.dragonsurvival.items.DragonDoorItem;
import by.jackraidenph.dragonsurvival.nest.BigNestBlock;
import by.jackraidenph.dragonsurvival.nest.MediumNestBlock;
import by.jackraidenph.dragonsurvival.nest.NestBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
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
    public static DragonDoor spruceDoor, acaciaDoor, birchDoor, jungleDoor, oakDoor, darkOakDoor, crimsonDoor, warpedDoor;
    public static DragonDoor legacyDoor, ironDoor, murdererDoor, sleeperDoor, stoneDoor;
    public static DragonDoor caveDoor, forestDoor, seaDoor;
    public static MediumNestBlock mediumSeaNest, mediumCaveNest, mediumForestNest;
    public static BigNestBlock bigForestNest, bigCaveNest, bigSeaNest;

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        //WARNING: do not use final static initialization outside from here, because it breaks hot-swap
        IForgeRegistry<Block> forgeRegistry = event.getRegistry();
        dragon_altar = new DragonAltarBlock(Block.Properties
                .of(Material.HEAVY_METAL).harvestTool(ToolType.PICKAXE).harvestLevel(2)
                .strength(5.0f).lightLevel((state) -> 5).sound(SoundType.ANVIL));
        forgeRegistry.register(dragon_altar.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar0"));
        dragon_altar2 = new DragonAltarBlock(Block.Properties.copy(dragon_altar));
        forgeRegistry.register(dragon_altar2.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar1"));
        dragon_altar3 = new DragonAltarBlock(Block.Properties.copy(dragon_altar));
        forgeRegistry.register(dragon_altar3.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar2"));
        dragon_altar4 = new DragonAltarBlock(Block.Properties.copy(dragon_altar));
        forgeRegistry.register(dragon_altar4.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar3"));
        PREDATOR_STAR_BLOCK = new PredatorStarBlock(Block.Properties.of(Material.EGG).noCollission()
                .strength(10F, 9999F).randomTicks().harvestTool(ToolType.HOE).requiresCorrectToolForDrops()
                .sound(SoundType.NETHER_WART));
        forgeRegistry.register(PREDATOR_STAR_BLOCK.setRegistryName(DragonSurvivalMod.MODID, "predator_star"));
        
        smallCaveNest = new NestBlock(Block.Properties.of(Material.STONE).strength(3, 100).noOcclusion());
        smallSeaNest = new NestBlock(Block.Properties.of(Material.STONE).strength(3, 100).noOcclusion());
        smallForestNest = new NestBlock(Block.Properties.of(Material.STONE).strength(3, 100).noOcclusion());
        forgeRegistry.register(smallCaveNest.setRegistryName(DragonSurvivalMod.MODID, "cave_nest_small"));
        forgeRegistry.register(smallForestNest.setRegistryName(DragonSurvivalMod.MODID, "forest_nest_small"));
        forgeRegistry.register(smallSeaNest.setRegistryName(DragonSurvivalMod.MODID, "water_nest_small"));

        
        
        oakDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.OAK_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "oak_dragon_door", forgeRegistry);
        spruceDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.SPRUCE_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "spruce_dragon_door", forgeRegistry);
        acaciaDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.ACACIA_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "acacia_dragon_door", forgeRegistry);
        birchDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.BIRCH_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "birch_dragon_door", forgeRegistry);
        jungleDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.JUNGLE_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "jungle_dragon_door", forgeRegistry);
        darkOakDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.DARK_OAK_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "dark_oak_dragon_door", forgeRegistry);
        warpedDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.WARPED_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "warped_dragon_door", forgeRegistry);
        crimsonDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.CRIMSON_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "crimson_dragon_door", forgeRegistry);
        
        caveDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.STONE, Blocks.BLACKSTONE.defaultMaterialColor()).requiresCorrectToolForDrops().strength(7.0F).sound(SoundType.GILDED_BLACKSTONE).noOcclusion(), DragonDoor.OpenRequirement.CAVE), "cave_dragon_door", forgeRegistry);
        forestDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.STONE, Blocks.DARK_PRISMARINE.defaultMaterialColor()).requiresCorrectToolForDrops().strength(7.0F).sound(SoundType.STONE).noOcclusion(), DragonDoor.OpenRequirement.FOREST), "forest_dragon_door", forgeRegistry);
        seaDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, MaterialColor.COLOR_BROWN).requiresCorrectToolForDrops().strength(7.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.SEA), "sea_dragon_door", forgeRegistry);

        ironDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.METAL, MaterialColor.METAL).requiresCorrectToolForDrops().strength(5.0F).sound(SoundType.METAL).noOcclusion(), DragonDoor.OpenRequirement.POWER), "iron_dragon_door", forgeRegistry);
        
        // TODO set properties for these doors
        murdererDoor = registerBlock(new DragonDoor(AbstractBlock.Properties.copy(oakDoor), DragonDoor.OpenRequirement.NONE), "murderer_dragon_door", forgeRegistry);
        sleeperDoor = registerBlock(new DragonDoor(AbstractBlock.Properties.copy(oakDoor), DragonDoor.OpenRequirement.NONE), "sleeper_dragon_door", forgeRegistry);
        stoneDoor = registerBlock(new DragonDoor(AbstractBlock.Properties.copy(oakDoor), DragonDoor.OpenRequirement.NONE), "stone_dragon_door", forgeRegistry);
        legacyDoor = registerBlock(new DragonDoor(Block.Properties.of(Material.WOOD, Blocks.SPRUCE_PLANKS.defaultMaterialColor()).strength(3.0F).sound(SoundType.WOOD).noOcclusion(), DragonDoor.OpenRequirement.NONE), "legacy_dragon_door", forgeRegistry);

        mediumSeaNest = new MediumNestBlock(Block.Properties.copy(smallSeaNest));
        forgeRegistry.register(mediumSeaNest.setRegistryName(DragonSurvivalMod.MODID, "medium_sea_nest"));
        mediumCaveNest = new MediumNestBlock(Block.Properties.copy(smallCaveNest));
        forgeRegistry.register(mediumCaveNest.setRegistryName(DragonSurvivalMod.MODID, "medium_cave_nest"));
        mediumForestNest = new MediumNestBlock(Block.Properties.copy(smallForestNest));
        forgeRegistry.register(mediumForestNest.setRegistryName(DragonSurvivalMod.MODID, "medium_forest_nest"));

        bigCaveNest = new BigNestBlock(Block.Properties.copy(smallCaveNest));
        forgeRegistry.register(bigCaveNest.setRegistryName(DragonSurvivalMod.MODID, "big_cave_nest"));
        bigForestNest = new BigNestBlock(Block.Properties.copy(smallForestNest));
        forgeRegistry.register(bigForestNest.setRegistryName(DragonSurvivalMod.MODID, "big_forest_nest"));
        bigSeaNest = new BigNestBlock(Block.Properties.copy(smallSeaNest));
        forgeRegistry.register(bigSeaNest.setRegistryName(DragonSurvivalMod.MODID, "big_sea_nest"));
    }

    private static <B extends Block> B registerBlock(B block, String identifier, IForgeRegistry<Block> forgeRegistry) {
        block.setRegistryName(DragonSurvivalMod.MODID, identifier);
        forgeRegistry.register(block);
        return block;
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
        IForgeRegistry<Item> forgeRegistry = event.getRegistry();
        registerDoor(oakDoor, new Item.Properties(), forgeRegistry);
        registerDoor(spruceDoor, new Item.Properties(), forgeRegistry);
        registerDoor(acaciaDoor, new Item.Properties(), forgeRegistry);
        registerDoor(jungleDoor, new Item.Properties(), forgeRegistry);
        registerDoor(darkOakDoor, new Item.Properties(), forgeRegistry);
        registerDoor(birchDoor, new Item.Properties(), forgeRegistry);
        registerDoor(caveDoor, new Item.Properties(), forgeRegistry);
        registerDoor(seaDoor, new Item.Properties(), forgeRegistry);
        registerDoor(forestDoor, new Item.Properties(), forgeRegistry);
        registerDoor(ironDoor, new Item.Properties(), forgeRegistry);
        registerDoor(murdererDoor, new Item.Properties(), forgeRegistry);
        registerDoor(warpedDoor, new Item.Properties(), forgeRegistry);
        registerDoor(crimsonDoor, new Item.Properties(), forgeRegistry);
        registerDoor(sleeperDoor, new Item.Properties(), forgeRegistry);
        registerDoor(stoneDoor, new Item.Properties(), forgeRegistry);
        registerDoor(legacyDoor, new Item.Properties(), forgeRegistry);
        

        forgeRegistry.register(new BlockItem(PREDATOR_STAR_BLOCK, new Item.Properties().tab(ItemsInit.items)).setRegistryName("predator_star"));

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
        forgeRegistry.register(new BlockItem(block, itemProperties.tab(ItemsInit.items)).setRegistryName(block.getRegistryName()));
    }

    private static void registerSingleItem(Block block, Item.Properties properties, IForgeRegistry<Item> forgeRegistry) {
        registerItem(block, properties.stacksTo(1), forgeRegistry);
    }

    private static void registerDoor(Block block, Item.Properties itemProps, IForgeRegistry<Item> forgeRegistry) {
        forgeRegistry.register(new DragonDoorItem(block, itemProps.tab(ItemsInit.items)).setRegistryName(block.getRegistryName()));
    }
}