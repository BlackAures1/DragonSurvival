package by.jackraidenph.dragonsurvival.config;

import java.util.Arrays;
import java.util.List;

import by.jackraidenph.dragonsurvival.util.DragonLevel;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;

public class ServerConfig {

	// General
	public final ForgeConfigSpec.DoubleValue maxFlightSpeed;
	public final ForgeConfigSpec.BooleanValue mineStarBlock;
    public final ForgeConfigSpec.BooleanValue sizeChangesHitbox;
    public final ForgeConfigSpec.BooleanValue hitboxGrowsPastHuman;
    public final ForgeConfigSpec.BooleanValue startWithWings;
    
    // Specifics
    public final ForgeConfigSpec.BooleanValue customDragonFoods;
    public final ForgeConfigSpec.BooleanValue healthAdjustments;
    public final ForgeConfigSpec.IntValue minHealth;
    public final ForgeConfigSpec.IntValue maxHealth;
    // Bonuses
    public final ForgeConfigSpec.BooleanValue bonuses;
    public final ForgeConfigSpec.BooleanValue attackDamage;
    public final ForgeConfigSpec.DoubleValue babyBonusDamage;
    public final ForgeConfigSpec.DoubleValue youngBonusDamage;
    public final ForgeConfigSpec.DoubleValue adultBonusDamage;
    public final ForgeConfigSpec.BooleanValue clawsAreTools;
    public final ForgeConfigSpec.IntValue baseHarvestLevel;
    public final ForgeConfigSpec.IntValue bonusHarvestLevel;
    public final ForgeConfigSpec.ConfigValue<DragonLevel> bonusUnlockedAt;
    public final ForgeConfigSpec.IntValue speedupEffectLevel; // 0 = Disabled
    // Cave Dragon
    public final ForgeConfigSpec.BooleanValue caveFireImmunity;
    public final ForgeConfigSpec.BooleanValue caveLavaSwimming;
    public final ForgeConfigSpec.IntValue caveLavaSwimmingTicks; // 0 = Disabled
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> caveSpeedupBlocks;
    // Forest Dragon
    public final ForgeConfigSpec.DoubleValue forestFallReduction; // 0.0 = Disabled
    public final ForgeConfigSpec.BooleanValue forestBushImmunity;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> forestSpeedupBlocks;
    // Sea Dragon
    public final ForgeConfigSpec.BooleanValue seaSwimmingBonuses;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> seaSpeedupBlocks;
    // Penalties
    public final ForgeConfigSpec.BooleanValue penalties;
    // Cave Dragon
    public final ForgeConfigSpec.DoubleValue caveWaterDamage; // 0.0 = Disabled
    public final ForgeConfigSpec.DoubleValue caveRainDamage; // 0.0 = Disabled
    // Forest Dragon
    public final ForgeConfigSpec.IntValue forestStressTicks; // 0 = Disabled
    public final ForgeConfigSpec.IntValue forestStressEffectDuration;
    public final ForgeConfigSpec.DoubleValue stressExhaustion;
    // Sea Dragon
    public final ForgeConfigSpec.IntValue seaTicksWithoutWater; // 0 = Disabled
    public final ForgeConfigSpec.DoubleValue seaDehydrationDamage;
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> seaHydrationBlocks;
    public final ForgeConfigSpec.BooleanValue seaAllowWaterBottles;
    public final ForgeConfigSpec.IntValue seaTicksWithoutWaterRestored; // 0 = Disabled
    public final ForgeConfigSpec.ConfigValue<List<? extends String>> seaAdditionalWaterUseables;
    
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
				.comment("Whether dragons start out with wings.")
				.define("startWithWings", false);
		
