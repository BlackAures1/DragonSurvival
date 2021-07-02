package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.capability.Capabilities;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.gecko.DragonEntity;
import by.jackraidenph.dragonsurvival.handlers.BlockInit;
import by.jackraidenph.dragonsurvival.handlers.ClientEvents;
import by.jackraidenph.dragonsurvival.handlers.DragonFoodHandler;
import by.jackraidenph.dragonsurvival.handlers.EntityTypesInit;
import by.jackraidenph.dragonsurvival.handlers.EventHandler;
import by.jackraidenph.dragonsurvival.handlers.SpecificsHandler;
import by.jackraidenph.dragonsurvival.handlers.WingObtainmentController;
import by.jackraidenph.dragonsurvival.nest.DismantleNest;
import by.jackraidenph.dragonsurvival.nest.NestEntity;
import by.jackraidenph.dragonsurvival.nest.SleepInNest;
import by.jackraidenph.dragonsurvival.nest.ToggleRegeneration;
import by.jackraidenph.dragonsurvival.network.*;
import by.jackraidenph.dragonsurvival.util.BiomeDictionaryHelper;
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
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.MobSpawnInfo;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

import static net.minecraft.command.Commands.argument;
import static net.minecraft.command.Commands.literal;

@Mod(DragonSurvivalMod.MODID)
public class DragonSurvivalMod {
    public static final String MODID = "dragonsurvival";
    public static final Logger LOGGER = LogManager.getLogger("Dragon Survival");
    private static final String PROTOCOL_VERSION = "2";
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(new ResourceLocation(MODID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);

    private static int nextPacketId = 0;

    public DragonSurvivalMod() {
        GeckoLib.initialize();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::setup);
        ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, ConfigHandler.clientSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigHandler.commonSpec);
        ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, ConfigHandler.serverSpec);
        
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new DragonFoodHandler());
        MinecraftForge.EVENT_BUS.register(new SpecificsHandler());
        MinecraftForge.EVENT_BUS.addListener(EventPriority.HIGH, this::biomeLoadingEvent);
        MinecraftForge.EVENT_BUS.addListener(this::serverRegisterCommandsEvent);
    }
    
    private static <T> void register(Class<T> clazz, IMessage<T> message) {
        CHANNEL.registerMessage(nextPacketId++, clazz, message::encode, message::decode, message::handle);
    }

    private void setup(final FMLCommonSetupEvent event) {
    	WingObtainmentController.loadDragonPhrases();
        Capabilities.register();
        LOGGER.info("Successfully registered capabilities!");

        register(PacketSyncCapabilityMovement.class, new PacketSyncCapabilityMovement());
        register(SyncCapabilityDebuff.class, new SyncCapabilityDebuff());
        register(PacketSyncXPDevour.class, new PacketSyncXPDevour());
        register(PacketSyncPredatorStats.class, new PacketSyncPredatorStats());
        register(SynchronizeNest.class, new SynchronizeNest());
        register(OpenDragonInventory.class, new OpenDragonInventory());
        register(SyncSize.class, new SyncSize());
        register(ToggleWings.class, new ToggleWings());
        register(OpenCrafting.class,new OpenCrafting());

        CHANNEL.registerMessage(nextPacketId++, SynchronizeDragonCap.class, (synchronizeDragonCap, packetBuffer) -> {
            packetBuffer.writeInt(synchronizeDragonCap.playerId);
            packetBuffer.writeByte(synchronizeDragonCap.dragonType.ordinal());
            packetBuffer.writeBoolean(synchronizeDragonCap.hiding);
            packetBuffer.writeFloat(synchronizeDragonCap.size);
            packetBuffer.writeBoolean(synchronizeDragonCap.hasWings);
            packetBuffer.writeInt(synchronizeDragonCap.lavaAirSupply);
            packetBuffer.writeInt(synchronizeDragonCap.passengerId);
        }, packetBuffer -> {
            int id = packetBuffer.readInt();
            DragonType type = DragonType.values()[packetBuffer.readByte()];
            boolean hiding = packetBuffer.readBoolean();
            float size = packetBuffer.readFloat();
            boolean hasWings = packetBuffer.readBoolean();
            int lavaAirSupply = packetBuffer.readInt();
            int passengerId = packetBuffer.readInt();
            return new SynchronizeDragonCap(id, hiding, type, size, hasWings, lavaAirSupply, passengerId);
        }, (synchronizeDragonCap, contextSupplier) -> { 
            if (contextSupplier.get().getDirection().getReceptionSide() == LogicalSide.SERVER) {
                CHANNEL.send(PacketDistributor.ALL.noArg(), synchronizeDragonCap);
                ServerPlayerEntity serverPlayerEntity = contextSupplier.get().getSender();
                DragonStateProvider.getCap(serverPlayerEntity).ifPresent(dragonStateHandler -> {
                    dragonStateHandler.setIsHiding(synchronizeDragonCap.hiding);
                    dragonStateHandler.setType(synchronizeDragonCap.dragonType);
                    dragonStateHandler.setSize(synchronizeDragonCap.size, serverPlayerEntity);
                    dragonStateHandler.setHasWings(synchronizeDragonCap.hasWings);
                    dragonStateHandler.setLavaAirSupply(synchronizeDragonCap.lavaAirSupply);
                    dragonStateHandler.setPassengerId(synchronizeDragonCap.passengerId);
                    serverPlayerEntity.setForcedPose(null);
                    serverPlayerEntity.refreshDimensions();
                });
            } else {
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> new PacketProxy().refreshInstances(synchronizeDragonCap, contextSupplier));
            }
        });

        CHANNEL.registerMessage(nextPacketId++, ToggleRegeneration.class, (toggleRegeneration, packetBuffer) -> {
            packetBuffer.writeBlockPos(toggleRegeneration.nestPos);
            packetBuffer.writeBoolean(toggleRegeneration.state);
        }, packetBuffer -> new ToggleRegeneration(packetBuffer.readBlockPos(), packetBuffer.readBoolean()), (toggleRegeneration, contextSupplier) -> {
            ServerWorld serverWorld = contextSupplier.get().getSender().getLevel();
            TileEntity tileEntity = serverWorld.getBlockEntity(toggleRegeneration.nestPos);
            if (tileEntity instanceof NestEntity) {
                ((NestEntity) tileEntity).regenerationMode = toggleRegeneration.state;
                tileEntity.setChanged();
                contextSupplier.get().setPacketHandled(true);
            }
        });

        CHANNEL.registerMessage(nextPacketId++, DismantleNest.class, (dismantleNest, packetBuffer) -> {
            packetBuffer.writeBlockPos(dismantleNest.nestPos);
        }, packetBuffer -> new DismantleNest(packetBuffer.readBlockPos()), (dismantleNest, contextSupplier) -> {
            ServerWorld serverWorld = contextSupplier.get().getSender().getLevel();
            TileEntity tileEntity = serverWorld.getBlockEntity(dismantleNest.nestPos);
            if (tileEntity instanceof NestEntity) {
                serverWorld.destroyBlock(dismantleNest.nestPos, true);
                contextSupplier.get().setPacketHandled(true);
            }
        });

        CHANNEL.registerMessage(nextPacketId++, SleepInNest.class, (sleepInNest, packetBuffer) -> {
            packetBuffer.writeBlockPos(sleepInNest.nestPos);
        }, packetBuffer -> new SleepInNest(packetBuffer.readBlockPos()), (sleepInNest, contextSupplier) -> {
            ServerPlayerEntity serverPlayerEntity = contextSupplier.get().getSender();
            if (serverPlayerEntity.getLevel().isNight()) {
                serverPlayerEntity.startSleepInBed(sleepInNest.nestPos);
                serverPlayerEntity.setRespawnPosition(serverPlayerEntity.getLevel().dimension(), sleepInNest.nestPos, 0.0F, false, true); // Float is respawnAngle
                // check these boolean values, might need to be switched.
            }

        });

        CHANNEL.registerMessage(nextPacketId++, GiveNest.class, (giveNest, packetBuffer) -> {
                    packetBuffer.writeEnum(giveNest.dragonType);
                },
                packetBuffer -> new GiveNest(packetBuffer.readEnum(DragonType.class)), (giveNest, contextSupplier) -> {
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
                    if (playerEntity.getOffhandItem().isEmpty()) {
                        playerEntity.setItemInHand(Hand.OFF_HAND, itemStack);
                    } else {
                        ItemStack stack = playerEntity.getOffhandItem().copy();
                        playerEntity.setItemInHand(Hand.OFF_HAND, itemStack);
                        if (!playerEntity.inventory.add(stack)) {
                            playerEntity.drop(stack, false, false);
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
                        Entity entity = minecraft.level.getEntity(diggingStatus.playerId);
                        if (entity instanceof PlayerEntity) {
                            ClientEvents.dragonsDigging.put(entity.getId(), diggingStatus.status);
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
                        Entity entity = Minecraft.getInstance().level.getEntity(startJump.playerId);
                        if (entity instanceof PlayerEntity) {
                            ClientEvents.dragonsJumpingTicks.put(entity.getId(), startJump.ticks);
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
                            ClientEvents.dummyDragon2.getAndSet(EntityTypesInit.dragonEntity.create(myPlayer.level));
                            ClientEvents.dummyDragon2.get().player = myPlayer.getId();

                            PlayerEntity thatPlayer = (PlayerEntity) myPlayer.level.getEntity(refreshDragons.playerId);
                            if (thatPlayer != null) {
                                DragonEntity dragonEntity = EntityTypesInit.dragonEntity.create(myPlayer.level);
                                dragonEntity.player = thatPlayer.getId();
                                ClientEvents.playerDragonHashMap.computeIfAbsent(thatPlayer.getId(), integer -> new AtomicReference<>(dragonEntity)).getAndSet(dragonEntity);
                            }
                        });
                        thread.start();
                        contextSupplier.get().setPacketHandled(true);
                    }
                });
        LOGGER.info("Successfully registered packets!");
    }
    
    @SubscribeEvent
    public void biomeLoadingEvent(BiomeLoadingEvent event) {
    	List<BiomeDictionary.Type> includeList = Arrays.asList(BiomeDictionaryHelper.toBiomeTypeArray(ConfigHandler.COMMON.predatorBiomesInclude.get()));
        List<BiomeDictionary.Type> excludeList = Arrays.asList(BiomeDictionaryHelper.toBiomeTypeArray(ConfigHandler.COMMON.predatorBiomesExclude.get()));
        List<MobSpawnInfo.Spawners> spawns = event.getSpawns().getSpawner(EntityClassification.MONSTER);
        ResourceLocation biomeName = event.getName();
        if (biomeName == null) return;
        RegistryKey<Biome> biome = RegistryKey.create(ForgeRegistries.Keys.BIOMES, biomeName);
        Set<BiomeDictionary.Type> biomeTypes = BiomeDictionary.getTypes(biome);
    	if (spawns.stream().anyMatch(x -> x.type.getCategory() == EntityClassification.MONSTER) 
    			&& biomeTypes.stream().anyMatch(x -> includeList.contains(x) 
    			&& biomeTypes.stream().noneMatch(excludeList::contains))) {
    		spawns.add(new MobSpawnInfo.Spawners(EntityTypesInit.MAGICAL_BEAST, ConfigHandler.COMMON.predatorSpawnWeight.get(), ConfigHandler.COMMON.minPredatorSpawn.get(), ConfigHandler.COMMON.maxPredatorSpawn.get()));
    	}
    }
    
    @SubscribeEvent
    public void serverRegisterCommandsEvent(RegisterCommandsEvent event) {
    	CommandDispatcher<CommandSource> commandDispatcher = event.getDispatcher();
        RootCommandNode<CommandSource> rootCommandNode = commandDispatcher.getRoot();
        LiteralCommandNode<CommandSource> dragon = literal("dragon").requires(commandSource -> commandSource.hasPermission(2)).build();

        ArgumentCommandNode<CommandSource, String> dragonType = argument("dragon_type", StringArgumentType.string()).suggests((context, builder) -> ISuggestionProvider.suggest(new String[]{"cave", "sea", "forest"}, builder)).build();

        ArgumentCommandNode<CommandSource, Integer> dragonStage = argument("dragon_stage", IntegerArgumentType.integer(1, 3)).build();

        ArgumentCommandNode<CommandSource, Boolean> giveWings = argument("wings", BoolArgumentType.bool()).executes(context -> {
            String type = context.getArgument("dragon_type", String.class);
            int stage = context.getArgument("dragon_stage", Integer.TYPE);
            boolean wings = context.getArgument("wings", Boolean.TYPE);
            ServerPlayerEntity serverPlayerEntity = context.getSource().getPlayerOrException();
            serverPlayerEntity.getCapability(DragonStateProvider.DRAGON_CAPABILITY).ifPresent(dragonStateHandler -> {
                DragonType dragonType1 = DragonType.valueOf(type.toUpperCase());
                dragonStateHandler.setType(dragonType1);
                DragonLevel dragonLevel = DragonLevel.values()[stage - 1];
                dragonStateHandler.setHasWings(wings);
                dragonStateHandler.setSize(dragonLevel.size, serverPlayerEntity);
                dragonStateHandler.setPassengerId(0);
                CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> serverPlayerEntity), new SynchronizeDragonCap(serverPlayerEntity.getId(), false, dragonType1, dragonLevel.size, wings, ConfigHandler.SERVER.caveLavaSwimmingTicks.get(), dragonStateHandler.getPassengerId()));
                serverPlayerEntity.refreshDimensions();
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
