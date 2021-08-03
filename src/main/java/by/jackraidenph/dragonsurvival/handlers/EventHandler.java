package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.entity.MagicalPredatorEntity;
import by.jackraidenph.dragonsurvival.nest.NestEntity;
import by.jackraidenph.dragonsurvival.network.DiggingStatus;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.network.RefreshDragons;
import by.jackraidenph.dragonsurvival.network.StartJump;
import by.jackraidenph.dragonsurvival.network.SyncCapabilityDebuff;
import by.jackraidenph.dragonsurvival.network.SynchronizeDragonCap;
import by.jackraidenph.dragonsurvival.util.DamageSources;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.Pose;
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
import net.minecraft.item.*;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.network.play.server.SSetPassengersPacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.EffectUtils;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraft.world.IWorld;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.Console;
import java.lang.reflect.Field;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class EventHandler {
	
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event){
        if (!(event.getTarget() instanceof PlayerEntity) || event.getHand() != Hand.MAIN_HAND)
            return;
        PlayerEntity target = (PlayerEntity)event.getTarget();
        PlayerEntity self = event.getPlayer();
        DragonStateProvider.getCap(target).ifPresent(targetCap -> {
            if (targetCap.isDragon() && target.getPose() == Pose.CROUCHING && targetCap.getSize() >= 40 && !target.isVehicle()) {
                DragonStateProvider.getCap(self).ifPresent(selfCap -> {
                    if (!selfCap.isDragon() || selfCap.getLevel() == DragonLevel.BABY){
                        if (event.getTarget() instanceof ServerPlayerEntity){
                            self.startRiding(target);
                            ((ServerPlayerEntity)event.getTarget()).connection.send(new SSetPassengersPacket(target));
                            targetCap.setPassengerId(self.getId());
                            DragonSurvivalMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> target), new SynchronizeDragonCap(target.getId(), targetCap.isHiding(), targetCap.getType(), targetCap.getSize(), targetCap.hasWings(), targetCap.getLavaAirSupply(), self.getId()));
                        }
                        event.setCancellationResult(ActionResultType.SUCCESS);
                        event.setCanceled(true);
                    }
                });
            }
        });
    }

	@SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
		if (playerTickEvent.phase != TickEvent.Phase.START)
			return;
        PlayerEntity playerEntity = playerTickEvent.player;
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                if (playerEntity instanceof ServerPlayerEntity) {
                    PlayerInteractionManager interactionManager = ((ServerPlayerEntity) playerEntity).gameMode;
                    Field field = PlayerInteractionManager.class.getDeclaredFields()[5]; // FIXME: Don't do this...
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

    @SubscribeEvent
    public static void reduceFlightFallDamage(LivingHurtEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        if (livingEntity.level.isClientSide())
            return;
        DamageSource damageSource = event.getSource();
        DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
            if (damageSource == DamageSource.FALL && dragonStateHandler.isDragon() && dragonStateHandler.hasWings() && DragonSizeHandler.serverWingsEnabled.containsKey(livingEntity.getId()) && DragonSizeHandler.serverWingsEnabled.get(livingEntity.getId())){
                float dragonFallDamage = Math.min(livingEntity.getMaxHealth() / 2f, event.getAmount() / 2f);
                float effectiveHealth = livingEntity.getHealth() + livingEntity.getAbsorptionAmount();
                event.setAmount(dragonFallDamage >= effectiveHealth ? effectiveHealth - 1 : dragonFallDamage);
            }
        });
    }

    @SubscribeEvent
    public static void negateFlightFallDamage(LivingAttackEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        if (livingEntity.level.isClientSide())
            return;
        DamageSource damageSource = event.getSource();
        DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
            if (damageSource == DamageSource.FALL && livingEntity.isPassenger() && DragonStateProvider.isDragon(livingEntity.getVehicle()))
                event.setCanceled(true);
            else if (damageSource == DamageSource.FALL && dragonStateHandler.isDragon() && dragonStateHandler.hasWings() && DragonSizeHandler.serverWingsEnabled.containsKey(livingEntity.getId()) && DragonSizeHandler.serverWingsEnabled.get(livingEntity.getId())){
                float dragonFallDamage = Math.min(livingEntity.getMaxHealth() / 2f, event.getAmount() / 2f <= 3f ? 0f : event.getAmount() / 2f);
                float effectiveHealth = livingEntity.getHealth() + livingEntity.getAbsorptionAmount();
                float damage = dragonFallDamage >= effectiveHealth ? effectiveHealth - 1 : dragonFallDamage;
                if (damage <= 0)
                    event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void onServerPlayerTick(TickEvent.PlayerTickEvent event) { // TODO: Find a better way of doing this.
        if (!(event.player instanceof ServerPlayerEntity))
            return;
        ServerPlayerEntity player = (ServerPlayerEntity)event.player;
        DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
            int passengerId = dragonStateHandler.getPassengerId();
            Entity passenger = player.level.getEntity(passengerId);
            boolean flag = false;
            if (!dragonStateHandler.isDragon() && player.isVehicle() && player.getPassengers().get(0) instanceof ServerPlayerEntity){
                flag = true;
                player.getPassengers().get(0).stopRiding();
                player.connection.send(new SSetPassengersPacket(player));
            } else if (player.isSpectator() && passenger != null && player.getPassengers().get(0) instanceof ServerPlayerEntity) {
                flag = true;
                player.getPassengers().get(0).stopRiding();
                player.connection.send(new SSetPassengersPacket(player));
            } else if (dragonStateHandler.isDragon() && dragonStateHandler.getSize() != 40 && player.isVehicle()  && player.getPassengers().get(0) instanceof ServerPlayerEntity){
                flag = true;
                player.getPassengers().get(0).stopRiding();
                player.connection.send(new SSetPassengersPacket(player));
            } else if (player.isSleeping() && player.isVehicle()  && player.getPassengers().get(0) instanceof ServerPlayerEntity){
                flag = true;
                player.getPassengers().get(0).stopRiding();
                player.connection.send(new SSetPassengersPacket(player));
            }
            if (passenger != null && passenger instanceof ServerPlayerEntity) {
                DragonStateHandler passengerCap = DragonStateProvider.getCap(passenger).orElseGet(null);
                if (passengerCap != null){
                    if (passengerCap.isDragon() && passengerCap.getLevel() != DragonLevel.BABY){
                        flag = true;
                        passenger.stopRiding();
                        player.connection.send(new SSetPassengersPacket(player));
                    } else if (passenger.getRootVehicle() != player.getRootVehicle()) {
                        flag = true;
                        passenger.stopRiding();
                        player.connection.send(new SSetPassengersPacket(player));
                    }
                }
            }
            if (flag || passenger == null || !player.hasPassenger(passenger) || passenger.isSpectator() || player.isSpectator()){
                dragonStateHandler.setPassengerId(0);
                DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SynchronizeDragonCap(player.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getSize(), dragonStateHandler.hasWings(), dragonStateHandler.getLavaAirSupply(), 0));
            }

        });
    }

    @SubscribeEvent
    public static void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
        if (player.getVehicle() == null || !(player.getVehicle() instanceof ServerPlayerEntity))
            return;
        ServerPlayerEntity vehicle = (ServerPlayerEntity)player.getVehicle();
        DragonStateProvider.getCap(player).ifPresent(playerCap -> {
            DragonStateProvider.getCap(vehicle).ifPresent(vehicleCap -> {
                player.stopRiding();
                vehicle.connection.send(new SSetPassengersPacket(vehicle));
                vehicleCap.setPassengerId(0);
                DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> vehicle), new SynchronizeDragonCap(player.getId(), vehicleCap.isHiding(), vehicleCap.getType(), vehicleCap.getSize(), vehicleCap.hasWings(), vehicleCap.getLavaAirSupply(), 0));
            });
        });
    }


    /**
     * Adds dragon avoidance goal
     */
    @SubscribeEvent
    public static void onJoin(EntityJoinWorldEvent joinWorldEvent) {
        Entity entity = joinWorldEvent.getEntity();
        if (!(entity instanceof MonsterEntity || entity instanceof VillagerEntity || entity instanceof GolemEntity || entity instanceof HorseEntity || entity instanceof SkeletonHorseEntity) & entity instanceof CreatureEntity) {

            ((MobEntity) entity).goalSelector.addGoal(5, new AvoidEntityGoal(
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
            DragonSurvivalMod.LOGGER.info("Successfully attached capabilities to the " + event.getObject().getClass().getSimpleName());
        }
    }


    @SubscribeEvent
    public static void onDeath(LivingDeathEvent e) {
        LivingEntity livingEntity = e.getEntityLiving();
        if (livingEntity instanceof PlayerEntity || livingEntity instanceof MagicalPredatorEntity)
            return;

        if (livingEntity instanceof AnimalEntity && livingEntity.level.getRandom().nextDouble() < ConfigHandler.COMMON.predatorAnimalSpawnChance.get()) {
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
                        DragonStateHandler.DragonMovementData movementData = capOld.getMovementData();
                        capNew.setMovementData(movementData.bodyYaw, movementData.headYaw, movementData.headPitch, movementData.bite);
                        capNew.setSize(capOld.getSize());
                        capNew.setType(capOld.getType());
                        capNew.setHasWings(capOld.hasWings());
                        capNew.setLavaAirSupply(ConfigHandler.SERVER.caveLavaSwimmingTicks.get());

                        DragonStateHandler.updateModifiers(e.getOriginal(), e.getPlayer());

                        e.getPlayer().refreshDimensions();
                    }
                }));
    }

    @SubscribeEvent
    public static void changedDimension(PlayerEvent.PlayerChangedDimensionEvent changedDimensionEvent) {
        PlayerEntity playerEntity = changedDimensionEvent.getPlayer();
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SynchronizeDragonCap(playerEntity.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getSize(), dragonStateHandler.hasWings(), dragonStateHandler.getLavaAirSupply(), 0));
            DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new RefreshDragons(playerEntity.getId()));
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
                String[] tagStringSplit = ConfigHandler.SERVER.oresTag.get().split(":");
                ResourceLocation ores = new ResourceLocation(tagStringSplit[0], tagStringSplit[1]);
                // Checks to make sure the ore does not drop itself or another ore from the tag (no going infinite with ores)
                ITag<Item> oresTag = ItemTags.getAllTags().getTag(ores);
                if (!oresTag.contains(block.asItem()))
                    return;
                List<ItemStack> drops = block.getDrops(blockState, new LootContext.Builder((ServerWorld) world)
                        .withParameter(LootParameters.ORIGIN, new Vector3d(blockPos.getX(), blockPos.getY(), blockPos.getZ()))
                        .withParameter(LootParameters.TOOL, mainHandItem));
                DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
	                final boolean suitableOre = (playerEntity.getMainHandItem().isCorrectToolForDrops(blockState) || 
	                		(dragonStateHandler.isDragon() && dragonStateHandler.canHarvestWithPaw(blockState))) 
	                		&& drops.stream().noneMatch(item -> oresTag.contains(item.getItem()));
	                if (suitableOre && !playerEntity.isCreative()) {
	                    if (dragonStateHandler.isDragon()) {
	                        if (playerEntity.getRandom().nextDouble() < ConfigHandler.SERVER.dragonOreDustChance.get()) {
	                            world.addFreshEntity(new ItemEntity((World) world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, new ItemStack(ItemsInit.elderDragonDust)));
	                        }
	                        if (playerEntity.getRandom().nextDouble() < ConfigHandler.SERVER.dragonOreBoneChance.get()) {
	                            world.addFreshEntity(new ItemEntity((World) world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, new ItemStack(ItemsInit.elderDragonBone)));
	                        }
	                    } else {
	                        if (playerEntity.getRandom().nextDouble() < ConfigHandler.SERVER.humanOreDustChance.get()) {
	                            world.addFreshEntity(new ItemEntity((World) world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, new ItemStack(ItemsInit.elderDragonDust)));
	                        }
	                        if (playerEntity.getRandom().nextDouble() < ConfigHandler.SERVER.humanOreBoneChance.get()) {
	                            world.addFreshEntity(new ItemEntity((World) world, blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5, new ItemStack(ItemsInit.elderDragonBone)));
	                        }
	                    }
	                }
	            });
            }
        }
    }

    @SubscribeEvent
    public static void createAltar(PlayerInteractEvent.RightClickBlock rightClickBlock) {
        ItemStack itemStack = rightClickBlock.getItemStack();
        if (itemStack.getItem() == ItemsInit.elderDragonBone) {
            if(!rightClickBlock.getPlayer().isSpectator()) {
        		
                final World world = rightClickBlock.getWorld();
                final BlockPos blockPos = rightClickBlock.getPos();
                BlockState blockState = world.getBlockState(blockPos);
                final Block block = blockState.getBlock();
                
                boolean replace = false;
                rightClickBlock.getPlayer().isSpectator();
                rightClickBlock.getPlayer().isCreative();
                	BlockItemUseContext deirection = new BlockItemUseContext(
                    		rightClickBlock.getPlayer(), 
                    		rightClickBlock.getHand(), 
                    		rightClickBlock.getItemStack(), 
                    		new BlockRayTraceResult(
                    				new Vector3d(0, 0, 0),
                    				rightClickBlock.getPlayer().getDirection(), 
                    				blockPos, 
                    				false));
                if (block == Blocks.STONE) {
                    world.setBlockAndUpdate(blockPos, BlockInit.dragon_altar_stone.getStateForPlacement(deirection));
                    replace = true;
                } else if (block == Blocks.MOSSY_COBBLESTONE) {
                    world.setBlockAndUpdate(blockPos, BlockInit.dragon_altar_mossy_cobblestone.getStateForPlacement(deirection));
                    replace = true;
                } else if (block == Blocks.SANDSTONE) {
                    world.setBlockAndUpdate(blockPos, BlockInit.dragon_altar_sandstone.getStateForPlacement(deirection));
                    replace = true;
                } else if (block == Blocks.RED_SANDSTONE) {
                    world.setBlockAndUpdate(blockPos, BlockInit.dragon_altar_red_sandstone.getStateForPlacement(deirection));
                    replace = true;
                } else if (block == Blocks.OAK_LOG) {
                    world.setBlockAndUpdate(blockPos, BlockInit.dragon_altar_oak_log.getStateForPlacement(deirection));
                    replace = true;
                } else if (block == Blocks.PURPUR_BLOCK) {
                    world.setBlockAndUpdate(blockPos, BlockInit.dragon_altar_purpur_block.getStateForPlacement(deirection));
                    replace = true;
                } else if (block == Blocks.NETHER_BRICKS) {
                    world.setBlockAndUpdate(blockPos, BlockInit.dragon_altar_nether_bricks.getStateForPlacement(deirection));
                    replace = true;
                } else if (block == Blocks.BLACKSTONE) {
                	rightClickBlock.getPlayer().getDirection();
                    world.setBlockAndUpdate(blockPos, BlockInit.dragon_altar_blackstone.getStateForPlacement(deirection));
                    replace = true;
                }
                
                if (replace) {
                	if(!rightClickBlock.getPlayer().isCreative()) {
                		itemStack.shrink(1);
                	}
                    rightClickBlock.setCanceled(true);
                    world.playSound(rightClickBlock.getPlayer(), blockPos, SoundEvents.WITHER_SPAWN, SoundCategory.PLAYERS, 1, 1);
                    rightClickBlock.setCancellationResult(ActionResultType.SUCCESS);
                }
            }
            
            

        }
    }


}
