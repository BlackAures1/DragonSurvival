package by.jackraidenph.dragonsurvival.util;

import net.minecraft.util.DamageSource;

public class DamageSources {
	public static final DamageSource STAR_DRAIN = (new DamageSource("starDrain")).bypassArmor().bypassMagic();
	public static final DamageSource WATER_BURN = (new DamageSource("waterBurn")).bypassArmor();
	
}
