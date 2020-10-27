package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.capability.PlayerStateCapability;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.entity.MagicalPredatorEntity;
import by.jackraidenph.dragonsurvival.handlers.BlocksInit;
import by.jackraidenph.dragonsurvival.handlers.EntityTypesInit;
import by.jackraidenph.dragonsurvival.handlers.TileEntityTypesInit;
import by.jackraidenph.dragonsurvival.models.DragonModel;
import by.jackraidenph.dragonsurvival.network.*;
import by.jackraidenph.dragonsurvival.renderer.MagicalPredatorRenderer;
import by.jackraidenph.dragonsurvival.renderer.PredatorStarTESR;
import by.jackraidenph.dragonsurvival.shader.ShaderHelper;
import by.jackraidenph.dragonsurvival.util.ConfigurationHandler;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
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
    PlayerEntity player;
    //TODO server crash
    DragonModel model = new DragonModel();

    public DragonSurvivalMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onTextureStitchEvent);
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ConfigurationHandler.SPEC);
        ShaderHelper.initShaders();
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static <T> void register(Class<T> clazz, IMessage<T> message) {
        INSTANCE.registerMessage(nextId++, clazz, message::encode, message::decode, message::handle);
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
        RenderTypeLookup.setRenderLayer(BlocksInit.DRAGON_ALTAR_BLOCK, RenderType.getTranslucent());
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.MAGICAL_BEAST, MagicalPredatorRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesInit.PREDATOR_STAR_TILE_ENTITY_TYPE, PredatorStarTESR::new);
    }

    @SubscribeEvent
    public void onCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(DragonSurvivalMod.MODID, "playerstatehandler"), new PlayerStateProvider());
            LOGGER.info("Successfully attached capability to the PlayerEntity!");
        }
    }

    @SubscribeEvent
    public void onLoggedIn(PlayerEvent.PlayerLoggedInEvent e) {
        PlayerStateProvider.getCap(e.getPlayer()).ifPresent(cap ->
                cap.getMovementData().ifPresent(data ->
                        INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketSyncCapabilityMovement(data))));
    }

    @SubscribeEvent
    public void onLoggedOut(PlayerEvent.PlayerLoggedOutEvent e) {
        PlayerStateProvider.getCap(e.getPlayer()).ifPresent(cap ->
                cap.getMovementData().ifPresent(data ->
                        INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketSyncCapabilityMovement(data))));
    }

    @SubscribeEvent
    public void onDeath(LivingDeathEvent e) {
        if (e.getEntityLiving() instanceof PlayerEntity || e.getEntityLiving() instanceof MagicalPredatorEntity)
            return;

        if (e.getEntityLiving().world.getRandom().nextInt(30) == 0) {
            MagicalPredatorEntity beast = EntityTypesInit.MAGICAL_BEAST.create(e.getEntityLiving().world);
            e.getEntityLiving().world.addEntity(beast);
            beast.setPositionAndUpdate(e.getEntityLiving().getPosX(), e.getEntityLiving().getPosY(), e.getEntityLiving().getPosZ());
        }
    }

    @SubscribeEvent
    public void onTextureStitchEvent(TextureStitchEvent.Pre event) {
        event.addSprite(new ResourceLocation(DragonSurvivalMod.MODID, "te/star/cage"));
        event.addSprite(new ResourceLocation(DragonSurvivalMod.MODID, "te/star/wind"));
        event.addSprite(new ResourceLocation(DragonSurvivalMod.MODID, "te/star/open_eye"));
        event.addSprite(new ResourceLocation(DragonSurvivalMod.MODID, "te/star/wind_vertical"));
        LOGGER.info("Successfully added sprites!");
    }

    @SubscribeEvent
    public void onRender(RenderLivingEvent.Pre e) {
        if (e.getEntity() instanceof PlayerEntity) {
            player = (PlayerEntity) e.getEntity();
            PlayerStateProvider.getCap(player).ifPresent(cap -> {
                if (cap.getIsDragon()) {
                    e.setCanceled(true);

                    model.setRotationAngles(
                            player,
                            player.limbSwing,
                            MathHelper.lerp(e.getPartialRenderTick(), player.prevLimbSwingAmount, player.limbSwingAmount),
                            player.ticksExisted,
                            player.getYaw(e.getPartialRenderTick()),
                            player.getPitch(e.getPartialRenderTick()));

                    String texture = "textures/dragon/" + cap.getType().toString().toLowerCase() + ".png";

                    model.render(
                            e.getMatrixStack(),
                            e.getBuffers().getBuffer(RenderType.getEntityTranslucentCull(new ResourceLocation(DragonSurvivalMod.MODID, texture))),
                            e.getLight(),
                            LivingRenderer.getPackedOverlay(player, 0.0f),
                            e.getPartialRenderTick(),
                            player.getYaw(e.getPartialRenderTick()),
                            player.getPitch(e.getPartialRenderTick()),
                            1.0f);
                }
            });
        }
    }

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent e) {
        if (!(e.getEntity() instanceof MonsterEntity || e.getEntity() instanceof VillagerEntity) & e.getEntity() instanceof CreatureEntity) {
            ((MobEntity) e.getEntity()).goalSelector.addGoal(2, new AvoidEntityGoal(
                    (CreatureEntity) e.getEntity(),
                    PlayerEntity.class,
                    livingEntity -> PlayerStateProvider.getCap((PlayerEntity) livingEntity).orElse(null).getIsDragon(),
                    20.0F,
                    1.3F,
                    1.5F,
                    EntityPredicates.CAN_AI_TARGET));
        }

        /*if (e.getEntity() instanceof MagicalBeastEntity)
            if (new Random().nextFloat() + 0.1F <= 0.3F) {
                SkeletonEntity skeletonEntity = new SkeletonEntity(EntityType.SKELETON, e.getWorld());
                e.getWorld().addEntity(skeletonEntity);
                //skeletonEntity.setPositionAndUpdate(e.getEntity().getPosX(), e.getEntity().getPosY(), e.getEntity().getPosZ());
                MagicalBeastEntity beastEntity = (MagicalBeastEntity) e.getEntity();
                skeletonEntity.startRiding(beastEntity);
            }*/
    }

    @SubscribeEvent
    public void onClone(PlayerEvent.Clone e) {
        PlayerStateProvider.getCap(e.getPlayer()).ifPresent(capNew ->
                PlayerStateProvider.getCap(e.getOriginal()).ifPresent(capOld -> {
                    if (!capNew.getIsDragon())
                        return;

                    capNew.setMovementData(capOld.getMovementData().orElse(null), true);
                    capNew.setLevel(capNew.getLevel());
                    capNew.setType(capNew.getType());
                }));
    }
}
