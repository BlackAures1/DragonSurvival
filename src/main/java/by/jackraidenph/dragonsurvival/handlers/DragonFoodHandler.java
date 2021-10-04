package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.util.DragonType;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.*;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Stream;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DragonFoodHandler {

	private static Map<DragonType, Map<Item, Food>> DRAGON_FOODS;
	public static List<Item> CAVE_D_FOOD;
	public static List<Item> FOREST_D_FOOD;
	public static List<Item> SEA_D_FOOD;
	public static boolean isDrawingOverlay;

	private Minecraft mc;
	private final ResourceLocation FOOD_ICONS;
	private final Random rand;


	public DragonFoodHandler() {
		if (FMLLoader.getDist() == Dist.CLIENT)
			mc = Minecraft.getInstance();
		rand = new Random();
		FOOD_ICONS = new ResourceLocation(DragonSurvivalMod.MODID + ":textures/gui/dragon_hud.png");
		isDrawingOverlay = false;
	}
	
	@SubscribeEvent
	public static void onConfigLoad(ModConfig.Loading event) {
		if (event.getConfig().getType() == Type.SERVER)
			rebuildFoodMap();
	}
	
	@SubscribeEvent
	public static void onConfigReload(ModConfig.Reloading event) {
		if (event.getConfig().getType() == Type.SERVER)
			rebuildFoodMap();
	}
	
	private static void rebuildFoodMap() {
		HashMap<DragonType, Map<Item, Food>> dragonMap = new HashMap<DragonType, Map<Item, Food>>();
		dragonMap.put(DragonType.CAVE, buildDragonFoodMap(DragonType.CAVE));
		dragonMap.put(DragonType.FOREST, buildDragonFoodMap(DragonType.FOREST));
		dragonMap.put(DragonType.SEA, buildDragonFoodMap(DragonType.SEA));
		DRAGON_FOODS = new HashMap<>(dragonMap);
	}
	
	public static List<Item> getSafeEdibleFoods(DragonType dragonType) {
        List<Item> foods = new ArrayList<>();
        for (Item item : DRAGON_FOODS.get(dragonType).keySet()) {
			boolean safe = true;
			final Food food = DRAGON_FOODS.get(dragonType).get(item);
			if (food != null) {
				for (Pair<EffectInstance, Float> effect : food.getEffects()) {
					Effect e = effect.getFirst().getEffect();
					if (!e.isBeneficial() && e != Effects.CONFUSION) { // Because we decided to leave confusion on pufferfish
						safe = false;
						break;
					}
				}
				if (safe)
					foods.add(item);
			}
		}
        return foods;
    }
	
	private static Map<Item, Food> buildDragonFoodMap(DragonType type) {
		HashMap<Item, Food> foodMap = new HashMap<Item, Food>();
		String[] configFood;
		switch (type) {
			case CAVE:
				configFood = ConfigHandler.SERVER.caveDragonFoods.get().toArray(new String[0]);
				break;
			case FOREST:
				configFood = ConfigHandler.SERVER.forestDragonFoods.get().toArray(new String[0]);
				break;
			case SEA:
				configFood = ConfigHandler.SERVER.seaDragonFoods.get().toArray(new String[0]);
				break;
			default:
				configFood = new String[0];
				break;
		}
		configFood = Stream.of(configFood).sorted(Comparator.reverseOrder()).toArray(String[]::new);
		for (String entry : configFood) {
			final String[] sEntry = entry.split(":");
			final ResourceLocation rlEntry = new ResourceLocation(sEntry[1], sEntry[2]);
			if (sEntry[0].equalsIgnoreCase("tag")) {
				final ITag<Item> tag = ItemTags.getAllTags().getTag(rlEntry);
				if (tag != null && tag.getValues().size() != 0) {
					for (Item item : tag.getValues())
						foodMap.put(item, calculateDragonFoodProperties(item, type,
								sEntry.length == 5 ? Integer.parseInt(sEntry[3]) : item.getFoodProperties() != null ? item.getFoodProperties().getNutrition() : 1,
								sEntry.length == 5 ? Integer.parseInt(sEntry[4]) : item.getFoodProperties() != null ? (int) (item.getFoodProperties().getNutrition() * (item.getFoodProperties().getSaturationModifier() * 2.0F)) : 0,
								true));
				}
				else
					DragonSurvivalMod.LOGGER.warn("Null or empty tag '{}:{}' in {} dragon food config.", sEntry[1], sEntry[2], type.toString().toLowerCase());
			} else {
				final Item item = ForgeRegistries.ITEMS.getValue(rlEntry);
				if (item != null && item != Items.AIR) {
					foodMap.put(item, calculateDragonFoodProperties(item, type,
							sEntry.length == 5 ? Integer.parseInt(sEntry[3]) : item.getFoodProperties() != null ? item.getFoodProperties().getNutrition() : 1,
							sEntry.length == 5 ? Integer.parseInt(sEntry[4]) : item.getFoodProperties() != null ? (int) (item.getFoodProperties().getNutrition() * (item.getFoodProperties().getSaturationModifier() * 2.0F)) : 0,
							true));
				} else
					DragonSurvivalMod.LOGGER.warn("Unknown item '{}:{}' in {} dragon food config.", sEntry[1], sEntry[2], type.toString().toLowerCase());
			}
		}
		for (Item item : ForgeRegistries.ITEMS.getValues())
			if (!foodMap.containsKey(item) && item.isEdible())
				foodMap.put(item, calculateDragonFoodProperties(item, type, 0, 0, false));
		return new HashMap<>(foodMap);
	}

	@Nullable
	private static Food calculateDragonFoodProperties(Item item, DragonType type, int nutrition, int saturation, boolean dragonFood) {
		if (!ConfigHandler.SERVER.customDragonFoods.get() || type == DragonType.NONE)
			return item.getFoodProperties();
		Food.Builder builder = new Food.Builder();
		if (dragonFood) {
			builder.nutrition(nutrition)
				.saturationMod(((float)saturation / (float)nutrition) / 2.0F);
			if (item.getFoodProperties() != null) {
				Food humanFood = item.getFoodProperties();
				if (humanFood.isMeat())
					builder.meat();
				if (humanFood.canAlwaysEat())
					builder.alwaysEat();
				if (humanFood.isFastFood())
					builder.fast();
				for (Pair<EffectInstance, Float> effect : humanFood.getEffects())
					if (effect.getFirst().getEffect() != Effects.HUNGER && effect.getFirst().getEffect() != Effects.POISON)
						builder.effect(() -> effect.getFirst(), effect.getSecond());
			}
		} else {
			Food humanFood = item.getFoodProperties();
			builder.nutrition(humanFood.getNutrition())
				.saturationMod(humanFood.getSaturationModifier());
			if (humanFood.isMeat())
				builder.meat();
			if (humanFood.canAlwaysEat())
				builder.alwaysEat();
			if (humanFood.isFastFood())
				builder.fast();
			for (Pair<EffectInstance, Float> effect : humanFood.getEffects())
				if (effect.getFirst().getEffect() != Effects.HUNGER)
					builder.effect(() -> effect.getFirst(), effect.getSecond());
			builder.effect(() -> new EffectInstance(Effects.HUNGER, 20 * 60, 0), 1.0F);
		}
		return builder.build();
	}
	
	@Nullable
	public static Food getDragonFoodProperties(Item item, DragonType type) {
		if (DRAGON_FOODS == null || !ConfigHandler.SERVER.customDragonFoods.get() || type == DragonType.NONE)
			return item.getFoodProperties();
		if (DRAGON_FOODS.get(type).containsKey(item))
			return DRAGON_FOODS.get(type).get(item);
		return null;
	}
	
	public static boolean isDragonEdible(Item item, DragonType type) {
		if (ConfigHandler.SERVER.customDragonFoods.get() && type != DragonType.NONE)
			return DRAGON_FOODS.get(type).containsKey(item);
		return item.getFoodProperties() != null;
	}
	
	public static void dragonEat(FoodStats foodStats, Item item, ItemStack itemStack, DragonType type) {
		if (isDragonEdible(item, type)) {
			Food food = getDragonFoodProperties(item, type);
			foodStats.eat(food.getNutrition(), food.getSaturationModifier());
		}
	}
	
	public static int getUseDuration(ItemStack item, DragonType type) {
		if (isDragonEdible(item.getItem(), type)) {
			return item.getItem().getFoodProperties() != null && item.getItem().getFoodProperties().isFastFood() ? 16 : 32;
		} else
			return item.getUseDuration(); // VERIFY THIS
	}
	
	@SubscribeEvent
	public void onItemUseStart(LivingEntityUseItemEvent.Start event) {
		DragonStateProvider.getCap(event.getEntityLiving()).ifPresent(dragonStateHandler -> {
			if (dragonStateHandler.isDragon())
				event.setDuration(getUseDuration(event.getItem(), dragonStateHandler.getType()));
		});
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.DEDICATED_SERVER)
	public void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
		DragonStateProvider.getCap(event.getEntityLiving()).ifPresent(dragonStateHandler -> {
			if (dragonStateHandler.isDragon()) {
				ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
				ServerWorld level = player.getLevel();
				Hand hand = event.getHand();
				ItemStack stack = player.getItemInHand(event.getHand());
				
				int i = stack.getCount();
				int j = stack.getDamageValue();
				ActionResult<ItemStack> actionresult = stack.use(level, player, hand);
				ItemStack itemstack = actionresult.getObject();
				if (itemstack == stack && itemstack.getCount() == i && getUseDuration(itemstack, dragonStateHandler.getType()) <= 0 && itemstack.getDamageValue() == j) {
					event.setCancellationResult(actionresult.getResult());
				} else if (actionresult.getResult() == ActionResultType.FAIL && getUseDuration(itemstack, dragonStateHandler.getType()) > 0 && !player.isUsingItem()) {
					event.setCancellationResult(actionresult.getResult());
				} else {
					player.setItemInHand(hand, itemstack);
					if (player.isCreative()) {
						itemstack.setCount(i);
						if (itemstack.isDamageableItem() && itemstack.getDamageValue() != j) {
							itemstack.setDamageValue(j);
						}
					}

					if (itemstack.isEmpty()) {
						player.setItemInHand(hand, ItemStack.EMPTY);
					}

					if (!player.isUsingItem()) {
						player.refreshContainer(player.inventoryMenu);
					}

		            event.setCancellationResult(actionresult.getResult());
				}
				event.setCanceled(true);
			}
		});
	}
	
	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void onRenderFoodBar(RenderGameOverlayEvent.Pre event) {
		ClientPlayerEntity player = this.mc.player;
		
		isDrawingOverlay = !event.isCanceled() && ConfigHandler.SERVER.customDragonFoods.get();
		if (!isDrawingOverlay)
			return;
		
		DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
			if (dragonStateHandler.isDragon()) {
				
				if (event.getType() != RenderGameOverlayEvent.ElementType.FOOD || player.isCreative() || player.isSpectator())
					return;
				
				event.setCanceled(true);
				
				rand.setSeed(player.tickCount * 312871L);
				
				RenderSystem.enableBlend();
				this.mc.getTextureManager().bind(FOOD_ICONS);
				
				final int right_height = ForgeIngameGui.right_height;
				ForgeIngameGui.right_height += 10;
				
				final int left = this.mc.getWindow().getGuiScaledWidth() / 2 + 91;
                final int top = this.mc.getWindow().getGuiScaledHeight() - right_height;
                
                final FoodStats food = player.getFoodData();

                final int type = dragonStateHandler.getType() == DragonType.FOREST ? 0 : dragonStateHandler.getType() == DragonType.CAVE ? 9 : 18;
                
                final boolean hunger = player.hasEffect(Effects.HUNGER);
                
                for (int i = 0; i < 10; ++i) {
                	int idx = i * 2 + 1;
                	int y = top;
                	
                	if (food.getSaturationLevel() <= 0.0F && player.tickCount % (food.getFoodLevel() * 3 + 1) == 0)
                		y = top + (rand.nextInt(3) - 1);

					mc.gui.blit(event.getMatrixStack(), left - i * 8 - 9, y, (hunger ? 117 : 0), type, 9, 9);
                	
                	if (idx < food.getFoodLevel())
                		mc.gui.blit(event.getMatrixStack(), left - i * 8 - 9, y, (hunger ? 72 : 36), type, 9, 9);
                	else if (idx == food.getFoodLevel())
                		mc.gui.blit(event.getMatrixStack(), left - i * 8 - 9, y, (hunger ? 81 : 45), type, 9, 9);
                }
                
        		this.mc.getTextureManager().bind(AbstractGui.GUI_ICONS_LOCATION);
        		RenderSystem.disableBlend();
			} else
				isDrawingOverlay = false;
		});
	}
}
