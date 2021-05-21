package by.jackraidenph.dragonsurvival.config;

import java.util.Arrays;
import java.util.List;

import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {

	// General
	public final ForgeConfigSpec.DoubleValue maxFlightSpeed;
	public final ForgeConfigSpec.BooleanValue mineStarBlock;
    public final ForgeConfigSpec.BooleanValue sizeChangesHitbox;
    public final ForgeConfigSpec.BooleanValue hitboxGrowsPastHuman;
    public final ForgeConfigSpec.BooleanValue startWithWings;
    
    // Dragon Debuffs
    public final ForgeConfigSpec.BooleanValue enableDragonDebuffs;
    public final ForgeConfigSpec.BooleanValue enableDragonFood;
    // TODO: Add config options for debuffs, such as max time out of water for sea and water burn damage for cave
    
    // Ore Loot (Networked for JEI) TODO: Addon for JEI to display the ore droprates when clicking on dust or bones.
    public final ForgeConfigSpec.DoubleValue humanOreDustChance;
    public final ForgeConfigSpec.DoubleValue dragonOreDustChance;
    public final ForgeConfigSpec.DoubleValue humanOreBoneChance;
    public final ForgeConfigSpec.DoubleValue dragonOreBoneChance;
    public final ForgeConfigSpec.ConfigValue<String> oresTag;
	
    // Dragon Food (Networked for Dragonfruit)
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> caveDragonFoods;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> forestDragonFoods;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> seaDragonFoods;
    
	ServerConfig(ForgeConfigSpec.Builder builder){
		builder.push("server");
		
		// General
		builder.push("general");
		maxFlightSpeed = builder
				.defineInRange("maxFlightSpeed", 0.3, 0.1, 1);
		mineStarBlock = builder
				.comment("Whether silk touch hoes can be used to harvest Predator Stars.")
				.define("harvestableStarBlock", false);
		sizeChangesHitbox = builder
				.comment("Whether the dragon size determines its hitbox size.")
				.define("sizeChangesHitbox", true);
		hitboxGrowsPastHuman = builder
				.comment("Whether the dragon hitbox grows past a human hitbox.")
				.define("largerDragonHitbox", true);
		startWithWings = builder
				.comment("Whether the dragon starts out with wings.")
				.define("startWithWings", false);
		
		// Dragon Debuffs
		builder.pop().push("debuffs");
		enableDragonDebuffs = builder
				.comment("Whether different dragon types get negative effects.")
				.define("enableDebuffs", true);
		enableDragonFood = builder
				.comment("Force dragons to eat a unique diet specific to their type.")
				.define("enableFood", true);
		
		// Ore Loot
		builder.pop().push("oreLoot");
		humanOreDustChance = builder
				.comment("The odds of dust dropping when a human harvests an ore.")
				.defineInRange("humanOreDustChance", 0.0033, 0.0, 1.0);
        dragonOreDustChance = builder
        		.comment("The odds of dust dropping when a dragon harvests an ore.")
        		.defineInRange("dragonOreDustChance", 0.4, 0.0, 1.0);
        humanOreBoneChance = builder
        		.comment("The odds of a bone dropping when a human harvests an ore.")
        		.defineInRange("humanOreBoneChance", 0.0, 0.0, 1.0);
        dragonOreBoneChance = builder
        		.comment("The odds of a bone dropping when a dragon harvests an ore.")
        		.defineInRange("dragonOreBoneChance", 0.01, 0.0, 1.0);
        oresTag = builder
        		.comment("The tag that contains all ores that can drop dust/bones when harvested. Will not drop if the ore drops another of the items in this tag.")
        		.define("oresTag", "forge:ores");
        
		// Dragon Food
		builder.pop().push("food");
		builder.comment("Dragon food formatting: item/tag:modid:id:food:saturation. Food/saturation values are optional as the human values will be used if missing.");
		caveDragonFoods = builder
				.defineList("caveDragon", Arrays.asList(
						"tag:minecraft:coals:1:1",
						"item:minecraft:charcoal:1:2",
						"item:dragonsurvival:charged_coal:5:7",
						"item:dragonsurvival:charred_meat:9:12",
						"item:minecraft:golden_apple",
						"item:minecraft:enchanted_golden_apple",
						"item:minecraft:honey_bottle",
						"item:minecraft:milk_bucket"
						), (food) -> isValidFoodConfig(food));
		forestDragonFoods = builder
				.defineList("forestDragon", Arrays.asList(
						"tag:forge:raw_meats:5:7",
						"item:minecraft:sweet_berries:2:2",
						"item:minecraft:rotten_flesh:2:3",
						"item:minecraft:spider_eye:7:8",
						"item:minecraft:rabbit:7:13",
						"item:minecraft:poisonous_potato:7:10",
						"item:minecraft:chorus_fruit:9:12",
						"item:minecraft:golden_apple",
						"item:minecraft:enchanted_golden_apple",
						"item:minecraft:honey_bottle",
						"item:minecraft:milk_bucket"
						), (food) -> isValidFoodConfig(food));
		seaDragonFoods = builder
				.defineList("seaDragon", Arrays.asList(
						"tag:forge:raw_fishes:6:7",
						"item:minecraft:dried_kelp:2:3",
						"item:minecraft:pufferfish:10:15",
						"item:minecraft:golden_apple",
						"item:minecraft:enchanted_golden_apple",
						"item:minecraft:honey_bottle",
						"item:minecraft:milk_bucket"
						), (food) -> isValidFoodConfig(food));
	}
	
	private boolean isValidFoodConfig(Object food) {
		final String[] foodSplit = String.valueOf(food).split(":");
		if (foodSplit.length < 3 || foodSplit.length > 5 || foodSplit.length == 4 ||!(foodSplit[0].equalsIgnoreCase("item") || foodSplit[0].equalsIgnoreCase("tag")))
			return false;
		try {
			if (foodSplit.length == 5) {
				final int value = Integer.parseInt(foodSplit[3]);
				final int saturation = Integer.parseInt(foodSplit[4]);
				if (value > 20 || value < 1 || saturation < 1 || saturation > 20)
					return false;
			}
		} catch (NumberFormatException ex) {
			return false;
		}
		return true;
	}
    
}
