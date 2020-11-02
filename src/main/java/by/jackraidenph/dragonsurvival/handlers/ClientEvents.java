package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.containers.DragonInventoryContainer;
import by.jackraidenph.dragonsurvival.gui.DragonInventoryScreen;
import by.jackraidenph.dragonsurvival.models.DragonModel;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvents {

    public static DragonModel<PlayerEntity> bodyAndLimbs = new DragonModel<>();
    public static DragonModel<PlayerEntity> neckAndHead = new DragonModel<>();

    public static float bodyYaw;
    public static float neckYaw;

    public static DragonModel<PlayerEntity> dragonModel = new DragonModel<>();

    static {
        neckAndHead.renderBody = false;
        bodyAndLimbs.renderHead = false;
        neckAndHead.Head.showModel = false;
    }

    @SubscribeEvent
    public static void openPlayerInventory(GuiOpenEvent guiOpenEvent) {
        Screen screen = guiOpenEvent.getGui();
        PlayerEntity playerEntity = Minecraft.getInstance().player;
        if (screen instanceof InventoryScreen && playerEntity.container instanceof DragonInventoryContainer) {
            guiOpenEvent.setGui(new DragonInventoryScreen(Minecraft.getInstance().player));
        }
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent renderHandEvent) {
        ClientPlayerEntity player = Minecraft.getInstance().player;

        PlayerStateProvider.getCap(player).ifPresent(playerStateHandler -> {
            if (playerStateHandler.getIsDragon()) {
                if (renderHandEvent.getItemStack().isEmpty())
                    renderHandEvent.setCanceled(true);
                MatrixStack matrixStack = renderHandEvent.getMatrixStack();
                matrixStack.push();
                bodyAndLimbs.player = player;
                neckAndHead.player = player;
                float partialTicks = renderHandEvent.getPartialTicks();
                float playerYaw = player.getYaw(partialTicks);
                float playerPitch = player.getPitch(partialTicks);
                bodyAndLimbs.setRotationAngles(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, playerYaw, playerPitch);
                neckAndHead.setRotationAngles(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, playerYaw, playerPitch);
                String texture = "textures/dragon/" + playerStateHandler.getType().toString().toLowerCase() + ".png";
                matrixStack.rotate(Vector3f.XP.rotationDegrees(player.rotationPitch));
                matrixStack.rotate(Vector3f.YP.rotationDegrees(180));
                matrixStack.rotate(Vector3f.YP.rotationDegrees(player.rotationYaw));
                matrixStack.rotate(Vector3f.YP.rotationDegrees(-bodyYaw));
                matrixStack.translate(0, -2, -1);
                ResourceLocation resourceLocation = new ResourceLocation(DragonSurvivalMod.MODID, texture);
                IVertexBuilder buffer = renderHandEvent.getBuffers().getBuffer(RenderType.getEntityTranslucentCull(resourceLocation));
                int packedOverlay = LivingRenderer.getPackedOverlay(player, 0);
                int light = renderHandEvent.getLight();
                bodyAndLimbs.render(renderHandEvent.getMatrixStack(), buffer, light, packedOverlay, partialTicks, playerYaw, playerPitch, 1);
                matrixStack.translate(0, 0, 0.15);
                neckAndHead.render(renderHandEvent.getMatrixStack(), buffer, light, packedOverlay, partialTicks, playerYaw, playerPitch, 1);
                matrixStack.pop();
            }
        });
    }

    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent keyInputEvent) {
        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player != null && (minecraft.gameSettings.keyBindForward.isPressed() || minecraft.gameSettings.keyBindLeft.isPressed() || minecraft.gameSettings.keyBindRight.isPressed() || minecraft.gameSettings.keyBindBack.isPressed())) {
            bodyYaw = minecraft.player.rotationYaw;
            neckYaw = -minecraft.player.rotationYawHead;
        }
    }

    @SubscribeEvent
    public static void onRender(RenderLivingEvent.Pre<PlayerEntity, PlayerModel<PlayerEntity>> e) {
        if (e.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) e.getEntity();
            PlayerStateProvider.getCap(player).ifPresent(cap -> {
                if (cap.getIsDragon()) {
                    e.setCanceled(true);

                    ClientEvents.dragonModel.setRotationAngles(
                            player,
                            player.limbSwing,
                            MathHelper.lerp(e.getPartialRenderTick(), player.prevLimbSwingAmount, player.limbSwingAmount),
                            player.ticksExisted,
                            player.getYaw(e.getPartialRenderTick()),
                            player.getPitch(e.getPartialRenderTick()));
                    int level = cap.getLevel();
                    String texture = "textures/dragon/" + cap.getType().toString().toLowerCase() + ".png";
                    e.getMatrixStack().rotate(Vector3f.YP.rotationDegrees(-ClientEvents.bodyYaw));
                    ClientEvents.dragonModel.render(
                            e.getMatrixStack(),
                            e.getBuffers().getBuffer(RenderType.getEntityTranslucentCull(new ResourceLocation(DragonSurvivalMod.MODID, texture))),
                            e.getLight(),
                            LivingRenderer.getPackedOverlay(player, 0.0f),
                            e.getPartialRenderTick(),
                            player.getYaw(e.getPartialRenderTick()),
                            player.getPitch(e.getPartialRenderTick()),
                            1.0f);
                }
            });
        }
    }
}
