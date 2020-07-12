package by.jackraidenph.dragonsurvival.gui;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapability;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
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
                $ -> PlayerStateProvider.getCap(Minecraft.getInstance().player)
                        .filter(cap -> !cap.getIsDragon())
                        .ifPresent(cap -> {
                            Vec3d placeHolder = new Vec3d(0, 0, 0);
                            DragonSurvivalMod.INSTANCE.sendToServer(new PacketSyncCapability(true, DragonType.CAVE, 0));
                            DragonSurvivalMod.INSTANCE.sendToServer(new PacketSyncCapabilityMovement(0, 0, 0, placeHolder, placeHolder));
                            cap.setIsDragon(true);
                            cap.setType(DragonType.CAVE);
                            cap.setLevel(0);
                        })

        ));
        this.addButton(new ExtendedButton(0, 20, 200, 20, "FOREST",
                $ -> PlayerStateProvider.getCap(Minecraft.getInstance().player)
                        .filter(cap -> !cap.getIsDragon())
                        .ifPresent(cap -> {
                            Vec3d placeHolder = new Vec3d(0, 0, 0);
                            DragonSurvivalMod.INSTANCE.sendToServer(new PacketSyncCapability(true, DragonType.FOREST, 0));
                            DragonSurvivalMod.INSTANCE.sendToServer(new PacketSyncCapabilityMovement(0, 0, 0, placeHolder, placeHolder));
                            cap.setIsDragon(true);
                            cap.setType(DragonType.FOREST);
                            cap.setLevel(0);
                        })
        ));
        this.addButton(new ExtendedButton(0, 40, 200, 20, "SEA",
                $ -> PlayerStateProvider.getCap(Minecraft.getInstance().player)
                        .filter(cap -> !cap.getIsDragon())
                        .ifPresent(cap -> {
                            Vec3d placeHolder = new Vec3d(0, 0, 0);
                            DragonSurvivalMod.INSTANCE.sendToServer(new PacketSyncCapability(true, DragonType.SEA, 0));
                            DragonSurvivalMod.INSTANCE.sendToServer(new PacketSyncCapabilityMovement(0, 0, 0, placeHolder, placeHolder));
                            cap.setIsDragon(true);
                            cap.setType(DragonType.SEA);
                            cap.setLevel(0);
                        })
        ));
    }
}
