package by.jackraidenph.dragonsurvival.nest;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.Functions;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
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
    static final ResourceLocation RED_HEART = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/red_heart.png");
    private final NestEntity nestEntity;

    private final PlayerEntity playerEntity;

    public NestScreen(NestContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        nestEntity = screenContainer.nestEntity;
        playerEntity = inv.player;
    }

    public void render(MatrixStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
        this.renderTooltip(matrixStack, p_render_1_, p_render_2_);
    }

    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {};
    
    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) { // WIP
        renderBackground(matrixStack);
        TextureManager textureManager = minecraft.getTextureManager();
        textureManager.bind(BACKGROUND);
        blit(matrixStack, leftPos, topPos, 0, 0, imageWidth, imageHeight);
        switch (nestEntity.type) {
        case CAVE:
            textureManager.bind(CAVE_NEST0);
            break;
        case FOREST:
            textureManager.bind(FOREST_NEST0);
            break;
        case SEA:
            textureManager.bind(SEA_NEST0);
            break;
        }
        blit(matrixStack, leftPos + 8, topPos + 8, 0, 0, 160, 49, 160, 49);
        switch (nestEntity.type) {
            case CAVE:
                textureManager.bind(CAVE_NEST1);
                break;
            case FOREST:
                textureManager.bind(FOREST_NEST1);
                break;
            case SEA:
                textureManager.bind(SEA_NEST1);
                break;
        }
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, (nestEntity.energy / 64f));
        blit(matrixStack, leftPos + 8, topPos + 8, 0, 0, 160, 49, 160, 49);
        textureManager.getTexture(RED_HEART);
        textureManager.bind(RED_HEART);
        int yv = (int) (35 * (nestEntity.energy / 64f));
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.25F + (nestEntity.energy / 64f) * 0.5F);
        blit(matrixStack, leftPos + 122, topPos + 12 + 35 - yv, 0, 35 - yv, 38, yv, 38, 35);
    }

    @Override
    protected void init() {
        super.init();
        //buttons are transparent
        ExtendedButton sleep = addButton(new ExtendedButton(leftPos + 8, topPos + 60, 33, 18, new StringTextComponent(""), p_onPress_1_ -> {
        }) {
            @Override
            public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partial) {
            	
            }

            @Override
            public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
                playerEntity.closeContainer();
                DragonSurvivalMod.CHANNEL.sendToServer(new SleepInNest(nestEntity.getBlockPos()));
                return true;
            }
        });
        ExtendedButton regenerate = addButton(new ExtendedButton(sleep.x + sleep.getWidth() + 4, topPos + 60, 33, 18, new StringTextComponent(""), p_onPress_1_ -> {
        }) {
            @Override
            public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partial) {

            }

            @Override
            public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
                nestEntity.regenerationMode = !nestEntity.regenerationMode;
                DragonSurvivalMod.CHANNEL.sendToServer(new ToggleRegeneration(nestEntity.getBlockPos(), nestEntity.regenerationMode));
                if (nestEntity.regenerationMode)
                    playerEntity.sendMessage(new TranslationTextComponent("ds.regen.on"), playerEntity.getUUID());
                else
                    playerEntity.sendMessage(new TranslationTextComponent("ds.regen.off"), playerEntity.getUUID());
                return true;
            }
        });
        addButton(new ExtendedButton(regenerate.x + regenerate.getWidth() + 4, topPos + 60, 33, 18, new StringTextComponent(""), p_onPress_1_ -> {
        }) {
            @Override
            public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partial) {
            }

            @Override
            public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
                playerEntity.closeContainer();
                DragonSurvivalMod.CHANNEL.sendToServer(new DismantleNest(nestEntity.getBlockPos()));
                return true;
            }
        });
    }
}
