package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.containers.DragonInventoryContainer;
import by.jackraidenph.dragonsurvival.entity.MagicalPredatorEntity;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.item.*;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.lang.reflect.Field;
import java.util.HashSet;

@Mod.EventBusSubscriber
public class EventHandler {

    static HashSet<Item> itemsForbiddenForDragon = new HashSet<>();

    static {
        itemsForbiddenForDragon.add(Items.CROSSBOW);
        itemsForbiddenForDragon.add(Items.BOW);
        itemsForbiddenForDragon.add(Items.SHIELD);
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
        if (playerTickEvent.phase == TickEvent.Phase.START) {
            PlayerEntity playerEntity = playerTickEvent.player;
            if ((!DragonSurvivalMod.playerIsDragon(playerEntity) || playerEntity.isCreative()) && playerEntity.container instanceof DragonInventoryContainer) {
                setPlayerContainer(playerEntity);
            } else if (!playerEntity.isCreative() && !playerEntity.isSpectator() && playerEntity.container.getClass() == PlayerContainer.class) {
                PlayerStateProvider.getCap(playerEntity).ifPresent(playerStateHandler -> {
                    if (playerStateHandler.getIsDragon())
                        setDragonContainer(playerEntity);
                });
            }

            PlayerStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
                if (dragonStateHandler.getIsDragon()) {
                    for (int i = 0; i < playerEntity.inventory.getSizeInventory(); i++) {
                        ItemStack stack = playerEntity.inventory.getStackInSlot(i);
                        if (itemsForbiddenForDragon.contains(stack.getItem())) {
                            playerEntity.dropItem(playerEntity.inventory.removeStackFromSlot(i), true, false);
                            break;
                        }
                    }
                }
            });
        }
    }

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
                    livingEntity -> PlayerStateProvider.getCap((PlayerEntity) livingEntity).orElse(null).getIsDragon(),
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
                    if (capOld.getIsDragon()) {
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
            if (dragonStateHandler.getIsDragon()) {
                dragonStateHandler.syncCapabilityData(!playerEntity.world.isRemote);
            }
        });
    }

    @SubscribeEvent
    public static void modifyBreakSpeed(PlayerEvent.BreakSpeed breakSpeedEvent) {
        PlayerEntity playerEntity = breakSpeedEvent.getPlayer();
        PlayerStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.getIsDragon()) {
                ItemStack mainStack = playerEntity.getHeldItemMainhand();
                Item item = mainStack.getItem();
                if (item instanceof ToolItem || item instanceof SwordItem || item instanceof ShearsItem) {
                    breakSpeedEvent.setNewSpeed(breakSpeedEvent.getOriginalSpeed() * 0.7f);
                }
            }
        });
    }
}
