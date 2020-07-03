package by.jackraidenph.dragonsurvival.gui;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.network.MessageSyncCapability;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.math.Vec3d;
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
        this.addButton(new ExtendedButton(0, 0, 200, 20, "CAVE",
                $ -> {
                    if (Minecraft.getInstance().player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).isPresent())
                        if (!Minecraft.getInstance().player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).orElse(null).getIsDragon()) {
                            Vec3d placeHolder = new Vec3d(0, 0, 0);
                            DragonSurvivalMod.INSTANCE.sendToServer(new MessageSyncCapability(DragonType.CAVE, 0, 0, 0, 0, 0, placeHolder, placeHolder));
                            System.out.println("not stonks");
                        } else
                            System.out.println("stonks");
                }
        ));
        this.addButton(new ExtendedButton(0, 20, 200, 20, "FOREST",
                $ -> {
                    if (Minecraft.getInstance().player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).isPresent())
                        if (!Minecraft.getInstance().player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).orElse(null).getIsDragon()) {
                            Vec3d placeHolder = new Vec3d(0, 0, 0);
                            DragonSurvivalMod.INSTANCE.sendToServer(new MessageSyncCapability(DragonType.FOREST, 0, 0, 0, 0, 0, placeHolder, placeHolder));
                            System.out.println("not stonks");
                        } else
                            System.out.println("stonks");
                }
        ));
        this.addButton(new ExtendedButton(0, 40, 200, 20, "SEA",
                $ -> {
                    if (Minecraft.getInstance().player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).isPresent())
                        if (!Minecraft.getInstance().player.getCapability(PlayerStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).orElse(null).getIsDragon()) {
                            Vec3d placeHolder = new Vec3d(0, 0, 0);
                            DragonSurvivalMod.INSTANCE.sendToServer(new MessageSyncCapability(DragonType.SEA, 0, 0, 0, 0, 0, placeHolder, placeHolder));
                            System.out.println("not stonks");
                        } else
                            System.out.println("stonks");
                }
        ));
    }
}
