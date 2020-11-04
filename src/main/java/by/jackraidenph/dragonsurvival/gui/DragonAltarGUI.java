package by.jackraidenph.dragonsurvival.gui;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.network.ChooseRandomRespawnPosition;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapability;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.util.DragonType;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraftforge.fml.client.gui.widget.ExtendedButton;

import java.util.HashMap;
import java.util.Random;

public class DragonAltarGUI extends Screen {
    private static final ResourceLocation BACKGROUND_TEXTURE = new ResourceLocation(DragonSurvivalMod.MODID, "textures/gui/dragon_altar.png");
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

        this.renderBackground();
        super.render(mouseX, mouseY, partialTicks);

        this.minecraft.getTextureManager().bindTexture(BACKGROUND_TEXTURE);

        blit(startX, startY, 0, 0, 434 / 2, 496 / 2, xSize, ySize);


        if (mouseY > startY + 6 && mouseY < startY + 153) {
            if (mouseX > startX + 5 && mouseX < startX + 55) {
                blit(startX + 6, startY + 6, 217, 0, 49, 149, xSize, ySize);
            }

            if (mouseX > startX + 57 && mouseX < startX + 107) {
                blit(startX + 58, startY + 6, 266, 0, 49, 149, xSize, ySize);
            }

            if (mouseX > startX + 109 && mouseX < startX + 159) {
                blit(startX + 110, startY + 6, 315, 0, 49, 149, xSize, ySize);
            }

            if (mouseX > startX + 161 && mouseX < startX + 211) {
                blit(startX + 162, startY + 6, 364, 0, 49, 149, xSize, ySize);
            }
            if(mouseX>startX+5 && mouseX<startX+211) {
                fill(startX + 8, startY + 166, guiLeft + 210, guiTop + 242, 0xff333333);
                String warning =TextFormatting.RED + new TranslationTextComponent( "ds.dragon_altar_warning1").getString()+ TextFormatting.RESET + new TranslationTextComponent("ds.dragon_altar_warning2").getString();
                font.drawSplitString(warning, startX + 10, startY + 153 + 20, 200, 0xffffff);
            }
        }
    }

    @Override
    protected void init() {
        super.init();

        this.guiLeft = (this.width - this.xSize / 2) / 2;
        this.guiTop = (this.height - this.ySize) / 2;

        this.addButton(new ExtendedButton(this.guiLeft + 6, this.guiTop + 6, 49, 147, "CAVE",
                $ -> {
                    initiateDragonForm(DragonType.CAVE);
                    minecraft.player.sendMessage(new TranslationTextComponent("ds.cave_dragon_choice"));
                })
        );

        this.addButton(new ExtendedButton(this.guiLeft + 58, this.guiTop + 6, 49, 147, "FOREST",
                $ -> {
                    initiateDragonForm(DragonType.FOREST);
                    minecraft.player.sendMessage(new TranslationTextComponent("ds.forest_dragon_choice"));
                })

        );

        this.addButton(new ExtendedButton(this.guiLeft + 110, this.guiTop + 6, 49, 147, "SEA",
                $ -> {
                    initiateDragonForm(DragonType.SEA);
                    minecraft.player.sendMessage(new TranslationTextComponent("ds.sea_dragon_choice"));
                })

        );

        addButton(new ExtendedButton(guiLeft + 162, guiTop + 6, 49, 147, "Human", b -> {
            PlayerStateProvider.getCap(minecraft.player).ifPresent(playerStateHandler -> {
                playerStateHandler.setIsDragon(false);
                playerStateHandler.setIsHiding(false);
                DragonSurvivalMod.INSTANCE.sendToServer(new PacketSyncCapability(false, false, DragonType.NONE, 0));
                minecraft.player.sendMessage(new TranslationTextComponent("ds.choice_human"));
            });
        }));
    }

    private void initiateDragonForm(DragonType type) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (player == null)
            return;
        player.closeScreen();
        PlayerStateProvider.getCap(player)
                .ifPresent(cap -> {
                    Vec3d placeHolder = new Vec3d(0, 0, 0);
                    DragonSurvivalMod.INSTANCE.sendToServer(new PacketSyncCapability(true, true, type, 0));
                    DragonSurvivalMod.INSTANCE.sendToServer(new PacketSyncCapabilityMovement(0, 0, 0, placeHolder, placeHolder));
                    cap.setIsDragon(true);
                    cap.setType(type);
                    cap.setLevel(0);

                    Random random = player.world.rand;
                    BlockPos.Mutable pos = new BlockPos.Mutable(random.nextInt(2000) - 1000, player.getPosY(), random.nextInt(2000) - 1000);
                    player.world.getChunkProvider().getChunk(pos.getX() >> 4, pos.getZ() >> 4, ChunkStatus.FULL, true);

                    int y = 200;
                    pos.setY(y);
                    //TODO get solid position
                    while (player.world.isAirBlock(pos)) {
                        pos.setY(pos.getY() - 1);
                        if (pos.getY() < 10)
                            break;
                    }

                    System.out.println(pos);
                    player.setPosition(pos.getX(), pos.getY(), pos.getZ());
                    DragonSurvivalMod.INSTANCE.sendToServer(new ChooseRandomRespawnPosition(pos));
                });
    }
}
