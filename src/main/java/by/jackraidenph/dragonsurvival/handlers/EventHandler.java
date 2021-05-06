package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.entity.MagicalPredatorEntity;
import by.jackraidenph.dragonsurvival.nest.NestEntity;
import by.jackraidenph.dragonsurvival.network.DiggingStatus;
import by.jackraidenph.dragonsurvival.network.RefreshDragons;
import by.jackraidenph.dragonsurvival.network.StartJump;
import by.jackraidenph.dragonsurvival.network.SynchronizeDragonCap;
import by.jackraidenph.dragonsurvival.util.ConfigurationHandler;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneOreBlock;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.passive.horse.SkeletonHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.*;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootContext.Builder;
import net.minecraft.loot.LootParameters;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.lang.reflect.Field;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
        if (playerTickEvent.phase == TickEvent.Phase.START) {
            PlayerEntity playerEntity = playerTickEvent.player;
            DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
                if (dragonStateHandler.isDragon()) {
                    for (int i = 0; i < playerEntity.inventory.getContainerSize(); i++) {
                        ItemStack stack = playerEntity.inventory.getItem(i);
                        Item item = stack.getItem();
                        if (item instanceof CrossbowItem || item instanceof BowItem || item instanceof ShieldItem) {
                            playerEntity.drop(playerEntity.inventory.removeItemNoUpdate(i), true, false);
                        }
                    }
                    if (playerEntity instanceof ServerPlayerEntity) {
                        PlayerInteractionManager interactionManager = ((ServerPlayerEntity) playerEntity).gameMode;
                        Field field = PlayerInteractionManager.class.getDeclaredFields()[5];
                        field.setAccessible(true);
                        if (field.getType() == boolean.class) {
                            try {
                                boolean isMining = field.getBoolean(interactionManager);
                                DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new DiggingStatus(playerEntity.getId(), isMining));
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
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
                PlayerContainer playerContainer = new PlayerContainer(playerEntity.inventory, playerEntity.level.isClientSide, playerEntity);
                field.set(playerEntity, playerContainer);
                playerEntity.containerMenu = playerContainer;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Adds dragon avoidance goal
     */
    @SubscribeEvent
    public static void onJoin(EntityJoinWorldEvent joinWorldEvent) {
        Entity entity = joinWorldEvent.getEntity();
        if (!(entity instanceof MonsterEntity || entity instanceof VillagerEntity || entity instanceof GolemEntity || entity instanceof HorseEntity || entity instanceof SkeletonHorseEntity) & entity instanceof CreatureEntity) {
            ((MobEntity) entity).goalSelector.addGoal(2, new AvoidEntityGoal(
                    (CreatureEntity) entity, PlayerEntity.class,
                    livingEntity -> DragonStateProvider.isDragon((PlayerEntity) livingEntity),
                    20.0F, 1.3F, 1.5F, EntityPredicates.ATTACK_ALLOWED));
        }
        if (entity instanceof HorseEntity) {
            HorseEntity horseEntity = (HorseEntity) entity;
            horseEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(horseEntity, PlayerEntity.class, 0, true, false, livingEntity -> livingEntity.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElseGet(null).getLevel() != DragonLevel.ADULT));
            horseEntity.targetSelector.addGoal(4, new AvoidEntityGoal<>(horseEntity, PlayerEntity.class, livingEntity -> livingEntity.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).getLevel() == DragonLevel.ADULT, 20, 1.3, 1.5, EntityPredicates.ATTACK_ALLOWED::test));
        }
    }

    @SubscribeEvent
    public static void onCapabilityAttachment(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(DragonSurvivalMod.MODID, "playerstatehandler"), new DragonStateProvider());
            DragonSurvivalMod.LOGGER.info("Successfully attached capability to the " + event.getObject().getClass().getSimpleName());
        }
    }


    @SubscribeEvent
    public static void onDeath(LivingDeathEvent e) {
        LivingEntity livingEntity = e.getEntityLiving();
        if (livingEntity instanceof PlayerEntity || livingEntity instanceof MagicalPredatorEntity)
            return;

        if (livingEntity instanceof AnimalEntity && livingEntity.level.getRandom().nextInt(30) == 0) {
            MagicalPredatorEntity beast = EntityTypesInit.MAGICAL_BEAST.create(livingEntity.level);
            livingEntity.level.addFreshEntity(beast);
            beast.teleportToWithTicket(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone e) {
        DragonStateProvider.getCap(e.getPlayer()).ifPresent(capNew ->
                DragonStateProvider.getCap(e.getOriginal()).ifPresent(capOld -> {
                    if (capOld.isDragon()) {
                        capNew.setIsDragon(true);
                        DragonStateHandler.DragonMovementData movementData = capOld.getMovementData();
                        capNew.setMovementData(movementData.bodyYaw, movementData.headYaw, movementData.headPitch);
                        capNew.setLevel(capOld.getLevel());
                        capNew.setType(capOld.getType());
                        capNew.setHasWings(capOld.hasWings());
                        e.getPlayer().getAttribute(Attributes.MAX_HEALTH).setBaseValue(e.getOriginal().getAttribute(Attributes.MAX_HEALTH).getBaseValue());
                    }
                }));
    }

    @SubscribeEvent
    public static void changedDimension(PlayerEvent.PlayerChangedDimensionEvent changedDimensionEvent) {
        PlayerEntity playerEntity = changedDimensionEvent.getPlayer();
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SynchronizeDragonCap(playerEntity.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getLevel(), dragonStateHandler.isDragon(), dragonStateHandler.getHealth(), dragonStateHandler.hasWings()));
            DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new RefreshDragons(playerEntity.getId()));
        });
    }

    @SubscribeEvent
    public static void modifyBreakSpeed(PlayerEvent.BreakSpeed breakSpeedEvent) {
        PlayerEntity playerEntity = breakSpeedEvent.getPlayer();
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                ItemStack mainStack = playerEntity.getMainHandItem();
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
        DragonStateProvider.getCap(mounting).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
            	if (mountEvent.getEntityBeingMounted() instanceof AbstractHorseEntity || mountEvent.getEntityBeingMounted() instanceof PigEntity || mountEvent.getEntityBeingMounted() instanceof StriderEntity)
                    mountEvent.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void onItemDestroyed(LivingEntityUseItemEvent.Finish destroyItemEvent) {
        ItemStack itemStack = destroyItemEvent.getItem();
        Item item = itemStack.getItem();
        LivingEntity livingEntity = destroyItemEvent.getEntityLiving();
        DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                PlayerEntity playerEntity = (PlayerEntity) livingEntity;
                if (item.isEdible()) {
                    Food food = item.getFoodProperties();
                    assert food != null;
                    boolean bad = false;
                    if (item != Items.HONEY_BOTTLE && item != Items.CAKE && item != Items.GOLDEN_APPLE && item != Items.MILK_BUCKET && item != Items.ENCHANTED_GOLDEN_APPLE) {
                        bad = true;
                    }
                    switch (dragonStateHandler.getType()) {
                        case FOREST:
                            if (food == Foods.RABBIT || food == Foods.ROTTEN_FLESH || food == Foods.CHICKEN || food == Foods.BEEF || food == Foods.PORKCHOP || food == Foods.MUTTON) {
                                bad = false;
                                livingEntity.removeEffect(Effects.HUNGER);
                                if (food == Foods.CHICKEN) {
                                    playerEntity.getFoodData().eat(0, 5.8f);
                                } else if (food == Foods.PORKCHOP || food == Foods.BEEF) {
                                    playerEntity.getFoodData().eat(-1, 6.4f);
                                } else if (food == Foods.ROTTEN_FLESH) {
                                    playerEntity.getFoodData().eat(-1, 2.2f);
                                } else if (food == Foods.RABBIT) {
                                    playerEntity.getFoodData().eat(2, 11.2f);
                                }

                            }
                            break;
                        case SEA:
                            if (food == Foods.SALMON || food == Foods.TROPICAL_FISH || food == Foods.COD || food == Foods.PUFFERFISH || food == Foods.DRIED_KELP) {
                                bad = false;
                                livingEntity.removeEffect(Effects.HUNGER);
                                livingEntity.removeEffect(Effects.CONFUSION);
                                livingEntity.removeEffect(Effects.POISON);
                                if (food == Foods.TROPICAL_FISH) {
                                    playerEntity.getFoodData().eat(1, 6.8f);
                                } else if (food == Foods.SALMON) {
                                    playerEntity.getFoodData().eat(0, 6.8f);
                                } else if (food == Foods.COD) {
                                    playerEntity.getFoodData().eat(0, 6.6f);
                                } else if (food == Foods.PUFFERFISH) {
                                    playerEntity.getFoodData().eat(9, 12.8f);
                                } else {
                                    playerEntity.getFoodData().eat(1, 2.4f);
                                }
                            }
                            break;
                        case CAVE:
                            if (item == ItemsInit.chargedCoal || item == ItemsInit.charredMeat)
                                bad = false;
                            break;
                    }
                    if (bad)
                        livingEntity.addEffect(new EffectInstance(Effects.HUNGER, 20 * 60, 0));
                }
            }
        });
    }

    @SubscribeEvent
    public static void consumeSpecialFood(PlayerInteractEvent.RightClickItem rightClickItem) {
        PlayerEntity playerEntity = rightClickItem.getPlayer();
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon() && playerEntity.getFoodData().needsFood()) {
                ItemStack itemStack = rightClickItem.getItemStack();
                Item item = itemStack.getItem();
                if (dragonStateHandler.getType() == DragonType.CAVE) {
                    if (item == Items.COAL) {
                        itemStack.shrink(1);
                        playerEntity.getFoodData().eat(1, 0.5F);
                    } else if (item == Items.CHARCOAL) {
                        itemStack.shrink(1);
                        playerEntity.getFoodData().eat(1, 1.0F);
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void onJump(LivingEvent.LivingJumpEvent jumpEvent) {
        final LivingEntity livingEntity = jumpEvent.getEntityLiving();
        DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                switch (dragonStateHandler.getLevel()) {
                    case BABY:
                        livingEntity.push(0, 0.025, 0); //1+ block
                        break;
                    case YOUNG:
                        livingEntity.push(0, 0.1, 0); //1.5+ block
                        break;
                    case ADULT:
                        livingEntity.push(0, 0.15, 0); //2+ blocks
                        break;
                }
                if (livingEntity instanceof ServerPlayerEntity) {
                    if (livingEntity.getServer().isSingleplayer())
                        DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new StartJump(livingEntity.getId(), 42));
                    else
                        DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new StartJump(livingEntity.getId(), 21));
                }
            }
        });
    }

    @SubscribeEvent
    public static void sleepCheck(SleepingLocationCheckEvent sleepingLocationCheckEvent) {
        BlockPos sleepingLocation = sleepingLocationCheckEvent.getSleepingLocation();
        World world = sleepingLocationCheckEvent.getEntity().level;
        if (world.isNight() && world.getBlockEntity(sleepingLocation) instanceof NestEntity)
            sleepingLocationCheckEvent.setResult(Event.Result.ALLOW);
    }
    
    @SubscribeEvent
    public static void dropDragonDust(BlockEvent.BreakEvent breakEvent) {
        if (!breakEvent.isCanceled()) {
            IWorld world = breakEvent.getWorld();
            if (world instanceof ServerWorld) {
                BlockState blockState = breakEvent.getState();
                BlockPos blockPos = breakEvent.getPos();
                PlayerEntity playerEntity = breakEvent.getPlayer();
                Block block = blockState.getBlock();
                ItemStack mainHandItem = playerEntity.getItemInHand(Hand.MAIN_HAND);
                double random;
                // Modded Ore Support
                String[] tagStringSplit = ConfigurationHandler.ORE_LOOT.oreBlocksTag.get().split(":");
                ResourceLocation ores = new ResourceLocation(tagStringSplit[0], tagStringSplit[1]);
                // Checks to make sure the ore does not drop itself (so you can't go infinite with this unless you get enough of the drop to craft the ore or something)
                final boolean suitableOre = ItemTags.getAllTags().getTag(ores).contains(block.asItem()) && 
                		!block.getDrops(blockState, new LootContext.Builder((ServerWorld)world)
                				.withParameter(LootParameters.ORIGIN, new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                				.withParameter(LootParameters.TOOL, mainHandItem))
                		.stream().anyMatch(item -> item.getItem() == block.asItem()); 
                if (suitableOre ) {
                    if (DragonStateProvider.isDragon(playerEntity)) {
                        random = playerEntity.getRandom().nextDouble();
                        if (playerEntity.getRandom().nextDouble() < ConfigurationHandler.ORE_LOOT.dragonOreDustChance.get()) {
                            world.addFreshEntity(new ItemEntity((World) world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, new ItemStack(ItemsInit.elderDragonDust)));
                        }
                        if (playerEntity.getRandom().nextDouble() < ConfigurationHandler.ORE_LOOT.dragonOreBoneChance.get()) {
                            world.addFreshEntity(new ItemEntity((World) world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, new ItemStack(ItemsInit.elderDragonBone)));
                        }
                    } else {
                    	if (playerEntity.getRandom().nextDouble() < ConfigurationHandler.ORE_LOOT.humanOreDustChance.get()) {
                            world.addFreshEntity(new ItemEntity((World) world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, new ItemStack(ItemsInit.elderDragonDust)));
                        }
                        if (playerEntity.getRandom().nextDouble() < ConfigurationHandler.ORE_LOOT.humanOreBoneChance.get()) {
                            world.addFreshEntity(new ItemEntity((World) world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, new ItemStack(ItemsInit.elderDragonBone)));
                        }
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public static void createAltar(PlayerInteractEvent.RightClickBlock rightClickBlock) {
        ItemStack itemStack = rightClickBlock.getItemStack();
        if (itemStack.getItem() == ItemsInit.elderDragonBone) {

            final World world = rightClickBlock.getWorld();
            final BlockPos blockPos = rightClickBlock.getPos();
            BlockState blockState = world.getBlockState(blockPos);
            final Block block = blockState.getBlock();
            boolean replace = false;
            if (block == Blocks.STONE) {
                world.setBlockAndUpdate(blockPos, BlockInit.dragon_altar3.defaultBlockState());
                replace = true;
            } else if (block == Blocks.SANDSTONE) {
                world.setBlockAndUpdate(blockPos, BlockInit.dragon_altar4.defaultBlockState());
                replace = true;
            } else if (block == Blocks.MOSSY_COBBLESTONE) {
                world.setBlockAndUpdate(blockPos, BlockInit.dragon_altar.defaultBlockState());
                replace = true;
            } else if (block == Blocks.OAK_LOG) {
                world.setBlockAndUpdate(blockPos, BlockInit.dragon_altar2.defaultBlockState());
                replace = true;
            }
            if (replace) {
                itemStack.shrink(1);
                rightClickBlock.setCanceled(true);
                world.playSound(rightClickBlock.getPlayer(), blockPos, SoundEvents.STONE_PLACE, SoundCategory.PLAYERS, 1, 1);
                rightClickBlock.setCancellationResult(ActionResultType.SUCCESS);
            }

        }
    }

    @SubscribeEvent
    public static void reduceFallDistance(LivingFallEvent livingFallEvent) {
    	//TODO prevent death from falling
        LivingEntity livingEntity = livingFallEvent.getEntityLiving();
        if (DragonStateProvider.isDragon(livingEntity))
            livingFallEvent.setDistance(livingFallEvent.getDistance() - 1);
    }
    
    
    
}
