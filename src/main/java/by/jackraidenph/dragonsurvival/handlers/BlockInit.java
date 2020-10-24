package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.blocks.DragonAltarBlock;
import by.jackraidenph.dragonsurvival.blocks.DragonGateBlock;
import by.jackraidenph.dragonsurvival.blocks.DragonGateController;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
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

    public static final Block dragon_altar = new DragonAltarBlock(
            Block.Properties
                    .create(Material.ANVIL)
                    .harvestTool(ToolType.PICKAXE)
                    .harvestLevel(2)
                    .hardnessAndResistance(5.0f)
                    .lightValue(5)
                    .sound(SoundType.ANVIL));

    public static final DragonGateBlock dragonGate=new DragonGateBlock(Block.Properties.create(Material.WOOD).hardnessAndResistance(-1,30));
    public static final DragonGateController dragonGateController=new DragonGateController(Block.Properties.create(Material.WOOD).hardnessAndResistance(5,30));

    @SubscribeEvent
    public static void registerBlocks(final RegistryEvent.Register<Block> event) {
        event.getRegistry().register(dragon_altar);
        IForgeRegistry<Block> forgeRegistry= event.getRegistry();
        forgeRegistry.register(dragonGate.setRegistryName(DragonSurvivalMod.MODID,"dragon_gate"));
        forgeRegistry.register(dragonGateController.setRegistryName(DragonSurvivalMod.MODID,"dragon_gate_controller"));

    }

    @SubscribeEvent
    public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
        event.getRegistry().register(new BlockItem(dragon_altar, new Item.Properties().group(ItemGroup.DECORATIONS)).setRegistryName("dragon_altar"));
        IForgeRegistry<Item> forgeRegistry= event.getRegistry();
//        forgeRegistry.register(new BlockItem(dragonGate,new Item.Properties().group(blocks)).setRegistryName(dragonGate.getRegistryName()));
        forgeRegistry.register(new BlockItem(dragonGateController,new Item.Properties().group(blocks)).setRegistryName(dragonGateController.getRegistryName()));
    }
}