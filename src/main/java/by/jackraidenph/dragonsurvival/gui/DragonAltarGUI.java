package by.jackraidenph.dragonsurvival.gui;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.network.GiveNest;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.network.ResetPlayer;
import by.jackraidenph.dragonsurvival.network.SynchronizeDragonCap;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import java.util.HashMap;

public class DragonAltarGUI extends Screen {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(DragonSurvivalMod.MODID, "textures/gui/dragon_altar_texture.png");
    private final int xSize = 852 / 2;
    private final int ySize = 500 / 2;
    private int guiLeft;
    private int guiTop;
    private HashMap<Integer, Integer> wtf = Maps.newHashMap();

    public DragonAltarGUI(ITextComponent title) {
        super(title);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    @Override
    public void render(int mouseX, int mouseY, float partialTicks) {
        if (this.minecraft == null)
            return;
        this.renderBackground();
        int startX = this.guiLeft;
        int startY = this.guiTop;
        super.render(mouseX, mouseY, partialTicks);

        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);

        blit(startX, startY, 0, 0, 215, 158, 512, 512);


        if (mouseY > startY + 6 && mouseY < startY + 153) {
            if (mouseX > startX + 5 && mouseX < startX + 55) {
                blit(startX + 6, startY + 6, 217, 0, 49, 149, 512, 512);
            }

            if (mouseX > startX + 57 && mouseX < startX + 107) {
                blit(startX + 58, startY + 6, 266, 0, 49, 149, 512, 512);
            }

            if (mouseX > startX + 109 && mouseX < startX + 159) {
                blit(startX + 110, startY + 6, 315, 0, 49, 149, 512, 512);
            }

            if (mouseX > startX + 161 && mouseX < startX + 211) {
                blit(startX + 161, startY + 6, 364, 0, 49, 149, 512, 512);
            }
            //warning
//            if(mouseX>startX+5 && mouseX<startX+211) {
//                fill(startX + 8, startY + 166, guiLeft + 210, guiTop + 242, 0xff333333);
//                String warning =TextFormatting.RED + new TranslationTextComponent( "ds.dragon_altar_warning1").getString()+ TextFormatting.RESET + new TranslationTextComponent("ds.dragon_altar_warning2").getString();
//                font.drawSplitString(warning, startX + 10, startY + 153 + 20, 200, 0xffffff);
//            }
        }
    }

    @Override
    protected void init() {
        super.init();

        this.guiLeft = (this.width - this.xSize / 2) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        this.addButton(new ExtendedButton(this.guiLeft + 6, this.guiTop + 6, 49, 147, "CAVE",
                button -> {
                    initiateDragonForm(DragonType.CAVE);
                    minecraft.player.sendMessage(new TranslationTextComponent("ds.cave_dragon_choice"));
                })
        );

        this.addButton(new ExtendedButton(this.guiLeft + 58, this.guiTop + 6, 49, 147, "FOREST",
                button -> {
                    initiateDragonForm(DragonType.FOREST);
                    minecraft.player.sendMessage(new TranslationTextComponent("ds.forest_dragon_choice"));
                })

        );

        this.addButton(new ExtendedButton(this.guiLeft + 110, this.guiTop + 6, 49, 147, "SEA",
                button -> {
                    initiateDragonForm(DragonType.SEA);
                    minecraft.player.sendMessage(new TranslationTextComponent("ds.sea_dragon_choice"));
                })

        );

        addButton(new ExtendedButton(guiLeft + 162, guiTop + 6, 49, 147, "Human", b -> {
            DragonStateProvider.getCap(minecraft.player).ifPresent(playerStateHandler -> {
                playerStateHandler.setIsDragon(false);
                playerStateHandler.setIsHiding(false);
                playerStateHandler.setLevel(DragonLevel.BABY);
                playerStateHandler.setType(DragonType.NONE);
                playerStateHandler.setHasWings(false);
                DragonSurvivalMod.CHANNEL.sendToServer(new SynchronizeDragonCap(minecraft.player.getEntityId(), false, DragonType.NONE, DragonLevel.BABY, false, 20, false));
                DragonSurvivalMod.CHANNEL.sendToServer(new ResetPlayer());
                minecraft.player.closeScreen();
                minecraft.player.sendMessage(new TranslationTextComponent("ds.choice_human"));
            });
        }));
    }

    private void initiateDragonForm(DragonType type) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null)
            return;
        player.closeScreen();
        DragonStateProvider.getCap(player).ifPresent(cap -> {
            DragonSurvivalMod.CHANNEL.sendToServer(new SynchronizeDragonCap(player.getEntityId(), false, type, DragonLevel.BABY, true, DragonLevel.BABY.initialHealth, false));
            DragonSurvivalMod.CHANNEL.sendToServer(new GiveNest(type));
            player.world.playSound(player, player.getPosition(), SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.PLAYERS, 1, 0.7f);
            DragonSurvivalMod.CHANNEL.sendToServer(new PacketSyncCapabilityMovement(player.getEntityId(), 0, 0, 0));
            cap.setIsDragon(true);
            cap.setType(type);
            cap.setLevel(DragonLevel.BABY);
            cap.setHasWings(false);
            player.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(DragonLevel.BABY.initialHealth);
//                    Random random = player.world.rand;
//                    BlockPos.Mutable pos = new BlockPos.Mutable(random.nextInt(2000) - 1000, player.getPosY(), random.nextInt(2000) - 1000);
//                    DragonSurvivalMod.INSTANCE.sendToServer(new SetRespawnPosition(pos));
        });
    }
}
