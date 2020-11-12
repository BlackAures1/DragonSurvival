package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.containers.DragonInventoryContainer;
import by.jackraidenph.dragonsurvival.entity.MagicalPredatorEntity;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.*;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
        if (playerTickEvent.phase == TickEvent.Phase.START) {
            PlayerEntity playerEntity = playerTickEvent.player;

            PlayerStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
                if (dragonStateHandler.isDragon()) {
                    for (int i = 0; i < playerEntity.inventory.getSizeInventory(); i++) {
                        ItemStack stack = playerEntity.inventory.getStackInSlot(i);
                        Item item = stack.getItem();
                        if (item instanceof CrossbowItem || item instanceof BowItem || item instanceof ShieldItem) {
                            playerEntity.dropItem(playerEntity.inventory.removeStackFromSlot(i), true, false);
                        }
                    }
                }
            });
        }
    }

    @Deprecated
    public static void setPlayerContainer(PlayerEntity playerEntity) {
        Field field = PlayerEntity.class.getDeclaredFields()[15];
        if (field.getType() == PlayerContainer.class) {
            field.setAccessible(true);
            try {
                PlayerContainer playerContainer = new PlayerContainer(playerEntity.inventory, playerEntity.world.isRemote, playerEntity);
                field.set(playerEntity, playerContainer);
                playerEntity.openContainer = playerContainer;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Deprecated
    public static void setDragonContainer(PlayerEntity playerEntity) {
        Field field = PlayerEntity.class.getDeclaredFields()[15];
        if (field.getType() == PlayerContainer.class) {
            field.setAccessible(true);
            try {
                DragonInventoryContainer dragonInventoryContainer = new DragonInventoryContainer(playerEntity.inventory, playerEntity.world.isRemote, playerEntity);
                field.set(playerEntity, dragonInventoryContainer);
                playerEntity.openContainer = dragonInventoryContainer;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds dragon avoidance goal
     */
    @SubscribeEvent
    public static void onJoin(EntityJoinWorldEvent e) {
        if (!(e.getEntity() instanceof MonsterEntity || e.getEntity() instanceof VillagerEntity) & e.getEntity() instanceof CreatureEntity) {
            ((MobEntity) e.getEntity()).goalSelector.addGoal(2, new AvoidEntityGoal(
                    (CreatureEntity) e.getEntity(),
                    PlayerEntity.class,
                    livingEntity -> PlayerStateProvider.getCap((PlayerEntity) livingEntity).orElse(null).isDragon(),
                    20.0F, 1.3F, 1.5F, EntityPredicates.CAN_AI_TARGET));
        }
    }

    @SubscribeEvent
    public static void onCapabilityAttachment(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(DragonSurvivalMod.MODID, "playerstatehandler"), new PlayerStateProvider());
            DragonSurvivalMod.LOGGER.info("Successfully attached capability to the " + event.getObject().getClass().getSimpleName());
        }
    }

    @SubscribeEvent
    public static void onLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
        PlayerStateProvider.getCap(e.getPlayer()).ifPresent(cap ->
                cap.getMovementData().ifPresent(data ->
                        DragonSurvivalMod.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketSyncCapabilityMovement(data))));
    }

    @SubscribeEvent
    public static void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent e) {
        PlayerStateProvider.getCap(e.getPlayer()).ifPresent(cap ->
                cap.getMovementData().ifPresent(data ->
                        DragonSurvivalMod.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketSyncCapabilityMovement(data))));
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent e) {
        if (e.getEntityLiving() instanceof PlayerEntity || e.getEntityLiving() instanceof MagicalPredatorEntity)
            return;

        if (e.getEntityLiving().world.getRandom().nextInt(30) == 0) {
            MagicalPredatorEntity beast = EntityTypesInit.MAGICAL_BEAST.create(e.getEntityLiving().world);
            e.getEntityLiving().world.addEntity(beast);
            beast.setPositionAndUpdate(e.getEntityLiving().getPosX(), e.getEntityLiving().getPosY(), e.getEntityLiving().getPosZ());
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone e) {
        PlayerStateProvider.getCap(e.getPlayer()).ifPresent(capNew ->
                PlayerStateProvider.getCap(e.getOriginal()).ifPresent(capOld -> {
                    if (capOld.isDragon()) {
                        capNew.setIsDragon(true);
                        capNew.setMovementData(capOld.getMovementData().orElse(new DragonStateHandler.DragonMovementData(0, 0, 0, Vec3d.ZERO, Vec3d.ZERO)), false);
                        capNew.setLevel(capOld.getLevel());
                        capNew.setType(capOld.getType());
                        e.getPlayer().getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(e.getOriginal().getAttribute(SharedMonsterAttributes.MAX_HEALTH).getBaseValue());
                    }
                }));
    }

    /**
     * Synchronizes the capability after death
     */
    @SubscribeEvent
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent playerRespawnEvent) {
        PlayerEntity playerEntity = playerRespawnEvent.getPlayer();
        PlayerStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                dragonStateHandler.syncCapabilityData(!playerEntity.world.isRemote);
            }
        });
    }

    @SubscribeEvent
    public static void modifyBreakSpeed(PlayerEvent.BreakSpeed breakSpeedEvent) {
        PlayerEntity playerEntity = breakSpeedEvent.getPlayer();
        PlayerStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                ItemStack mainStack = playerEntity.getHeldItemMainhand();
                Item item = mainStack.getItem();
                if (item instanceof ToolItem || item instanceof SwordItem || item instanceof ShearsItem) {
                    breakSpeedEvent.setNewSpeed(breakSpeedEvent.getOriginalSpeed() * 0.7f);
                }
            }
        });
    }

    @SubscribeEvent
    public static void disableMounts(EntityMountEvent mountEvent) {
        Entity mounting = mountEvent.getEntityMounting();
        PlayerStateProvider.getCap(mounting).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                if (mountEvent.getEntityBeingMounted() instanceof AbstractHorseEntity)
                    mountEvent.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void onItemDestroyed(LivingEntityUseItemEvent.Finish destroyItemEvent) {
        ItemStack itemStack = destroyItemEvent.getItem();
        Item item = itemStack.getItem();
        LivingEntity livingEntity = destroyItemEvent.getEntityLiving();
        PlayerStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                PlayerEntity playerEntity = (PlayerEntity) livingEntity;
                if (item.isFood()) {
                    Food food = item.getFood();
                    assert food != null;
                    boolean bad = false;
                    if (item != Items.HONEY_BOTTLE && item != Items.CAKE && item != Items.GOLDEN_APPLE && item != Items.MILK_BUCKET && item != Items.ENCHANTED_GOLDEN_APPLE) {
                        bad = true;
                    }
                    switch (dragonStateHandler.getType()) {
                        case FOREST:
                            if (food == Foods.RABBIT || food == Foods.ROTTEN_FLESH || food == Foods.CHICKEN || food == Foods.BEEF || food == Foods.PORKCHOP || food == Foods.MUTTON) {
                                bad = false;
                                livingEntity.removePotionEffect(Effects.HUNGER);
                                if (food == Foods.CHICKEN) {
                                    playerEntity.getFoodStats().addStats(0, 5.8f);
                                } else if (food == Foods.PORKCHOP) {
                                    playerEntity.getFoodStats().addStats(-1, 6.4f);
                                } else if (food == Foods.ROTTEN_FLESH) {
                                    playerEntity.getFoodStats().addStats(-2, 2.2f);
                                } else if (food == Foods.RABBIT || food == Foods.BEEF) {
                                    playerEntity.getFoodStats().addStats(2, 11.2f);
                                }

                            }
                            break;
                        case SEA:
                            if (food == Foods.SALMON || food == Foods.TROPICAL_FISH || food == Foods.COD || food == Foods.PUFFERFISH || food == Foods.DRIED_KELP) {
                                bad = false;
                                livingEntity.removePotionEffect(Effects.HUNGER);
                                livingEntity.removePotionEffect(Effects.NAUSEA);
                                livingEntity.removePotionEffect(Effects.POISON);
                                if (food == Foods.TROPICAL_FISH) {
                                    playerEntity.getFoodStats().addStats(1, 6.8f);
                                } else if (food == Foods.SALMON) {
                                    playerEntity.getFoodStats().addStats(0, 6.8f);
                                } else if (food == Foods.COD) {
                                    playerEntity.getFoodStats().addStats(0, 6.6f);
                                } else if (food == Foods.PUFFERFISH) {
                                    playerEntity.getFoodStats().addStats(4, 12.8f);
                                } else {
                                    playerEntity.getFoodStats().addStats(1, 2.4f);
                                }
                            }
                            break;
                    }
                    if (bad)
                        livingEntity.addPotionEffect(new EffectInstance(Effects.HUNGER, 20 * 60, 0));
                }
            }
        });
    }

    @SubscribeEvent
    public static void consumeSpecialFood(PlayerInteractEvent.RightClickItem rightClickItem) {
        PlayerEntity playerEntity = rightClickItem.getPlayer();
        PlayerStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon() && playerEntity.getFoodStats().needFood()) {
                ItemStack itemStack = rightClickItem.getItemStack();
                Item item = itemStack.getItem();
                if (dragonStateHandler.getType() == DragonType.CAVE) {
                    if (item == Items.COAL) {
                        itemStack.shrink(1);
                        playerEntity.getFoodStats().addStats(2, 7);
                    } else if (item == Items.CHARCOAL) {
                        itemStack.shrink(1);
                        playerEntity.getFoodStats().addStats(2, 3);
                    } else if (item == Items.REDSTONE) {
                        itemStack.shrink(1);
                        playerEntity.getFoodStats().addStats(5, 13);
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent jumpEvent) {
        PlayerStateProvider.getCap(jumpEvent.getEntityLiving()).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon())
                switch (dragonStateHandler.getLevel()) {
                    case BABY:
                        jumpEvent.getEntityLiving().addVelocity(0, 0.025, 0); //1+ block
                        break;
                    case YOUNG:
                        jumpEvent.getEntityLiving().addVelocity(0, 0.1, 0); //1.5+ block
                        break;
                    case ADULT:
                        jumpEvent.getEntityLiving().addVelocity(0, 0.15, 0); //2+ blocks
                        break;
                }
        });
    }
}
