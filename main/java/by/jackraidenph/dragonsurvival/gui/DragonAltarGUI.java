package by.jackraidenph.dragonsurvival.gui;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.network.MessageSyncCapability;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

public class DragonAltarGUI extends Screen {

    public DragonAltarGUI(ITextComponent p_i51108_1_) {
        super(p_i51108_1_);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    protected void init() {
        super.init();
        this.addButton(new ExtendedButton(0, 0, 200, 20, "test",
                $ -> {
                    if (!Minecraft.getInstance().player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).orElse(null).getIsDragon()) {
                        DragonSurvivalMod.INSTANCE.sendToServer(new MessageSyncCapability(true, "cave", 0));
                        System.out.println("not stonks");
                    } else
                        System.out.println("stonks");
                }
        ));

    }
}
