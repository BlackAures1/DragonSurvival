package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.models.DragonModel2;
import by.jackraidenph.dragonsurvival.network.OpenDragonInventory;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.GameSettings;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.model.PlayerModel;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.*;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvents {

    public static float bodyYaw;
    public static float neckYaw;
    static boolean showingInventory;

    public static DragonModel2 thirdPersonModel = new DragonModel2(false);
    public static DragonModel2 firstPersonModel = new DragonModel2(true);
    public static DragonModel2 thirdPersonArmor = new DragonModel2(false);
    public static DragonModel2 firstPersonArmor = new DragonModel2(true);

    static {
        firstPersonModel.Head.showModel = false;
        firstPersonModel.Neckand_1.showModel = false;
        firstPersonModel.NeckandMain.showModel = false;

        firstPersonArmor.NeckandMain.showModel = false;
        firstPersonArmor.NeckandHead.showModel = false;
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent renderHandEvent) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        PlayerStateProvider.getCap(player).ifPresent(playerStateHandler -> {
            if (playerStateHandler.isDragon()) {
                if (renderHandEvent.getItemStack().isEmpty())
                    renderHandEvent.setCanceled(true);
                MatrixStack eventMatrixStack = renderHandEvent.getMatrixStack();
                eventMatrixStack.push();
                float partialTicks = renderHandEvent.getPartialTicks();
                float playerYaw = player.getYaw(partialTicks);
                float playerPitch = player.getPitch(partialTicks);
                firstPersonModel.setRotationAngles(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, playerYaw, playerPitch);
                ResourceLocation texture = constructTexture(playerStateHandler.getType(), playerStateHandler.getLevel());
                eventMatrixStack.rotate(Vector3f.XP.rotationDegrees(player.rotationPitch));
                eventMatrixStack.rotate(Vector3f.YP.rotationDegrees(180));
                eventMatrixStack.rotate(Vector3f.YP.rotationDegrees(player.rotationYaw));
                eventMatrixStack.rotate(Vector3f.YP.rotationDegrees(-bodyYaw));
                eventMatrixStack.translate(0, -2, -1);
                IRenderTypeBuffer buffers = renderHandEvent.getBuffers();
                IVertexBuilder buffer = buffers.getBuffer(RenderType.getEntityTranslucentCull(texture));
                int packedOverlay = LivingRenderer.getPackedOverlay(player, 0);
                int light = renderHandEvent.getLight();
                firstPersonModel.render(eventMatrixStack, buffer, light, packedOverlay, partialTicks, playerYaw, playerPitch, 1);

                firstPersonModel.copyModelAttributesTo(firstPersonArmor);
                firstPersonArmor.setRotationAngles(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, playerYaw, playerPitch);
                setArmorVisibility(firstPersonArmor, player);

//                eventMatrixStack.scale(1.4f, 1.4f, 1.4f);
                ResourceLocation chestplate = new ResourceLocation(DragonSurvivalMod.MODID, constructArmorTexture(player, EquipmentSlotType.CHEST));
                firstPersonArmor.render(eventMatrixStack, buffers.getBuffer(RenderType.getEntityTranslucentCull(chestplate)), light, packedOverlay, partialTicks, playerYaw, playerPitch, 1);
                ResourceLocation legs = new ResourceLocation(DragonSurvivalMod.MODID, constructArmorTexture(player, EquipmentSlotType.LEGS));
                firstPersonArmor.render(eventMatrixStack, buffers.getBuffer(RenderType.getEntityTranslucentCull(legs)), light, packedOverlay, partialTicks, playerYaw, playerPitch, 1);
                ResourceLocation boots = new ResourceLocation(DragonSurvivalMod.MODID, constructArmorTexture(player, EquipmentSlotType.FEET));
                firstPersonArmor.render(eventMatrixStack, buffers.getBuffer(RenderType.getEntityTranslucentCull(boots)), light, packedOverlay, partialTicks, playerYaw, playerPitch, 1);
                eventMatrixStack.translate(0, 0, 0.15);
                eventMatrixStack.pop();

            }
        });
    }

    @SubscribeEvent
    public static void onOpenScreen(GuiOpenEvent openEvent) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (openEvent.getGui() instanceof InventoryScreen && !player.isCreative() && DragonSurvivalMod.isDragon(player)) {
            openEvent.setCanceled(true);
            showingInventory = false;
        }

    }

    /**
     * The event stops being fired if jump key is pressed during movement
     */
    @SubscribeEvent
    public static void onKey(InputEvent.KeyInputEvent keyInputEvent) {
        Minecraft minecraft = Minecraft.getInstance();
        GameSettings gameSettings = minecraft.gameSettings;
        InputMappings.Input input = InputMappings.getInputByCode(keyInputEvent.getKey(), keyInputEvent.getScanCode());
        if (DragonSurvivalMod.isDragon(minecraft.player) && !minecraft.player.isCreative() && gameSettings.keyBindInventory.isActiveAndMatches(input) && !showingInventory) {
            DragonSurvivalMod.INSTANCE.sendToServer(new OpenDragonInventory());
            showingInventory = true;
        }
    }

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.START) {
            Minecraft minecraft = Minecraft.getInstance();
            ClientPlayerEntity player = minecraft.player;
            if (player != null) {
                float bodyAndHeadYawDiff = bodyYaw - player.rotationYawHead;
                if (minecraft.gameSettings.thirdPersonView == 0) {
                    if (Math.abs(bodyAndHeadYawDiff) > 170) {
                        bodyYaw -= Math.signum(bodyAndHeadYawDiff) * 2;
                    }
                }
                if ((player.getMotion().x != 0 && player.getMotion().z != 0)) {
                    bodyYaw = player.rotationYaw;
                    neckYaw = -player.rotationYawHead;
                }


            }
        }
    }

    @SubscribeEvent
    public static void onRender(RenderLivingEvent.Pre<PlayerEntity, PlayerModel<PlayerEntity>> e) {
        if (e.getEntity() instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) e.getEntity();
            PlayerStateProvider.getCap(player).ifPresent(cap -> {
                if (cap.isDragon()) {
                    e.setCanceled(true);

                    float partialRenderTick = e.getPartialRenderTick();
                    thirdPersonModel.setRotationAngles(player, player.limbSwing,
                            MathHelper.lerp(partialRenderTick, player.prevLimbSwingAmount, player.limbSwingAmount),
                            player.ticksExisted, player.getYaw(partialRenderTick), player.getPitch(partialRenderTick));

                    ResourceLocation texture = constructTexture(cap.getType(), cap.getLevel());
                    MatrixStack matrixStack = e.getMatrixStack();
                    matrixStack.push();
                    //don't rotate if viewing a screen
                    if (Minecraft.getInstance().currentScreen == null)
                        matrixStack.rotate(Vector3f.YP.rotationDegrees(-ClientEvents.bodyYaw));
                    thirdPersonModel.render(
                            matrixStack,
                            e.getBuffers().getBuffer(RenderType.getEntityTranslucentCull(texture)),
                            e.getLight(), LivingRenderer.getPackedOverlay(player, 0.0f),
                            partialRenderTick, player.getYaw(partialRenderTick), player.getPitch(partialRenderTick), 1.0f);

                    thirdPersonModel.copyModelAttributesTo(thirdPersonArmor);
                    thirdPersonArmor.setRotationAngles(player, player.limbSwing, MathHelper.lerp(partialRenderTick, player.prevLimbSwingAmount, player.limbSwingAmount), player.ticksExisted, player.getYaw(partialRenderTick), player.getPitch(partialRenderTick));

                    setArmorVisibility(thirdPersonArmor, player);
                    String helmetTexture = constructArmorTexture(player, EquipmentSlotType.HEAD);
                    thirdPersonArmor.render(matrixStack, e.getBuffers().getBuffer(RenderType.getEntityTranslucentCull(new ResourceLocation(DragonSurvivalMod.MODID, helmetTexture))), e.getLight(), LivingRenderer.getPackedOverlay(player, 0.0f), partialRenderTick, player.getYaw(partialRenderTick), player.getPitch(partialRenderTick), 1.0f);
                    String chestPlateTexture = constructArmorTexture(player, EquipmentSlotType.CHEST);
                    thirdPersonArmor.render(matrixStack, e.getBuffers().getBuffer(RenderType.getEntityTranslucentCull(new ResourceLocation(DragonSurvivalMod.MODID, chestPlateTexture))), e.getLight(), LivingRenderer.getPackedOverlay(player, 0.0f), partialRenderTick, player.getYaw(partialRenderTick), player.getPitch(partialRenderTick), 1.0f);
                    String legsTexture = constructArmorTexture(player, EquipmentSlotType.LEGS);
                    thirdPersonArmor.render(matrixStack, e.getBuffers().getBuffer(RenderType.getEntityTranslucentCull(new ResourceLocation(DragonSurvivalMod.MODID, legsTexture))), e.getLight(), LivingRenderer.getPackedOverlay(player, 0.0f), partialRenderTick, player.getYaw(partialRenderTick), player.getPitch(partialRenderTick), 1.0f);
                    String bootsTexture = constructArmorTexture(player, EquipmentSlotType.FEET);
                    thirdPersonArmor.render(matrixStack, e.getBuffers().getBuffer(RenderType.getEntityTranslucentCull(new ResourceLocation(DragonSurvivalMod.MODID, bootsTexture))), e.getLight(), LivingRenderer.getPackedOverlay(player, 0.0f), partialRenderTick, player.getYaw(partialRenderTick), player.getPitch(partialRenderTick), 1.0f);
                    matrixStack.pop();
                }
            });
        }
    }

    private static ResourceLocation constructTexture(DragonType dragonType, DragonLevel stage) {
        if (ClientModEvents.customSkinPresence) {
            switch (stage) {
                case BABY:
                    return ClientModEvents.customNewbornSkin;
                case YOUNG:
                    return ClientModEvents.customYoungSkin;
                case ADULT:
                    return ClientModEvents.customAdultSkin;
            }
        } else {
            String texture;
            texture = "textures/dragon/";
            switch (dragonType) {
                case SEA:
                    texture += "sea";
                    break;
                case CAVE:
                    texture += "cave";
                    break;
                case FOREST:
                    texture += "forest";
                    break;
            }

            switch (stage) {
                case BABY:
                    texture += "_newborn";
                    break;
                case YOUNG:
                    texture += "_young";
                    break;
                case ADULT:
                    texture += "_adult";
                    break;
            }
            texture += ".png";

            return new ResourceLocation(DragonSurvivalMod.MODID, texture);
        }
        return null;
    }

    private static String constructArmorTexture(PlayerEntity playerEntity, EquipmentSlotType equipmentSlot) {
        String texture = "textures/armor/";
        Item item = playerEntity.getItemStackFromSlot(equipmentSlot).getItem();
        if (item instanceof ArmorItem) {
            ArmorItem armorItem = (ArmorItem) item;
            IArmorMaterial armorMaterial = armorItem.getArmorMaterial();
            if (armorMaterial == ArmorMaterial.DIAMOND) {
                texture += "diamond_";
            } else if (armorMaterial == ArmorMaterial.IRON) {
                texture += "iron_";
            } else if (armorMaterial == ArmorMaterial.LEATHER) {
                texture += "leather_";
            } else if (armorMaterial == ArmorMaterial.GOLD) {
                texture += "gold_";
            } else if (armorMaterial == ArmorMaterial.CHAIN) {
                texture += "chainmail_";
            }
            texture += "dragon_";
            switch (equipmentSlot) {
                case HEAD:
                    texture += "helmet";
                    break;
                case CHEST:
                    texture += "chestplate";
                    break;
                case LEGS:
                    texture += "leggings";
                    break;
                case FEET:
                    texture += "boots";
                    break;
            }
            texture += ".png";
            return texture;
        }

        return texture + "empty_armor.png";
    }

    private static void setArmorVisibility(DragonModel2 dragonModel2, PlayerEntity player) {
        dragonModel2.Head.showModel = player.getItemStackFromSlot(EquipmentSlotType.HEAD).getItem() instanceof ArmorItem;
        dragonModel2.main_body.showModel = player.getItemStackFromSlot(EquipmentSlotType.CHEST).getItem() instanceof ArmorItem;

        dragonModel2.Elbow1.showModel = player.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() instanceof ArmorItem;
        dragonModel2.Elbow2.showModel = player.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() instanceof ArmorItem;
        dragonModel2.Elbow3.showModel = player.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() instanceof ArmorItem;
        dragonModel2.Elbow4.showModel = player.getItemStackFromSlot(EquipmentSlotType.FEET).getItem() instanceof ArmorItem;
    }

    static ResourceLocation HUDTextures = new ResourceLocation(DragonSurvivalMod.MODID, "textures/gui/dragon_hud.png");

    /**
     * Render nest health
     */
    @SubscribeEvent
    public static void onHud(RenderGameOverlayEvent.Post renderGameOverlayEvent) {
        ClientPlayerEntity clientPlayerEntity = Minecraft.getInstance().player;
        MainWindow mainWindow = renderGameOverlayEvent.getWindow();
        float partialTicks = renderGameOverlayEvent.getPartialTicks();
//        if (DragonSurvivalMod.playerIsDragon(clientPlayerEntity)) {
//            if (renderGameOverlayEvent.getType() == RenderGameOverlayEvent.ElementType.TEXT) {
//                Minecraft minecraft = Minecraft.getInstance();
//                Screen screen = minecraft.currentScreen;
//                RenderSystem.pushMatrix();
//                minecraft.getTextureManager().bindTexture(HUDTextures);
//                //heart background
//                Functions.blit(screen, 20, mainWindow.getScaledHeight() - 80, 78, 627, 76, 72, 700, 700);
//                minecraft.getTextureManager().bindTexture(HUDTextures);
//                //health heart
//                Functions.blit(screen, 20, mainWindow.getScaledHeight() - 80 + 72, 154, 627 + 72, 76, (int) (-(72) * (clientPlayerEntity.getHealth() / clientPlayerEntity.getMaxHealth())), 700, 700);
//                //health int
//                minecraft.fontRenderer.drawString("" + (int) clientPlayerEntity.getHealth(), 20 + 32, mainWindow.getScaledHeight() - 50, 0xffffff);
//                RenderSystem.popMatrix();
//            }
//        }
    }

