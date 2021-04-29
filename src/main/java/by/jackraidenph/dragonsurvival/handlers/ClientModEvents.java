package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.gecko.DragonModel;
import by.jackraidenph.dragonsurvival.gecko.DragonRenderer;
import by.jackraidenph.dragonsurvival.gui.DragonScreen;
import by.jackraidenph.dragonsurvival.nest.NestScreen;
import by.jackraidenph.dragonsurvival.renderer.MagicalPredatorRenderer;
import by.jackraidenph.dragonsurvival.renderer.PredatorStarTESR;
import by.jackraidenph.dragonsurvival.shader.ShaderHelper;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
@SuppressWarnings("unused")
public class ClientModEvents {

    public static KeyBinding TOGGLE_WINGS;

    public static final String SKINS = "https://raw.githubusercontent.com/DragonSurvivalTeam/DragonSurvival/master/src/test/resources/";

    @SubscribeEvent
    public static void onTextureStitchEvent(TextureStitchEvent.Pre event) {
        event.addSprite(new ResourceLocation(DragonSurvivalMod.MODID, "te/star/cage"));
        event.addSprite(new ResourceLocation(DragonSurvivalMod.MODID, "te/star/wind"));
        event.addSprite(new ResourceLocation(DragonSurvivalMod.MODID, "te/star/open_eye"));
        event.addSprite(new ResourceLocation(DragonSurvivalMod.MODID, "te/star/wind_vertical"));
        DragonSurvivalMod.LOGGER.info("Successfully added sprites!");
    }

    @SubscribeEvent
    public static void setupClient(final FMLClientSetupEvent event) {
        RenderTypeLookup.setRenderLayer(BlockInit.dragon_altar, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockInit.mediumCaveNest, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockInit.mediumForestNest, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockInit.mediumSeaNest, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockInit.bigCaveNest, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockInit.bigForestNest, RenderType.getCutout());
        RenderTypeLookup.setRenderLayer(BlockInit.bigSeaNest, RenderType.getCutout());
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.MAGICAL_BEAST, MagicalPredatorRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesInit.PREDATOR_STAR_TILE_ENTITY_TYPE, PredatorStarTESR::new);
        ShaderHelper.initShaders();

        ScreenManager.registerFactory(Containers.nestContainer, NestScreen::new);
        ScreenManager.registerFactory(Containers.dragonContainer, DragonScreen::new);

        TOGGLE_WINGS = new KeyBinding("Toggle wings", GLFW.GLFW_KEY_G, "Dragon Survival");
        ClientRegistry.registerKeyBinding(TOGGLE_WINGS);

        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.dragonEntity, manager -> new DragonRenderer(manager, ClientEvents.dragonModel = new DragonModel()));

//        if (ConfigurationHandler.disableClientHandlerSpam.get()) {
//            Field loggerfield = ClientPlayNetHandler.class.getDeclaredFields()[0];
//            loggerfield.setAccessible(true);
//            if (loggerfield.getType() == Logger.class) {
//                try {
//                    Logger logger = (Logger) loggerfield.get(ClientPlayNetHandler.class);
//                    if (logger instanceof org.apache.logging.log4j.core.Logger)
//                    {
//                        org.apache.logging.log4j.core.Logger logr= (org.apache.logging.log4j.core.Logger) logger;
//                        Field privateconfig=logr.getClass().getDeclaredField("privateConfig");
//                        privateconfig.setAccessible(true);
//                        Object ob=privateconfig.get(logr);
//                        Field level=ob.getClass().getDeclaredField("loggerConfigLevel");
//                        level.setAccessible(true);
//                        level.set(ob,Level.ERROR);
//                        logr.get().setLevel(Level.ERROR);
//                        logr.setLevel(Level.ERROR);
//                    }
//                } catch (IllegalAccessException | NoSuchFieldException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    /**
     * Loads a custom image for skin from the repository. The image name must be [player's UUID]_[stage].png
     */
    public static ResourceLocation loadCustomSkin(PlayerEntity playerEntity, DragonLevel dragonStage) throws IOException {
        UUID uuid = playerEntity.getUniqueID();
        URL url;
        switch (dragonStage) {
            case BABY:
                url = new URL(SKINS + uuid + "_newborn.png");
                break;
            case YOUNG:
                url = new URL(SKINS + uuid + "_young.png");
                break;
            case ADULT:
                url = new URL(SKINS + uuid + "_adult.png");
                break;
            default:
                url = null;
        }
        InputStream inputStream = url.openConnection().getInputStream();
        NativeImage customTexture = NativeImage.read(inputStream);
        ResourceLocation resourceLocation;
        Minecraft.getInstance().getTextureManager().loadTexture(resourceLocation = new ResourceLocation(DragonSurvivalMod.MODID, dragonStage.name), new DynamicTexture(customTexture));
        return resourceLocation;
    }

    /**
     * Loads a custom image for skin based on profile name
     */
    public static ResourceLocation loadCustomSkinForName(PlayerEntity playerEntity, DragonLevel dragonStage) throws IOException {
        String name = playerEntity.getGameProfile().getName();
        URL url;
        switch (dragonStage) {
            case BABY:
                url = new URL(SKINS + name + "_newborn.png");
                break;
            case YOUNG:
                url = new URL(SKINS + name + "_young.png");
                break;
            case ADULT:
                url = new URL(SKINS + name + "_adult.png");
                break;
            default:
                url = null;
        }
        InputStream inputStream = url.openConnection().getInputStream();
        NativeImage customTexture = NativeImage.read(inputStream);
        ResourceLocation resourceLocation;
        Minecraft.getInstance().getTextureManager().loadTexture(resourceLocation = new ResourceLocation(DragonSurvivalMod.MODID,name.toLowerCase()+"_"+dragonStage.name), new DynamicTexture(customTexture));
        return resourceLocation;
    }
}
