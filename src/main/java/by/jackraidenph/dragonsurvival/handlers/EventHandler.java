package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.entity.MagicalPredatorEntity;
import by.jackraidenph.dragonsurvival.nest.NestEntity;
import by.jackraidenph.dragonsurvival.registration.BlockInit;
import by.jackraidenph.dragonsurvival.registration.DragonEffects;
import by.jackraidenph.dragonsurvival.registration.EntityTypesInit;
import by.jackraidenph.dragonsurvival.registration.ItemsInit;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.monster.HoglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.*;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.SleepingLocationCheckEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@SuppressWarnings("unused")
@Mod.EventBusSubscriber
public class EventHandler {

    static int cycle = 0;

    /**
     * Check every 2 seconds
     */
    @SubscribeEvent
    public static void removeElytraFromDragon(TickEvent.PlayerTickEvent playerTickEvent) {
        if (!ConfigHandler.COMMON.dragonsAllowedToUseElytra.get() && playerTickEvent.phase == TickEvent.Phase.START) {
            PlayerEntity playerEntity = playerTickEvent.player;
            DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
                if (dragonStateHandler.isDragon() && playerEntity instanceof ServerPlayerEntity && cycle >= 40) {
                    //chestplate slot is #38
                    ItemStack stack = playerEntity.inventory.getItem(38);
                    Item item = stack.getItem();
                    if (item instanceof ElytraItem) {
                        playerEntity.drop(playerEntity.inventory.removeItemNoUpdate(38), true, false);
                    }
                    cycle = 0;
                } else cycle++;
            });
        }
    }

    /**
     * Adds dragon avoidance goal
     */
    @SubscribeEvent
    public static void onJoin(EntityJoinWorldEvent joinWorldEvent) {
        Entity entity = joinWorldEvent.getEntity();
        if (entity instanceof AnimalEntity && !(entity instanceof WolfEntity || entity instanceof HoglinEntity)) {

            ((AnimalEntity) entity).goalSelector.addGoal(5, new AvoidEntityGoal(
                    (AnimalEntity) entity, PlayerEntity.class,
                    livingEntity -> DragonStateProvider.isDragon((PlayerEntity) livingEntity) && !((PlayerEntity) livingEntity).hasEffect(DragonEffects.ANIMAL_PEACE),
                    20.0F, 1.3F, 1.5F, EntityPredicates.ATTACK_ALLOWED));
        }
        if (entity instanceof HorseEntity) {
            HorseEntity horseEntity = (HorseEntity) entity;
            horseEntity.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(horseEntity, PlayerEntity.class, 0, true, false, livingEntity -> livingEntity.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElseGet(null).getLevel() != DragonLevel.ADULT));
            horseEntity.targetSelector.addGoal(4, new AvoidEntityGoal<>(horseEntity, PlayerEntity.class, livingEntity -> livingEntity.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).getLevel() == DragonLevel.ADULT && !livingEntity.hasEffect(DragonEffects.ANIMAL_PEACE), 20, 1.3, 1.5, EntityPredicates.ATTACK_ALLOWED::test));
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent e) {
        LivingEntity livingEntity = e.getEntityLiving();
        World world = livingEntity.level;
        if (livingEntity instanceof AnimalEntity && livingEntity.level.getRandom().nextDouble() < ConfigHandler.COMMON.predatorAnimalSpawnChance.get()) {
            if (world.getEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB(livingEntity.blockPosition()).inflate(50), playerEntity -> playerEntity.hasEffect(DragonEffects.PREDATOR_ANTI_SPAWN)).isEmpty()) {
                MagicalPredatorEntity beast = EntityTypesInit.MAGICAL_BEAST.create(livingEntity.level);
                livingEntity.level.addFreshEntity(beast);
                beast.teleportToWithTicket(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
            }
        }
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
                    if (!rightClickBlock.getPlayer().isCreative()) {
                        itemStack.shrink(1);
                    }
                    rightClickBlock.setCanceled(true);
                    world.playSound(rightClickBlock.getPlayer(), blockPos, SoundEvents.WITHER_SPAWN, SoundCategory.PLAYERS, 1, 1);
                    rightClickBlock.setCancellationResult(ActionResultType.SUCCESS);
                }
            }
        }
    }

    @SubscribeEvent
    public static void returnBeacon(PlayerEvent.ItemCraftedEvent craftedEvent) {
        IInventory inventory = craftedEvent.getInventory();
        ItemStack result = craftedEvent.getCrafting();
        if (result.getItem() == BlockInit.dragonBeacon.asItem()) {
            craftedEvent.getPlayer().addItem(new ItemStack(Items.BEACON));
        }
    }

}
