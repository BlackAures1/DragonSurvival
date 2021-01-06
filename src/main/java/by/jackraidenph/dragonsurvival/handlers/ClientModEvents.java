package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.gui.DragonScreen;
import by.jackraidenph.dragonsurvival.nest.NestScreen;
import by.jackraidenph.dragonsurvival.renderer.MagicalPredatorRenderer;
import by.jackraidenph.dragonsurvival.renderer.PredatorStarTESR;
import by.jackraidenph.dragonsurvival.shader.ShaderHelper;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {
    /**
     * Whether all three custom skins exist
     */
    public static boolean customSkinPresence;
    public static ResourceLocation customNewbornSkin;
    public static ResourceLocation customYoungSkin;
    public static ResourceLocation customAdultSkin;

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
        RenderingRegistry.registerEntityRenderingHandler(EntityTypesInit.MAGICAL_BEAST, MagicalPredatorRenderer::new);
        ClientRegistry.bindTileEntityRenderer(TileEntityTypesInit.PREDATOR_STAR_TILE_ENTITY_TYPE, PredatorStarTESR::new);
        ShaderHelper.initShaders();

        ScreenManager.registerFactory(Containers.nestContainer, NestScreen::new);
        ScreenManager.registerFactory(Containers.dragonContainer, DragonScreen::new);
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
        Minecraft.getInstance().getTextureManager().loadTexture(resourceLocation = new ResourceLocation(DragonSurvivalMod.MODID, dragonStage.name), new DynamicTexture(customTexture));
        return resourceLocation;
    }

    /**
     * Loads a custom image for skin from the root directory. The image name must be [player's name]_[stage].png or [player's UUID]_[stage].png
     *
     * @param dragonStage <b>newborn</b>, <b>young</b> or <b>adult</b>
     * @return usable resourceLocation of the image
     */
    @SuppressWarnings("unused")
    private static ResourceLocation loadCustomSkin(Minecraft minecraft, String dragonStage) {
        GameProfile gameProfile = minecraft.getSession().getProfile();
        try {
            ResourceLocation skinLocation;
            String name = gameProfile.getName();
            String uuid = gameProfile.getId().toString();
            NativeImage customTexture = null;
            File fileNameA = new File(minecraft.gameDir, name + "_" + dragonStage + ".png");
            File fileNameB = new File(minecraft.gameDir, uuid + "_" + dragonStage + ".png");
            if (fileNameA.exists())
                customTexture = NativeImage.read(new FileInputStream(fileNameA));
            else if (fileNameB.exists()) {
                customTexture = NativeImage.read(new FileInputStream(fileNameB));
            }
            if (customTexture != null) {
                minecraft.getTextureManager().loadTexture(skinLocation = new ResourceLocation(DragonSurvivalMod.MODID, dragonStage), new DynamicTexture(customTexture));
                return skinLocation;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
