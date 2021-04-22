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
import net.minecraft.client.renderer.Quaternion;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.ClickType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.util.ResourceLocation;
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
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        minecraft.getTextureManager().bindTexture(BACKGROUND);
        int i = guiLeft;
        int j = guiTop;
        this.blit(i, j, 0, 0, xSize, ySize);
        //x, y, u, v, width, height
        blit(i, j - 26, 0, 198, 28, 40);
        if (isPointInRegion(28, -26, 28, 40, mouseX, mouseY))
            blit(i + 28, j - 26, 28, 198, 30, 40);
        else
            blit(i + 28, j - 26, 57, 198, 30, 40);
        if (isPointInRegion(57, -26, 30, 40, mouseX, mouseY))
            blit(i + 57, j - 26, 86, 198, 30, 40);
        else
            blit(i + 57, j - 26, 115, 198, 30, 40);
        if (isPointInRegion(86, -25, 30, 40, mouseX, mouseY))
            blit(i + 86, j - 26, 144, 198, 30, 40);
        else
            blit(i + 86, j - 26, 173, 198, 30, 40);
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
        quaternion.multiply(quaternion1);
        matrixstack.rotate(quaternion);
        float renderYawOffset = livingEntity.renderYawOffset;
        float rotationYaw = livingEntity.rotationYaw;
        float rotationPitch = livingEntity.rotationPitch;
        float prevRotationYawHead = livingEntity.prevRotationYawHead;
        float rotationYawHead = livingEntity.rotationYawHead;
        livingEntity.renderYawOffset = 180.0F + atanX * 20.0F;
        livingEntity.rotationYaw = atanX * 40.0F;
        livingEntity.rotationPitch = -atanY * 20.0F;
        livingEntity.rotationYawHead = livingEntity.rotationYaw;
        livingEntity.prevRotationYawHead = livingEntity.rotationYaw;
        EntityRendererManager entityrenderermanager = Minecraft.getInstance().getRenderManager();
        quaternion1.conjugate();
        entityrenderermanager.setCameraOrientation(quaternion1);
        entityrenderermanager.setRenderShadow(false);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = Minecraft.getInstance().getRenderTypeBuffers().getBufferSource();
        matrixstack.rotate(Vector3f.YN.rotationDegrees(180));
        entityrenderermanager.renderEntityStatic(livingEntity, 0.0D, 0.0D, 0.0D, 0, 1.0F, matrixstack, irendertypebuffer$impl, 15728880);
        irendertypebuffer$impl.finish();
        entityrenderermanager.setRenderShadow(true);
        livingEntity.renderYawOffset = renderYawOffset;
        livingEntity.rotationYaw = rotationYaw;
        livingEntity.rotationPitch = rotationPitch;
        livingEntity.prevRotationYawHead = prevRotationYawHead;
        livingEntity.rotationYawHead = rotationYawHead;
        RenderSystem.popMatrix();
    }

    @Override
    public void recipesUpdated() {
        recipeBookGui.recipesUpdated();
    }

    @Override
    public RecipeBookGui getRecipeGui() {
        return recipeBookGui;
    }

    @Override
    protected boolean isPointInRegion(int x, int y, int width, int height, double mouseX, double mouseY) {
        return (!widthTooNarrow || !recipeBookGui.isVisible()) && super.isPointInRegion(x, y, width, height, mouseX, mouseY);
    }

    @Override
    protected void init() {
        xSize = 204;
        ySize = 166;
        super.init();
        widthTooNarrow = width < 379;
        this.recipeBookGui.init(this.width, this.height, this.minecraft, this.widthTooNarrow, this.container);
        this.removeRecipeBookGui = true;
        int offSet = 30;
        this.guiLeft = this.recipeBookGui.updateScreenPosition(this.widthTooNarrow, this.width + offSet, this.xSize);
        this.children.add(this.recipeBookGui);
    }

    public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
        this.renderBackground();
        this.hasActivePotionEffects = !this.recipeBookGui.isVisible();
        if (this.recipeBookGui.isVisible() && this.widthTooNarrow) {
            this.drawGuiContainerBackgroundLayer(p_render_3_, p_render_1_, p_render_2_);
            this.recipeBookGui.render(p_render_1_, p_render_2_, p_render_3_);
        } else {
            this.recipeBookGui.render(p_render_1_, p_render_2_, p_render_3_);
            super.render(p_render_1_, p_render_2_, p_render_3_);
            this.recipeBookGui.renderGhostRecipe(this.guiLeft, this.guiTop, false, p_render_3_);
        }

        this.renderHoveredToolTip(p_render_1_, p_render_2_);
        this.recipeBookGui.renderTooltip(this.guiLeft, this.guiTop, p_render_1_, p_render_2_);
        this.oldMouseX = (float) p_render_1_;
        this.oldMouseY = (float) p_render_2_;
        this.func_212932_b(this.recipeBookGui);
    }

    public boolean mouseClicked(double p_mouseClicked_1_, double p_mouseClicked_3_, int p_mouseClicked_5_) {
        if (this.recipeBookGui.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_)) {
            return true;
        } else {
            return (!this.widthTooNarrow || !this.recipeBookGui.isVisible()) && super.mouseClicked(p_mouseClicked_1_, p_mouseClicked_3_, p_mouseClicked_5_);
        }
    }

    public boolean mouseReleased(double p_mouseReleased_1_, double p_mouseReleased_3_, int p_mouseReleased_5_) {
        if (this.buttonClicked) {
            this.buttonClicked = false;
            return true;
        } else {
            return super.mouseReleased(p_mouseReleased_1_, p_mouseReleased_3_, p_mouseReleased_5_);
        }
    }

    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        boolean flag = mouseX < (double) guiLeftIn || mouseY < (double) guiTopIn || mouseX >= (double) (guiLeftIn + this.xSize) || mouseY >= (double) (guiTopIn + this.ySize);
        return this.recipeBookGui.func_195604_a(mouseX, mouseY, this.guiLeft, this.guiTop, this.xSize, this.ySize, mouseButton) && flag;
    }

    /**
     * Called when the mouse is clicked over a slot or outside the gui.
     */
    protected void handleMouseClick(Slot slotIn, int slotId, int mouseButton, ClickType type) {
        super.handleMouseClick(slotIn, slotId, mouseButton, type);
        this.recipeBookGui.slotClicked(slotIn);
    }

    public void removed() {
        if (this.removeRecipeBookGui) {
            this.recipeBookGui.removed();
        }

        super.removed();
    }


}
