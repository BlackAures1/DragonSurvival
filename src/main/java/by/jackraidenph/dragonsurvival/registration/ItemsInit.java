package by.jackraidenph.dragonsurvival.registration;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.network.SyncSize;
import by.jackraidenph.dragonsurvival.network.SynchronizeDragonCap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SSetPassengersPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ItemsInit {
    public static Item heartElement;
    public static Item starBone, elderDragonBone, elderDragonDust;
    public static ItemGroup items = new ItemGroup("dragon.survival.blocks") {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(elderDragonBone);
        }
    };
    public static Item charredMeat, charredVegetable, charredMushroom, charredSeafood, chargedCoal, charredSoup;
    public static Item huntingNet;
    public static Item passiveVetoBeacon, passiveMagicBeacon, passivePeaceBeacon;

    @SubscribeEvent
    public static void register(final RegistryEvent.Register<Item> event) {
        heartElement = new Item(new Item.Properties().tab(items).stacksTo(64)) {
            @Override
            public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
                LazyOptional<DragonStateHandler> dragonStateHandlerLazyOptional = playerIn.getCapability(DragonStateProvider.DRAGON_CAPABILITY);
                if (dragonStateHandlerLazyOptional.isPresent()) {
                    DragonStateHandler dragonStateHandler = dragonStateHandlerLazyOptional.orElseGet(() -> null);
                    if (dragonStateHandler.isDragon()) {
                    	float size = dragonStateHandler.getSize();
                        if (size < 40) {
                        	size += 2;
                        	dragonStateHandler.setSize(size, playerIn);
                            playerIn.getItemInHand(handIn).shrink(1);
                            if (!worldIn.isClientSide){
                                DragonSurvivalMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerIn), new SyncSize(playerIn.getId(), size));
                                if (playerIn.getVehicle() != null && playerIn.getVehicle() instanceof ServerPlayerEntity){
                                    ServerPlayerEntity vehicle = (ServerPlayerEntity) playerIn.getVehicle();
                                    DragonStateProvider.getCap(vehicle).ifPresent(vehicleCap -> {
                                        playerIn.stopRiding();
                                        vehicle.connection.send(new SSetPassengersPacket(vehicle));
                                        DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> vehicle), new SynchronizeDragonCap(vehicle.getId(), vehicleCap.isHiding(), vehicleCap.getType(), vehicleCap.getSize(), vehicleCap.hasWings(), vehicleCap.getLavaAirSupply(), 0));
                                    });
                                }
                            }

                            playerIn.refreshDimensions();
                            return ActionResult.success(playerIn.getItemInHand(handIn));
                        }
                    }
                }
                return super.use(worldIn, playerIn, handIn);
            }
        };
        heartElement.setRegistryName(DragonSurvivalMod.MODID, "heart_element");

        starBone = new Item(new Item.Properties().tab(items)) {
            @Override
            public ActionResult<ItemStack> use(World worldIn, PlayerEntity playerIn, Hand handIn) {
                LazyOptional<DragonStateHandler> playerStateProvider = playerIn.getCapability(DragonStateProvider.DRAGON_CAPABILITY);
                if (playerStateProvider.isPresent()) {
                    DragonStateHandler dragonStateHandler = playerStateProvider.orElse(null);
                    if (dragonStateHandler.isDragon()) {
                    	float size = dragonStateHandler.getSize();
                        if (size > 14) {
                        	size -= 2;
                        	dragonStateHandler.setSize(size, playerIn);
                        	playerIn.getItemInHand(handIn).shrink(1);
                            if (!worldIn.isClientSide){
                                DragonSurvivalMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerIn), new SyncSize(playerIn.getId(), size));
                                if (dragonStateHandler.getPassengerId() != 0){
                                    Entity mount = worldIn.getEntity(dragonStateHandler.getPassengerId());
                                    if (mount != null){
                                        mount.stopRiding();
                                        ((ServerPlayerEntity)playerIn).connection.send(new SSetPassengersPacket(playerIn));
                                        DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) playerIn), new SynchronizeDragonCap(playerIn.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getSize(), dragonStateHandler.hasWings(), dragonStateHandler.getLavaAirSupply(), 0));
                                    }
                                }
                            }

                            playerIn.refreshDimensions();
                            return ActionResult.success(playerIn.getItemInHand(handIn));
                        }
                    }
                }

                return super.use(worldIn, playerIn, handIn);
            }
        }.setRegistryName(DragonSurvivalMod.MODID, "star_bone");

        elderDragonDust = new Item(new Item.Properties().tab(items)).setRegistryName(DragonSurvivalMod.MODID, "elder_dragon_dust");
        elderDragonBone = new Item(new Item.Properties().tab(items)).setRegistryName(DragonSurvivalMod.MODID, "elder_dragon_bone");

        chargedCoal = new Item(new Item.Properties().tab(items)) {
        	@Override
        	public int getBurnTime(ItemStack itemStack) {
        		return 3200;
        	}
        }.setRegistryName(DragonSurvivalMod.MODID, "charged_coal");
        charredMeat = new Item(new Item.Properties().tab(items).food(new Food.Builder().nutrition(1).saturationMod(0.4F).meat()
        		.effect(() -> new EffectInstance(Effects.HUNGER, 20 * 15, 0), 1.0F)
        		.build())).setRegistryName(DragonSurvivalMod.MODID, "charred_meat");
        charredVegetable = new Item(new Item.Properties().tab(items).food(new Food.Builder().nutrition(1).saturationMod(0.4F).meat()
                .effect(() -> new EffectInstance(Effects.HUNGER, 20 * 15, 0), 1.0F)
                .build())).setRegistryName(DragonSurvivalMod.MODID, "charred_vegetable");
        charredMushroom = new Item(new Item.Properties().tab(items).food(new Food.Builder().nutrition(1).saturationMod(0.4F).meat()
                .effect(() -> new EffectInstance(Effects.HUNGER, 20 * 15, 0), 1.0F)
                .build())).setRegistryName(DragonSurvivalMod.MODID, "charred_mushroom");
        charredSeafood = new Item(new Item.Properties().tab(items).food(new Food.Builder().nutrition(1).saturationMod(0.4F).meat()
                .effect(() -> new EffectInstance(Effects.HUNGER, 20 * 15, 0), 1.0F)
                .build())).setRegistryName(DragonSurvivalMod.MODID, "charred_seafood");
        charredSoup = new Item(new Item.Properties().tab(items).food(new Food.Builder().nutrition(1).saturationMod(0.4F).meat()
                .effect(() -> new EffectInstance(Effects.POISON, 20 * 15, 0), 1.0F)
                .build())).setRegistryName(DragonSurvivalMod.MODID, "charged_soup");
        IForgeRegistry<Item> registry = event.getRegistry();
        registry.registerAll(heartElement, starBone, elderDragonBone, chargedCoal, charredMeat, charredVegetable, charredMushroom, charredSoup, charredSeafood, elderDragonDust);

        huntingNet = new Item(new Item.Properties()).setRegistryName("dragonsurvival", "dragon_hunting_mesh");
        registry.register(huntingNet);
        passiveMagicBeacon = new Item(new Item.Properties()).setRegistryName(DragonSurvivalMod.MODID, "beacon_magic_1");
        registry.register(passiveMagicBeacon);
        passivePeaceBeacon = new Item(new Item.Properties()).setRegistryName(DragonSurvivalMod.MODID, "beacon_peace_1");
        registry.register(passivePeaceBeacon);
        passiveVetoBeacon = new Item(new Item.Properties()).setRegistryName(DragonSurvivalMod.MODID, "beacon_veto_1");
        registry.register(passiveVetoBeacon);
    }
}
