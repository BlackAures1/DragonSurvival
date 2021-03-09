package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.blocks.DragonAltarBlock;
import by.jackraidenph.dragonsurvival.blocks.DragonDoor;
import by.jackraidenph.dragonsurvival.blocks.PredatorStarBlock;
import by.jackraidenph.dragonsurvival.items.DragonDoorItem;
import by.jackraidenph.dragonsurvival.nest.MediumNestBlock;
import by.jackraidenph.dragonsurvival.nest.NestBlock;
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
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class BlockInit {
    public static Block PREDATOR_STAR_BLOCK;
    public static ItemGroup blocks = new ItemGroup("dragon.survival.blocks") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(dragon_altar);
        }
    };

    public static Block dragon_altar;
    public static NestBlock smallCaveNest, smallForestNest, smallSeaNest;
    public static DragonDoor dragonDoor;
    public static MediumNestBlock mediumSeaNest, mediumCaveNest, mediumForestNest;

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
        IForgeRegistry<Block> forgeRegistry = event.getRegistry();
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
    }

    @SuppressWarnings("ConstantConditions")
    @SubscribeEvent
    public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new BlockItem(dragon_altar, new Item.Properties().group(blocks)).setRegistryName("dragon_altar"));
        IForgeRegistry<Item> forgeRegistry = event.getRegistry();
        Item dragonDoorItem = new DragonDoorItem(dragonDoor, new Item.Properties().group(blocks)).setRegistryName(dragonDoor.getRegistryName());
        forgeRegistry.register(dragonDoorItem);

        forgeRegistry.register(new BlockItem(smallCaveNest, new Item.Properties()).setRegistryName(smallCaveNest.getRegistryName()));
        forgeRegistry.register(new BlockItem(smallForestNest, new Item.Properties()).setRegistryName(smallForestNest.getRegistryName()));
        forgeRegistry.register(new BlockItem(smallSeaNest, new Item.Properties()).setRegistryName(smallSeaNest.getRegistryName()));

        forgeRegistry.register(new BlockItem(PREDATOR_STAR_BLOCK, new Item.Properties().group(blocks)).setRegistryName("predator_star"));

        forgeRegistry.register(new BlockItem(mediumSeaNest, new Item.Properties()).setRegistryName(mediumSeaNest.getRegistryName()));
        forgeRegistry.register(new BlockItem(mediumCaveNest, new Item.Properties()).setRegistryName(mediumCaveNest.getRegistryName()));
        forgeRegistry.register(new BlockItem(mediumForestNest, new Item.Properties()).setRegistryName(mediumForestNest.getRegistryName()));

    }
}