package by.jackraidenph.dragonsurvival.handlers;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import javax.annotation.Nullable;

import com.mojang.datafixers.util.Pair;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.FoodStats;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DragonFoodHandler {

	private static Map<DragonType, Map<Item, Pair<Integer, Integer>>> DRAGON_FOODS;
	
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
		HashMap<DragonType, Map<Item, Pair<Integer, Integer>>> dragonMap = new HashMap<DragonType, Map<Item, Pair<Integer, Integer>>>();
		dragonMap.put(DragonType.CAVE, buildDragonFoodMap(DragonType.CAVE));
		dragonMap.put(DragonType.FOREST, buildDragonFoodMap(DragonType.FOREST));
		dragonMap.put(DragonType.SEA, buildDragonFoodMap(DragonType.SEA));
		DRAGON_FOODS = new HashMap<DragonType, Map<Item, Pair<Integer, Integer>>>(dragonMap);
	}
	
	private static Map<Item, Pair<Integer, Integer>> buildDragonFoodMap(DragonType type) {
		HashMap<Item, Pair<Integer, Integer>> foodMap = new HashMap<Item, Pair<Integer, Integer>>();
		String[] configFood;
		switch (type) {
			case CAVE:
				configFood = ConfigHandler.SERVER.caveDragonFoods.get().toArray(String[]::new);
				break;
			case FOREST:
				configFood = ConfigHandler.SERVER.forestDragonFoods.get().toArray(String[]::new);
				break;
			case SEA:
				configFood = ConfigHandler.SERVER.seaDragonFoods.get().toArray(String[]::new);
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
				if (tag != null && tag.getValues().size() != 0)
					for (Item item : tag.getValues())
						foodMap.put(item, new Pair<Integer, Integer>(
								sEntry.length == 5 ? Integer.parseInt(sEntry[3]) : item.getFoodProperties() != null ? 
										item.getFoodProperties().getNutrition() : 1, 
								sEntry.length == 5 ? Integer.parseInt(sEntry[4]) : item.getFoodProperties() != null ? 
										(int)(item.getFoodProperties().getNutrition() * (item.getFoodProperties().getSaturationModifier() + 1.0F)) : 1));
				else
					DragonSurvivalMod.LOGGER.error("Null or empty tag '{}:{}' in {} dragon food config.", sEntry[1], sEntry[2], type.toString().toLowerCase());
			} else {
				final Item item = ForgeRegistries.ITEMS.getValue(rlEntry);
				if (item != null)
					foodMap.put(item, new Pair<Integer, Integer>(
							sEntry.length == 5 ? Integer.parseInt(sEntry[3]) : item.getFoodProperties() != null ? 
									item.getFoodProperties().getNutrition() : 1, 
							sEntry.length == 5 ? Integer.parseInt(sEntry[4]) : item.getFoodProperties() != null ? 
									(int)(item.getFoodProperties().getNutrition() * (item.getFoodProperties().getSaturationModifier() + 1.0F)) : 1));
				else
					DragonSurvivalMod.LOGGER.error("Unknown item '{}:{}' in {} dragon food config.", sEntry[1], sEntry[2], type.toString().toLowerCase());
			}
		}
		return new HashMap<Item, Pair<Integer, Integer>>(foodMap);
	}

	@Nullable
	public static Food getDragonFoodProperties(Item item, DragonType type) {
		if (DRAGON_FOODS == null || !ConfigHandler.SERVER.enableDragonFood.get() || type == DragonType.NONE)
			return item.getFoodProperties();
		Food.Builder builder = new Food.Builder();
		if (DRAGON_FOODS.get(type).containsKey(item)) {
			builder.nutrition(DRAGON_FOODS.get(type).get(item).getFirst())
				.saturationMod((float)DRAGON_FOODS.get(type).get(item).getSecond() / (float)DRAGON_FOODS.get(type).get(item).getFirst());
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
			return builder.build();
		} else { // lmao
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
			return builder.build();
		}
	}
	
	public static boolean isDragonEdible(Item item, DragonType type) {
		return item.getFoodProperties() != null || DRAGON_FOODS.get(type).containsKey(item);
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
	
	/*@SubscribeEvent
	public void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
		DragonStateProvider.getCap(event.getEntityLiving()).ifPresent(dragonStateHandler -> {
			if (dragonStateHandler.isDragon()) {
				
				
				//FIXME: Use this forge event to replace dragonUseItem and dragonUse mixins after understanding the methods they inject into.
				
				
			}
		});
	}*/
	
	
	
}
