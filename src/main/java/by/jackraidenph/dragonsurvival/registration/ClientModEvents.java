package by.jackraidenph.dragonsurvival.registration;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.gecko.PrinceRenderer;
import by.jackraidenph.dragonsurvival.gecko.*;
import by.jackraidenph.dragonsurvival.handlers.ClientEvents;
import by.jackraidenph.dragonsurvival.nest.NestScreen;
import by.jackraidenph.dragonsurvival.renderer.PrincessRenderer;
import by.jackraidenph.dragonsurvival.renderer.*;
import by.jackraidenph.dragonsurvival.shader.ShaderHelper;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.entity.SpriteRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.resources.IReloadableResourceManager;
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
        Minecraft minecraft = event.getMinecraftSupplier().get();

        RenderTypeLookup.setRenderLayer(BlockInit.dragon_altar_stone, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.dragon_altar_sandstone, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.dragon_altar_red_sandstone, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.dragon_altar_purpur_block, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.dragon_altar_oak_log, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.dragon_altar_nether_bricks, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.dragon_altar_mossy_cobblestone, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.dragon_altar_blackstone, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.mediumCaveNest, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.mediumForestNest, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.mediumSeaNest, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.bigCaveNest, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.bigForestNest, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.bigSeaNest, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.birchDoor, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.acaciaDoor, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.peaceDragonBeacon, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.vetoDragonBeacon, RenderType.cutout());
        RenderTypeLookup.setRenderLayer(BlockInit.magicDragonBeacon, RenderType.cutout());

        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.MAGICAL_BEAST, MagicalPredatorRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.BOLAS_ENTITY, manager -> new SpriteRenderer<>(manager, minecraft.getItemRenderer()));

        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.PRINCESS, manager -> new PrincessRenderer(manager, (IReloadableResourceManager) minecraft.getResourceManager()));
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.HUNTER_HOUND, by.jackraidenph.dragonsurvival.renderer.HunterHoundRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.SHOOTER_HUNTER, by.jackraidenph.dragonsurvival.renderer.ShooterHunterRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.SQUIRE_HUNTER, by.jackraidenph.dragonsurvival.renderer.SquireHunterRenderer::new);

        ClientRegistry.bindTileEntityRenderer(TileEntityTypesInit.PREDATOR_STAR_TILE_ENTITY_TYPE, PredatorStarTESR::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesInit.helmetTile, HelmetEntityRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesInit.dragonBeacon, DragonBeaconRenderer::new);
        ShaderHelper.initShaders();

        ScreenManager.register(Containers.nestContainer, NestScreen::new);

        TOGGLE_WINGS = new KeyBinding("Toggle wings", GLFW.GLFW_KEY_G, "Dragon Survival");
        ClientRegistry.registerKeyBinding(TOGGLE_WINGS);
        //Gecko renderers
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.DRAGON, manager -> new DragonRenderer(manager, ClientEvents.dragonModel = new DragonModel()));
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.KNIGHT, manager -> new KnightRenderer(manager, new KnightModel()));
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.PRINCESS_ON_HORSE, manager -> new by.jackraidenph.dragonsurvival.gecko.PrincessRenderer(manager, new PrincessModel()));
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.PRINCE_ON_HORSE, manager -> new PrinceRenderer(manager, new PrinceModel()));
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
        Minecraft.getInstance().getTextureManager().register(resourceLocation = new ResourceLocation(DragonSurvivalMod.MODID,name.toLowerCase()+"_"+dragonStage.name), new DynamicTexture(customTexture));
        return resourceLocation;
    }
}
