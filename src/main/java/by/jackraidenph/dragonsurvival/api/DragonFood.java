package by.jackraidenph.dragonsurvival.api;

import javax.annotation.Nullable;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.handlers.DragonFoodHandler;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.entity.Entity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;

public class DragonFood {

	public static boolean isEdible(Item item, Entity entity) {
		if (entity != null && DragonStateProvider.isDragon(entity))
			return DragonFoodHandler.isDragonEdible(item, DragonStateProvider.getCap(entity).orElseGet(null).getType());
		return item.isEdible();
	}
	
	@Nullable
	public static Food getEffectiveFoodProperties(Item item, Entity entity) {
		if (entity != null && DragonStateProvider.isDragon(entity))
			return DragonFoodHandler.getDragonFoodProperties(item, DragonStateProvider.getCap(entity).orElseGet(null).getType());
		return item.getFoodProperties();
	}
	
	public static DragonType getDragonType(Entity entity) {
		if (entity != null && DragonStateProvider.isDragon(entity))
			return DragonStateProvider.getCap(entity).orElseGet(null).getType();
		return DragonType.NONE;
	}
	
	public static boolean isDrawingDragonFood() {
		return DragonFoodHandler.isDrawingOverlay;
	}
}
