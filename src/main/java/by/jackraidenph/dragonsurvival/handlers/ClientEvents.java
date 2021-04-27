package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.gecko.DragonEntity;
import by.jackraidenph.dragonsurvival.gecko.DragonModel;
import by.jackraidenph.dragonsurvival.network.OpenDragonInventory;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import com.google.common.collect.HashMultimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Vector3f;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import software.bernie.geckolib3.core.processor.IBone;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

@Mod.EventBusSubscriber(value = Dist.CLIENT)
@SuppressWarnings("unused")
public class ClientEvents {

    public static float bodyYaw;
    public static DragonModel dragonModel;
    static boolean showingInventory;
    static HashMap<UUID, Boolean> warnings = new HashMap<>();
    static HashMap<String, Boolean> warningsForName = new HashMap<>();
    /**
     * Default skins
     */
    static HashMultimap<String, ResourceLocation> skinCache = HashMultimap.create(1, 3);
    /**
     * Skins by name
     */
    static HashMultimap<String, ResourceLocation> skinCacheForName = HashMultimap.create(1, 3);
    /**
     * Instance used for rendering first-person dragon model
     */
    public static AtomicReference<DragonEntity> dummyDragon2;
    /**
     * Instances used for rendering third-person dragon models
     */
    public static ConcurrentHashMap<Integer, AtomicReference<DragonEntity>> playerDragonHashMap = new ConcurrentHashMap<>(20);
    public static ConcurrentHashMap<Integer, Boolean> dragonsFlying = new ConcurrentHashMap<>(20);
    /**
     * States of digging/breaking blocks
     */
    public static ConcurrentHashMap<Integer, Boolean> dragonsDigging = new ConcurrentHashMap<>(20);
    /**
     * Durations of jumps
     */
    public static ConcurrentHashMap<Integer, Integer> dragonsJumpingTicks = new ConcurrentHashMap<>(20);

    @SubscribeEvent
    public static void decreaseJumpDuration(TickEvent.PlayerTickEvent playerTickEvent) {
        if (playerTickEvent.phase == TickEvent.Phase.END) {
            PlayerEntity playerEntity = playerTickEvent.player;
            dragonsJumpingTicks.computeIfPresent(playerEntity.getEntityId(), (playerEntity1, integer) -> integer > 0 ? integer - 1 : integer);
        }
    }

