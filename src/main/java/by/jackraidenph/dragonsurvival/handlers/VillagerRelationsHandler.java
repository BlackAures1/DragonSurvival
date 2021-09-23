package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.Functions;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.entity.PrincessEntity;
import by.jackraidenph.dragonsurvival.util.EffectInstance2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.AbstractVillagerEntity;
import net.minecraft.entity.monster.ZombieEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraftforge.event.entity.living.LivingSetAttackTargetEvent;
import net.minecraftforge.event.entity.living.PotionEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class VillagerRelationsHandler {
    //   @SubscribeEvent
//   public static void onDeath(LivingDeathEvent deathEvent) {
//     LivingEntity livingEntity = deathEvent.getEntityLiving();
//     Entity killer = deathEvent.getSource().func_76346_g();
//     if (killer instanceof PlayerEntity) {
//       PlayerEntity playerEntity = (PlayerEntity)killer;
//       if (livingEntity instanceof AbstractVillagerEntity) {
//         World world = killer.level;
//
//         if (livingEntity instanceof by.jackraidenph.dragonsurvival.entity.Prince) {
//           if (!world.field_72995_K)
//             world.func_217376_c((Entity)new ItemEntity(world, livingEntity.func_226277_ct_(), livingEntity.func_226278_cu_(), livingEntity.func_226281_cx_(), new ItemStack((IItemProvider)Items.field_151010_B)));
//           applyEvilMarker(playerEntity);
//         } else if (livingEntity instanceof PrincessEntity) {
//           Item flower = Items.field_190931_a;
//           DyeColor dyeColor = DyeColor.func_196056_a(((PrincessEntity)livingEntity).getColor());
//           switch (dyeColor) {
//             case BLUE:
//               flower = Items.field_221621_aW;
//               break;
//             case RED:
//               flower = Items.field_221624_aZ;
//               break;
//             case BLACK:
//               flower = Items.field_221690_bg;
//               break;
//             case YELLOW:
//               flower = Items.field_221619_aU;
//               break;
//             case PURPLE:
//               flower = Items.field_221910_fm;
//               break;
//             case WHITE:
//               flower = Items.field_221680_bb; break;
//           }
//           if (!world.field_72995_K)
//             world.func_217376_c((Entity)new ItemEntity(world, livingEntity.func_226277_ct_(), livingEntity.func_226278_cu_(), livingEntity.func_226281_cx_(), new ItemStack((IItemProvider)flower)));
//           applyEvilMarker(playerEntity);
//         } else {
//           DragonStateProvider.getCap(killer).ifPresent(dragonStateHandler -> {
//                 if (dragonStateHandler.isDragon()) {
//                   AbstractVillagerEntity villagerEntity = (AbstractVillagerEntity)livingEntity;
//
//                   MerchantOffers merchantOffers = villagerEntity.func_213706_dY();
//
//                   if (villagerEntity instanceof VillagerEntity) {
//                     VillagerEntity villager = (VillagerEntity)villagerEntity;
//
//                     int level = villager.func_213700_eh().func_221132_c();
//
//                     if (world.field_73012_v.nextInt(100) < 30) {
//                       Optional<MerchantOffer> offer = ((List<MerchantOffer>)merchantOffers.stream().filter(()).collect(Collectors.toList())).stream().findAny();
//
//                       offer.ifPresent(());
//                     }
//
//                     if (!world.field_72995_K) {
//                       playerEntity.func_195068_e(level * 100);
//                       applyEvilMarker(playerEntity);
//                     }
//                   } else if (villagerEntity instanceof WanderingTraderEntity) {
//                     WanderingTraderEntity wanderingTrader = (WanderingTraderEntity)villagerEntity;
//                     if (!world.field_72995_K) {
//                       playerEntity.func_195068_e(200);
//                       if (world.field_73012_v.nextInt(100) < 30) {
//                         ItemStack itemStack = ((MerchantOffer)((List<MerchantOffer>)wanderingTrader.func_213706_dY().stream().filter(()).collect(Collectors.toList())).get(wanderingTrader.func_70681_au().nextInt(wanderingTrader.func_213706_dY().size()))).func_222200_d();
//                         world.func_217376_c((Entity)new ItemEntity(world, wanderingTrader.func_226277_ct_(), wanderingTrader.func_226278_cu_(), wanderingTrader.func_226281_cx_(), itemStack));
//                       }
//                       applyEvilMarker(playerEntity);
//                     }
//                   }
//                 }
//               });
//         }
//       } else if (livingEntity instanceof by.jackraidenph.dragonsurvival.entity.DragonHunter) {
//         DragonStateProvider.getCap((Entity)playerEntity).ifPresent(dragonStateHandler -> {
//               if (dragonStateHandler.isDragon()) {
//                 applyEvilMarker(playerEntity);
//               } else if (livingEntity instanceof KnightHunter) {
//                 playerEntity.func_195064_c(new EffectInstance(Effects.field_220309_E, Functions.minutesToTicks(5)));
//               }
//             });
//       }
//     }
//   }
//
//   public static List<EntityType<? extends CreatureEntity>> dragonHunters;
//
    @SubscribeEvent
    public static void entityTargets(LivingSetAttackTargetEvent setAttackTargetEvent) {
        Entity entity = setAttackTargetEvent.getEntity();
        LivingEntity target = setAttackTargetEvent.getTarget();
        if (entity instanceof IronGolemEntity) {
            if (target instanceof by.jackraidenph.dragonsurvival.entity.DragonHunter)
                ((IronGolemEntity) entity).setTarget(null);
        } else if (entity instanceof ZombieEntity &&
                target instanceof PrincessEntity) {
            ((ZombieEntity) entity).setTarget(null);
        }
    }

    //
//
//
//
    public static void applyEvilMarker(PlayerEntity playerEntity) {
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                EffectInstance effectInstance = playerEntity.getEffect(DragonEffects.EVIL_DRAGON);
                if (effectInstance == null) {
                    playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(10)));
                } else {
                    int duration = effectInstance.getDuration();
                    if (duration <= Functions.minutesToTicks(10)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(20)));
                    } else if (duration <= Functions.minutesToTicks(30)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(30)));
                    } else if (duration <= Functions.minutesToTicks(60)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(40)));
                    } else if (duration <= Functions.minutesToTicks(100)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(20)));
                    } else if (duration <= Functions.minutesToTicks(120)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(30)));
                    } else if (duration <= Functions.minutesToTicks(150)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(50)));
                    } else if (duration <= Functions.minutesToTicks(200)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(50)));
                    } else if (duration <= Functions.minutesToTicks(250)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(50)));
                    } else if (duration <= Functions.minutesToTicks(300)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(50)));
                    } else if (duration <= Functions.minutesToTicks(350)) {
                        playerEntity.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, Functions.minutesToTicks(50)));
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
            if (timeLeft >= Functions.minutesToTicks(350))
                return 10;
            if (timeLeft >= Functions.minutesToTicks(300))
                return 9;
            if (timeLeft >= Functions.minutesToTicks(250))
                return 8;
            if (timeLeft >= Functions.minutesToTicks(200))
                return 7;
            if (timeLeft >= Functions.minutesToTicks(150))
                return 6;
            if (timeLeft >= Functions.minutesToTicks(120))
                return 5;
            if (timeLeft >= Functions.minutesToTicks(100))
                return 4;
            if (timeLeft >= Functions.minutesToTicks(60))
                return 3;
            if (timeLeft >= Functions.minutesToTicks(30))
                return 2;
            if (timeLeft >= Functions.minutesToTicks(10))
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

    //
