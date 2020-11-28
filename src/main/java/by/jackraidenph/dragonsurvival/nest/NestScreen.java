package by.jackraidenph.dragonsurvival.nest;

import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

public class NestScreen extends ContainerScreen<NestContainer> {
    public NestScreen(NestContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);

    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
        renderBackground();
        drawCenteredString(font, "Health: " + container.nestEntity.health, width / 2, height / 2, 0xffffff);
    }

    @Override
    protected void init() {
        super.init();
    }
}
