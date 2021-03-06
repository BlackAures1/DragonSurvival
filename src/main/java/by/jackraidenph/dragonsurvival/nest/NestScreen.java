package by.jackraidenph.dragonsurvival.nest;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class NestScreen extends ContainerScreen<NestContainer> {
    static final ResourceLocation BACKGROUND = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/nest_ui.png");
    static final ResourceLocation CAVE_NEST0 = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/cave_nest_0.png");
    static final ResourceLocation CAVE_NEST1 = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/cave_nest_1.png");
    static final ResourceLocation FOREST_NEST0 = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/forest_nest_0.png");
    static final ResourceLocation FOREST_NEST1 = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/forest_nest_1.png");
    static final ResourceLocation SEA_NEST0 = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/sea_nest_0.png");
    static final ResourceLocation SEA_NEST1 = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/sea_nest_1.png");
    private final NestEntity nestEntity;

    private final PlayerEntity playerEntity;
    public NestScreen(NestContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        nestEntity = screenContainer.nestEntity;
        playerEntity = inv.player;
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        renderBackground();
        TextureManager textureManager = minecraft.getTextureManager();
        textureManager.bindTexture(BACKGROUND);
        blit(guiLeft, guiTop, 0, 0, xSize, ySize);
        switch (nestEntity.type) {
            case CAVE:
                textureManager.bindTexture(CAVE_NEST0);
                break;
            case FOREST:
                textureManager.bindTexture(FOREST_NEST0);
                break;
            case SEA:
                textureManager.bindTexture(SEA_NEST0);
                break;
        }
        blit(guiLeft + 8, guiTop + 8, 0, 0, 160, 49, 160, 49);
    }

    @Override
    protected void init() {
        super.init();
        //buttons are transparent
        ExtendedButton sleep = addButton(new ExtendedButton(guiLeft + 8, guiTop + 60, 33, 18, "", p_onPress_1_ -> {
        }) {
            @Override
            public void renderButton(int mouseX, int mouseY, float partial) {
            }

            @Override
            public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
                playerEntity.closeScreen();
                DragonSurvivalMod.CHANNEL.sendToServer(new SleepInNest(nestEntity.getPos()));
                return true;
            }
        });
        ExtendedButton regenerate = addButton(new ExtendedButton(sleep.x + sleep.getWidth() + 4, guiTop + 60, 33, 18, "", p_onPress_1_ -> {
        }) {
            @Override
            public void renderButton(int mouseX, int mouseY, float partial) {

            }

            @Override
            public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
                nestEntity.regenerationMode = !nestEntity.regenerationMode;
                DragonSurvivalMod.CHANNEL.sendToServer(new ToggleRegeneration(nestEntity.getPos(), nestEntity.regenerationMode));
                if (nestEntity.regenerationMode)
                    playerEntity.sendMessage(new TranslationTextComponent("ds.regen.on"));
                else
                    playerEntity.sendMessage(new TranslationTextComponent("ds.regen.off"));
                return true;
            }
        });
        ExtendedButton carry = addButton(new ExtendedButton(regenerate.x + regenerate.getWidth() + 4, guiTop + 60, 33, 18, "", p_onPress_1_ -> {
        }) {
            @Override
            public void renderButton(int mouseX, int mouseY, float partial) {
            }

            @Override
            public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
                playerEntity.closeScreen();
                DragonSurvivalMod.CHANNEL.sendToServer(new DismantleNest(nestEntity.getPos()));
                return true;
            }
        });
    }
}