//   @SubscribeEvent
//   public static void specialTasks(EntityJoinWorldEvent joinWorldEvent) {
//     World world = joinWorldEvent.getWorld();
//     Entity entity = joinWorldEvent.getEntity();
//     if (entity instanceof IronGolemEntity) {
//       IronGolemEntity golemEntity = (IronGolemEntity)entity;
//       golemEntity.field_70715_bh.func_75776_a(5, (Goal)new NearestAttackableTargetGoal((MobEntity)golemEntity, PlayerEntity.class, 0, true, false, livingEntity ->
//
//             (DragonStateProvider.isDragon((Entity)livingEntity) && livingEntity.func_70644_a(DragonEffects.EVIL_DRAGON))));
//     }
//
//     if (entity instanceof AbstractVillagerEntity && !(entity instanceof by.jackraidenph.dragonsurvival.entity.Prince)) {
//       AbstractVillagerEntity abstractVillagerEntity = (AbstractVillagerEntity)entity;
//       abstractVillagerEntity.field_70714_bg.func_75776_a(10, (Goal)new AvoidEntityGoal((CreatureEntity)abstractVillagerEntity, PlayerEntity.class, livingEntity ->
//             (DragonStateProvider.isDragon((Entity)livingEntity) && livingEntity.func_70644_a(DragonEffects.EVIL_DRAGON)), 16.0F, 1.0D, 1.0D, EntityPredicates.field_188444_d::test));
//     }
//   }
//
//
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

    //
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
//
//
//   @SubscribeEvent
//   public static void spawnHunters(TickEvent.PlayerTickEvent playerTickEvent) {
//     if (playerTickEvent.phase == TickEvent.Phase.END) {
//       PlayerEntity player = playerTickEvent.player;
//       if (DragonStateProvider.isDragon((Entity)player) && player.func_70644_a(DragonEffects.EVIL_DRAGON) && !player.field_70170_p.field_72995_K &&
//         !player.func_184812_l_() && !player.func_175149_v() && player.func_70089_S()) {
//         ServerWorld serverWorld = (ServerWorld)player.field_70170_p;
//         Capabilities.getVillageRelationships((Entity)player).ifPresent(villageRelationShips -> {
//               if (villageRelationShips.hunterSpawnDelay == 0) {
//                 BlockPos spawnPosition = Functions.findRandomSpawnPosition(player, 1, 4, 10.0F);
//                 if (spawnPosition != null) {
//                   int levelOfEvil = computeLevelOfEvil(player);
//                   for (int i = 0; i < levelOfEvil; i++) {
//                     Functions.spawn((MobEntity)Objects.requireNonNull(((EntityType)dragonHunters.get(serverWorld.field_73012_v.nextInt(dragonHunters.size()))).func_200721_a((World)serverWorld)), spawnPosition, serverWorld);
//                   }
//                   if (serverWorld.func_241119_a_(player.func_233580_cy_(), 3)) {
//                     villageRelationShips.hunterSpawnDelay = Functions.secondsToTicks(20) + Functions.secondsToTicks(serverWorld.field_73012_v.nextInt(10));
//                   } else {
//                     villageRelationShips.hunterSpawnDelay = Functions.secondsToTicks(60) + Functions.secondsToTicks(serverWorld.field_73012_v.nextInt(20));
//                   }
//                 }
//               } else {
//                 villageRelationShips.hunterSpawnDelay--;
//               }
//             });
//       }
//     }
//   }
//
//
//   private static int timeLeft = Functions.minutesToTicks(2) + Functions.minutesToTicks(ThreadLocalRandom.current().nextInt(30));
//
//   @SubscribeEvent
//   public static void spawnPrinceOrPrincess(TickEvent.WorldTickEvent serverTickEvent) {
//     World world = serverTickEvent.world;
//     if (world instanceof ServerWorld) {
//       ServerWorld serverWorld = (ServerWorld)world;
//       if (!serverWorld.func_217369_A().isEmpty() && serverWorld.func_234923_W_() == World.field_234918_g_)
//         if (timeLeft == 0) {
//           ServerPlayerEntity player = serverWorld.func_217472_l_();
//           BlockPos blockPos = Functions.findRandomSpawnPosition((PlayerEntity)player, 1, 2, 20.0F);
//           if (blockPos != null) {
//             EntityType<? extends PrincessEntity> entityType = world.field_73012_v.nextBoolean() ? EntityTypesInit.PRINCESS : (EntityType)EntityTypesInit.PRINCE;
//             PrincessEntity princessEntity = (PrincessEntity)entityType.func_200721_a(world);
//             princessEntity.func_70107_b(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p());
//             princessEntity.func_213386_a((IServerWorld)serverWorld, serverWorld.func_175649_E(player.func_233580_cy_()), SpawnReason.NATURAL, null, null);
//
//             serverWorld.func_217376_c((Entity)princessEntity);
//
//             DyeColor dyeColor = DyeColor.func_196056_a(princessEntity.getColor());
//             ListNBT pattern = (new BannerPattern.Builder()).func_222477_a(BannerPattern.values()[world.field_73012_v.nextInt((BannerPattern.values()).length)], DyeColor.values()[world.field_73012_v.nextInt((DyeColor.values()).length)]).func_222476_a();
//
//             int knights = world.field_73012_v.nextInt(3) + 3;
//             for (int i = 0; i < knights; i++) {
//               KnightHunter knightHunter = (KnightHunter)EntityTypesInit.KNIGHT_HUNTER.func_200721_a((World)serverWorld);
//               knightHunter.func_70107_b(blockPos.func_177958_n(), blockPos.func_177956_o(), blockPos.func_177952_p());
//               knightHunter.field_70714_bg.func_75776_a(5, (Goal)new FollowMobGoal(PrincessEntity.class, (MobEntity)knightHunter, 8));
//               knightHunter.func_213386_a((IServerWorld)serverWorld, serverWorld.func_175649_E(player.func_233580_cy_()), SpawnReason.NATURAL, null, null);
//               ItemStack itemStack = new ItemStack((IItemProvider)Items.field_185159_cQ);
//               CompoundNBT compoundNBT = new CompoundNBT();
//               compoundNBT.func_74768_a("Base", princessEntity.getColor());
//               compoundNBT.func_218657_a("Patterns", (INBT)pattern);
//               itemStack.func_77983_a("BlockEntityTag", (INBT)compoundNBT);
//               knightHunter.func_184611_a(Hand.OFF_HAND, itemStack);
//               serverWorld.func_217376_c((Entity)knightHunter);
//             }
//
//             timeLeft = Functions.minutesToTicks(2) + Functions.minutesToTicks(world.field_73012_v.nextInt(30));
//           }
//         } else {
//           timeLeft--;
//         }
//     }
//   }
}