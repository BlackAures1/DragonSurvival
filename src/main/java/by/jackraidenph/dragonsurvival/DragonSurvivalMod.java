package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.capability.Capabilities;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.gecko.DragonEntity;
import by.jackraidenph.dragonsurvival.handlers.BlockInit;
import by.jackraidenph.dragonsurvival.handlers.ClientEvents;
import by.jackraidenph.dragonsurvival.handlers.EntityTypesInit;
import by.jackraidenph.dragonsurvival.nest.DismantleNest;
import by.jackraidenph.dragonsurvival.nest.NestEntity;
import by.jackraidenph.dragonsurvival.nest.SleepInNest;
import by.jackraidenph.dragonsurvival.nest.ToggleRegeneration;
import by.jackraidenph.dragonsurvival.network.*;
import by.jackraidenph.dragonsurvival.util.ConfigurationHandler;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.ArgumentCommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.command.CommandSource;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import java.util.concurrent.atomic.AtomicReference;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

@Mod(DragonSurvivalMod.MODID)
public class DragonSurvivalMod {
    public static final String MODID = "dragonsurvival";
    public static final Logger LOGGER = LogManager.getLogger("Dragon Survival");
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    private static int nextPacketId = 0;

    public DragonSurvivalMod() {
        GeckoLib.initialize();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStart);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigurationHandler.SPEC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static <T> void register(Class<T> clazz, IMessage<T> message) {
        CHANNEL.registerMessage(nextPacketId++, clazz, message::encode, message::decode, message::handle);
    }

    private void setup(final FMLCommonSetupEvent event) {
        Capabilities.register();
        LOGGER.info("Successfully registered " + DragonStateHandler.class.getSimpleName() + "!");

        register(PacketSyncCapabilityMovement.class, new PacketSyncCapabilityMovement());
        register(PacketSyncXPDevour.class, new PacketSyncXPDevour());
        register(PacketSyncPredatorStats.class, new PacketSyncPredatorStats());
        register(SetRespawnPosition.class, new SetRespawnPosition());
        register(ResetPlayer.class, new ResetPlayer());
        register(SynchronizeNest.class, new SynchronizeNest());
        register(OpenDragonInventory.class, new OpenDragonInventory());
        register(SyncLevel.class, new SyncLevel());
        register(ToggleWings.class, new ToggleWings());

        //TODO synchronize health
        CHANNEL.registerMessage(nextPacketId++, SynchronizeDragonCap.class, (synchronizeDragonCap, packetBuffer) -> {
            packetBuffer.writeInt(synchronizeDragonCap.playerId);
            packetBuffer.writeByte(synchronizeDragonCap.dragonLevel.ordinal());
            packetBuffer.writeByte(synchronizeDragonCap.dragonType.ordinal());
            packetBuffer.writeBoolean(synchronizeDragonCap.hiding);
            packetBuffer.writeBoolean(synchronizeDragonCap.isDragon);
            packetBuffer.writeFloat(synchronizeDragonCap.health);
            packetBuffer.writeBoolean(synchronizeDragonCap.hasWings);

        }, packetBuffer -> {
            int id = packetBuffer.readInt();
            DragonLevel level = DragonLevel.values()[packetBuffer.readByte()];
            DragonType type = DragonType.values()[packetBuffer.readByte()];
            boolean hiding = packetBuffer.readBoolean();
            boolean isDragon = packetBuffer.readBoolean();
            return new SynchronizeDragonCap(id, hiding, type, level, isDragon, packetBuffer.readFloat(), packetBuffer.readBoolean());
        }, (synchronizeDragonCap, contextSupplier) -> {
            if (contextSupplier.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
                CHANNEL.send(PacketDistributor.ALL.noArg(), synchronizeDragonCap);
                ServerPlayerEntity serverPlayerEntity = contextSupplier.get().getSender();
                DragonStateProvider.getCap(serverPlayerEntity).ifPresent(dragonStateHandler -> {
                    dragonStateHandler.setIsHiding(synchronizeDragonCap.hiding);
                    dragonStateHandler.setLevel(synchronizeDragonCap.dragonLevel, serverPlayerEntity);
                    dragonStateHandler.setIsDragon(synchronizeDragonCap.isDragon);
                    dragonStateHandler.setType(synchronizeDragonCap.dragonType);
                    dragonStateHandler.setHealth(synchronizeDragonCap.health);
                    dragonStateHandler.setHasWings(synchronizeDragonCap.hasWings);
                });
            } else {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> new PacketProxy().refreshInstances(synchronizeDragonCap, contextSupplier));
            }
        });

