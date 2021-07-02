package by.jackraidenph.dragonsurvival.gui;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.containers.DragonContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

public class DragonScreen extends DisplayEffectsScreen<DragonContainer> implements IRecipeShownListener {
    private final RecipeBookGui recipeBookGui = new RecipeBookGui();
    static final ResourceLocation BACKGROUND = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/dragon_inventory.png");
    private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");
    private boolean widthTooNarrow;
    /**
     * The old x position of the mouse pointer
     */
    private float oldMouseX;
    /**
     * The old y position of the mouse pointer
     */
    private float oldMouseY;
    private boolean removeRecipeBookGui;
    private boolean buttonClicked;

    public DragonScreen(DragonContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        

        passEvents = true;
    }

    @Override
    public void tick() {
        recipeBookGui.tick();
    }
    
    @Override
    protected void renderLabels(MatrixStack matrixStack, int mouseX, int mouseY) {};
    
    
    @Override
    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bind(BACKGROUND);
        int i = leftPos;
        int j = topPos;
        this.blit(matrixStack, i, j, 0, 0, imageWidth, imageHeight);
        //x, y, u, v, width, height
        blit(matrixStack, i, j - 26, 0, 198, 28, 40);
        if (isHovering(28, -26, 28, 40, mouseX, mouseY))
            blit(matrixStack, i + 28, j - 26, 28, 198, 30, 40);
        else
            blit(matrixStack, i + 28, j - 26, 57, 198, 30, 40);
        if (isHovering(57, -26, 30, 40, mouseX, mouseY))
            blit(matrixStack, i + 57, j - 26, 86, 198, 30, 40);
        else
            blit(matrixStack, i + 57, j - 26, 115, 198, 30, 40);
        if (isHovering(86, -25, 30, 40, mouseX, mouseY))
            blit(matrixStack, i + 86, j - 26, 144, 198, 30, 40);
        else
            blit(matrixStack, i + 86, j - 26, 173, 198, 30, 40);
        drawEntityOnScreen(i + 51, j + 75, 30, (float) (i + 51) - this.oldMouseX, (float) (j + 75 - 50) - this.oldMouseY, this.minecraft.player);
    }

    private static void drawEntityOnScreen(int posX, int posY, int scale, float mouseX, float mouseY, LivingEntity livingEntity) {
        float atanX = (float) Math.atan(mouseX / 40.0F);
        float atanY = (float) Math.atan(mouseY / 40.0F);
        RenderSystem.pushMatrix();
        RenderSystem.translatef((float) posX, (float) posY, 1050.0F);
        RenderSystem.scalef(1.0F, 1.0F, -1.0F);
        MatrixStack matrixstack = new MatrixStack();
        matrixstack.translate(10.0D, -5.0D, 1000.0D);
        matrixstack.scale((float) scale, (float) scale, (float) scale);
        Quaternion quaternion = Vector3f.ZP.rotationDegrees(180.0F);
        Quaternion quaternion1 = Vector3f.XP.rotationDegrees(atanY * 20.0F);
        quaternion.mul(quaternion1);
        matrixstack.mulPose(quaternion);
        float renderYawOffset = livingEntity.yBodyRot;
        float rotationYaw = livingEntity.yRot;
        float rotationPitch = livingEntity.xRot;
        float prevRotationYawHead = livingEntity.yHeadRotO;
        float rotationYawHead = livingEntity.yHeadRot;
        livingEntity.yBodyRot = 180.0F + atanX * 20.0F;
        livingEntity.yRot = atanX * 40.0F;
        livingEntity.xRot = -atanY * 20.0F;
        livingEntity.yHeadRot = livingEntity.yRot;
        livingEntity.yHeadRotO = livingEntity.yRot;
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getEntityRenderDispatcher();
        quaternion1.conj();
        entityrenderermanager.overrideCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().renderBuffers().bufferSource();
        matrixstack.mulPose(Vector3f.YN.rotationDegrees(180));
        entityrenderermanager.render(livingEntity, 0.0D, 0.0D, 0.0D, 0, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        irendertypebuffer$impl.endBatch();
        entityrenderermanager.setRenderShadow(true);
        livingEntity.yBodyRot = renderYawOffset;
        livingEntity.yRot = rotationYaw;
        livingEntity.xRot = rotationPitch;
        livingEntity.yHeadRotO = prevRotationYawHead;
        livingEntity.yHeadRot = rotationYawHead;
        RenderSystem.popMatrix();
    }

    @Override
    public void recipesUpdated() {
        recipeBookGui.recipesUpdated();
    }

    @Override
    public RecipeBookGui getRecipeBookComponent() {
        return recipeBookGui;
    }

    @Override
    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        return (!widthTooNarrow || !recipeBookGui.isVisible()) && super.isHovering(x, y, width, height, mouseX, mouseY);
    }

    @Override
    protected void init() {
    	imageWidth = 204;
    	imageHeight = 166;
        super.init();
        widthTooNarrow = width < 379;
        this.recipeBookGui.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.menu);
        this.removeRecipeBookGui = true;
        int offSet = 30;
        this.leftPos = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width + offSet, this.imageWidth);
        this.children.add(this.recipeBookGui);
        this.setInitialFocus(this.recipeBookGui);
        /*this.addButton(new ImageButton(leftPos + this.imageWidth - offSet, topPos + imageHeight - 26, 20, 18, 0, 0, 19, RECIPE_BUTTON_TEXTURE, (p_214086_1_) -> {
            this.recipeBookGui.initVisuals(this.widthTooNarrow);
            this.recipeBookGui.toggleVisibility();
            this.leftPos = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width + offSet, this.imageWidth);
            ((ImageButton) p_214086_1_).setPosition(this.leftPos + this.imageWidth - offSet, topPos + imageHeight - 26);
            this.buttonClicked = true;
        }));*/
    }

    @Override
    public void render(MatrixStack matrixStack, int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground(matrixStack);
        this.doRenderEffects = !this.recipeBookGui.isVisible();
        if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
            this.renderBg(matrixStack, p_render_3_, p_render_1_, p_render_2_);
            this.recipeBookGui.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
        } else {
            this.recipeBookGui.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
            super.render(matrixStack, p_render_1_, p_render_2_, p_render_3_);
            this.recipeBookGui.renderGhostRecipe(matrixStack, this.leftPos, this.topPos, false, p_render_3_);
        }

        this.renderTooltip(matrixStack, p_render_1_, p_render_2_);
        this.recipeBookGui.renderTooltip(matrixStack, this.leftPos, this.topPos, p_render_1_, p_render_2_);
        this.oldMouseX = (float) p_render_1_;
        this.oldMouseY = (float) p_render_2_;
        this.magicalSpecialHackyFocus(this.recipeBookGui);
    }

    @Override
    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        if (this.recipeBookGui.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_)) {
            return true;
        } else {
            return (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
        }
    }

    @Override
    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        if (this.buttonClicked) {
            this.buttonClicked = false;
            return true;
        } else {
            return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
        }
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        boolean flag = mouseX < (double) guiLeftIn || mouseY < (double) guiTopIn || mouseX >= (double) (guiLeftIn + this.imageWidth) || mouseY >= (double) (guiTopIn + this.imageHeight);
        return this.recipeBookGui.hasClickedOutside(mouseX, mouseY, this.leftPos, this.topPos, this.imageWidth, this.imageHeight, mouseButton) && flag;
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    @Override
    protected void slotClicked(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        super.slotClicked(slotIn, slotId, mouseButton, type);
        this.recipeBookGui.slotClicked(slotIn);
    }

    @Override
    public void removed() {
        if (this.removeRecipeBookGui) {
            this.recipeBookGui.removed();
        }

        super.removed();
    }


}
