package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.network.SyncLevel;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemsInit {
    public static Item heartElement;
    public static Item starBone, elderDragonBone, elderDragonDust;
    public static ItemGroup items = new ItemGroup("dragon.survival.blocks") {
        @Override
        public ItemStack createIcon() {
            return new ItemStack(elderDragonDust);
        }
    };
    public static Item charredMeat, chargedCoal;

    @SubscribeEvent
    public static void register(final RegistryEvent.Register<Item> event) {
        heartElement = new Item(new Item.Properties().group(items).maxStackSize(64)) {
            @Override
            public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
                LazyOptional<DragonStateHandler> dragonStateHandlerLazyOptional = playerIn.getCapability(DragonStateProvider.DRAGON_CAPABILITY);
                if (dragonStateHandlerLazyOptional.isPresent()) {
                    DragonStateHandler dragonStateHandler = dragonStateHandlerLazyOptional.orElseGet(() -> null);
                    if (dragonStateHandler.isDragon()) {
                        float maxHealth = playerIn.getMaxHealth();
                        if (maxHealth < 40) {
                            IAttributeInstance currentHealth = playerIn.getAttribute(SharedMonsterAttributes.MAX_HEALTH);
                            currentHealth.setBaseValue(currentHealth.getBaseValue() + 2);
                            playerIn.getHeldItem(handIn).shrink(1);

                            if (currentHealth.getBaseValue() >= DragonLevel.ADULT.initialHealth) {
                                dragonStateHandler.setLevel(DragonLevel.ADULT);
                                if (!worldIn.isRemote)
                                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncLevel(playerIn.getEntityId(), DragonLevel.ADULT));
                            } else if (currentHealth.getBaseValue() >= DragonLevel.YOUNG.initialHealth) {
                                dragonStateHandler.setLevel(DragonLevel.YOUNG);
                                if (!worldIn.isRemote)
                                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncLevel(playerIn.getEntityId(), DragonLevel.YOUNG));
                            }

                            return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
                        }
                    }
                }
                return super.onItemRightClick(worldIn, playerIn, handIn);
            }
        };
        heartElement.setRegistryName(DragonSurvivalMod.MODID, "heart_element");

        starBone = new Item(new Item.Properties().group(items)) {
            @Override
            public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
                LazyOptional<DragonStateHandler> playerStateProvider = playerIn.getCapability(DragonStateProvider.DRAGON_CAPABILITY);
                if (playerStateProvider.isPresent()) {
                    DragonStateHandler dragonStateHandler = playerStateProvider.orElse(null);
                    if (dragonStateHandler.isDragon()) {
                        IAttributeInstance health = playerIn.getAttribute(SharedMonsterAttributes.MAX_HEALTH);
                        if (health.getValue() > 14) {
                            health.setBaseValue(health.getBaseValue() - 2);
                            if (health.getValue() < DragonLevel.YOUNG.initialHealth) {
                                dragonStateHandler.setLevel(DragonLevel.BABY);
                                if (!worldIn.isRemote)
                                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncLevel(playerIn.getEntityId(), DragonLevel.BABY));
                            } else if (health.getValue() < DragonLevel.ADULT.initialHealth) {
                                dragonStateHandler.setLevel(DragonLevel.YOUNG);
                                if (!worldIn.isRemote)
                                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncLevel(playerIn.getEntityId(), DragonLevel.YOUNG));
                            }
                            playerIn.getHeldItem(handIn).shrink(1);
                            return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
                        }
                    }
                }

                return super.onItemRightClick(worldIn, playerIn, handIn);
            }
        }.setRegistryName(DragonSurvivalMod.MODID, "star_bone");

        elderDragonDust = new Item(new Item.Properties().group(items)).setRegistryName(DragonSurvivalMod.MODID, "elder_dragon_dust");
        elderDragonBone = new Item(new Item.Properties().group(items)).setRegistryName(DragonSurvivalMod.MODID, "elder_dragon_bone");

        chargedCoal = new Item(new Item.Properties().group(items).food(new Food.Builder().hunger(5).saturation(7).build())) {
            @Override
            public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
                DragonStateHandler dragonStateProvider = playerIn.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null);
                if (dragonStateProvider.isDragon() && dragonStateProvider.getType() == DragonType.CAVE)
                    return super.onItemRightClick(worldIn, playerIn, handIn);
                return ActionResult.resultPass(playerIn.getHeldItem(handIn));
            }
        }.setRegistryName(DragonSurvivalMod.MODID, "charged_coal");
        charredMeat = new Item(new Item.Properties().group(items).food(new Food.Builder().hunger(10).saturation(13).build())) {
            @Override
            public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
                DragonStateHandler dragonStateProvider = playerIn.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null);
                if (dragonStateProvider.isDragon() && dragonStateProvider.getType() == DragonType.CAVE)
                    return super.onItemRightClick(worldIn, playerIn, handIn);
                return ActionResult.resultPass(playerIn.getHeldItem(handIn));
            }
        }.setRegistryName(DragonSurvivalMod.MODID, "charred_meat");
        event.getRegistry().registerAll(heartElement, starBone, elderDragonBone, chargedCoal, charredMeat, elderDragonDust);
    }
}
