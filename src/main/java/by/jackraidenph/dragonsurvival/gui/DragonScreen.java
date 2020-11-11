package by.jackraidenph.dragonsurvival.gui;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.containers.DragonContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.recipebook.RecipeBookGui;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class DragonScreen extends DisplayEffectsScreen<DragonContainer> implements IRecipeShownListener {
    private final RecipeBookGui recipeBookGui = new RecipeBookGui();
    static final ResourceLocation BACKGROUND = new ResourceLocation(DragonSurvivalMod.MODID, "textures/ui/dragon_inventory.png");
    private static final ResourceLocation RECIPE_BUTTON_TEXTURE = new ResourceLocation("textures/gui/recipe_button.png");
    private boolean widthTooNarrow;

    public DragonScreen(DragonContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
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
    }
}
