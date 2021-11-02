package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.Functions;
import by.jackraidenph.dragonsurvival.capability.Capabilities;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.entity.PrincessEntity;
import by.jackraidenph.dragonsurvival.gecko.Knight;
import by.jackraidenph.dragonsurvival.gecko.Prince;
import by.jackraidenph.dragonsurvival.gecko.Princess;
import by.jackraidenph.dragonsurvival.goals.FollowMobGoal;
import by.jackraidenph.dragonsurvival.registration.DragonEffects;
import by.jackraidenph.dragonsurvival.registration.EntityTypesInit;
import by.jackraidenph.dragonsurvival.util.EffectInstance2;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.WanderingTraderEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.*;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Mod.EventBusSubscriber
public class VillagerRelationsHandler {
    @SubscribeEvent
    public static void onDeath(LivingDeathEvent deathEvent) {
        LivingEntity livingEntity = deathEvent.getEntityLiving();
        Entity killer = deathEvent.getSource().getEntity();
        if (killer instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) killer;
            if (livingEntity instanceof AbstractVillagerEntity) {
                World world = killer.level;
                livingEntity.getType().getRegistryName();
                if (!(livingEntity instanceof Princess)) {

                    if (DragonStateProvider.isDragon(killer)) {
                        AbstractVillagerEntity villagerEntity = (AbstractVillagerEntity) livingEntity;

                        MerchantOffers merchantOffers = villagerEntity.getOffers();

                        if (villagerEntity instanceof VillagerEntity) {
                            VillagerEntity villager = (VillagerEntity) villagerEntity;

                            int level = villager.getVillagerData().getLevel();

                            if (world.random.nextInt(100) < 30) {
                                Optional<MerchantOffer> offer = merchantOffers.stream().filter(merchantOffer -> merchantOffer.getResult().getItem() != Items.EMERALD).collect(Collectors.toList()).stream().findAny();

                                offer.ifPresent(merchantOffer -> world.addFreshEntity(new ItemEntity(world, villager.getX(), villager.getY(), villager.getZ(), merchantOffer.getResult())));
                            }

                            if (!world.isClientSide) {
                                playerEntity.giveExperiencePoints(level * ConfigHandler.COMMON.xpGain.get());
//                                applyEvilMarker(playerEntity);
                            }
                        } else if (villagerEntity instanceof WanderingTraderEntity) {
                            WanderingTraderEntity wanderingTrader = (WanderingTraderEntity) villagerEntity;
                            if (!world.isClientSide) {
                                playerEntity.giveExperiencePoints(2 * ConfigHandler.COMMON.xpGain.get());
                                if (world.random.nextInt(100) < 30) {
                                    ItemStack itemStack = wanderingTrader.getOffers().stream().filter((merchantOffer -> merchantOffer.getResult().getItem() != Items.EMERALD)).collect(Collectors.toList()).get(wanderingTrader.getRandom().nextInt(wanderingTrader.getOffers().size())).getResult();
                                    world.addFreshEntity(new ItemEntity(world, wanderingTrader.getX(), wanderingTrader.getY(), wanderingTrader.getZ(), itemStack));
                                }
//                                applyEvilMarker(playerEntity);
                            }
                        }
                    }

                }
            } else if (livingEntity instanceof by.jackraidenph.dragonsurvival.entity.DragonHunter) {
                if (DragonStateProvider.isDragon(playerEntity)) {
//                    applyEvilMarker(playerEntity);
                } else if (livingEntity instanceof Knight) {
                    playerEntity.addEffect(new EffectInstance(Effects.BAD_OMEN, Functions.minutesToTicks(5)));
                }
            }
            String typeName = livingEntity.getType().getRegistryName().toString();
            if (DragonStateProvider.isDragon(playerEntity) && ConfigHandler.COMMON.evilDragonStatusGivers.get().contains(typeName)) {
                applyEvilMarker(playerEntity);
            }
        }
    }

    public static List<? extends EntityType<? extends CreatureEntity>> dragonHunters;

    @SubscribeEvent
    public static void entityTargets(LivingSetAttackTargetEvent setAttackTargetEvent) {
        Entity entity = setAttackTargetEvent.getEntity();
        LivingEntity target = setAttackTargetEvent.getTarget();
        if (entity instanceof IronGolemEntity) {
            if (target instanceof by.jackraidenph.dragonsurvival.entity.DragonHunter)
                ((IronGolemEntity) entity).setTarget(null);
        } else if (entity instanceof ZombieEntity &&
                (target instanceof PrincessEntity || target instanceof Princess)) {
            ((ZombieEntity) entity).setTarget(null);
        }
    }

    public static void applyEvilMarker(PlayerEntity playerEntity) {
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                EffectInstance effectInstance = playerEntity.getEffect(DragonEffects.EVIL_DRAGON);
                if (effectInstance == null) {
                    playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(1)));
                } else {
                    int duration = effectInstance.getDuration();
                    if (duration <= Functions.minutesToTicks(1)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(5), 1));
                    } else if (duration <= Functions.minutesToTicks(5)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(10), 2));
                    } else if (duration <= Functions.minutesToTicks(10)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(15), 3));
                    } else if (duration <= Functions.minutesToTicks(15)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(20), 4));
                    } else if (duration <= Functions.minutesToTicks(20)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(25), 5));
                    } else if (duration <= Functions.minutesToTicks(25)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(30), 6));
                    } else if (duration <= Functions.minutesToTicks(30)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(60), 7));
                    } else if (duration <= Functions.minutesToTicks(60)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(120), 8));
                    } else if (duration <= Functions.minutesToTicks(120)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(240), 9));
                    } else if (duration <= Functions.minutesToTicks(240)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(400), 10));
                    }
                }
            }
        });
    }

    public static int computeLevelOfEvil(PlayerEntity playerEntity) {
        if (DragonStateProvider.isDragon(playerEntity) && playerEntity.hasEffect(DragonEffects.EVIL_DRAGON)) {
            EffectInstance effectInstance = playerEntity.getEffect(DragonEffects.EVIL_DRAGON);
            assert effectInstance != null;
            int timeLeft = effectInstance.getDuration();
            if (timeLeft >= Functions.minutesToTicks(240))
                return 10;
            if (timeLeft >= Functions.minutesToTicks(120))
                return 9;
            if (timeLeft >= Functions.minutesToTicks(60))
                return 8;
            if (timeLeft >= Functions.minutesToTicks(30))
                return 7;
            if (timeLeft >= Functions.minutesToTicks(25))
                return 6;
            if (timeLeft >= Functions.minutesToTicks(20))
                return 5;
            if (timeLeft >= Functions.minutesToTicks(15))
                return 4;
            if (timeLeft >= Functions.minutesToTicks(10))
                return 3;
            if (timeLeft >= Functions.minutesToTicks(5))
                return 2;
            if (timeLeft >= Functions.minutesToTicks(1))
                return 1;
        }
        return 0;
    }

    @SubscribeEvent
    public static void voidEvilStatus(PotionEvent.PotionAddedEvent potionAddedEvent) {
        EffectInstance effectInstance = potionAddedEvent.getPotionEffect();
        LivingEntity livingEntity = potionAddedEvent.getEntityLiving();
        if (effectInstance.getEffect() == Effects.HERO_OF_THE_VILLAGE)
            livingEntity.removeEffect(DragonEffects.EVIL_DRAGON);
    }

    @SubscribeEvent
    public static void specialTasks(EntityJoinWorldEvent joinWorldEvent) {
        World world = joinWorldEvent.getWorld();
        Entity entity = joinWorldEvent.getEntity();
        if (entity instanceof IronGolemEntity) {
            IronGolemEntity golemEntity = (IronGolemEntity) entity;
            golemEntity.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(golemEntity, PlayerEntity.class, 0, true, false, livingEntity ->

                    (DragonStateProvider.isDragon(livingEntity) && livingEntity.hasEffect(DragonEffects.EVIL_DRAGON))));
        }

        if (entity instanceof AbstractVillagerEntity && !(entity instanceof Prince)) {
            AbstractVillagerEntity abstractVillagerEntity = (AbstractVillagerEntity) entity;
            abstractVillagerEntity.goalSelector.addGoal(10, new AvoidEntityGoal<>(abstractVillagerEntity, PlayerEntity.class, livingEntity ->
                    (DragonStateProvider.isDragon(livingEntity) && livingEntity.hasEffect(DragonEffects.EVIL_DRAGON)), 16.0F, 1.0D, 1.0D, EntityPredicates.NO_CREATIVE_OR_SPECTATOR::test));
        }
    }

    @SubscribeEvent
    public static void interactions(PlayerInteractEvent.EntityInteract event) {
        PlayerEntity playerEntity = event.getPlayer();
        Entity livingEntity = event.getTarget();
        if (livingEntity instanceof AbstractVillagerEntity) {
            if (DragonStateProvider.isDragon(playerEntity) && playerEntity.hasEffect(DragonEffects.EVIL_DRAGON)) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent
    public static void hurtEntity(AttackEntityEvent attackEntityEvent) {
        Entity attacked = attackEntityEvent.getTarget();
        PlayerEntity attacker = attackEntityEvent.getPlayer();
        if (attacked instanceof AbstractVillagerEntity) {
            if (attacker.hasEffect(DragonEffects.EVIL_DRAGON)) {
                int duration = attacker.getEffect(DragonEffects.EVIL_DRAGON).getDuration();
                attacker.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, duration + Functions.secondsToTicks(5)));
            } else {
                attacker.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.secondsToTicks(5)));
            }
        }
    }

    @SubscribeEvent
    public static void spawnHunters(TickEvent.PlayerTickEvent playerTickEvent) {
        if (!dragonHunters.isEmpty() && playerTickEvent.phase == TickEvent.Phase.END) {
            PlayerEntity player = playerTickEvent.player;
            if (DragonStateProvider.isDragon(player) && player.hasEffect(DragonEffects.EVIL_DRAGON) && !player.level.isClientSide &&
                    !player.isCreative() && !player.isSpectator() && player.isAlive()) {
                ServerWorld serverWorld = (ServerWorld) player.level;
                if (serverWorld.dimension() == World.OVERWORLD) {
                    Capabilities.getVillageRelationships(player).ifPresent(villageRelationShips -> {
                        if (villageRelationShips.hunterSpawnDelay == 0) {
                            BlockPos spawnPosition = Functions.findRandomSpawnPosition(player, 1, 4, 14.0F);
                            if (spawnPosition != null && spawnPosition.getY() >= ConfigHandler.COMMON.riderSpawnLowerBound.get() && spawnPosition.getY() <= ConfigHandler.COMMON.riderSpawnUpperBound.get()) {
                                Optional<RegistryKey<Biome>> biomeRegistryKey = serverWorld.getBiomeName(spawnPosition);
                                if (biomeRegistryKey.isPresent()) {
                                    RegistryKey<Biome> biome = biomeRegistryKey.get();
                                    if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN))
                                        return;
                                }
                                int levelOfEvil = computeLevelOfEvil(player);
                                for (int i = 0; i < levelOfEvil; i++) {
                                    Functions.spawn(Objects.requireNonNull((dragonHunters.get(serverWorld.random.nextInt(dragonHunters.size()))).create(serverWorld)), spawnPosition, serverWorld);
                                }
                                if (serverWorld.isCloseToVillage(player.blockPosition(), 3)) {
                                    villageRelationShips.hunterSpawnDelay = Functions.minutesToTicks(ConfigHandler.COMMON.hunterSpawnDelay.get() / 3) + Functions.minutesToTicks(serverWorld.random.nextInt(ConfigHandler.COMMON.hunterSpawnDelay.get() / 6));
                                } else {
                                    villageRelationShips.hunterSpawnDelay = Functions.minutesToTicks(ConfigHandler.COMMON.hunterSpawnDelay.get()) + Functions.minutesToTicks(serverWorld.random.nextInt(ConfigHandler.COMMON.hunterSpawnDelay.get() / 3));
                                }
                            }
                        } else {
                            villageRelationShips.hunterSpawnDelay--;
                        }
                    });
                }
            }
        }
    }

    //change to minutes
    private static int timeLeft = Functions.minutesToTicks(ConfigHandler.COMMON.princessSpawnDelay.get()) + Functions.minutesToTicks(ThreadLocalRandom.current().nextInt(30));

    @SubscribeEvent
    public static void spawnPrinceOrPrincess(TickEvent.WorldTickEvent serverTickEvent) {
        World world = serverTickEvent.world;
        if (world instanceof ServerWorld) {
            ServerWorld serverWorld = (ServerWorld) world;
            if (!serverWorld.players().isEmpty() && serverWorld.dimension() == World.OVERWORLD)
                if (timeLeft == 0) {
                    ServerPlayerEntity player = serverWorld.getRandomPlayer();
                    if (player != null && player.isAlive() && !player.isCreative() && !player.isSpectator()) {
                        BlockPos blockPos = Functions.findRandomSpawnPosition(player, 1, 2, 20.0F);
                        if (blockPos != null && blockPos.getY() >= ConfigHandler.COMMON.riderSpawnLowerBound.get() && blockPos.getY() <= ConfigHandler.COMMON.riderSpawnUpperBound.get()) {
                            Optional<RegistryKey<Biome>> biomeRegistryKey = serverWorld.getBiomeName(blockPos);
                            if (biomeRegistryKey.isPresent()) {
                                RegistryKey<Biome> biome = biomeRegistryKey.get();
                                if (BiomeDictionary.hasType(biome, BiomeDictionary.Type.OCEAN))
                                    return;
                            }
                            EntityType<? extends Princess> entityType = world.random.nextBoolean() ? EntityTypesInit.PRINCESS_ON_HORSE : EntityTypesInit.PRINCE_ON_HORSE;
                            Princess princessEntity = entityType.create(world);
                            princessEntity.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                            princessEntity.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(player.blockPosition()), SpawnReason.NATURAL, null, null);

                            serverWorld.addFreshEntity(princessEntity);

                            ListNBT pattern = (new BannerPattern.Builder()).addPattern(BannerPattern.values()[world.random.nextInt((BannerPattern.values()).length)], DyeColor.values()[world.random.nextInt((DyeColor.values()).length)]).toListTag();

                            int knights = world.random.nextInt(3) + 3;
                            for (int i = 0; i < knights; i++) {
                                Knight knightHunter = EntityTypesInit.KNIGHT.create(serverWorld);
                                knightHunter.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
                                knightHunter.goalSelector.addGoal(5, new FollowMobGoal(Princess.class, knightHunter, 8));
                                knightHunter.finalizeSpawn(serverWorld, serverWorld.getCurrentDifficultyAt(player.blockPosition()), SpawnReason.NATURAL, null, null);
                                ItemStack itemStack = new ItemStack(Items.SHIELD);
                                CompoundNBT compoundNBT = new CompoundNBT();
                                compoundNBT.putInt("Base", princessEntity.getColor());
                                compoundNBT.put("Patterns", pattern);
                                itemStack.addTagElement("BlockEntityTag", compoundNBT);
                                knightHunter.setItemInHand(Hand.OFF_HAND, itemStack);
                                serverWorld.addFreshEntity(knightHunter);
                            }

                            timeLeft = Functions.minutesToTicks(ConfigHandler.COMMON.princessSpawnDelay.get()) + Functions.minutesToTicks(world.random.nextInt(ConfigHandler.COMMON.princessSpawnDelay.get() / 2));
                        }
                    }
                } else {
                    timeLeft--;
                }
        }
    }

    /**
     * Save duration of 'evil dragon'
     */
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
        if (playerTickEvent.phase == TickEvent.Phase.END) {
            PlayerEntity playerEntity = playerTickEvent.player;
            if (!playerEntity.level.isClientSide) {
                if (playerEntity.hasEffect(DragonEffects.EVIL_DRAGON)) {
                    Capabilities.getVillageRelationships(playerEntity).ifPresent(villageRelationShips -> villageRelationShips.evilStatusDuration = playerEntity.getEffect(DragonEffects.EVIL_DRAGON).getDuration());
                }
            }
        }
    }
}