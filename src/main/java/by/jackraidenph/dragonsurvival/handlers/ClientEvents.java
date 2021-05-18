package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.gecko.DragonEntity;
import by.jackraidenph.dragonsurvival.gecko.DragonModel;
import by.jackraidenph.dragonsurvival.network.OpenDragonInventory;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.renderer.CaveLavaFluidRenderer;
import by.jackraidenph.dragonsurvival.util.ConfigurationHandler;
import by.jackraidenph.dragonsurvival.util.DragonBodyMovementType;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import com.google.common.collect.HashMultimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.GameSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.FluidBlockRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.client.renderer.ViewFrustum;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.Effects;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.event.RenderBlockOverlayEvent.OverlayType;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.PacketDistributor.TargetPoint;
import software.bernie.geckolib3.core.processor.IBone;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicReference;

import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(Dist.CLIENT)
@SuppressWarnings("unused")
public class ClientEvents {

    public static DragonModel dragonModel;
    static boolean showingInventory;
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
            dragonsJumpingTicks.computeIfPresent(playerEntity.getId(), (playerEntity1, integer) -> integer > 0 ? integer - 1 : integer);
        }
    }

    @SubscribeEvent
    public static void onRenderOverlayPreTick(RenderGameOverlayEvent.Pre event) {
    	ClientPlayerEntity player = Minecraft.getInstance().player;
    	DragonStateProvider.getCap(player).ifPresent(playerStateHandler -> {
    		if (event.getType() == RenderGameOverlayEvent.ElementType.AIR && playerStateHandler.getType() == DragonType.SEA) {
    			event.setCanceled(true);
    			return;
    		}
    		if (event.getType() == RenderGameOverlayEvent.ElementType.AIR && player.getAirSupply() >= player.getMaxAirSupply() && playerStateHandler.getLavaAirSupply() < EventHandler.maxLavaAirSupply) {
    			GL11.glEnable(GL11.GL_BLEND);
    			
    			int right_height = 49; // 39

                final int left = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + 91;
                final int top = Minecraft.getInstance().getWindow().getGuiScaledHeight() - right_height;
                final int full = MathHelper.ceil((double) (playerStateHandler.getLavaAirSupply() - 2) * 10.0D / EventHandler.maxLavaAirSupply);
                final int partial = MathHelper.ceil((double) playerStateHandler.getLavaAirSupply() * 10.0D / EventHandler.maxLavaAirSupply) - full;

                for (int i = 0; i < full + partial; ++i)
                	Minecraft.getInstance().gui.blit(event.getMatrixStack(), left - i * 8 - 9, top, (i < full ? 16 : 25), 18, 9, 9);
                GL11.glDisable(GL11.GL_BLEND);
        	}
    	});
    }
    
    private static boolean wasCaveDragon = false;
    private static FluidBlockRenderer prevFluidRenderer;
    
    @SubscribeEvent
    public static void onRenderWorldLastEvent(RenderWorldLastEvent event) { // TODO: clean up time :)
    	ClientPlayerEntity player = Minecraft.getInstance().player;
    	DragonStateProvider.getCap(player).ifPresent(playerStateHandler -> {
    		if (playerStateHandler.getType() == DragonType.CAVE) {
    			if (!wasCaveDragon) {
    				RenderType lavaType = RenderType.translucent();
                    RenderTypeLookup.setRenderLayer(Fluids.LAVA, lavaType);
                    RenderTypeLookup.setRenderLayer(Fluids.FLOWING_LAVA, lavaType);
                    try {
                    	Field field = BlockRendererDispatcher.class.getDeclaredFields()[2];
                    	field.setAccessible(true);
                    	FluidBlockRenderer fluidRenderer = (FluidBlockRenderer)field.get(Minecraft.getInstance().getBlockRenderer());
                    	prevFluidRenderer = fluidRenderer;
                    	field.set(Minecraft.getInstance().getBlockRenderer(), new CaveLavaFluidRenderer());
                    } catch(Exception ex) {
                    	ex.printStackTrace();
                    }
                    Minecraft.getInstance().levelRenderer.allChanged();
    			}
    		}else {
    			if (wasCaveDragon) {
    				if (prevFluidRenderer != null) {
    					RenderType lavaType = RenderType.solid();
                        RenderTypeLookup.setRenderLayer(Fluids.LAVA, lavaType);
                        RenderTypeLookup.setRenderLayer(Fluids.FLOWING_LAVA, lavaType);
                        try {
                        	Field field = BlockRendererDispatcher.class.getDeclaredFields()[2];
                        	field.setAccessible(true);
                        	FluidBlockRenderer fluidRenderer = (FluidBlockRenderer)field.get(Minecraft.getInstance().getBlockRenderer());
                        	field.set(Minecraft.getInstance().getBlockRenderer(), prevFluidRenderer);
                        } catch(Exception ex) {
                        	ex.printStackTrace();
                        }
    				}
    				Minecraft.getInstance().levelRenderer.allChanged();
    			}
    		}
    		wasCaveDragon = playerStateHandler.getType() == DragonType.CAVE;
    	});
    }
    
    @SubscribeEvent
    public static void onRenderHand(RenderHandEvent renderHandEvent) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (dummyDragon2 == null) {
            dummyDragon2 = new AtomicReference<>(EntityTypesInit.dragonEntity.create(player.level));
            dummyDragon2.get().player = player.getId();
        }
        DragonStateProvider.getCap(player).ifPresent(playerStateHandler -> {
            if (playerStateHandler.isDragon()) {
                if (renderHandEvent.getItemStack().isEmpty())
                    renderHandEvent.setCanceled(true);
                MatrixStack eventMatrixStack = renderHandEvent.getMatrixStack();
                try {
	                eventMatrixStack.pushPose();
	                float partialTicks = renderHandEvent.getPartialTicks();
	                float playerYaw = player.getViewYRot(partialTicks);
	                float playerPitch = player.getViewXRot(partialTicks);
	                ResourceLocation texture = getSkin(player, playerStateHandler, playerStateHandler.getLevel());
	                eventMatrixStack.mulPose(Vector3f.XP.rotationDegrees(player.xRot));
	                eventMatrixStack.mulPose(Vector3f.YP.rotationDegrees(180));
	                eventMatrixStack.mulPose(Vector3f.YP.rotationDegrees(player.yRot));
	                eventMatrixStack.mulPose(Vector3f.YN.rotationDegrees((float)playerStateHandler.getMovementData().bodyYaw));
	                eventMatrixStack.translate(0, -2, -1);
	                IRenderTypeBuffer buffers = renderHandEvent.getBuffers();
	//                int packedOverlay = LivingRenderer.getPackedOverlay(player, 0);
	                int light = renderHandEvent.getLight();
	                
	                EntityRenderer<? super DragonEntity> dragonRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(dummyDragon2.get());
	                dummyDragon2.get().copyPosition(player);
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
	                if (!player.isInvisible())
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
                	eventMatrixStack.popPose();
                }

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
        GameSettings gameSettings = minecraft.options;
        InputMappings.Input input = InputMappings.getKey(keyInputEvent.getKey(), keyInputEvent.getScanCode());
        if (minecraft.screen == null && DragonStateProvider.isDragon(minecraft.player) && !minecraft.player.isCreative() && gameSettings.keyInventory.isActiveAndMatches(input) && !showingInventory) {
            DragonSurvivalMod.CHANNEL.sendToServer(new OpenDragonInventory());
            showingInventory = true;
        }
    }

    private static Vector3d getInputVector(Vector3d movement, float fricSpeed, float yRot) {
        double d0 = movement.lengthSqr();
        if (d0 < 1.0E-7D) {
           return Vector3d.ZERO;
        } else {
           Vector3d vector3d = (d0 > 1.0D ? movement.normalize() : movement).scale((double)fricSpeed);
           float f = MathHelper.sin(yRot * ((float)Math.PI / 180F));
           float f1 = MathHelper.cos(yRot * ((float)Math.PI / 180F));
           return new Vector3d(vector3d.x * (double)f1 - vector3d.z * (double)f, vector3d.y, vector3d.z * (double)f1 + vector3d.x * (double)f);
        }
     }
    
    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent clientTickEvent) {
        if (clientTickEvent.phase == TickEvent.Phase.START) {
            Minecraft minecraft = Minecraft.getInstance();
            ClientPlayerEntity player = minecraft.player;
            if (player != null) {
            	DragonStateProvider.getCap(player).ifPresent(playerStateHandler -> {
            		if (playerStateHandler.isDragon()) {
            			float bodyAndHeadYawDiff = (((float)playerStateHandler.getMovementData().bodyYaw) - player.yHeadRot);
            			
	                    Vector3d moveVector = getInputVector(new Vector3d(player.input.leftImpulse, 0, player.input.forwardImpulse), 1F, player.yRot);
	                    boolean isFlying = false;
	                    if (FlightController.wingsEnabled && !player.isOnGround() && !player.isInWater() && !player.isInLava()) { // TODO: Remove this when fixing flight system
	                    	moveVector = new Vector3d(player.getX() - player.xo, player.getY() - player.yo, player.getZ() - player.zo);
	                    	isFlying = true;
	                    }
	                    float f = (float)MathHelper.atan2(moveVector.z, moveVector.x) * (180F / (float)Math.PI) - 90F;
	                    float f1 = (float)(Math.pow(moveVector.x, 2) + Math.pow(moveVector.z, 2));
	                    
	                    if (f1 > 0.000028) {
	                    	if (isFlying || (minecraft.options.getCameraType() != PointOfView.FIRST_PERSON && ConfigurationHandler.thirdPersonBodyMovement.get() == DragonBodyMovementType.DRAGON) ||
	                    			minecraft.options.getCameraType() == PointOfView.FIRST_PERSON && ConfigurationHandler.firstPersonBodyMovement.get() == DragonBodyMovementType.DRAGON) {
	            				float f2 = MathHelper.wrapDegrees(f - (float)playerStateHandler.getMovementData().bodyYaw);
	            				playerStateHandler.getMovementData().bodyYaw += 0.5F * f2;
	                    	} else if ((minecraft.options.getCameraType() != PointOfView.FIRST_PERSON && ConfigurationHandler.thirdPersonBodyMovement.get() == DragonBodyMovementType.VANILLA) ||
	                    			minecraft.options.getCameraType() == PointOfView.FIRST_PERSON && ConfigurationHandler.firstPersonBodyMovement.get() == DragonBodyMovementType.VANILLA) {
	                    		
	                    		float f5 = MathHelper.abs(MathHelper.wrapDegrees(player.yRot) - f);
	                    		if (95.0F < f5 && f5 < 265.0F) {
	                    			f -= 180.0F;
	                    		}
                    			
                    			float _f = MathHelper.wrapDegrees(f - (float)playerStateHandler.getMovementData().bodyYaw);
                    			playerStateHandler.getMovementData().bodyYaw += _f * 0.3F;
                    			float _f1 = MathHelper.wrapDegrees(player.yRot - (float)playerStateHandler.getMovementData().bodyYaw);
                    			boolean flag = _f1 < -90.0F || _f1 >= 90.0F;
                    			
                    			if (_f1 < -75.0F) {
                    				_f1 = -75.0F;
                    			}

                    			if (_f1 >= 75.0F) {
                    				_f1 = 75.0F;
                    			}

                    			playerStateHandler.getMovementData().bodyYaw = player.yRot - _f1;
                    			if (_f1 * _f1 > 2500.0F) {
                    				playerStateHandler.getMovementData().bodyYaw += _f1 * 0.2F;
                    			}
	                    	}
            				if (bodyAndHeadYawDiff > 180)
            					playerStateHandler.getMovementData().bodyYaw -= 360;
            				if (bodyAndHeadYawDiff <= -180)
            					playerStateHandler.getMovementData().bodyYaw += 360;
	                    	playerStateHandler.setMovementData(playerStateHandler.getMovementData().bodyYaw, player.yHeadRot, player.xRot, player.swinging && player.getAttackStrengthScale(-3.0f) != 1);
		                	DragonSurvivalMod.CHANNEL.send(PacketDistributor.SERVER.noArg(), new PacketSyncCapabilityMovement(player.getId(), playerStateHandler.getMovementData().bodyYaw, playerStateHandler.getMovementData().headYaw, playerStateHandler.getMovementData().headPitch, playerStateHandler.getMovementData().bite));
	                	} else if (Math.abs(bodyAndHeadYawDiff) > 180F) {
	                    	if (Math.abs(bodyAndHeadYawDiff) > 360F)
	                    		playerStateHandler.getMovementData().bodyYaw -= bodyAndHeadYawDiff;
	                    	else {
	                    		float turnSpeed = Math.min(1F + (float)Math.pow(Math.abs(bodyAndHeadYawDiff) - 180F, 1.5F) / 30F, 50F);
	                    		playerStateHandler.setMovementData((float)playerStateHandler.getMovementData().bodyYaw - Math.signum(bodyAndHeadYawDiff) * turnSpeed, player.yHeadRot, player.xRot, player.swinging && player.getAttackStrengthScale(-3.0f) != 1);
	                    	}
	                    	DragonSurvivalMod.CHANNEL.send(PacketDistributor.SERVER.noArg(), new PacketSyncCapabilityMovement(player.getId(), playerStateHandler.getMovementData().bodyYaw, playerStateHandler.getMovementData().headYaw, playerStateHandler.getMovementData().headPitch, playerStateHandler.getMovementData().bite));
	                    } else if (playerStateHandler.getMovementData().bite != (player.swinging && player.getAttackStrengthScale(-3.0f) != 1) || player.yHeadRot != playerStateHandler.getMovementData().headYaw) {
	                    	playerStateHandler.setMovementData(playerStateHandler.getMovementData().bodyYaw, player.yHeadRot, player.xRot, player.swinging && player.getAttackStrengthScale(-3.0f) != 1);
	                    	DragonSurvivalMod.CHANNEL.send(PacketDistributor.SERVER.noArg(), new PacketSyncCapabilityMovement(player.getId(), playerStateHandler.getMovementData().bodyYaw, playerStateHandler.getMovementData().headYaw, playerStateHandler.getMovementData().headPitch, playerStateHandler.getMovementData().bite));
	                    }
            		}
            	});
            }
        }
    }
    

    /**
     * Called for every player.
     */
    @SubscribeEvent
    public static void thirdPersonPreRender(RenderPlayerEvent.Pre renderPlayerEvent) {

        PlayerEntity player = renderPlayerEvent.getPlayer();
        if (!playerDragonHashMap.containsKey(player.getId())) {
            DragonEntity dummyDragon = EntityTypesInit.dragonEntity.create(player.level);
            dummyDragon.player = player.getId();
            playerDragonHashMap.put(player.getId(), new AtomicReference<>(dummyDragon));
        }
        DragonStateProvider.getCap(player).ifPresent(cap -> {
            if (cap.isDragon()) {
                renderPlayerEvent.setCanceled(true);
                float partialRenderTick = renderPlayerEvent.getPartialRenderTick();
                float limbSwingAmount = MathHelper.lerp(partialRenderTick, player.animationSpeedOld, player.animationSpeed);
                float yaw = player.getViewYRot(partialRenderTick);
                float pitch = player.getViewXRot(partialRenderTick);
                DragonLevel dragonStage = cap.getLevel();
                ResourceLocation texture = getSkin(player, cap, dragonStage);
                MatrixStack matrixStack = renderPlayerEvent.getMatrixStack();
                try {
	                matrixStack.pushPose();
	                matrixStack.mulPose(Vector3f.YN.rotationDegrees((float)cap.getMovementData().bodyYaw));
	                float size = cap.getSize();
	                float scale = Math.max(size / 40, DragonLevel.BABY.maxWidth);
	                matrixStack.scale(scale, scale, scale);
	                int eventLight = renderPlayerEvent.getLight();
	                DragonEntity dummyDragon = playerDragonHashMap.get(player.getId()).get();
	                EntityRenderer<? super DragonEntity> dragonRenderer = Minecraft.getInstance().getEntityRenderDispatcher().getRenderer(dummyDragon);
	                dummyDragon.copyPosition(player);
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
	                if (player.isCrouching()) { // FIXME why does this exist... Why do the models auto-crouch align perfectly but when shift key is down they break?
	                	switch (dragonStage) {
	                        case ADULT:
	                            matrixStack.translate(0, 0.125, 0);
	                            break;
	                        case YOUNG:
	                            matrixStack.translate(0, 0.25, 0);
	                            break;
	                        case BABY:
	                            matrixStack.translate(0, 0.325, 0);
	                    }
	                } else if (player.isSwimming()) { // FIXME yea this too, I just copied what was up there to shift the model for swimming but I swear this should be done differently...
	                	switch (dragonStage) {
                        case ADULT:
                            matrixStack.translate(0, -0.35, 0);
                            break;
                        case YOUNG:
                            matrixStack.translate(0, -0.25, 0);
                            break;
                        case BABY:
                            matrixStack.translate(0, -0.15, 0);
	                	}
	                }
	                if (!player.isInvisible())
	                	dragonRenderer.render(dummyDragon, yaw, partialRenderTick, matrixStack, renderTypeBuffer, eventLight);

                    String helmetTexture = constructArmorTexture(player, EquipmentSlotType.HEAD);
                    String chestPlateTexture = constructArmorTexture(player, EquipmentSlotType.CHEST);
                    String legsTexture = constructArmorTexture(player, EquipmentSlotType.LEGS);
                    String bootsTexture = constructArmorTexture(player, EquipmentSlotType.FEET);

                    //scale to try to prevent texture fighting (problem is in consecutive renders)
                    matrixStack.scale(1.08f, 1.02f, 1.02f); // FIXME Causes issues with head turn
                    dragonModel.setCurrentTexture(new ResourceLocation(DragonSurvivalMod.MODID, helmetTexture));
                    dragonRenderer.render(dummyDragon, yaw, partialRenderTick, matrixStack, renderTypeBuffer, eventLight);
                    dragonModel.setCurrentTexture(new ResourceLocation(DragonSurvivalMod.MODID, chestPlateTexture));
                    dragonRenderer.render(dummyDragon, yaw, partialRenderTick, matrixStack, renderTypeBuffer, eventLight);
                    dragonModel.setCurrentTexture(new ResourceLocation(DragonSurvivalMod.MODID, legsTexture));
                    dragonRenderer.render(dummyDragon, yaw, partialRenderTick, matrixStack, renderTypeBuffer, eventLight);
                    dragonModel.setCurrentTexture(new ResourceLocation(DragonSurvivalMod.MODID, bootsTexture));
                    dragonRenderer.render(dummyDragon, yaw, partialRenderTick, matrixStack, renderTypeBuffer, eventLight);
                
	                ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
	                ItemStack right = player.getMainHandItem();
	                matrixStack.mulPose(Vector3f.XP.rotationDegrees(45.0F));
	                matrixStack.translate(-0.45f, 1, 0);
	                final int combinedOverlayIn = LivingRenderer.getOverlayCoords(player, 0);
	                itemRenderer.renderStatic(right, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, eventLight, combinedOverlayIn, matrixStack, renderTypeBuffer);
	                ItemStack left = player.getOffhandItem();
	                matrixStack.translate(0.9, 0, 0);
	                itemRenderer.renderStatic(left, ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, eventLight, combinedOverlayIn, matrixStack, renderTypeBuffer);
                } catch (Throwable throwable) {
                	 if (!(throwable instanceof NullPointerException))
                         throwable.printStackTrace();
                    matrixStack.popPose();
                } finally {
                	matrixStack.popPose();
                }
            }
        });
    }

    private static ResourceLocation getSkin(PlayerEntity player, DragonStateHandler cap, DragonLevel dragonStage) {
        ResourceLocation texture;
        UUID playerUniqueID = player.getUUID();
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
        Item item = playerEntity.getItemBySlot(equipmentSlot).getItem();
        if (item instanceof ArmorItem) {
            ArmorItem armorItem = (ArmorItem) item;
            IArmorMaterial armorMaterial = armorItem.getMaterial();
            if (armorMaterial == ArmorMaterial.NETHERITE) {
            	texture += "netherite_";
            } else if (armorMaterial == ArmorMaterial.DIAMOND) {
                texture += "diamond_";
            } else if (armorMaterial == ArmorMaterial.IRON) {
                texture += "iron_";
            } else if (armorMaterial == ArmorMaterial.LEATHER) {
                texture += "leather_";
            } else if (armorMaterial == ArmorMaterial.GOLD) {
                texture += "gold_";
            } else if (armorMaterial == ArmorMaterial.CHAIN) {
                texture += "chainmail_";
            } else if (armorMaterial == ArmorMaterial.TURTLE)
                texture += "turtle_";
            else {
                return texture + "empty_armor.png";
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

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void removeLavaFog(EntityViewRenderEvent.FogDensity event) {
    	ClientPlayerEntity player = Minecraft.getInstance().player;
    	DragonStateProvider.getCap(player).ifPresent(cap -> {
    		if (cap.isDragon() && cap.getType() == DragonType.CAVE && event.getInfo().getFluidInCamera().is(FluidTags.LAVA)) {
    			event.setDensity(0.1F);
    			event.setCanceled(true);
    		}
    	});
    }
    
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void removeFireOverlay(RenderBlockOverlayEvent event) {
    	ClientPlayerEntity player = Minecraft.getInstance().player;
    	DragonStateProvider.getCap(player).ifPresent(cap -> {
    		if (cap.isDragon() && cap.getType() == DragonType.CAVE && event.getOverlayType() == OverlayType.FIRE)
    			event.setCanceled(true);
    	});
    }
    
    static ResourceLocation HUDTextures = new ResourceLocation(DragonSurvivalMod.MODID, "textures/gui/dragon_hud.png");
}
