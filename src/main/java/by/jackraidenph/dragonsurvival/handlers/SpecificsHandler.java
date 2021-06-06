package by.jackraidenph.dragonsurvival.handlers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.electronwill.nightconfig.core.Config;
import com.mojang.blaze3d.systems.RenderSystem;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.network.DiggingStatus;
import by.jackraidenph.dragonsurvival.network.StartJump;
import by.jackraidenph.dragonsurvival.network.SyncCapabilityDebuff;
import by.jackraidenph.dragonsurvival.renderer.CaveLavaFluidRenderer;
import by.jackraidenph.dragonsurvival.util.DamageSources;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.FluidBlockRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.passive.PigEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.passive.horse.AbstractHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.item.ShearsItem;
import net.minecraft.item.ShieldItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LightType;
import net.minecraft.world.World;
import net.minecraft.world.lighting.WorldLightManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpecificsHandler {

	private static final ResourceLocation DRAGON_HUD = new ResourceLocation(DragonSurvivalMod.MODID + ":textures/gui/dragon_hud.png");
	
	private static Map<DragonType, List<Block>> DRAGON_SPEEDUP_BLOCKS;
	private static List<Block> SEA_DRAGON_HYDRATION_BLOCKS;
	private static List<Item> SEA_DRAGON_HYDRATION_USE_ALTERNATIVES;
	
	@SubscribeEvent
	public static void onConfigLoad(ModConfig.Loading event) {
		if (event.getConfig().getType() == Type.SERVER) {
			rebuildSpeedupBlocksMap();
			rebuildSeaHydrationLists();
		}
	}
	
	@SubscribeEvent
	public static void onConfigReload(ModConfig.Reloading event) {
		if (event.getConfig().getType() == Type.SERVER) {
			rebuildSpeedupBlocksMap();
			rebuildSeaHydrationLists();
		}
	}
	
	private static void rebuildSpeedupBlocksMap() {
		HashMap<DragonType, List<Block>> speedupMap = new HashMap<DragonType, List<Block>>();
		speedupMap.put(DragonType.CAVE, buildDragonSpeedupMap(DragonType.CAVE));
		speedupMap.put(DragonType.FOREST, buildDragonSpeedupMap(DragonType.FOREST));
		speedupMap.put(DragonType.SEA, buildDragonSpeedupMap(DragonType.SEA));
		DRAGON_SPEEDUP_BLOCKS = speedupMap;
	}
	
	private static List<Block> buildDragonSpeedupMap(DragonType type) {
		ArrayList<Block> speedupMap = new ArrayList<Block>();
		String[] configSpeedups;
		switch (type) {
			case CAVE:
				configSpeedups = ConfigHandler.SERVER.caveSpeedupBlocks.get().toArray(new String[0]);
				break;
			case FOREST:
				configSpeedups = ConfigHandler.SERVER.forestSpeedupBlocks.get().toArray(new String[0]);
				break;
			case SEA:
				configSpeedups = ConfigHandler.SERVER.seaSpeedupBlocks.get().toArray(new String[0]);
				break;
			default:
				configSpeedups = new String[0];
				break;
		}
		for (String entry : configSpeedups) {
			final String[] sEntry = entry.split(":");
			final ResourceLocation rlEntry = new ResourceLocation(sEntry[1], sEntry[2]);
			if (sEntry[0].equalsIgnoreCase("tag")) {
				final ITag<Block> tag = BlockTags.getAllTags().getTag(rlEntry);
				if (tag != null && tag.getValues().size() != 0)
					for (Block block : tag.getValues())
						speedupMap.add(block);
				else
					DragonSurvivalMod.LOGGER.error("Null or empty tag '{}:{}' in {} dragon speedup block config.", sEntry[1], sEntry[2], type.toString().toLowerCase());
			} else {
				final Block block = ForgeRegistries.BLOCKS.getValue(rlEntry);
				if (block != null)
					speedupMap.add(block);
				else
					DragonSurvivalMod.LOGGER.error("Unknown block '{}:{}' in {} dragon speedup block config.", sEntry[1], sEntry[2], type.toString().toLowerCase());
			}
		}
		return speedupMap;
	}
	
	private static void rebuildSeaHydrationLists() {
		ArrayList<Block> hydrationBlocks = new ArrayList<Block>();
		String[] configHydrationBlocks = ConfigHandler.SERVER.seaHydrationBlocks.get().toArray(new String[0]);
		for (String entry : configHydrationBlocks) {
			final String[] sEntry = entry.split(":");
			final ResourceLocation rlEntry = new ResourceLocation(sEntry[1], sEntry[2]);
			if (sEntry[0].equalsIgnoreCase("tag")) {
				final ITag<Block> tag = BlockTags.getAllTags().getTag(rlEntry);
				if (tag != null && tag.getValues().size() != 0)
					for (Block block : tag.getValues())
						hydrationBlocks.add(block);
				else
					DragonSurvivalMod.LOGGER.error("Null or empty tag '{}:{}' in sea dragon hydraton block config.", sEntry[1], sEntry[2]);
			} else {
				final Block block = ForgeRegistries.BLOCKS.getValue(rlEntry);
				if (block != null)
					hydrationBlocks.add(block);
				else
					DragonSurvivalMod.LOGGER.error("Unknown block '{}:{}' in sea dragon hydration block config.", sEntry[1], sEntry[2]);
			}
		}
		SEA_DRAGON_HYDRATION_BLOCKS = hydrationBlocks;
		ArrayList<Item> hydrationItems = new ArrayList<Item>();
		String[] configHydrationItems = ConfigHandler.SERVER.seaAdditionalWaterUseables.get().toArray(new String[0]);
		for (String entry : configHydrationItems) {
			final String[] sEntry = entry.split(":");
			final ResourceLocation rlEntry = new ResourceLocation(sEntry[1], sEntry[2]);
			if (sEntry[0].equalsIgnoreCase("tag")) {
				final ITag<Item> tag = ItemTags.getAllTags().getTag(rlEntry);
				if (tag != null && tag.getValues().size() != 0)
					for (Item item : tag.getValues())
						hydrationItems.add(item);
				else
					DragonSurvivalMod.LOGGER.error("Null or empty tag '{}:{}' in sea dragon hydration block config.", sEntry[1], sEntry[2]);
			} else {
				final Item item = ForgeRegistries.ITEMS.getValue(rlEntry);
				if (item != null)
					hydrationItems.add(item);
				else
					DragonSurvivalMod.LOGGER.error("Unknown block '{}:{}' in sea dragon hydration block config.", sEntry[1], sEntry[2]);
			}
		}
		SEA_DRAGON_HYDRATION_USE_ALTERNATIVES = hydrationItems;
	}
	
	
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
    public void onRenderOverlayPreTick(RenderGameOverlayEvent.Pre event) {
    	Minecraft mc = Minecraft.getInstance();
    	ClientPlayerEntity player = mc.player;
    	DragonStateProvider.getCap(player).ifPresent(playerStateHandler -> {
    		if (event.getType() == RenderGameOverlayEvent.ElementType.AIR) {
    			if (playerStateHandler.getType() == DragonType.SEA && ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.seaSwimmingBonuses.get())
    				event.setCanceled(true);
    			if (playerStateHandler.getDebuffData().timeWithoutWater > 0 && playerStateHandler.getType() == DragonType.SEA && ConfigHandler.SERVER.penalties.get() && ConfigHandler.SERVER.seaTicksWithoutWater.get() != 0) {
    				RenderSystem.enableBlend();
        			mc.getTextureManager().bind(DRAGON_HUD);
        			
        			final int right_height = ForgeIngameGui.right_height;
    				ForgeIngameGui.right_height += 10;

    				int maxTimeWithoutWater = ConfigHandler.SERVER.seaTicksWithoutWater.get();
    				int timeWithoutWater = maxTimeWithoutWater - playerStateHandler.getDebuffData().timeWithoutWater;
    				boolean flag = false;
    				if (timeWithoutWater < 0) {
    					flag = true;
    					timeWithoutWater = Math.abs(timeWithoutWater);
    				}
    				
                    final int left = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + 91;
                    final int top = Minecraft.getInstance().getWindow().getGuiScaledHeight() - right_height;
                    final int full = flag ? MathHelper.floor((double) timeWithoutWater * 10.0D / maxTimeWithoutWater) : MathHelper.ceil((double) (timeWithoutWater - 2) * 10.0D / maxTimeWithoutWater);
                    final int partial = MathHelper.ceil((double) timeWithoutWater * 10.0D / maxTimeWithoutWater) - full;

                    for (int i = 0; i < full + partial; ++i)
                    	Minecraft.getInstance().gui.blit(event.getMatrixStack(), left - i * 8 - 9, top, (flag ? 18 : i < full ? 0 : 9), 36, 9, 9);
                    	
                    
                    mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
                    RenderSystem.disableBlend();
    			}
    			if (playerStateHandler.getLavaAirSupply() < ConfigHandler.SERVER.caveLavaSwimmingTicks.get() && playerStateHandler.getType() == DragonType.CAVE && ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.caveLavaSwimmingTicks.get() != 0 && ConfigHandler.SERVER.caveLavaSwimming.get()) {
    				RenderSystem.enableBlend();
        			mc.getTextureManager().bind(DRAGON_HUD);
        			
        			final int right_height = ForgeIngameGui.right_height;
    				ForgeIngameGui.right_height += 10;

                    final int left = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + 91;
                    final int top = Minecraft.getInstance().getWindow().getGuiScaledHeight() - right_height;
                    final int full = MathHelper.ceil((double) (playerStateHandler.getLavaAirSupply() - 2) * 10.0D / ConfigHandler.SERVER.caveLavaSwimmingTicks.get());
                    final int partial = MathHelper.ceil((double) playerStateHandler.getLavaAirSupply() * 10.0D / ConfigHandler.SERVER.caveLavaSwimmingTicks.get()) - full;

                    for (int i = 0; i < full + partial; ++i)
                    	Minecraft.getInstance().gui.blit(event.getMatrixStack(), left - i * 8 - 9, top, (i < full ? 0 : 9), 27, 9, 9);
                    
                    mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
                    RenderSystem.disableBlend();
    			}
    			if (playerStateHandler.getDebuffData().timeInDarkness > 0 && playerStateHandler.getType() == DragonType.FOREST && ConfigHandler.SERVER.penalties.get() && ConfigHandler.SERVER.forestStressTicks.get() != 0 && !player.hasEffect(DragonEffects.STRESS)) {
    				RenderSystem.enableBlend();
        			mc.getTextureManager().bind(DRAGON_HUD);
        			
        			final int right_height = ForgeIngameGui.right_height;
    				ForgeIngameGui.right_height += 10;

    				final int maxTimeInDarkness = ConfigHandler.SERVER.forestStressTicks.get();
    				final int timeInDarkness = maxTimeInDarkness - Math.min(playerStateHandler.getDebuffData().timeInDarkness, maxTimeInDarkness);
    				
                    final int left = Minecraft.getInstance().getWindow().getGuiScaledWidth() / 2 + 91;
                    final int top = Minecraft.getInstance().getWindow().getGuiScaledHeight() - right_height;
                    final int full = MathHelper.ceil((double) (timeInDarkness - 2) * 10.0D / maxTimeInDarkness);
                    final int partial = MathHelper.ceil((double) timeInDarkness * 10.0D / maxTimeInDarkness) - full;

                    for (int i = 0; i < full + partial; ++i)
                    	Minecraft.getInstance().gui.blit(event.getMatrixStack(), left - i * 8 - 9, top, (i < full ? 0 : 9), 45, 9, 9);
                    
                    mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
                    RenderSystem.disableBlend();
    			}
    		}
    	});
    }
    
    private static boolean wasCaveDragon = false;
    private static FluidBlockRenderer prevFluidRenderer;
    
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public void onRenderWorldLastEvent(RenderWorldLastEvent event) { // TODO: clean up time :)
    	ClientPlayerEntity player = Minecraft.getInstance().player;
    	DragonStateProvider.getCap(player).ifPresent(playerStateHandler -> {
    		if (playerStateHandler.getType() == DragonType.CAVE && ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.caveLavaSwimming.get()) {
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
    public void onDamage(LivingAttackEvent event) {
        LivingEntity livingEntity = event.getEntityLiving();
        DamageSource damageSource = event.getSource();
        DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                if (damageSource.isFire() && dragonStateHandler.getType() == DragonType.CAVE && ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.caveFireImmunity.get())
                    	event.setCanceled(true);
                else if (damageSource == DamageSource.SWEET_BERRY_BUSH && dragonStateHandler.getType() == DragonType.FOREST && ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.forestBushImmunity.get())
                	event.setCanceled(true);
            }
        });
    }
    
    @SubscribeEvent
    public void removeLavaFootsteps(PlaySoundAtEntityEvent event) {
    	if (!(event.getEntity() instanceof PlayerEntity))
    		return;
    	PlayerEntity player = (PlayerEntity)event.getEntity();
    	DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
    		if (dragonStateHandler.getType() == DragonType.CAVE && ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.caveLavaSwimming.get() && DragonSizeHandler.getOverridePose(player) == Pose.SWIMMING && event.getSound().getRegistryName().getPath().contains(".step"))
    			event.setCanceled(true);
    	});
    }
    
    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
		if (playerTickEvent.phase != TickEvent.Phase.START)
			return;
        PlayerEntity playerEntity = playerTickEvent.player;
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                World world = playerEntity.level;
                BlockState blockUnder = world.getBlockState(playerEntity.blockPosition().below());
                Block block = blockUnder.getBlock();
                if (!world.isClientSide && ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.speedupEffectLevel.get() > 0 && DRAGON_SPEEDUP_BLOCKS != null && DRAGON_SPEEDUP_BLOCKS.get(dragonStateHandler.getType()).contains(block))
                    playerEntity.addEffect(new EffectInstance(Effects.MOVEMENT_SPEED, 65, ConfigHandler.SERVER.speedupEffectLevel.get() - 1, false, false));
                switch (dragonStateHandler.getType()) {
                    case CAVE:
                        if (ConfigHandler.SERVER.penalties.get() && !playerEntity.isCreative() && !playerEntity.isSpectator() && ((playerEntity.isInWaterOrBubble() && ConfigHandler.SERVER.caveWaterDamage.get() != 0.0) || (playerEntity.isInWaterOrRain() && !playerEntity.isInWater() && ConfigHandler.SERVER.caveRainDamage.get() != 0.0) || (SEA_DRAGON_HYDRATION_BLOCKS != null && (SEA_DRAGON_HYDRATION_BLOCKS.contains(block) || SEA_DRAGON_HYDRATION_BLOCKS.contains(world.getBlockState(playerEntity.blockPosition()).getBlock())) && ConfigHandler.SERVER.caveRainDamage.get() != 0.0))) {
                        	if (playerEntity.isInWaterOrBubble() && playerEntity.tickCount % 10 == 0 && ConfigHandler.SERVER.caveWaterDamage.get() != 0.0)
                        		playerEntity.hurt(DamageSources.WATER_BURN, ConfigHandler.SERVER.caveWaterDamage.get().floatValue());
                        	else if (((playerEntity.isInWaterOrRain() && !playerEntity.isInWaterOrBubble()) || (SEA_DRAGON_HYDRATION_BLOCKS != null && (SEA_DRAGON_HYDRATION_BLOCKS.contains(block) || SEA_DRAGON_HYDRATION_BLOCKS.contains(world.getBlockState(playerEntity.blockPosition()).getBlock())))) && playerEntity.tickCount % 40 == 0 && ConfigHandler.SERVER.caveRainDamage.get() != 0.0)
                        		playerEntity.hurt(DamageSources.WATER_BURN, ConfigHandler.SERVER.caveRainDamage.get().floatValue());
                        	if (playerEntity.tickCount % 5 == 0)
                        		playerEntity.playSound(SoundEvents.LAVA_EXTINGUISH, 1.0F, (playerEntity.getRandom().nextFloat() - playerEntity.getRandom().nextFloat()) * 0.2F + 1.0F);
                            if (world.isClientSide)
                            	world.addParticle(ParticleTypes.POOF, 
                            			playerEntity.getX() + world.random.nextDouble() *  (world.random.nextBoolean() ? 1 : -1), 
                            			playerEntity.getY() + 0.5F,
                            			playerEntity.getZ() + world.random.nextDouble() *  (world.random.nextBoolean() ? 1 : -1), 
                            			0, 0, 0);
                        }
                        if (playerEntity.isOnFire() && ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.caveFireImmunity.get())
                            playerEntity.clearFire();
                        if (playerEntity.isEyeInFluid(FluidTags.LAVA)&& ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.caveLavaSwimming.get() && ConfigHandler.SERVER.caveLavaSwimmingTicks.get() != 0) {
                            if (!playerEntity.canBreatheUnderwater() && !playerEntity.abilities.invulnerable) {
                            	dragonStateHandler.setLavaAirSupply(dragonStateHandler.getLavaAirSupply() - 1);
                               if (dragonStateHandler.getLavaAirSupply() == -20) {
                            	   dragonStateHandler.setLavaAirSupply(0);
                            	   if (!playerEntity.level.isClientSide)
                            		   playerEntity.hurt(DamageSource.DROWN, 2F); //LAVA_YES
                               }
                            }
                            if (!playerEntity.level.isClientSide && playerEntity.isPassenger() && playerEntity.getVehicle() != null && !playerEntity.getVehicle().canBeRiddenInWater(playerEntity)) {
                            	playerEntity.stopRiding();
                            }
                         } else if (dragonStateHandler.getLavaAirSupply() < ConfigHandler.SERVER.caveLavaSwimmingTicks.get() && !playerEntity.isEyeInFluid(FluidTags.WATER))
                        	 dragonStateHandler.setLavaAirSupply(Math.min(dragonStateHandler.getLavaAirSupply() + (int)Math.ceil( ConfigHandler.SERVER.caveLavaSwimmingTicks.get() * 0.0133333F), ConfigHandler.SERVER.caveLavaSwimmingTicks.get()));
                        break;
                    case FOREST:
                        if (ConfigHandler.SERVER.penalties.get() && ConfigHandler.SERVER.forestStressTicks.get() > 0 && !playerEntity.isCreative() && !playerEntity.isSpectator()) {
                            WorldLightManager lightManager = world.getChunkSource().getLightEngine();
                            if ((lightManager.getLayerListener(LightType.BLOCK).getLightValue(playerEntity.blockPosition()) < 3 && lightManager.getLayerListener(LightType.SKY).getLightValue(playerEntity.blockPosition()) < 3)) {
                        		if (dragonStateHandler.getDebuffData().timeInDarkness < ConfigHandler.SERVER.forestStressTicks.get())
                        			dragonStateHandler.getDebuffData().timeInDarkness++;
                        		if (dragonStateHandler.getDebuffData().timeInDarkness == 1 && !playerEntity.level.isClientSide)
                        			DragonSurvivalMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new SyncCapabilityDebuff(playerEntity.getId(), dragonStateHandler.getDebuffData().timeWithoutWater, dragonStateHandler.getDebuffData().timeInDarkness));
                        		if (dragonStateHandler.getDebuffData().timeInDarkness == ConfigHandler.SERVER.forestStressTicks.get() && !world.isClientSide && playerEntity.tickCount % 21 == 0)
                                    playerEntity.addEffect(new EffectInstance(DragonEffects.STRESS, ConfigHandler.SERVER.forestStressEffectDuration.get() * 20));
                            } else if (dragonStateHandler.getDebuffData().timeInDarkness > 0) {
                           	 dragonStateHandler.getDebuffData().timeInDarkness = (Math.max(dragonStateHandler.getDebuffData().timeInDarkness - (int)Math.ceil( ConfigHandler.SERVER.forestStressTicks.get() * 0.02F), 0));
                            	if (dragonStateHandler.getDebuffData().timeInDarkness == 0 && !playerEntity.level.isClientSide)
                            		DragonSurvivalMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new SyncCapabilityDebuff(playerEntity.getId(), dragonStateHandler.getDebuffData().timeWithoutWater, dragonStateHandler.getDebuffData().timeInDarkness));
                            }
                		}
                        break;
                    case SEA:
                        if (playerEntity.isEyeInFluid(FluidTags.WATER) && playerEntity.getAirSupply() < playerEntity.getMaxAirSupply())
                            playerEntity.setAirSupply(playerEntity.getMaxAirSupply());
                        if (ConfigHandler.SERVER.penalties.get() && ConfigHandler.SERVER.seaTicksWithoutWater.get() > 0 && !playerEntity.isCreative() && !playerEntity.isSpectator()) {
                            if (!playerEntity.isInWaterRainOrBubble() && (SEA_DRAGON_HYDRATION_BLOCKS != null && !SEA_DRAGON_HYDRATION_BLOCKS.contains(block) && !SEA_DRAGON_HYDRATION_BLOCKS.contains(world.getBlockState(playerEntity.blockPosition()).getBlock()))) {
                            	if (dragonStateHandler.getDebuffData().timeWithoutWater < ConfigHandler.SERVER.seaTicksWithoutWater.get() * 2)
                            		dragonStateHandler.getDebuffData().timeWithoutWater++;
                            	if (dragonStateHandler.getDebuffData().timeWithoutWater == ConfigHandler.SERVER.seaTicksWithoutWater.get() + 1 && !playerEntity.level.isClientSide)
                            		DragonSurvivalMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new SyncCapabilityDebuff(playerEntity.getId(), dragonStateHandler.getDebuffData().timeWithoutWater, dragonStateHandler.getDebuffData().timeInDarkness));
                            } else if (dragonStateHandler.getDebuffData().timeWithoutWater > 0) {
                            	int old = dragonStateHandler.getDebuffData().timeWithoutWater;
                            	 dragonStateHandler.getDebuffData().timeWithoutWater = (Math.max(dragonStateHandler.getDebuffData().timeWithoutWater - (int)Math.ceil( ConfigHandler.SERVER.seaTicksWithoutWater.get() * 0.005F), 0));
                            	if (old > ConfigHandler.SERVER.seaTicksWithoutWater.get() + 1 && dragonStateHandler.getDebuffData().timeWithoutWater <= ConfigHandler.SERVER.seaTicksWithoutWater.get() && !playerEntity.level.isClientSide)
                            		DragonSurvivalMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new SyncCapabilityDebuff(playerEntity.getId(), dragonStateHandler.getDebuffData().timeWithoutWater, dragonStateHandler.getDebuffData().timeInDarkness));
                            }
                            if (!world.isClientSide && dragonStateHandler.getDebuffData().timeWithoutWater > ConfigHandler.SERVER.seaTicksWithoutWater.get() && dragonStateHandler.getDebuffData().timeWithoutWater < ConfigHandler.SERVER.seaTicksWithoutWater.get() * 2) {
                            	if (playerEntity.tickCount % 40 == 0) {
                            		playerEntity.hurt(DamageSources.DEHYDRATION, ConfigHandler.SERVER.seaDehydrationDamage.get().floatValue());
                            	}
                            }
                            else if (!world.isClientSide && dragonStateHandler.getDebuffData().timeWithoutWater >= ConfigHandler.SERVER.seaTicksWithoutWater.get() * 2) {
                            	if (playerEntity.tickCount % 20 == 0) {
                            		playerEntity.hurt(DamageSources.DEHYDRATION, ConfigHandler.SERVER.seaDehydrationDamage.get().floatValue());
                            	}
                            }
                        }
                    break;
                }
                
                // Dragon Particles
                // TODO: Randomize along dragon body
                if (world.isClientSide && !playerEntity.isCreative() && !playerEntity.isSpectator()) {
                    if (dragonStateHandler.getType() == DragonType.SEA && dragonStateHandler.getDebuffData().timeWithoutWater >= ConfigHandler.SERVER.seaTicksWithoutWater.get())
                    	world.addParticle(ParticleTypes.WHITE_ASH,
                    			playerEntity.getX() + world.random.nextDouble() *  (world.random.nextBoolean() ? 1 : -1), 
                    			playerEntity.getY() + 0.5F, 
                    			playerEntity.getZ() + world.random.nextDouble() * (world.random.nextBoolean() ? 1 : -1), 
                    			0, 0, 0);
                    if (dragonStateHandler.getType() == DragonType.FOREST && dragonStateHandler.getDebuffData().timeInDarkness == ConfigHandler.SERVER.forestStressTicks.get())
                    	world.addParticle(ParticleTypes.SMOKE,
                    			playerEntity.getX() + world.random.nextDouble() *  (world.random.nextBoolean() ? 1 : -1), 
                    			playerEntity.getY() + 0.5F, 
                    			playerEntity.getZ() + world.random.nextDouble() *  (world.random.nextBoolean() ? 1 : -1), 
                    			0, 0, 0);
                }
            }
        });
    }

    @SubscribeEvent
    public void modifyBreakSpeed(PlayerEvent.BreakSpeed breakSpeedEvent) {
    	if (!ConfigHandler.SERVER.bonuses.get() || !ConfigHandler.SERVER.clawsAreTools.get())
    		return;
        PlayerEntity playerEntity = breakSpeedEvent.getPlayer();
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                ItemStack mainStack = playerEntity.getMainHandItem();
                BlockState blockState = breakSpeedEvent.getState();
                Item item = mainStack.getItem();
                if (!(item instanceof ToolItem || item instanceof SwordItem || item instanceof ShearsItem)) {
                    switch (dragonStateHandler.getLevel()) {
                        case BABY:
                        	if (ConfigHandler.SERVER.bonusUnlockedAt.get() != DragonLevel.BABY) {
                        		breakSpeedEvent.setNewSpeed(breakSpeedEvent.getOriginalSpeed() * 2.0F);
                        		break;
                        	}
                        case YOUNG:
                        	if (ConfigHandler.SERVER.bonusUnlockedAt.get() == DragonLevel.ADULT && dragonStateHandler.getLevel() != DragonLevel.BABY) {
                        		breakSpeedEvent.setNewSpeed(breakSpeedEvent.getOriginalSpeed() * 2.0F);
                        		break;
                        	}
                        case ADULT:
                            switch (dragonStateHandler.getType()) {
                                case FOREST:
                                    if (blockState.isToolEffective(ToolType.AXE)) {
                                        breakSpeedEvent.setNewSpeed(breakSpeedEvent.getOriginalSpeed() * 4.0F);
                                    } else breakSpeedEvent.setNewSpeed(breakSpeedEvent.getOriginalSpeed() * 2.0F);
                                    break;
                                case CAVE:
                                    if (blockState.isToolEffective(ToolType.PICKAXE)) {
                                        breakSpeedEvent.setNewSpeed(breakSpeedEvent.getOriginalSpeed() * 4.0F);
                                    } else breakSpeedEvent.setNewSpeed(breakSpeedEvent.getOriginalSpeed() * 2.0F);
                                    break;
                                case SEA:
                                    if (blockState.isToolEffective(ToolType.SHOVEL)) {
                                        breakSpeedEvent.setNewSpeed(breakSpeedEvent.getOriginalSpeed() * 4.0F);
                                    } else breakSpeedEvent.setNewSpeed(breakSpeedEvent.getOriginalSpeed() * 2.0F);
                                    if (playerEntity.isInWaterOrBubble()) {
                                        breakSpeedEvent.setNewSpeed(breakSpeedEvent.getNewSpeed() * 1.4f);
                                    }
                                    break;
                            }
                            break;
                    }
                } else {
                    breakSpeedEvent.setNewSpeed(breakSpeedEvent.getOriginalSpeed() * 0.7f);
                }
            }
        });
    }
    
    @SubscribeEvent
    public void dropBlocksMinedByPaw(PlayerEvent.HarvestCheck harvestCheck) {
    	if (!ConfigHandler.SERVER.bonuses.get() || !ConfigHandler.SERVER.clawsAreTools.get())
    		return;
        PlayerEntity playerEntity = harvestCheck.getPlayer();
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                ItemStack stack = playerEntity.getMainHandItem();
                Item item = stack.getItem();
                BlockState blockState = harvestCheck.getTargetBlock();
                if (!(item instanceof ToolItem || item instanceof SwordItem || item instanceof ShearsItem) && !harvestCheck.canHarvest()) {
                	harvestCheck.setCanHarvest(dragonStateHandler.canHarvestWithPaw(blockState));
            	}
            }
        });
    }

    @SubscribeEvent
    public void disableMounts(EntityMountEvent mountEvent) { //TODO: Add config here
        Entity mounting = mountEvent.getEntityMounting();
        DragonStateProvider.getCap(mounting).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                if (mountEvent.getEntityBeingMounted() instanceof AbstractHorseEntity || mountEvent.getEntityBeingMounted() instanceof PigEntity || mountEvent.getEntityBeingMounted() instanceof StriderEntity)
                    mountEvent.setCanceled(true);
            }
        });
    }
    
    @SubscribeEvent
    public void onItemDestroyed(LivingEntityUseItemEvent.Finish destroyItemEvent) {
    	if (!ConfigHandler.SERVER.penalties.get() || ConfigHandler.SERVER.seaTicksWithoutWater.get() == 0)
    		return;
        ItemStack itemStack = destroyItemEvent.getItem();
        DragonStateProvider.getCap(destroyItemEvent.getEntityLiving()).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                PlayerEntity playerEntity = (PlayerEntity)destroyItemEvent.getEntityLiving();
                if (ConfigHandler.SERVER.seaAllowWaterBottles.get() && itemStack.getItem() instanceof PotionItem) {
                    PotionItem potionItem = (PotionItem) itemStack.getItem();
                    if (PotionUtils.getPotion(itemStack) == Potions.WATER && dragonStateHandler.getType() == DragonType.SEA && !playerEntity.level.isClientSide) {
                    	dragonStateHandler.getDebuffData().timeWithoutWater = Math.max(dragonStateHandler.getDebuffData().timeWithoutWater - ConfigHandler.SERVER.seaTicksWithoutWaterRestored.get(), 0);
                    	DragonSurvivalMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new SyncCapabilityDebuff(playerEntity.getId(), dragonStateHandler.getDebuffData().timeWithoutWater, dragonStateHandler.getDebuffData().timeInDarkness));
                    }
                }
                if (SEA_DRAGON_HYDRATION_USE_ALTERNATIVES.contains(itemStack.getItem()) && !playerEntity.level.isClientSide) {
                	dragonStateHandler.getDebuffData().timeWithoutWater = Math.max(dragonStateHandler.getDebuffData().timeWithoutWater - ConfigHandler.SERVER.seaTicksWithoutWaterRestored.get(), 0);
                	DragonSurvivalMod.CHANNEL.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> playerEntity), new SyncCapabilityDebuff(playerEntity.getId(), dragonStateHandler.getDebuffData().timeWithoutWater, dragonStateHandler.getDebuffData().timeInDarkness));
                }
            }
        });
    }

    @SubscribeEvent
    public void onJump(LivingEvent.LivingJumpEvent jumpEvent) { //TODO: Add config here
        final LivingEntity livingEntity = jumpEvent.getEntityLiving();
        DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                switch (dragonStateHandler.getLevel()) {
                    case BABY:
                        livingEntity.push(0, 0.025, 0); //1+ block
                        break;
                    case YOUNG:
                        livingEntity.push(0, 0.1, 0); //1.5+ block
                        break;
                    case ADULT:
                        livingEntity.push(0, 0.15, 0); //2+ blocks
                        break;
                }
                if (livingEntity instanceof ServerPlayerEntity) {
                    if (livingEntity.getServer().isSingleplayer())
                        DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new StartJump(livingEntity.getId(), 20)); // 42
                    else
                        DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new StartJump(livingEntity.getId(), 10)); // 21
                }
            }
        });
    }
    
    @SubscribeEvent
    public void reduceFallDistance(LivingFallEvent livingFallEvent) {
    	if (!ConfigHandler.SERVER.bonuses.get() || ConfigHandler.SERVER.forestFallReduction.get() == 0)
    		return;
        LivingEntity livingEntity = livingFallEvent.getEntityLiving();
        DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                if (dragonStateHandler.getType() == DragonType.FOREST) {
                    livingFallEvent.setDistance(livingFallEvent.getDistance() - ConfigHandler.SERVER.forestFallReduction.get().floatValue());
                }
            }
        });
    }
    
}