//    @SubscribeEvent
//    public static void onMouseClickInScreen(GuiScreenEvent.MouseClickedEvent.Pre pre)
//    {
//        Screen screen=pre.getGui();
//        if(screen instanceof ContainerScreen)
//        {
//            ContainerScreen<?> containerScreen= (ContainerScreen<?>) screen;
//            Container container= containerScreen.getContainer();
//            double mouseX= pre.getMouseX();
//            double mouseY= pre.getMouseY();
//            for (Slot inventorySlot : container.inventorySlots) {
//                int slotX=inventorySlot.xPos+ containerScreen.getGuiLeft();
//                int slotY=inventorySlot.yPos+containerScreen.getGuiTop();
//                if (slotX<mouseX && slotX+16>mouseX && slotY<mouseY && slotY+16>mouseY)
//                {
//                    ItemStack itemStack=inventorySlot.getStack();
//                    Item item=itemStack.getItem();
//                    if(item instanceof ToolItem && inventorySlot.getSlotIndex()<10)
//                    {
//                        Minecraft.getInstance().player.sendMessage(new StringTextComponent("A dragon can't use "+itemStack.getDisplayName().getString()));
//                        pre.setCanceled(true);
//                    }
//                    System.out.println(inventorySlot.getSlotIndex());
//                    break;
//                }
//
//            }
//        }
//    }
}
