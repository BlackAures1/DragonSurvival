package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.network.StartJump;
import by.jackraidenph.dragonsurvival.network.SyncCapabilityDebuff;
import by.jackraidenph.dragonsurvival.registration.DragonEffects;
import by.jackraidenph.dragonsurvival.renderer.CaveLavaFluidRenderer;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
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
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.ToolType;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class SpecificsHandler {

	private static final ResourceLocation DRAGON_HUD = new ResourceLocation(DragonSurvivalMod.MODID + ":textures/gui/dragon_hud.png");

	static Map<DragonType, List<Block>> DRAGON_SPEEDUP_BLOCKS;
	static List<Block> SEA_DRAGON_HYDRATION_BLOCKS;
	static List<Item> SEA_DRAGON_HYDRATION_USE_ALTERNATIVES;

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
		HashMap<DragonType, List<Block>> speedupMap = new HashMap<>();
		speedupMap.put(DragonType.CAVE, buildDragonSpeedupMap(DragonType.CAVE));
		speedupMap.put(DragonType.FOREST, buildDragonSpeedupMap(DragonType.FOREST));
		speedupMap.put(DragonType.SEA, buildDragonSpeedupMap(DragonType.SEA));
		DRAGON_SPEEDUP_BLOCKS = speedupMap;
	}
	
	private static List<Block> buildDragonSpeedupMap(DragonType type) {
		ArrayList<Block> speedupMap = new ArrayList<>();
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
					speedupMap.addAll(tag.getValues());
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
		ArrayList<Block> hydrationBlocks = new ArrayList<>();
		String[] configHydrationBlocks = ConfigHandler.SERVER.seaHydrationBlocks.get().toArray(new String[0]);
		for (String entry : configHydrationBlocks) {
			final String[] sEntry = entry.split(":");
			final ResourceLocation rlEntry = new ResourceLocation(sEntry[1], sEntry[2]);
			if (sEntry[0].equalsIgnoreCase("tag")) {
				final ITag<Block> tag = BlockTags.getAllTags().getTag(rlEntry);
				if (tag != null && tag.getValues().size() != 0)
					hydrationBlocks.addAll(tag.getValues());
				else
					DragonSurvivalMod.LOGGER.warn("Null or empty tag '{}:{}' in sea dragon hydraton block config.", sEntry[1], sEntry[2]);
			} else {
				final Block block = ForgeRegistries.BLOCKS.getValue(rlEntry);
				if (block != null)
					hydrationBlocks.add(block);
				else
					DragonSurvivalMod.LOGGER.warn("Unknown block '{}:{}' in sea dragon hydration block config.", sEntry[1], sEntry[2]);
			}
		}
		SEA_DRAGON_HYDRATION_BLOCKS = hydrationBlocks;
		ArrayList<Item> hydrationItems = new ArrayList<>();
		String[] configHydrationItems = ConfigHandler.SERVER.seaAdditionalWaterUseables.get().toArray(new String[0]);
		for (String entry : configHydrationItems) {
			final String[] sEntry = entry.split(":");
			final ResourceLocation rlEntry = new ResourceLocation(sEntry[1], sEntry[2]);
			if (sEntry[0].equalsIgnoreCase("tag")) {
				final ITag<Item> tag = ItemTags.getAllTags().getTag(rlEntry);
				if (tag != null && tag.getValues().size() != 0)
					hydrationItems.addAll(tag.getValues());
				else
					DragonSurvivalMod.LOGGER.warn("Null or empty tag '{}:{}' in sea dragon hydration block config.", sEntry[1], sEntry[2]);
			} else {
				final Item item = ForgeRegistries.ITEMS.getValue(rlEntry);
				if (item != null)
					hydrationItems.add(item);
				else
					DragonSurvivalMod.LOGGER.warn("Unknown block '{}:{}' in sea dragon hydration block config.", sEntry[1], sEntry[2]);
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
				    double timeWithoutWater = maxTimeWithoutWater - playerStateHandler.getDebuffData().timeWithoutWater;
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
	public void onRenderWorldLastEvent(RenderWorldLastEvent event) {
		Minecraft minecraft = Minecraft.getInstance();
		ClientPlayerEntity player = minecraft.player;
		DragonStateProvider.getCap(player).ifPresent(playerStateHandler -> {
			if (playerStateHandler.getType() == DragonType.CAVE && ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.caveLavaSwimming.get()) {
				if (!wasCaveDragon) {
					RenderType lavaType = RenderType.translucent();
					RenderTypeLookup.setRenderLayer(Fluids.LAVA, lavaType);
					RenderTypeLookup.setRenderLayer(Fluids.FLOWING_LAVA, lavaType);
					prevFluidRenderer = minecraft.getBlockRenderer().liquidBlockRenderer;
					minecraft.getBlockRenderer().liquidBlockRenderer = new CaveLavaFluidRenderer();
					minecraft.levelRenderer.allChanged();
				}
    		}else {
    			if (wasCaveDragon) {
    				if (prevFluidRenderer != null) {
    					RenderType lavaType = RenderType.solid();
                        RenderTypeLookup.setRenderLayer(Fluids.LAVA, lavaType);
						RenderTypeLookup.setRenderLayer(Fluids.FLOWING_LAVA, lavaType);
						minecraft.getBlockRenderer().liquidBlockRenderer = prevFluidRenderer;
    				}
					minecraft.levelRenderer.allChanged();
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
				else if ((damageSource == DamageSource.CACTUS) && dragonStateHandler.getType() == DragonType.FOREST && ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.forestCactiImmunity.get())
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
    public void onJump(LivingEvent.LivingJumpEvent jumpEvent) {
        final LivingEntity livingEntity = jumpEvent.getEntityLiving();
        DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()) {
                switch (dragonStateHandler.getLevel()) {
                    case BABY:
                        livingEntity.push(0, ConfigHandler.SERVER.newbornJump.get(), 0); //1+ block
                        break;
                    case YOUNG:
                        livingEntity.push(0, ConfigHandler.SERVER.youngJump.get(), 0); //1.5+ block
                        break;
                    case ADULT:
                        livingEntity.push(0, ConfigHandler.SERVER.adultJump.get(), 0); //2+ blocks
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