		// Specifics
		builder.pop().push("specifics");
		customDragonFoods = builder
				.comment("Force dragons to eat a unique diet for their type.")
				.define("dragonFoods", true);
		healthAdjustments = builder
				.comment("Apply a health modifier for dragons.")
				.define("healthMod", true);
		minHealth = builder
				.comment("Minumum health dragons will start off with.")
				.defineInRange("minHealth", 14, 1, 100);
		maxHealth = builder
				.comment("Maximum health dragons can grow to.")
				.defineInRange("maxHealth", 40, 1, 100);
		builder.push("bonuses"); // Bonuses
		bonuses = builder
				.comment("Set to false to toggle off all dragon bonuses.")
				.define("bonuses", true);
		attackDamage = builder
				.comment("Apply an attack damage modifier for dragons.")
				.define("attackMod", true);
		babyBonusDamage = builder
				.comment("Attack modifier for baby dragons.")
				.defineInRange("babyAttackMod", 1.0, 0.0, 100.0);
		youngBonusDamage = builder
				.comment("Attack modifier for young dragons.")
				.defineInRange("youngAttackMod", 2.0, 0.0, 100.0);
		adultBonusDamage = builder
				.comment("Attack modifier for adult dragons.")
				.defineInRange("adultAttackMod", 3.0, 0.0, 100.0);
		clawsAreTools = builder
				.comment("Whether dragon claws function as tools.")
				.define("clawsAreTools", true);
		baseHarvestLevel = builder
				.comment("The harvest level to apply when dragons breaks a block, regardless of dragon/tool type.")
				.defineInRange("baseHarvestLevel", 0, -1, 100);
		bonusHarvestLevel = builder
				.comment("The harvest level to apply to a dragons specific tool type once unlocked.")
				.defineInRange("bonusHarvestLevel", 1, -1, 100);
		bonusUnlockedAt = builder
				.comment("The stage that dragons unlock the bonus harvest level.")
				.defineEnum("bonusUnlockedAt", DragonLevel.YOUNG, DragonLevel.values());
		speedupEffectLevel = builder
				.comment("The speed effect level for dragon block-specific speedups. Set to 0 to disable.")
				.defineInRange("speedupEffectLevel", 2, 0, 100);
		builder.push("cave"); // Cave Dragon Bonuses
		caveFireImmunity = builder
				.comment("Whether cave dragons are immune to fire damage types.")
				.define("fireImmunity", true);
		caveLavaSwimming = builder
				.comment("Set to false to disable cave dragon lava swimming.")
				.define("lavaSwimming", true);
		caveLavaSwimmingTicks = builder
				.comment("The maximum number of ticks a cave dragon can swim in lava. Set to 0 to allow unlimited air while under lava.")
				.defineInRange("lavaSwimTicks", 3600, 0, 100000);
		caveSpeedupBlocks = builder
				.comment("Blocks cave dragons gain speed when standing above. Formatting: block/tag:modid:id")
				.defineList("caveSpeedupBlocks", Arrays.asList(
						"tag:minecraft:base_stone_nether",
						"tag:minecraft:base_stone_overworld",
						"tag:minecraft:stone_bricks",
						"tag:minecraft:beacon_base_blocks",
						"tag:forge:cobblestone",
						"tag:forge:sandstone",
						"tag:forge:stone",
						"tag:forge:ores"
						), (block) -> isValidBlockConfig(block));
		builder.pop().push("forest"); // Forest Dragon Bonuses
		forestFallReduction = builder
				.comment("How many blocks of fall damage is mitigated for forest dragons. Set to 0.0 to disable.")
				.defineInRange("fallReduction", 5.0, 0.0, 100.0);
		forestBushImmunity = builder
				.comment("Whether forest dragons are immune to Sweet Berry Bush damage.")
				.define("bushImmunity", true);
		forestSpeedupBlocks = builder
				.comment("Blocks forest dragons gain speed when standing above. Formatting: block/tag:modid:id")
				.defineList("forestSpeedupBlocks", Arrays.asList(
						"tag:minecraft:logs",
						"tag:minecraft:leaves",
						"tag:minecraft:planks",
						"tag:forge:dirt"
						), (block) -> isValidBlockConfig(block));
		builder.pop().push("sea"); // Sea Dragon Bonuses
		seaSwimmingBonuses = builder
				.comment("Whether sea dragons gain bonus swim speed and unlimited air.")
				.define("waterBonuses", true);
		seaSpeedupBlocks = builder
				.comment("Blocks sea dragons gain speed when standing above. Formatting: block/tag:modid:id")
				.defineList("seaSpeedupBlocks", Arrays.asList(
						"tag:minecraft:ice",
						"tag:minecraft:impermeable",
						"tag:minecraft:sand",
						"tag:minecraft:coral_blocks",
						"tag:forge:sand"
						), (block) -> isValidBlockConfig(block));
		builder.pop(2).push("penalties");
		penalties = builder
				.comment("Set to false to toggle off all dragon penalties.")
				.define("penalties", true);
		builder.push("cave"); // Cave Dragon Penalties
		caveWaterDamage = builder
				.comment("The amount of damage taken per water damage tick (once every 10 ticks). Set to 0.0 to disable water damage.")
				.defineInRange("waterDamage", 1.0, 0.0, 100.0);
		caveRainDamage = builder
				.comment("The amount of damage taken per rain damage tick (once every 40 ticks). Set to 0.0 to disable rain damage.")
				.defineInRange("rainDamage", 1.0, 0.0, 100.0);
		builder.pop().push("forest"); // Forest Dragon Penalties
		forestStressTicks = builder
				.comment("The number of ticks in darkness before the forest dragon gets the Stressed effect. Set to 0 to disable to stress effect.")
				.defineInRange("ticksBeforeStressed", 200, 0, 10000);
		forestStressEffectDuration = builder
				.comment("The number of seconds the stress effect lasts for.")
				.defineInRange("stressEffectDuration", 30, 2, 100000);
		stressExhaustion = builder
				.comment("The amount of exhaustion applied per 10 ticks during the stress effect.")
				.defineInRange("stressExhaustion", 1.0, 0.1, 4.0);
		builder.pop().push("sea"); // Sea Dragon Penalties
		seaTicksWithoutWater = builder
				.comment("The number of ticks out of water before the sea dragon will start taking dehydration damage. Set to 0 to disable. Note: This value can stack up to double while dehydrated.")
				.defineInRange("ticksWithoutWater", 2400, 0, 100000);
		seaDehydrationDamage = builder
				.comment("The amount of damage taken per tick while dehydrated (once every 40 ticks unless fully dehydrated, then once every 20 ticks).")
				.defineInRange("dehydrationDamage", 1.0, 0.5, 100.0);
		seaHydrationBlocks = builder
				.comment("When sea dragons stand on these blocks, hydration is restored. Format: block/tag:modid:id")
				.defineList("seaHydrationBlocks", Arrays.asList(
						"tag:minecraft:ice",
						"block:minecraft:snow",
						"block:minecraft:snow_block"
						), (item) -> isValidBlockConfig(item));
		seaAllowWaterBottles = builder
				.comment("Set to false to disable sea dragons using vanilla water bottles to avoid dehydration.")
				.define("allowWaterBottles", true);
		seaTicksWithoutWaterRestored = builder
				.comment("How many ticks do water restoration items restore when used. Set to 0 to disable.")
				.defineInRange("waterItemRestorationTicks", 2400, 0, 100000);
		seaAdditionalWaterUseables = builder
				.comment("Additional modded USEABLE items that restore water when used (called from LivingEntityUseItemEvent.Finish). Format: item/tag:modid:id")
				.defineList("seaHydrationItems", Arrays.asList(
						"item:minecraft:enchanted_golden_apple"
						), (item) -> isValidItemConfig(item));
		// Ore Loot
		builder.pop(3).push("oreLoot");
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
        		.comment("The tag that contains all ores that can drop dust/bones when harvested. Will not drop if the ore drops another of the items in this tag. Format: modid:id")
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
	
	private boolean isValidBlockConfig(Object block) {
		final String[] blockSplit = String.valueOf(block).split(":");
		if (blockSplit.length != 3 ||!(blockSplit[0].equalsIgnoreCase("block") || blockSplit[0].equalsIgnoreCase("tag")))
			return false;
		return true;
	}
	
	private boolean isValidItemConfig(Object item) {
		final String[] itemSplit = String.valueOf(item).split(":");
		if (itemSplit.length != 3 ||!(itemSplit[0].equalsIgnoreCase("item") || itemSplit[0].equalsIgnoreCase("tag")))
			return false;
		return true;
	}
    
}
