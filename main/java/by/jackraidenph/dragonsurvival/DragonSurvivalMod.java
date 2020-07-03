package by.jackraidenph.dragonsurvival;

import by.jackraidenph.dragonsurvival.capability.IPlayerStateHandler;
import by.jackraidenph.dragonsurvival.capability.PlayerStateCapability;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.models.DragonModel;
import by.jackraidenph.dragonsurvival.network.IMessage;
import by.jackraidenph.dragonsurvival.network.MessageSyncCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.GetCollisionBoxesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
    IPlayerStateHandler cap;
    IPlayerStateHandler capUpd;
    PlayerEntity player;
    DragonModel model = new DragonModel();
    PlayerEntity playerUpd;

    public DragonSurvivalMod() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setupClient);
        MinecraftForge.EVENT_BUS.register(this);
    }

    private static <T> void register(Class<T> clazz, IMessage<T> message) {
        INSTANCE.registerMessage(nextId++, clazz, message::encode, message::decode, message::handle);
    }

    private void setup(final FMLCommonSetupEvent event) {
        PlayerStateCapability.register();
        LOGGER.info("Successfully registered PlayerStateCapabilityHandler!");
        register(MessageSyncCapability.class, new MessageSyncCapability());
        LOGGER.info("Successfully registered MessageSyncCapability!");
    }

    private void setupClient(final FMLClientSetupEvent event) {
        LOGGER.info("Successfully registered DragonRenderer!");
    }

    @SubscribeEvent
    public void onCapabileity(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof PlayerEntity) {
            event.addCapability(new ResourceLocation(DragonSurvivalMod.MODID, "playerstatehandler"), new PlayerStateProvider());
            LOGGER.info("Successfully attached capability to the PlayerEntity!");
        }
    }

    @SubscribeEvent
    public void onLogged(PlayerEvent.PlayerLoggedInEvent e) {
        if (((PlayerEntity) e.getEntityLiving()).getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).isPresent())
            INSTANCE.send(PacketDistributor.ALL.noArg(), new MessageSyncCapability(((PlayerEntity) e.getEntityLiving()).getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).orElse(null).getData()));
    }

    @SubscribeEvent
    public void onLogged(PlayerEvent.PlayerLoggedOutEvent e) {
        if (((PlayerEntity) e.getEntityLiving()).getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).isPresent())
            INSTANCE.send(PacketDistributor.ALL.noArg(), new MessageSyncCapability(((PlayerEntity) e.getEntityLiving()).getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).orElse(null).getData()));
    }

    @SubscribeEvent
    public void onRender(RenderLivingEvent.Pre e) {
        if (e.getEntity() instanceof PlayerEntity) {
            player = (PlayerEntity) e.getEntity();
            if (player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).isPresent()) {
                cap = player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).orElseGet(null);
                if (cap.getIsDragon()) {
                    e.setCanceled(true);

                    model.setRotationAngles(
                            player,
                            player.limbSwing,
                            MathHelper.lerp(e.getPartialRenderTick(), player.prevLimbSwingAmount, player.limbSwingAmount),
                            player.ticksExisted,
                            player.getYaw(e.getPartialRenderTick()),
                            player.getPitch(e.getPartialRenderTick()));

                    String texture = "textures/" + Minecraft.getInstance().player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY, Direction.DOWN).orElse(null).getType().toString().toLowerCase() + ".png";

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
            }
        }
    }

    /*@SubscribeEvent
    public void onLivingUpdate(LivingEvent.LivingUpdateEvent e) {
        if (e.getEntityLiving() instanceof PlayerEntity) {
            playerUpd = (PlayerEntity) e.getEntityLiving();
            if (playerUpd.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY, Direction.DOWN).isPresent()) {
                capUpd = playerUpd.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY, Direction.DOWN).orElse(null);
                if(capUpd.getIsDragon()){
                    capUpd.setMovementData(playerUpd.renderYawOffset, playerUpd.rotationYawHead, playerUpd.rotationPitch, );
                }
            }
        }
    }*/

    @SubscribeEvent
    public void onClone(PlayerEvent.Clone e) {
        if (!player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).isPresent())
            return;
        PlayerEntity player = e.getPlayer();
        IPlayerStateHandler cap = player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).orElse(null);
        IPlayerStateHandler oldCap = e.getOriginal().getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).orElse(null);
        cap.setData(oldCap.getData());
    }

    @SubscribeEvent
    public void onCollision(GetCollisionBoxesEvent e) {
        System.out.println(e);
        if (e.getEntity() instanceof PlayerEntity) {
            player = (PlayerEntity) e.getEntity();
            if (player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).isPresent()) {
                cap = player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).orElseGet(null);
                if (cap.getIsDragon()) {
                    System.out.println(e.getCollisionBoxesList());
                }
            }
        }
    }
}
