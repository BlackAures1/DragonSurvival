package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.capability.PlayerStateCapability;
import by.jackraidenph.dragonsurvival.capability.PlayerStateHandler;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.handlers.BlockInit;
import by.jackraidenph.dragonsurvival.handlers.EntityTypesInit;
import by.jackraidenph.dragonsurvival.handlers.TileEntityTypesInit;
import by.jackraidenph.dragonsurvival.network.*;
import by.jackraidenph.dragonsurvival.renderer.MagicalPredatorRenderer;
import by.jackraidenph.dragonsurvival.renderer.PredatorStarTESR;
import by.jackraidenph.dragonsurvival.shader.ShaderHelper;
import by.jackraidenph.dragonsurvival.util.ConfigurationHandler;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(DragonSurvivalMod.MODID)
public class DragonSurvivalMod {
    public static final String MODID = "dragonsurvival";
    public static final Logger LOGGER = LogManager.getLogger();
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int nextId = 0;

    public DragonSurvivalMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigurationHandler.SPEC);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static <T> void register(Class<T> clazz, IMessage<T> message) {
        INSTANCE.registerMessage(nextId++, clazz, message::encode, message::decode, message::handle);
    }

    public static boolean playerIsDragon(PlayerEntity player) {
        return PlayerStateProvider.getCap(player).filter(PlayerStateHandler::getIsDragon).isPresent();
    }

    private void setup(final FMLCommonSetupEvent event) {
        PlayerStateCapability.register();
        LOGGER.info("Successfully registered PlayerStateCapabilityHandler!");
        register(PacketSyncCapabilityMovement.class, new PacketSyncCapabilityMovement());
        register(PacketSyncCapability.class, new PacketSyncCapability());
        register(PacketSyncXPDevour.class, new PacketSyncXPDevour());
        register(PacketSyncPredatorStats.class, new PacketSyncPredatorStats());
        LOGGER.info("Successfully registered Messages!");
        EntityTypesInit.addSpawn();
        LOGGER.info("Successfully registered Entity Spawns!");
    }

    private void setupClient(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(BlockInit.dragon_altar, RenderType.getTranslucent());
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.MAGICAL_BEAST, MagicalPredatorRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesInit.PREDATOR_STAR_TILE_ENTITY_TYPE, PredatorStarTESR::new);
        ShaderHelper.initShaders();
    }
}
