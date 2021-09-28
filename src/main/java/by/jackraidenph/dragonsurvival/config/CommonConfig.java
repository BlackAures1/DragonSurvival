package by.jackraidenph.dragonsurvival.config;

import by.jackraidenph.dragonsurvival.util.BiomeDictionaryHelper;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

public class CommonConfig {

	// General
	public final ForgeConfigSpec.BooleanValue endVoidTeleport;
	public final ForgeConfigSpec.BooleanValue dragonsAllowedToUseElytra;

	// Predator
	public final ForgeConfigSpec.DoubleValue predatorDamageFactor;
	public final ForgeConfigSpec.DoubleValue predatorHealthFactor;
	public final ForgeConfigSpec.IntValue minPredatorSpawn;
	public final ForgeConfigSpec.IntValue maxPredatorSpawn;
	public final ForgeConfigSpec.IntValue predatorSpawnWeight;
	public final ForgeConfigSpec.DoubleValue predatorStarSpawnChance;
	public final ForgeConfigSpec.DoubleValue predatorAnimalSpawnChance;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> predatorBiomesInclude;
	public final ForgeConfigSpec.ConfigValue<List<? extends String>> predatorBiomesExclude;

	//Dragon hunters
	public final ForgeConfigSpec.DoubleValue knightHealth;
	public final ForgeConfigSpec.DoubleValue knightDamage;
	public final ForgeConfigSpec.DoubleValue knightArmor;
	public final ForgeConfigSpec.DoubleValue knightSpeed;

	public final ForgeConfigSpec.DoubleValue houndHealth;
	public final ForgeConfigSpec.DoubleValue houndDamage;
	public final ForgeConfigSpec.DoubleValue houndSpeed;
	public final ForgeConfigSpec.BooleanValue houndDoesSlowdown;

	public final ForgeConfigSpec.DoubleValue hunterHealth;
	public final ForgeConfigSpec.DoubleValue hunterDamage;
	public final ForgeConfigSpec.DoubleValue hunterArmor;
	public final ForgeConfigSpec.DoubleValue hunterSpeed;
	public final ForgeConfigSpec.BooleanValue hunterHasBolas;

	public final ForgeConfigSpec.DoubleValue squireHealth;
	public final ForgeConfigSpec.DoubleValue squireDamage;
	public final ForgeConfigSpec.DoubleValue squireArmor;
	public final ForgeConfigSpec.DoubleValue squireSpeed;

	public final ForgeConfigSpec.DoubleValue princeHealth;
	public final ForgeConfigSpec.DoubleValue princeDamage;
	public final ForgeConfigSpec.DoubleValue princeArmor;
	public final ForgeConfigSpec.DoubleValue princeSpeed;

	CommonConfig(ForgeConfigSpec.Builder builder) {
		builder.push("common");

		// General
		builder.push("general");
		endVoidTeleport = builder
				.comment("Should the player be teleported to the overworld when they fall in the end?")
				.define("endVoidTeleport", true);
		dragonsAllowedToUseElytra = builder.comment("Whether dragons are allowed to use Elytra").define("elytraForDragon", false);
		// Predator
		builder.pop().push("predator");
		predatorDamageFactor = builder
				.defineInRange("predatorDamageFactor", 0.5, 0.5, 10);
		predatorHealthFactor = builder
				.defineInRange("predatorHealthFactor", 0.2, 0.2, 5);
		builder.push("spawnChances");
		minPredatorSpawn = builder
				.defineInRange("minimum", 0, 0, 64);
		maxPredatorSpawn = builder
				.defineInRange("maximum", 1, 0, 64);
		predatorSpawnWeight = builder
				.comment("Set weight to 0 to disable spawning.")
				.defineInRange("weight", 2, 0, 100);
		predatorStarSpawnChance = builder
				.comment("Chance for predators to spawn from stars. Set to 0.0 to disable.")
				.defineInRange("starSpawnChance", 0.3, 0, 1.0);
		predatorAnimalSpawnChance = builder
				.comment("Chance for predators to spawn when an animal dies. Set to 0.0 to disable.")
				.defineInRange("animalSpawnChance", 0.03, 0.0, 1.0);
		builder.pop().push("spawnBiomes");
		predatorBiomesInclude = builder
				.comment("The predator can only spawn in biomes with the included types.")
				.defineList("include", Collections.singletonList(END.toString()), o -> BiomeDictionary.Type.getAll().contains(BiomeDictionaryHelper.getType(String.valueOf(o))));
		predatorBiomesExclude = builder
				.comment("The predator cannot spawn in biomes with excluded types.")
				.defineList("exclude", Arrays.asList(OVERWORLD.toString(), NETHER.toString()), o -> BiomeDictionary.Type.getAll().contains(BiomeDictionaryHelper.getType(String.valueOf(o))));
		builder.pop();

		builder.pop().push("dragonHunters");
		builder.push("knight");
		knightHealth = builder.comment("Armored Knight health").defineInRange("knightHealth", 40d, 10d, 80d);
		knightDamage = builder.comment("Armored Knight base damage").defineInRange("knightDamage", 8d, 1d, 32d);
		knightArmor = builder.comment("Armored Knight armor").defineInRange("knightArmor", 10d, 0d, 30d);
		knightSpeed = builder.comment("Armored Knight speed").defineInRange("knightSpeed", 0.35d, 0.1d, 0.6d);
		builder.pop().push("hound");
		houndHealth = builder.comment("Knight Hound health").defineInRange("houndHealth", 10d, 8d, 40d);
		houndDamage = builder.comment("Knight Hound damage").defineInRange("houndDamage", 2d, 1d, 10d);
		houndSpeed = builder.comment("Knight Hound speed").defineInRange("houndSpeed", 0.35d, 0.1d, 0.6d);
		houndDoesSlowdown = builder.comment("Does Knight Hound apply speed slowdown?").define("houndDoesSlowdown", true);
		builder.pop().push("hunter");
		hunterHealth = builder.comment("Hunter health").defineInRange("hunterHealth", 24d, 10d, 60d);
		hunterDamage = builder.comment("Hunter damage").defineInRange("hunterDamage", 5d, 2d, 20d);
		hunterSpeed = builder.comment("Hunter speed").defineInRange("hunterSpeed", 0.35d, 0.1d, 0.6d);
		hunterArmor = builder.comment("Hunter armor").defineInRange("hunterArmor", 0d, 0d, 20d);
		hunterHasBolas = builder.comment("Is hunter able to throw a bolas?").define("hunterThrowsBolas", true);
		builder.pop().push("squire");
		squireHealth = builder.comment("Squire health").defineInRange("squireHealth", 24d, 10d, 60d);
		squireDamage = builder.comment("Squire damage").defineInRange("squireDamage", 2d, 2d, 20d);
		squireSpeed = builder.comment("Squire speed").defineInRange("squireSpeed", 0.35d, 0.1d, 0.6d);
		squireArmor = builder.comment("Squire armor").defineInRange("squireArmor", 0d, 0d, 20d);
		builder.pop().push("prince");
		princeHealth = builder.comment("Prince health").defineInRange("princeHealth", 20d, 10d, 60d);
		princeDamage = builder.comment("Prince base damage").defineInRange("princeDamage", 1d, 1d, 20d);
		princeSpeed = builder.comment("Prince speed").defineInRange("princeSpeed", 0.5d, 0.2d, 0.6d);
		princeArmor = builder.comment("Prince armor").defineInRange("princeArmor", 0d, 0d, 20d);
	}
}
