package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.items.HeartElement;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemsInit {
    public static HeartElement heartElement;
    public static Item starBone, elderDragonBone;

    @SubscribeEvent
    public static void register(final RegistryEvent.Register<Item> event) {
        heartElement = new HeartElement(new Item.Properties().group(BlockInit.blocks).maxStackSize(16));
        heartElement.setRegistryName(DragonSurvivalMod.MODID, "heart_element");
        starBone = new Item(new Item.Properties().group(BlockInit.blocks)) {
            @Override
            public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
                LazyOptional<DragonStateHandler> playerStateProvider = playerIn.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY);
                if (playerStateProvider.isPresent()) {
                    DragonStateHandler dragonStateHandler = playerStateProvider.orElse(null);
                    if (dragonStateHandler.isDragon()) {
                        IAttributeInstance health = playerIn.getAttribute(SharedMonsterAttributes.MAX_HEALTH);
                        if (health.getValue() > 14) {
                            health.setBaseValue(health.getBaseValue() - 2);
                            if (health.getValue() < DragonLevel.YOUNG.initialHealth) {
                                dragonStateHandler.setLevel(DragonLevel.BABY);
                            } else if (health.getValue() < DragonLevel.ADULT.initialHealth) {
                                dragonStateHandler.setLevel(DragonLevel.YOUNG);
                            }
                            playerIn.getHeldItem(handIn).shrink(1);
                            return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
                        }
                    }
                }

                return super.onItemRightClick(worldIn, playerIn, handIn);
            }
        }.setRegistryName(DragonSurvivalMod.MODID, "star_bone");
        elderDragonBone = new Item(new Item.Properties().group(BlockInit.blocks)).setRegistryName(DragonSurvivalMod.MODID, "elder_dragon_bone");
        event.getRegistry().registerAll(heartElement, starBone, elderDragonBone);
    }
}