        CHANNEL.registerMessage(nextPacketId++, ToggleRegeneration.class, (toggleRegeneration, packetBuffer) -> {
            packetBuffer.writeBlockPos(toggleRegeneration.nestPos);
            packetBuffer.writeBoolean(toggleRegeneration.state);
        }, packetBuffer -> new ToggleRegeneration(packetBuffer.readBlockPos(), packetBuffer.readBoolean()), (toggleRegeneration, contextSupplier) -> {
            ServerWorld serverWorld = contextSupplier.get().getSender().getServerWorld();
            TileEntity tileEntity = serverWorld.getTileEntity(toggleRegeneration.nestPos);
            if (tileEntity instanceof NestEntity) {
                ((NestEntity) tileEntity).regenerationMode = toggleRegeneration.state;
                tileEntity.markDirty();
                contextSupplier.get().setPacketHandled(true);
            }
        });

        CHANNEL.registerMessage(nextPacketId++, DismantleNest.class, (dismantleNest, packetBuffer) -> {
            packetBuffer.writeBlockPos(dismantleNest.nestPos);
        }, packetBuffer -> new DismantleNest(packetBuffer.readBlockPos()), (dismantleNest, contextSupplier) -> {
            ServerWorld serverWorld = contextSupplier.get().getSender().getServerWorld();
            TileEntity tileEntity = serverWorld.getTileEntity(dismantleNest.nestPos);
            if (tileEntity instanceof NestEntity) {
                serverWorld.destroyBlock(dismantleNest.nestPos, true);
                contextSupplier.get().setPacketHandled(true);
            }
        });

        CHANNEL.registerMessage(nextPacketId++, SleepInNest.class, (sleepInNest, packetBuffer) -> {
            packetBuffer.writeBlockPos(sleepInNest.nestPos);
        }, packetBuffer -> new SleepInNest(packetBuffer.readBlockPos()), (sleepInNest, contextSupplier) -> {
            ServerPlayerEntity serverPlayerEntity = contextSupplier.get().getSender();
            if (serverPlayerEntity.getServerWorld().isNightTime()) {
                serverPlayerEntity.trySleep(sleepInNest.nestPos);
                serverPlayerEntity.setSpawnPoint(sleepInNest.nestPos, false, true, DimensionType.OVERWORLD);
            }

        });

        CHANNEL.registerMessage(nextPacketId++, GiveNest.class, (giveNest, packetBuffer) -> {
                    packetBuffer.writeEnumValue(giveNest.dragonType);
                },
                packetBuffer -> new GiveNest(packetBuffer.readEnumValue(DragonType.class)), (giveNest, contextSupplier) -> {
                    ServerPlayerEntity playerEntity = contextSupplier.get().getSender();
                    Block item;
                    switch (giveNest.dragonType) {
                        case CAVE:
                            item = BlockInit.smallCaveNest;
                            break;
                        case FOREST:
                            item = BlockInit.smallForestNest;
                            break;
                        case SEA:
                            item = BlockInit.smallSeaNest;
                            break;
                        default:
                            item = null;
                    }
                    ItemStack itemStack = new ItemStack(item);
                    if (playerEntity.getHeldItemOffhand().isEmpty()) {
                        playerEntity.setHeldItem(Hand.OFF_HAND, itemStack);
                    } else {
                        ItemStack stack = playerEntity.getHeldItemOffhand().copy();
                        playerEntity.setHeldItem(Hand.OFF_HAND, itemStack);
                        if (!playerEntity.inventory.addItemStackToInventory(stack)) {
                            playerEntity.dropItem(stack, false, false);
                        }
                    }
                });

        CHANNEL.registerMessage(nextPacketId++, SetFlyState.class, (setFlyState, packetBuffer) -> {
                    packetBuffer.writeInt(setFlyState.playerid);
                    packetBuffer.writeBoolean(setFlyState.flying);
                },
                packetBuffer -> new SetFlyState(packetBuffer.readInt(), packetBuffer.readBoolean()),
                (setFlyState, contextSupplier) -> {
                    if (contextSupplier.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                        ClientEvents.dragonsFlying.put(setFlyState.playerid, setFlyState.flying);
                        contextSupplier.get().setPacketHandled(true);
                    }
                });

        CHANNEL.registerMessage(nextPacketId++, DiggingStatus.class, (diggingStatus, packetBuffer) -> {
                    packetBuffer.writeInt(diggingStatus.playerId);
                    packetBuffer.writeBoolean(diggingStatus.status);
                },
                packetBuffer -> new DiggingStatus(packetBuffer.readInt(), packetBuffer.readBoolean()),
                (diggingStatus, contextSupplier) -> {
                    if (contextSupplier.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                        Minecraft minecraft = Minecraft.getInstance();
                        Entity entity = minecraft.world.getEntityByID(diggingStatus.playerId);
                        if (entity instanceof PlayerEntity) {
                            ClientEvents.dragonsDigging.put(entity.getEntityId(), diggingStatus.status);
                            contextSupplier.get().setPacketHandled(true);
                        }
                    }
                });

