package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.gecko.DragonEntity;
import by.jackraidenph.dragonsurvival.models.DragonModel2;
import by.jackraidenph.dragonsurvival.models.Wings;
import by.jackraidenph.dragonsurvival.network.OpenDragonInventory;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import com.google.common.collect.HashMultimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.GameSettings;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.LivingRenderer;
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
import software.bernie.geckolib3.core.processor.IBone;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class ClientEvents {

    public static float bodyYaw;
    static boolean showingInventory;
    static HashMap<UUID, Boolean> warnings = new HashMap<>();
    static HashMap<String, Boolean> warningsForName = new HashMap<>();
    static HashMultimap<UUID, ResourceLocation> skinCache = HashMultimap.create(1, 3);
    static HashMultimap<String, ResourceLocation> skinCacheForName = HashMultimap.create(1, 3);
    public static DragonModel2 thirdPersonModel = new DragonModel2(false);
    public static DragonModel2 firstPersonModel = new DragonModel2(true);
    public static DragonModel2 thirdPersonArmor = new DragonModel2(false);
    public static DragonModel2 firstPersonArmor = new DragonModel2(true);
    static EntityRendererManager entityRendererManager;
    public static Wings wings = new Wings();
    /**
     * Instance used for rendering dragon model
     */
    static DragonEntity dummyDragon;
    static DragonEntity dummyDragonForArmor;

    static {
        firstPersonModel.Head.showModel = false;
        firstPersonModel.Neckand_1.showModel = false;
        firstPersonModel.NeckandMain.showModel = false;

        firstPersonArmor.NeckandMain.showModel = false;
        firstPersonArmor.NeckandHead.showModel = false;
        entityRendererManager = Minecraft.getInstance().getRenderManager();
    }

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent renderHandEvent) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (dummyDragon == null) {
            dummyDragon = EntityTypesInit.dragonEntity.create(player.world);
            dummyDragonForArmor = EntityTypesInit.dragonEntity.create(player.world);
        }
        assert dummyDragon != null;
        DragonStateProvider.getCap(player).ifPresent(playerStateHandler -> {
            if (playerStateHandler.isDragon()) {
                if (renderHandEvent.getItemStack().isEmpty())
                    renderHandEvent.setCanceled(true);
                MatrixStack eventMatrixStack = renderHandEvent.getMatrixStack();
                eventMatrixStack.push();
                float partialTicks = renderHandEvent.getPartialTicks();
                float playerYaw = player.getYaw(partialTicks);
                float playerPitch = player.getPitch(partialTicks);
                firstPersonModel.setRotationAngles(player, player.limbSwing, player.limbSwingAmount, player.ticksExisted, playerYaw, playerPitch);
                ResourceLocation texture = getSkin(player, playerStateHandler, playerStateHandler.getLevel());
                eventMatrixStack.rotate(Vector3f.XP.rotationDegrees(player.rotationPitch));
                eventMatrixStack.rotate(Vector3f.YP.rotationDegrees(180));
                eventMatrixStack.rotate(Vector3f.YP.rotationDegrees(player.rotationYaw));
                eventMatrixStack.rotate(Vector3f.YP.rotationDegrees(-bodyYaw));
                eventMatrixStack.translate(0, -2, -1);
                IRenderTypeBuffer buffers = renderHandEvent.getBuffers();
                int packedOverlay = LivingRenderer.getPackedOverlay(player, 0);
                int light = renderHandEvent.getLight();

                EntityRenderer<? super DragonEntity> dragonRenderer = Minecraft.getInstance().getRenderManager().getRenderer(dummyDragon);
                dummyDragon.copyLocationAndAnglesFrom(player);
                ClientModEvents.dragonModel.setCurrentTexture(texture);
                dragonRenderer.render(dummyDragon, playerYaw, partialTicks, eventMatrixStack, buffers, light);

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
        if (openEvent.getGui() instanceof InventoryScreen && !player.isCreative() && DragonStateProvider.isDragon(player)) {
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
        if (minecraft.currentScreen == null && DragonStateProvider.isDragon(minecraft.player) && !minecraft.player.isCreative() && gameSettings.keyBindInventory.isActiveAndMatches(input) && !showingInventory) {
            DragonSurvivalMod.CHANNEL.sendToServer(new OpenDragonInventory());
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

                if (player.getMotion().x != 0 || player.getMotion().z != 0) {
                    DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> dragonStateHandler.setMovementData(player.getYaw(1), player.rotationYawHead, player.rotationPitch));
                    //align body when moving
                    bodyYaw = player.rotationYawHead;
                }
            }
        }
    }

    /**
     * The player is always the local player
     */
    @SubscribeEvent
    public static void thirdPersonPreRender(RenderPlayerEvent.Pre renderPlayerEvent) {

        PlayerEntity player = renderPlayerEvent.getPlayer();
        DragonStateProvider.getCap(player).ifPresent(cap -> {
            if (cap.isDragon()) {
                renderPlayerEvent.setCanceled(true);

                float partialRenderTick = renderPlayerEvent.getPartialRenderTick();
                float limbSwingAmount = MathHelper.lerp(partialRenderTick, player.prevLimbSwingAmount, player.limbSwingAmount);
                float yaw = player.getYaw(partialRenderTick);
                float pitch = player.getPitch(partialRenderTick);
                thirdPersonModel.setRotationAngles(player, player.limbSwing, limbSwingAmount, player.ticksExisted, yaw, pitch);

                DragonLevel dragonStage = cap.getLevel();
                ResourceLocation texture = getSkin(player, cap, dragonStage);
                MatrixStack matrixStack = renderPlayerEvent.getMatrixStack();
                matrixStack.push();
                matrixStack.rotate(Vector3f.YP.rotationDegrees((float) -cap.getMovementData().bodyYaw));
                float maxHealth = player.getMaxHealth();
                float scale = Math.max(maxHealth / 40, DragonLevel.BABY.maxWidth);
                matrixStack.scale(scale, scale, scale);
                int eventLight = renderPlayerEvent.getLight();

                EntityRenderer<? super DragonEntity> dragonRenderer = Minecraft.getInstance().getRenderManager().getRenderer(dummyDragon);
                dummyDragon.copyLocationAndAnglesFrom(player);
                ClientModEvents.dragonModel.setCurrentTexture(texture);
                final IBone leftwing = ClientModEvents.dragonModel.getAnimationProcessor().getBone("Leftwing");
                final IBone rightWing = ClientModEvents.dragonModel.getAnimationProcessor().getBone("Leftwing2");
                leftwing.setHidden(!cap.hasWings());
                rightWing.setHidden(!cap.hasWings());
                dragonRenderer.render(dummyDragon, yaw, partialRenderTick, matrixStack, renderPlayerEvent.getBuffers(), eventLight);
                thirdPersonModel.copyModelAttributesTo(thirdPersonArmor);
                thirdPersonArmor.setRotationAngles(player, player.limbSwing, limbSwingAmount, player.ticksExisted, yaw, pitch);

                setArmorVisibility(thirdPersonArmor, player);
                String helmetTexture = constructArmorTexture(player, EquipmentSlotType.HEAD);

                thirdPersonArmor.render(matrixStack, renderPlayerEvent.getBuffers().getBuffer(RenderType.getEntityTranslucentCull(new ResourceLocation(DragonSurvivalMod.MODID, helmetTexture))), eventLight, LivingRenderer.getPackedOverlay(player, 0.0f), 0, 0, 0, 1.0f);
                String chestPlateTexture = constructArmorTexture(player, EquipmentSlotType.CHEST);
                thirdPersonArmor.render(matrixStack, renderPlayerEvent.getBuffers().getBuffer(RenderType.getEntityTranslucentCull(new ResourceLocation(DragonSurvivalMod.MODID, chestPlateTexture))), eventLight, LivingRenderer.getPackedOverlay(player, 0.0f), 0, 0, 0, 1.0f);
                String legsTexture = constructArmorTexture(player, EquipmentSlotType.LEGS);
                thirdPersonArmor.render(matrixStack, renderPlayerEvent.getBuffers().getBuffer(RenderType.getEntityTranslucentCull(new ResourceLocation(DragonSurvivalMod.MODID, legsTexture))), eventLight, LivingRenderer.getPackedOverlay(player, 0.0f), 0, 0, 0, 1.0f);
                String bootsTexture = constructArmorTexture(player, EquipmentSlotType.FEET);
                thirdPersonArmor.render(matrixStack, renderPlayerEvent.getBuffers().getBuffer(RenderType.getEntityTranslucentCull(new ResourceLocation(DragonSurvivalMod.MODID, bootsTexture))), eventLight, LivingRenderer.getPackedOverlay(player, 0.0f), 0, 0, 0, 1.0f);

//                if (cap.hasWings()) {
//                    thirdPersonModel.copyModelAttributesTo(wings);
//                    wings.setRotationAngles(player, player.limbSwing, limbSwingAmount, player.ticksExisted, yaw, pitch);
//                    String wingsTexture;
//                    switch (cap.getType()) {
//                        case SEA:
//                            wingsTexture = "wings_sea.png";
//                            break;
//                        case CAVE:
//                            wingsTexture = "wings_cave.png";
//                            break;
//                        case FOREST:
//                            wingsTexture = "wings_forest.png";
//                            break;
//                        default:
//                            wingsTexture = null;
//                    }
//                    if (wingsTexture != null)
//                        wings.render(matrixStack, renderPlayerEvent.getBuffers().getBuffer(RenderType.getEntityTranslucentCull(new ResourceLocation(DragonSurvivalMod.MODID, "textures/wings/" + wingsTexture))), eventLight, LivingRenderer.getPackedOverlay(player, 0), 0, 0, 0, 1.0f);
//                }
                matrixStack.pop();
            }
        });

    }

    private static ResourceLocation getSkin(PlayerEntity player, DragonStateHandler cap, DragonLevel dragonStage) {
        ResourceLocation texture;
        UUID playerUniqueID = player.getUniqueID();
        Optional<ResourceLocation> optionalResourceLocation = skinCache.get(playerUniqueID).stream().filter(location -> Boolean.parseBoolean(location.toString().endsWith(dragonStage.name) + "")).findFirst();
        if (optionalResourceLocation.isPresent()) {
            texture = optionalResourceLocation.get();
            return texture;
        } else {
            Optional<ResourceLocation> skinForName = skinCacheForName.get(player.getGameProfile().getName()).stream().filter(location -> Boolean.parseBoolean(location.toString().endsWith(dragonStage.name) + "")).findFirst();
            if (skinForName.isPresent()) {
                texture = skinForName.get();
                return texture;
            } else {
                Optional<ResourceLocation> defSkin = skinCache.get(playerUniqueID).stream().filter(location -> location.toString().endsWith(cap.getType().toString().toLowerCase(Locale.ROOT) + "_" + dragonStage.name + ".png")).findFirst();
                if (defSkin.isPresent()) {
                    texture = defSkin.get();
                    return texture;
                }
                try {
                    texture = ClientModEvents.loadCustomSkin(player, dragonStage);
                    skinCache.put(playerUniqueID, texture);
                } catch (IOException ioException) {
                    try {
                        texture = ClientModEvents.loadCustomSkinForName(player, dragonStage);
                        skinCacheForName.put(player.getGameProfile().getName(), texture);
                        if (warningsForName.get(player.getGameProfile().getName()) == null) {
                            DragonSurvivalMod.LOGGER.warn("No UUID-based skin for {}", player.getName().getString());
                            warningsForName.put(player.getGameProfile().getName(), true);
                        }
                    } catch (IOException e) {
                        if (warnings.get(playerUniqueID) == null) {
                            DragonSurvivalMod.LOGGER.info("Custom skin for user {} doesn't exist", playerUniqueID);
                            warnings.put(playerUniqueID, true);
                        }
                    } finally {
                        texture = constructTexture(cap.getType(), dragonStage);
                        skinCache.put(playerUniqueID, texture);
                    }
                }
            }
        }
        return texture;
    }

    private static ResourceLocation constructTexture(DragonType dragonType, DragonLevel stage) {

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
