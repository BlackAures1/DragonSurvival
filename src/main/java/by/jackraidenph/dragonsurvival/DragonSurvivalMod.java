package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.capability.PlayerStateCapability;
import by.jackraidenph.dragonsurvival.capability.PlayerStateHandler;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.entity.MagicalBeastEntity;
import by.jackraidenph.dragonsurvival.handlers.EntityTypesInit;
import by.jackraidenph.dragonsurvival.models.DragonModel;
import by.jackraidenph.dragonsurvival.network.IMessage;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapability;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.renderer.MagicalBeastRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.EntityPredicates;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
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
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int nextId = 0;
    PlayerEntity player;
    DragonModel model = new DragonModel();

    public DragonSurvivalMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        MinecraftForge.EVENT_BUS.register(this);
        EntityTypesInit.ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());

    }

    private static <T> void register(Class<T> clazz, IMessage<T> message) {
        INSTANCE.registerMessage(nextId++, clazz, message::encode, message::decode, message::handle);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PlayerStateCapability.register();
        LOGGER.info("Successfully registered PlayerStateCapabilityHandler!");
        register(PacketSyncCapabilityMovement.class, new PacketSyncCapabilityMovement());
        register(PacketSyncCapability.class, new PacketSyncCapability());
        LOGGER.info("Successfully registered Messages!");
        for (Biome b : Biome.BIOMES) {
            b.getSpawns(EntityTypesInit.MAGICAL_BEAST.get().getClassification()).add(new Biome.SpawnListEntry(EntityTypesInit.MAGICAL_BEAST.get(), 1, 1, 3));
            System.out.println(b);
        }
        LOGGER.info("Successfully registered Entity Spawns!");

    }

    private void setupClient(final FMLClientSetupEvent event) {
        LOGGER.info("Successfully registered DragonRenderer!");
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.MAGICAL_BEAST.get(), MagicalBeastRenderer::new);
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
        if (e.getEntityLiving() instanceof PlayerEntity)
            return;

        if (e.getEntityLiving().world.getRandom().nextInt(30) == 0) {
            MagicalBeastEntity beast = EntityTypesInit.MAGICAL_BEAST.get().create(e.getEntityLiving().world);
            e.getEntityLiving().world.addEntity(beast);
            //beast.setPositionAndUpdate(e.getEntityLiving().getPosX(), e.getEntityLiving().getPosY(), e.getEntityLiving().getPosZ());
        }

        if (e.getEntityLiving() instanceof MagicalBeastEntity) {

        }
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

                    PlayerStateHandler cap = capNew;
                    PlayerStateHandler oldCap = capOld;
                    cap.setMovementData(oldCap.getMovementData().orElse(null), true);
                    cap.setLevel(cap.getLevel());
                    cap.setType(cap.getType());
                }));
    }

    @SubscribeEvent
    public void onCollision(GetCollisionBoxesEvent e) {
        System.out.println(e);
        if (e.getEntity() instanceof PlayerEntity) {
            player = (PlayerEntity) e.getEntity();
            PlayerStateProvider.getCap(player).ifPresent(cap -> {
                if (cap.getIsDragon()) {
                    System.out.println(e.getCollisionBoxesList());
                }
            });
        }
    }
}