    public static boolean isRenderingFirstPerson;

    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent renderHandEvent) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (dummyDragon2 == null) {
            dummyDragon2 = new AtomicReference<>(EntityTypesInit.dragonEntity.create(player.world));
            dummyDragon2.get().player = player.getEntityId();
        }
        isRenderingFirstPerson = true;
        DragonStateProvider.getCap(player).ifPresent(playerStateHandler -> {
            if (playerStateHandler.isDragon()) {
                if (renderHandEvent.getItemStack().isEmpty())
                    renderHandEvent.setCanceled(true);
                MatrixStack eventMatrixStack = renderHandEvent.getMatrixStack();
                try {
                    eventMatrixStack.push();
                    float partialTicks = renderHandEvent.getPartialTicks();
                    float playerYaw = player.getYaw(partialTicks);
                    float playerPitch = player.getPitch(partialTicks);
                    ResourceLocation texture = getSkin(player, playerStateHandler, playerStateHandler.getLevel());
                    eventMatrixStack.rotate(Vector3f.XP.rotationDegrees(player.rotationPitch));
                    eventMatrixStack.rotate(Vector3f.YP.rotationDegrees(180));
                    eventMatrixStack.rotate(Vector3f.YP.rotationDegrees(player.rotationYaw));
                    eventMatrixStack.rotate(Vector3f.YP.rotationDegrees(-bodyYaw));
                    eventMatrixStack.translate(0, -2, -1);
                    IRenderTypeBuffer buffers = renderHandEvent.getBuffers();
//                int packedOverlay = LivingRenderer.getPackedOverlay(player, 0);
                    int light = renderHandEvent.getLight();

                    EntityRenderer<? super DragonEntity> dragonRenderer = Minecraft.getInstance().getRenderManager().getRenderer(dummyDragon2.get());
                    dummyDragon2.get().copyLocationAndAnglesFrom(player);
                    dragonModel.setCurrentTexture(texture);
                    final IBone neckandHead = dragonModel.getAnimationProcessor().getBone("NeckandHead");
                    if (neckandHead != null)
                        neckandHead.setHidden(true);
                    final IBone leftwing = dragonModel.getAnimationProcessor().getBone("Leftwing");
                    final IBone rightWing = dragonModel.getAnimationProcessor().getBone("Leftwing2");
                    if (leftwing != null)
                        leftwing.setHidden(!playerStateHandler.hasWings());
                    if (rightWing != null)
                        rightWing.setHidden(!playerStateHandler.hasWings());
                    dragonRenderer.render(dummyDragon2.get(), playerYaw, partialTicks, eventMatrixStack, buffers, light);

                    eventMatrixStack.scale(1.02f, 1.02f, 1.02f);
                    ResourceLocation chestplate = new ResourceLocation(DragonSurvivalMod.MODID, constructArmorTexture(player, EquipmentSlotType.CHEST));
                    dragonModel.setCurrentTexture(chestplate);
                    dragonRenderer.render(dummyDragon2.get(), playerYaw, partialTicks, eventMatrixStack, buffers, light);
                    ResourceLocation legs = new ResourceLocation(DragonSurvivalMod.MODID, constructArmorTexture(player, EquipmentSlotType.LEGS));
                    dragonModel.setCurrentTexture(legs);
                    dragonRenderer.render(dummyDragon2.get(), playerYaw, partialTicks, eventMatrixStack, buffers, light);
                    ResourceLocation boots = new ResourceLocation(DragonSurvivalMod.MODID, constructArmorTexture(player, EquipmentSlotType.FEET));
                    dragonModel.setCurrentTexture(boots);
                    dragonRenderer.render(dummyDragon2.get(), playerYaw, partialTicks, eventMatrixStack, buffers, light);
                    eventMatrixStack.translate(0, 0, 0.15);
                } catch (Throwable ignored) {
                    if (!(ignored instanceof NullPointerException))
                        ignored.printStackTrace();
                } finally {
                    eventMatrixStack.pop();
                }
            }
        });
        isRenderingFirstPerson = false;
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

    public static boolean isRenderingThirdPerson;
    /**
     * Called for every player.
     */
    @SubscribeEvent
    public static void thirdPersonPreRender(RenderPlayerEvent.Pre renderPlayerEvent) {

        PlayerEntity player = renderPlayerEvent.getPlayer();
        if (!playerDragonHashMap.containsKey(player.getEntityId())) {
            DragonEntity dummyDragon = EntityTypesInit.dragonEntity.create(player.world);
            dummyDragon.player = player.getEntityId();
            playerDragonHashMap.put(player.getEntityId(), new AtomicReference<>(dummyDragon));
        }
        isRenderingThirdPerson = true;
        DragonStateProvider.getCap(player).ifPresent(cap -> {
            if (cap.isDragon()) {
                renderPlayerEvent.setCanceled(true);
                float partialRenderTick = renderPlayerEvent.getPartialRenderTick();
                float limbSwingAmount = MathHelper.lerp(partialRenderTick, player.prevLimbSwingAmount, player.limbSwingAmount);
                float yaw = player.getYaw(partialRenderTick);
                float pitch = player.getPitch(partialRenderTick);
                DragonLevel dragonStage = cap.getLevel();
                ResourceLocation texture = getSkin(player, cap, dragonStage);
                MatrixStack matrixStack = renderPlayerEvent.getMatrixStack();

                try {
                    matrixStack.push();
                    matrixStack.rotate(Vector3f.YP.rotationDegrees((float) -cap.getMovementData().bodyYaw));
                    float maxHealth = player.getMaxHealth();
                    float scale = Math.max(maxHealth / 40, DragonLevel.BABY.maxWidth);
                    matrixStack.scale(scale, scale, scale);
                    int eventLight = renderPlayerEvent.getLight();

                    DragonEntity dummyDragon = playerDragonHashMap.get(player.getEntityId()).get();
                    EntityRenderer<? super DragonEntity> dragonRenderer = Minecraft.getInstance().getRenderManager().getRenderer(dummyDragon);
                    dummyDragon.copyLocationAndAnglesFrom(player);
                    dragonModel.setCurrentTexture(texture);
                    final IBone leftwing = dragonModel.getAnimationProcessor().getBone("Leftwing");
                    final IBone rightWing = dragonModel.getAnimationProcessor().getBone("Leftwing2");
                    if (leftwing != null)
                        leftwing.setHidden(!cap.hasWings());
                    if (rightWing != null)
                        rightWing.setHidden(!cap.hasWings());
                    IBone neckHead = dragonModel.getAnimationProcessor().getBone("NeckandHead");
                    if (neckHead != null)
                        neckHead.setHidden(false);
                    final IRenderTypeBuffer renderTypeBuffer = renderPlayerEvent.getBuffers();
                    if (player.isCrouching()) {
                        switch (dragonStage) {
                            case ADULT:
                                matrixStack.translate(0, 0.2, 0);
                                break;
                            case YOUNG:
                                matrixStack.translate(0, 0.25, 0);
                                break;
                            case BABY:
                                matrixStack.translate(0, 0.3, 0);
                        }
                    }
                    dragonRenderer.render(dummyDragon, yaw, partialRenderTick, matrixStack, renderTypeBuffer, eventLight);

                    String helmetTexture = constructArmorTexture(player, EquipmentSlotType.HEAD);
                    String chestPlateTexture = constructArmorTexture(player, EquipmentSlotType.CHEST);
                    String legsTexture = constructArmorTexture(player, EquipmentSlotType.LEGS);
                    String bootsTexture = constructArmorTexture(player, EquipmentSlotType.FEET);

                    //scale to try to prevent texture fighting (problem is in consecutive renders)
                    matrixStack.scale(1.08f, 1.02f, 1.02f);
                    dragonModel.setCurrentTexture(new ResourceLocation(DragonSurvivalMod.MODID, helmetTexture));
                    dragonRenderer.render(dummyDragon, yaw, partialRenderTick, matrixStack, renderTypeBuffer, eventLight);
                    dragonModel.setCurrentTexture(new ResourceLocation(DragonSurvivalMod.MODID, chestPlateTexture));
                    dragonRenderer.render(dummyDragon, yaw, partialRenderTick, matrixStack, renderTypeBuffer, eventLight);
                    dragonModel.setCurrentTexture(new ResourceLocation(DragonSurvivalMod.MODID, legsTexture));
                    dragonRenderer.render(dummyDragon, yaw, partialRenderTick, matrixStack, renderTypeBuffer, eventLight);
                    dragonModel.setCurrentTexture(new ResourceLocation(DragonSurvivalMod.MODID, bootsTexture));
                    dragonRenderer.render(dummyDragon, yaw, partialRenderTick, matrixStack, renderTypeBuffer, eventLight);

                    ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
                    ItemStack right = player.getHeldItemMainhand();
                    matrixStack.rotate(Vector3f.XP.rotationDegrees(45.0F));
                    matrixStack.translate(-0.45f, 1, 0);
                    final int combinedOverlayIn = LivingRenderer.getPackedOverlay(player, 0);
                    itemRenderer.renderItem(right, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, eventLight, combinedOverlayIn, matrixStack, renderTypeBuffer);
                    ItemStack left = player.getHeldItemOffhand();
                    matrixStack.translate(0.9, 0, 0);
                    itemRenderer.renderItem(left, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, eventLight, combinedOverlayIn, matrixStack, renderTypeBuffer);
                } catch (Throwable throwable) {
                    if (!(throwable instanceof NullPointerException))
                        throwable.printStackTrace();
                    matrixStack.pop();
                } finally {
                    matrixStack.pop();
                }
            }
        });
        isRenderingThirdPerson = false;
    }

    private static ResourceLocation getSkin(PlayerEntity player, DragonStateHandler cap, DragonLevel dragonStage) {
        ResourceLocation texture;
        UUID playerUniqueID = player.getUniqueID();
        final String playerName = player.getGameProfile().getName();

        Optional<ResourceLocation> skinForName = skinCacheForName.get(playerName).stream().filter(location -> Boolean.parseBoolean(location.toString().endsWith(playerName.toLowerCase()+"_"+dragonStage.name)+"")).findFirst();
        if (skinForName.isPresent()) {
            return skinForName.get();
        } else {
            Optional<ResourceLocation> defSkin = skinCache.get(playerName).stream().filter(location -> location.toString().endsWith(cap.getType().toString().toLowerCase(Locale.ROOT) + "_" + dragonStage.name + ".png")).findFirst();
            if (defSkin.isPresent()) {
                return defSkin.get();
            }

            try {
                texture = ClientModEvents.loadCustomSkinForName(player, dragonStage);
                skinCacheForName.put(playerName, texture);
            } catch (IOException e) {
                if (warningsForName.get(playerName) == null) {
                    DragonSurvivalMod.LOGGER.info("Custom skin for user {} doesn't exist", playerName);
                    warningsForName.put(playerName, true);
                }
            } finally {
                texture = constructTexture(cap.getType(), dragonStage);
                skinCache.put(playerName, texture);
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

    static ResourceLocation HUDTextures = new ResourceLocation(DragonSurvivalMod.MODID, "textures/gui/dragon_hud.png");
}
