package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.network.SyncCapabilityDebuff;
import by.jackraidenph.dragonsurvival.registration.DragonEffects;
import by.jackraidenph.dragonsurvival.util.DamageSources;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvents;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

@Mod.EventBusSubscriber
public class DragonTraitHandler {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
        if (playerTickEvent.phase != TickEvent.Phase.START)
            return;
        PlayerEntity playerEntity = playerTickEvent.player;
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                World world = playerEntity.level;
                BlockState blockUnder = world.getBlockState(playerEntity.blockPosition().below());
                Block block = blockUnder.getBlock();
                if (!world.isClientSide && ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.speedupEffectLevel.get() > 0 && SpecificsHandler.DRAGON_SPEEDUP_BLOCKS != null && SpecificsHandler.DRAGON_SPEEDUP_BLOCKS.get(dragonStateHandler.getType()).contains(block))
                    playerEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 65, ConfigHandler.SERVER.speedupEffectLevel.get() - 1, false, false));
                switch (dragonStateHandler.getType()) {
                    case CAVE:
                        if (ConfigHandler.SERVER.penalties.get() && !playerEntity.hasEffect(DragonEffects.VETO) && !playerEntity.isCreative() && !playerEntity.isSpectator() && ((playerEntity.isInWaterOrBubble() && ConfigHandler.SERVER.caveWaterDamage.get() != 0.0) || (playerEntity.isInWaterOrRain() && !playerEntity.isInWater() && ConfigHandler.SERVER.caveRainDamage.get() != 0.0) || (SpecificsHandler.SEA_DRAGON_HYDRATION_BLOCKS != null && (SpecificsHandler.SEA_DRAGON_HYDRATION_BLOCKS.contains(block) || SpecificsHandler.SEA_DRAGON_HYDRATION_BLOCKS.contains(world.getBlockState(playerEntity.blockPosition()).getBlock())) && ConfigHandler.SERVER.caveRainDamage.get() != 0.0))) {
                            if (playerEntity.isInWaterOrBubble() && playerEntity.tickCount % 10 == 0 && ConfigHandler.SERVER.caveWaterDamage.get() != 0.0)
                                playerEntity.hurt(DamageSources.WATER_BURN, ConfigHandler.SERVER.caveWaterDamage.get().floatValue());
                            else if (((playerEntity.isInWaterOrRain() && !playerEntity.isInWaterOrBubble()) || (SpecificsHandler.SEA_DRAGON_HYDRATION_BLOCKS != null && (SpecificsHandler.SEA_DRAGON_HYDRATION_BLOCKS.contains(block) || SpecificsHandler.SEA_DRAGON_HYDRATION_BLOCKS.contains(world.getBlockState(playerEntity.blockPosition()).getBlock())))) && playerEntity.tickCount % 40 == 0 && ConfigHandler.SERVER.caveRainDamage.get() != 0.0)
                                playerEntity.hurt(DamageSources.WATER_BURN, ConfigHandler.SERVER.caveRainDamage.get().floatValue());
                            if (playerEntity.tickCount % 5 == 0)
                                playerEntity.playSound(SoundEvents.LAVA_EXTINGUISH, 1.0F, (playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.2F + 1.0F);
                            if (world.isClientSide)
                                world.addParticle(ParticleTypes.POOF,
                                        playerEntity.getX() + world.random.nextDouble() * (world.random.nextBoolean() ? 1 : -1),
                                        playerEntity.getY() + 0.5F,
                                        playerEntity.getZ() + world.random.nextDouble() * (world.random.nextBoolean() ? 1 : -1),
                                        0, 0, 0);
                        }
                        if (playerEntity.isOnFire() && ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.caveFireImmunity.get())
                            playerEntity.clearFire();
                        if (playerEntity.isEyeInFluid(FluidTags.LAVA) && ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.caveLavaSwimming.get() && ConfigHandler.SERVER.caveLavaSwimmingTicks.get() != 0) {
                            if (!playerEntity.canBreatheUnderwater() && !playerEntity.abilities.invulnerable) {
                                dragonStateHandler.setLavaAirSupply(dragonStateHandler.getLavaAirSupply() - 1);
                                if (dragonStateHandler.getLavaAirSupply() == -20) {
                                    dragonStateHandler.setLavaAirSupply(0);
                                    if (!playerEntity.level.isClientSide)
                                        playerEntity.hurt(DamageSource.DROWN, 2F); //LAVA_YES
                                }
                            }
                            if (!playerEntity.level.isClientSide && playerEntity.isPassenger() && playerEntity.getVehicle() != null && !playerEntity.getVehicle().canBeRiddenInWater(playerEntity)) {
                                playerEntity.stopRiding();
                            }
                        } else if (dragonStateHandler.getLavaAirSupply() < ConfigHandler.SERVER.caveLavaSwimmingTicks.get() && !playerEntity.isEyeInFluid(FluidTags.WATER))
                            dragonStateHandler.setLavaAirSupply(Math.min(dragonStateHandler.getLavaAirSupply() + (int) Math.ceil(ConfigHandler.SERVER.caveLavaSwimmingTicks.get() * 0.0133333F), ConfigHandler.SERVER.caveLavaSwimmingTicks.get()));
                        break;
                    case FOREST:
                        if (ConfigHandler.SERVER.penalties.get() && !playerEntity.hasEffect(DragonEffects.MAGIC) && ConfigHandler.SERVER.forestStressTicks.get() > 0 && !playerEntity.isCreative() && !playerEntity.isSpectator()) {
                            WorldLightManager lightManager = world.getChunkSource().getLightEngine();
                            if ((lightManager.getLayerListener(LightType.BLOCK).getLightValue(playerEntity.blockPosition()) < 3 && lightManager.getLayerListener(LightType.SKY).getLightValue(playerEntity.blockPosition()) < 3)) {
                                if (dragonStateHandler.getDebuffData().timeInDarkness < ConfigHandler.SERVER.forestStressTicks.get())
                                    dragonStateHandler.getDebuffData().timeInDarkness++;
                                if (dragonStateHandler.getDebuffData().timeInDarkness == 1 && !playerEntity.level.isClientSide)
                                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new SyncCapabilityDebuff(playerEntity.getId(), dragonStateHandler.getDebuffData().timeWithoutWater, dragonStateHandler.getDebuffData().timeInDarkness));
                                if (dragonStateHandler.getDebuffData().timeInDarkness == ConfigHandler.SERVER.forestStressTicks.get() && !world.isClientSide && playerEntity.tickCount % 21 == 0)
                                    playerEntity.addEffect(new EffectInstance(DragonEffects.STRESS, ConfigHandler.SERVER.forestStressEffectDuration.get() * 20));
                            } else if (dragonStateHandler.getDebuffData().timeInDarkness > 0) {
                                dragonStateHandler.getDebuffData().timeInDarkness = (Math.max(dragonStateHandler.getDebuffData().timeInDarkness - (int) Math.ceil(ConfigHandler.SERVER.forestStressTicks.get() * 0.02F), 0));
                                if (dragonStateHandler.getDebuffData().timeInDarkness == 0 && !playerEntity.level.isClientSide)
                                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new SyncCapabilityDebuff(playerEntity.getId(), dragonStateHandler.getDebuffData().timeWithoutWater, dragonStateHandler.getDebuffData().timeInDarkness));
                            }
                        }
                        break;
                    case SEA:
                        if ((playerEntity.hasEffect(DragonEffects.PEACE) || playerEntity.isEyeInFluid(FluidTags.WATER)) && playerEntity.getAirSupply() < playerEntity.getMaxAirSupply())
                            playerEntity.setAirSupply(playerEntity.getMaxAirSupply());
                        if (ConfigHandler.SERVER.penalties.get() && !playerEntity.hasEffect(DragonEffects.PEACE) && ConfigHandler.SERVER.seaTicksWithoutWater.get() > 0 && !playerEntity.isCreative() && !playerEntity.isSpectator()) {
                            DragonStateHandler.DragonDebuffData debuffData = dragonStateHandler.getDebuffData();
                            if (!playerEntity.isInWaterRainOrBubble() && (SpecificsHandler.SEA_DRAGON_HYDRATION_BLOCKS != null && !SpecificsHandler.SEA_DRAGON_HYDRATION_BLOCKS.contains(block) && !SpecificsHandler.SEA_DRAGON_HYDRATION_BLOCKS.contains(world.getBlockState(playerEntity.blockPosition()).getBlock()))) {
                                if (debuffData.timeWithoutWater < ConfigHandler.SERVER.seaTicksWithoutWater.get() * 2)
                                    debuffData.timeWithoutWater++;
                                if (debuffData.timeWithoutWater == ConfigHandler.SERVER.seaTicksWithoutWater.get() + 1 && !playerEntity.level.isClientSide)
                                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new SyncCapabilityDebuff(playerEntity.getId(), debuffData.timeWithoutWater, debuffData.timeInDarkness));
                            } else if (debuffData.timeWithoutWater > 0) {
                                int old = debuffData.timeWithoutWater;
                                debuffData.timeWithoutWater = (Math.max(debuffData.timeWithoutWater - (int) Math.ceil(ConfigHandler.SERVER.seaTicksWithoutWater.get() * 0.005F), 0));
                                if (old > ConfigHandler.SERVER.seaTicksWithoutWater.get() + 1 && debuffData.timeWithoutWater <= ConfigHandler.SERVER.seaTicksWithoutWater.get() && !playerEntity.level.isClientSide)
                                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new SyncCapabilityDebuff(playerEntity.getId(), debuffData.timeWithoutWater, debuffData.timeInDarkness));
                            }
                            if (!world.isClientSide && debuffData.timeWithoutWater > ConfigHandler.SERVER.seaTicksWithoutWater.get() && debuffData.timeWithoutWater < ConfigHandler.SERVER.seaTicksWithoutWater.get() * 2) {
                                if (playerEntity.tickCount % 40 == 0) {
                                    playerEntity.hurt(DamageSources.DEHYDRATION, ConfigHandler.SERVER.seaDehydrationDamage.get().floatValue());
                                }
                            } else if (!world.isClientSide && debuffData.timeWithoutWater >= ConfigHandler.SERVER.seaTicksWithoutWater.get() * 2) {
                                if (playerEntity.tickCount % 20 == 0) {
                                    playerEntity.hurt(DamageSources.DEHYDRATION, ConfigHandler.SERVER.seaDehydrationDamage.get().floatValue());
                                }
                            }
                        } else if (!playerEntity.level.isClientSide && playerEntity.hasEffect(DragonEffects.PEACE)) {
                            DragonStateHandler.DragonDebuffData debuffData = dragonStateHandler.getDebuffData();
                            if (debuffData.timeWithoutWater > 0) {
                                debuffData.timeWithoutWater = 0;
                                DragonSurvivalMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new SyncCapabilityDebuff(playerEntity.getId(), debuffData.timeWithoutWater, debuffData.timeInDarkness));
                            }
                        }
                        break;
                }

                // Dragon Particles
                // TODO: Randomize along dragon body
                if (world.isClientSide && !playerEntity.isCreative() && !playerEntity.isSpectator()) {
                    if (dragonStateHandler.getType() == DragonType.SEA && !playerEntity.hasEffect(DragonEffects.PEACE) && dragonStateHandler.getDebuffData().timeWithoutWater >= ConfigHandler.SERVER.seaTicksWithoutWater.get())
                        world.addParticle(ParticleTypes.WHITE_ASH,
                                playerEntity.getX() + world.random.nextDouble() * (world.random.nextBoolean() ? 1 : -1),
                                playerEntity.getY() + 0.5F,
                                playerEntity.getZ() + world.random.nextDouble() * (world.random.nextBoolean() ? 1 : -1),
                                0, 0, 0);
                    if (dragonStateHandler.getType() == DragonType.FOREST && !playerEntity.hasEffect(DragonEffects.MAGIC) && dragonStateHandler.getDebuffData().timeInDarkness == ConfigHandler.SERVER.forestStressTicks.get())
                        world.addParticle(ParticleTypes.SMOKE,
                                playerEntity.getX() + world.random.nextDouble() * (world.random.nextBoolean() ? 1 : -1),
                                playerEntity.getY() + 0.5F,
                                playerEntity.getZ() + world.random.nextDouble() * (world.random.nextBoolean() ? 1 : -1),
                                0, 0, 0);
                }
            }
        });
    }
}
