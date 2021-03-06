package by.jackraidenph.dragonsurvival.nest;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class NestScreen extends ContainerScreen<NestContainer> {
    static final ResourceLocation BACKGROUND = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/nest_ui.png");
    static final ResourceLocation CAVE_NEST0 = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/cave_nest_0.png");
    static final ResourceLocation CAVE_NEST1 = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/cave_nest_1.png");
    static final ResourceLocation FOREST_NEST0 = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/forest_nest_0.png");
    static final ResourceLocation FOREST_NEST1 = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/forest_nest_1.png");
    static final ResourceLocation SEA_NEST0 = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/sea_nest_0.png");
    static final ResourceLocation SEA_NEST1 = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/sea_nest_1.png");
    private NestEntity nestEntity;

    public NestScreen(NestContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        nestEntity = screenContainer.nestEntity;
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
    }
}
