package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.Capabilities;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.capability.VillageRelationshipsProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.network.DiggingStatus;
import by.jackraidenph.dragonsurvival.network.RefreshDragons;
import by.jackraidenph.dragonsurvival.network.SynchronizeDragonCap;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.EffectInstance2;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SSetPassengersPacket;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.network.PacketDistributor;

@EventBusSubscriber
public class CapabilityController {
    @SubscribeEvent
    public static void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation("dragonsurvival", "playerstatehandler"), new DragonStateProvider());
            event.addCapability(new ResourceLocation("dragonsurvival", "village_relations"), new VillageRelationshipsProvider());
            DragonSurvivalMod.LOGGER.info("Successfully attached capabilities to the " + event.getObject().getClass().getSimpleName());
        }
    }

    @SubscribeEvent
    public static void onClone(PlayerEvent.Clone e) {
        PlayerEntity player = e.getPlayer();
        PlayerEntity original = e.getOriginal();
        DragonStateProvider.getCap(player).ifPresent(capNew ->
                DragonStateProvider.getCap(original).ifPresent(capOld -> {
                    if (capOld.isDragon()) {
                        DragonStateHandler.DragonMovementData movementData = capOld.getMovementData();
                        capNew.setMovementData(movementData.bodyYaw, movementData.headYaw, movementData.headPitch, movementData.bite);
                        capNew.setSize(capOld.getSize());
                        capNew.setType(capOld.getType());
                        capNew.setHasWings(capOld.hasWings());
                        capNew.setLavaAirSupply(ConfigHandler.SERVER.caveLavaSwimmingTicks.get());

                        DragonStateHandler.updateModifiers(original, player);

                        player.refreshDimensions();
                    }
                }));
        Capabilities.getVillageRelationships(player).ifPresent(villageRelationShips -> {
            Capabilities.getVillageRelationships(original).ifPresent(old -> {
                villageRelationShips.evilStatusDuration = old.evilStatusDuration;
                villageRelationShips.crimeLevel = old.crimeLevel;
                villageRelationShips.hunterSpawnDelay = old.hunterSpawnDelay;
                if (villageRelationShips.evilStatusDuration > 0) {
                    player.addEffect(new EffectInstance2(DragonEffects.EVIL_DRAGON, villageRelationShips.evilStatusDuration));
                }
            });
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
                    boolean isMining = interactionManager.isDestroyingBlock;
                    DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new DiggingStatus(playerEntity.getId(), isMining));
                }
            }
        });
    }

    /**
     * Mounting a dragon
     */
    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteractSpecific event) {
        if (!(event.getTarget() instanceof PlayerEntity) || event.getHand() != Hand.MAIN_HAND)
            return;
        PlayerEntity target = (PlayerEntity) event.getTarget();
        PlayerEntity self = event.getPlayer();
        DragonStateProvider.getCap(target).ifPresent(targetCap -> {
            if (targetCap.isDragon() && target.getPose() == Pose.CROUCHING && targetCap.getSize() >= 40 && !target.isVehicle()) {
                DragonStateProvider.getCap(self).ifPresent(selfCap -> {
                    if (!selfCap.isDragon() || selfCap.getLevel() == DragonLevel.BABY) {
                        if (event.getTarget() instanceof ServerPlayerEntity) {
                            self.startRiding(target);
                            ((ServerPlayerEntity) event.getTarget()).connection.send(new SSetPassengersPacket(target));
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
    public static void onServerPlayerTick(TickEvent.PlayerTickEvent event) { // TODO: Find a better way of doing this.
        if (!(event.player instanceof ServerPlayerEntity))
            return;
        ServerPlayerEntity player = (ServerPlayerEntity) event.player;
        DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
            int passengerId = dragonStateHandler.getPassengerId();
            Entity passenger = player.level.getEntity(passengerId);
            boolean flag = false;
            if (!dragonStateHandler.isDragon() && player.isVehicle() && player.getPassengers().get(0) instanceof ServerPlayerEntity) {
                flag = true;
                player.getPassengers().get(0).stopRiding();
                player.connection.send(new SSetPassengersPacket(player));
            } else if (player.isSpectator() && passenger != null && player.getPassengers().get(0) instanceof ServerPlayerEntity) {
                flag = true;
                player.getPassengers().get(0).stopRiding();
                player.connection.send(new SSetPassengersPacket(player));
            } else if (dragonStateHandler.isDragon() && dragonStateHandler.getSize() != 40 && player.isVehicle() && player.getPassengers().get(0) instanceof ServerPlayerEntity) {
                flag = true;
                player.getPassengers().get(0).stopRiding();
                player.connection.send(new SSetPassengersPacket(player));
            } else if (player.isSleeping() && player.isVehicle() && player.getPassengers().get(0) instanceof ServerPlayerEntity) {
                flag = true;
                player.getPassengers().get(0).stopRiding();
                player.connection.send(new SSetPassengersPacket(player));
            }
            if (passenger instanceof ServerPlayerEntity) {
                DragonStateHandler passengerCap = DragonStateProvider.getCap(passenger).orElseGet(null);
                if (passengerCap.isDragon() && passengerCap.getLevel() != DragonLevel.BABY) {
                    flag = true;
                    passenger.stopRiding();
                    player.connection.send(new SSetPassengersPacket(player));
                } else if (passenger.getRootVehicle() != player.getRootVehicle()) {
                    flag = true;
                    passenger.stopRiding();
                    player.connection.send(new SSetPassengersPacket(player));
                }
            }
            if (flag || passenger == null || !player.hasPassenger(passenger) || passenger.isSpectator() || player.isSpectator()) {
                dragonStateHandler.setPassengerId(0);
                DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SynchronizeDragonCap(player.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getSize(), dragonStateHandler.hasWings(), dragonStateHandler.getLavaAirSupply(), 0));
            }

        });
    }

    @SubscribeEvent
    public static void onPlayerDisconnect(PlayerEvent.PlayerLoggedOutEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        if (player.getVehicle() == null || !(player.getVehicle() instanceof ServerPlayerEntity))
            return;
        ServerPlayerEntity vehicle = (ServerPlayerEntity) player.getVehicle();
        DragonStateProvider.getCap(player).ifPresent(playerCap -> {
            DragonStateProvider.getCap(vehicle).ifPresent(vehicleCap -> {
                player.stopRiding();
                vehicle.connection.send(new SSetPassengersPacket(vehicle));
                vehicleCap.setPassengerId(0);
                DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> vehicle), new SynchronizeDragonCap(player.getId(), vehicleCap.isHiding(), vehicleCap.getType(), vehicleCap.getSize(), vehicleCap.hasWings(), vehicleCap.getLavaAirSupply(), 0));
            });
        });
    }

    @SubscribeEvent
    public static void changedDimension(PlayerEvent.PlayerChangedDimensionEvent changedDimensionEvent) {
        PlayerEntity playerEntity = changedDimensionEvent.getPlayer();
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SynchronizeDragonCap(playerEntity.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getSize(), dragonStateHandler.hasWings(), dragonStateHandler.getLavaAirSupply(), 0));
            DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new RefreshDragons(playerEntity.getId()));
        });
    }
}