        CHANNEL.registerMessage(nextPacketId++, StartJump.class, (startJump, packetBuffer) -> {
                    packetBuffer.writeInt(startJump.playerId);
                    packetBuffer.writeByte(startJump.ticks);
                },
                packetBuffer -> new StartJump(packetBuffer.readInt(), packetBuffer.readByte()),
                (startJump, contextSupplier) -> {
                    if (contextSupplier.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                        Entity entity = Minecraft.getInstance().world.getEntityByID(startJump.playerId);
                        if (entity instanceof PlayerEntity) {
                            ClientEvents.dragonsJumpingTicks.put(entity.getEntityId(), startJump.ticks);
                            contextSupplier.get().setPacketHandled(true);
                        }
                    }
                });

        CHANNEL.registerMessage(nextPacketId++, RefreshDragons.class, (refreshDragons, packetBuffer) -> {
                    packetBuffer.writeInt(refreshDragons.playerId);
                },
                packetBuffer -> new RefreshDragons(packetBuffer.readInt()),
                (refreshDragons, contextSupplier) -> {
                    if (contextSupplier.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
                        Thread thread = new Thread(() -> {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            ClientPlayerEntity myPlayer = Minecraft.getInstance().player;
                            ClientEvents.dummyDragon2.getAndSet(EntityTypesInit.dragonEntity.create(myPlayer.world));
                            ClientEvents.dummyDragon2.get().player = myPlayer.getEntityId();

                            PlayerEntity thatPlayer = (PlayerEntity) myPlayer.world.getEntityByID(refreshDragons.playerId);
                            if (thatPlayer != null) {
                                DragonEntity dragonEntity = EntityTypesInit.dragonEntity.create(myPlayer.world);
                                dragonEntity.player = thatPlayer.getEntityId();
                                ClientEvents.playerDragonHashMap.computeIfAbsent(thatPlayer.getEntityId(), integer -> new AtomicReference<>(dragonEntity)).getAndSet(dragonEntity);
                            }

                        });
                        thread.start();
                        contextSupplier.get().setPacketHandled(true);
                    }
                });
        LOGGER.info("Successfully registered packets!");
        EntityTypesInit.addSpawn();
        LOGGER.info("Successfully registered entity spawns!");
    }

    private void onServerStart(FMLServerStartingEvent serverStartingEvent) {
        CommandDispatcher<CommandSource> commandDispatcher = serverStartingEvent.getCommandDispatcher();
        RootCommandNode<CommandSource> rootCommandNode = commandDispatcher.getRoot();
        LiteralCommandNode<CommandSource> dragon = literal("dragon").requires(commandSource -> commandSource.hasPermissionLevel(2)).build();

        ArgumentCommandNode<CommandSource, String> dragonType = argument("dragon_type", StringArgumentType.string()).suggests((context, builder) -> ISuggestionProvider.suggest(new String[]{"cave", "sea", "forest"}, builder)).build();

        ArgumentCommandNode<CommandSource, Integer> dragonStage = argument("dragon_stage", IntegerArgumentType.integer(1, 3)).build();

        ArgumentCommandNode<CommandSource, Boolean> giveWings = argument("wings", BoolArgumentType.bool()).executes(context -> {
            String type = context.getArgument("dragon_type", String.class);
            int stage = context.getArgument("dragon_stage", Integer.TYPE);
            boolean wings = context.getArgument("wings", Boolean.TYPE);
            ServerPlayerEntity serverPlayerEntity = context.getSource().asPlayer();
            serverPlayerEntity.getCapability(DragonStateProvider.DRAGON_CAPABILITY).ifPresent(dragonStateHandler -> {
                DragonType dragonType1 = DragonType.valueOf(type.toUpperCase());
                dragonStateHandler.setType(dragonType1);
                DragonLevel dragonLevel = DragonLevel.values()[stage - 1];
                dragonStateHandler.setLevel(dragonLevel, serverPlayerEntity);
                dragonStateHandler.setIsDragon(true);
                dragonStateHandler.setHasWings(wings);
                CHANNEL.send(PacketDistributor.ALL.noArg(), new SynchronizeDragonCap(serverPlayerEntity.getEntityId(), false, dragonType1, dragonLevel, true, dragonLevel.initialHealth, wings));
            });
            return 1;
        }).build();

        rootCommandNode.addChild(dragon);
        dragon.addChild(dragonType);
        dragonType.addChild(dragonStage);
        dragonStage.addChild(giveWings);
        LOGGER.info("Registered commands");
    }
}
