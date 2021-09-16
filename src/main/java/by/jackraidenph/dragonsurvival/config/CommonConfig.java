package by.jackraidenph.dragonsurvival.config;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

import java.util.Arrays;
import java.util.List;

import by.jackraidenph.dragonsurvival.util.BiomeDictionaryHelper;
import net.minecraft.client.world.DimensionRenderInfo;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeConfigSpec;

public class CommonConfig {

	// General
    public final ForgeConfigSpec.BooleanValue endVoidTeleport;
    
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
	
	CommonConfig(ForgeConfigSpec.Builder builder){
		builder.push("common");
		
		// General
		builder.push("general");
		endVoidTeleport = builder
				.comment("Should the player be teleported to the overworld when they fall in the end?")
				.define("endVoidTeleport", true);
		
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
				.defineList("include", Arrays.asList(END.toString()), o -> BiomeDictionary.Type.getAll().contains(BiomeDictionaryHelper.getType(String.valueOf(o))));
		predatorBiomesExclude = builder
				.comment("The predator cannot spawn in biomes with excluded types.")
				.defineList("exclude", Arrays.asList(OVERWORLD.toString(), NETHER.toString()), o -> BiomeDictionary.Type.getAll().contains(BiomeDictionaryHelper.getType(String.valueOf(o))));
	}
}
