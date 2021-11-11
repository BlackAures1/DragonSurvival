package by.jackraidenph.dragonsurvival.config;

import by.jackraidenph.dragonsurvival.util.DragonLevel;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ServerConfig {

	// General
	public final ForgeConfigSpec.DoubleValue maxFlightSpeed;
	public final ForgeConfigSpec.BooleanValue mineStarBlock;
    public final ForgeConfigSpec.BooleanValue sizeChangesHitbox;
    public final ForgeConfigSpec.BooleanValue hitboxGrowsPastHuman;
	public final ForgeConfigSpec.BooleanValue startWithWings;
	public final ForgeConfigSpec.BooleanValue enableFlightFallDamage;
	public final ForgeConfigSpec.BooleanValue allowFlyingWithoutHunger;
	public final ForgeConfigSpec.IntValue altarUsageCooldown;
	
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
	public final ForgeConfigSpec.BooleanValue forestCactiImmunity;
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
	public final ForgeConfigSpec.BooleanValue seaTicksBasedOnTemperature;
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
		altarUsageCooldown = builder
				.comment("How long of a cooldown in seconds the altar has after each use.")
				.defineInRange("altarUsageCooldown", 1, 0, 1000);
		sizeChangesHitbox = builder
				.comment("Whether the dragon size determines its hitbox size.")
				.define("sizeChangesHitbox", true);
		hitboxGrowsPastHuman = builder
				.comment("Whether the dragon hitbox grows past a human hitbox.")
				.define("largerDragonHitbox", true);
		startWithWings = builder
				.comment("Whether dragons start out with wings.")
				.define("startWithWings", false);
		allowFlyingWithoutHunger = builder
				.comment("Whether dragons can fly when out of hunger")
				.define("allowFlyingWithoutHunger", false);
		enableFlightFallDamage = builder.comment("Whether damage from flight falling is enabled")
				.define("enableFlightFallDamage", true);

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
				), this::isValidBlockConfig);
		builder.pop().push("forest"); // Forest Dragon Bonuses
		forestFallReduction = builder
				.comment("How many blocks of fall damage is mitigated for forest dragons. Set to 0.0 to disable.")
				.defineInRange("fallReduction", 5.0, 0.0, 100.0);
		forestBushImmunity = builder
				.comment("Whether forest dragons are immune to Sweet Berry Bush damage.")
				.define("bushImmunity", true);
		forestCactiImmunity = builder
				.comment("Whether forest dragons are immune to Cactus damage.")
				.define("cactiImmunity", true);
		forestSpeedupBlocks = builder
				.comment("Blocks forest dragons gain speed when standing above. Formatting: block/tag:modid:id")
				.defineList("forestSpeedupBlocks", Arrays.asList(
						"tag:minecraft:logs",
						"tag:minecraft:leaves",
						"tag:minecraft:planks",
						"tag:forge:dirt"
				), this::isValidBlockConfig);
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
						"tag:forge:sand",
						"block:minecraft:dirt_path",
						"block:minecraft:sandstone",
						"block:minecraft:cut_sandstone",
						"block:minecraft:chiseled_sandstone",
						"block:minecraft:smooth_sandstone",
						"block:minecraft:red_sandstone",
						"block:minecraft:cut_red_sandstone",
						"block:minecraft:chiseled_red_sandstone",
						"block:minecraft:smooth_red_sandstone",
						"block:minecraft:water"
				), this::isValidBlockConfig);
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
				.defineInRange("ticksBeforeStressed", 70, 0, 10000);
		forestStressEffectDuration = builder
				.comment("The number of seconds the stress effect lasts for.")
				.defineInRange("stressEffectDuration", 50, 2, 100000);
		stressExhaustion = builder
				.comment("The amount of exhaustion applied per 10 ticks during the stress effect.")
				.defineInRange("stressExhaustion", 1.0, 0.1, 4.0);
		builder.pop().push("sea"); // Sea Dragon Penalties
		seaTicksWithoutWater = builder
				.comment("The number of ticks out of water before the sea dragon will start taking dehydration damage. Set to 0 to disable. Note: This value can stack up to double while dehydrated.")
				.defineInRange("ticksWithoutWater", 15000, 0, 100000);
		seaTicksBasedOnTemperature = builder
				.comment("Whether the sea dragon should lose more water in warmer biomes and less during the night.")
				.define("waterConsumptionWithTemperature", true);
		seaDehydrationDamage = builder
				.comment("The amount of damage taken per tick while dehydrated (once every 40 ticks unless fully dehydrated, then once every 20 ticks).")
				.defineInRange("dehydrationDamage", 1.0, 0.5, 100.0);
		seaHydrationBlocks = builder
				.comment("When sea dragons stand on these blocks, hydration is restored. Format: block/tag:modid:id")
				.defineList("seaHydrationBlocks", Arrays.asList(
						"tag:minecraft:ice",
						"block:minecraft:snow",
						"block:minecraft:snow_block"
				), this::isValidBlockConfig);
		seaAllowWaterBottles = builder
				.comment("Set to false to disable sea dragons using vanilla water bottles to avoid dehydration.")
				.define("allowWaterBottles", true);
		seaTicksWithoutWaterRestored = builder
				.comment("How many ticks do water restoration items restore when used. Set to 0 to disable.")
				.defineInRange("waterItemRestorationTicks", 5000, 0, 100000);
		seaAdditionalWaterUseables = builder
				.comment("Additional modded USEABLE items that restore water when used (called from LivingEntityUseItemEvent.Finish). Format: item/tag:modid:id")
				.defineList("seaHydrationItems", Collections.singletonList(
						"item:minecraft:enchanted_golden_apple"
				), this::isValidItemConfig);
		// Ore Loot
		builder.pop(3).push("oreLoot");
		humanOreDustChance = builder
				.comment("The odds of dust dropping when a human harvests an ore.")
				.defineInRange("humanOreDustChance", 0.1, 0.0, 1.0);
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
						"item:minecraft:golden_apple",
						"item:minecraft:enchanted_golden_apple",
						"item:dragonsurvival:charged_coal:4:2",
						"item:dragonsurvival:charred_meat:10:12",
						"item:dragonsurvival:charred_seafood:8:10",
						"item:dragonsurvival:charred_vegetable:8:9",
						"item:dragonsurvival:charred_mushroom:8:5",
						"item:dragonsurvival:charged_soup:20:15",
						"item:desolation:cinder_fruit:6:7",
						"item:desolation:powered_cinder_fruit:8:12",
						"item:desolation:activatedcharcoal:2:2",
						"item:desolation:infused_powder:10:10",
						"item:desolation:primed_ash:7:8",
						"item:pickletweaks:diamond_apple",
						"item:pickletweaks:emerald_apple",
						"item:undergarden:ditchbulb:5,6",
						"item:xreliquary:molten_core:1:1",
						"item:silents_mechanisms:coal_generator_fuels:1:1",
						"item:mekanism:dust_charcoal:1:1",
						"item:mekanism:dust_coal:1:1",
						"item:rats:nether_cheese",
						"item:potionsmaster:charcoal_powder:1:1",
						"item:potionsmaster:coal_powder:1:1",
						"item:potionsmaster:activated_charcoal:2:2",
						"item:thermal:coal_coke:1:1",
						"item:infernalexp:glowcoal:2:3",
						"item:resourcefulbees:coal_honeycomb:5:5",
						"item:resourcefulbees:netherite_honeycomb:5:5",
						"item:lazierae2:coal_dust:1:1",
						"item:wyrmroost:jewelled_apple",
						"item:silents_mechanisms:coal_dust:1:1",
						"item:potionsmaster:calcinatedcoal_powder:1:1",
						"item:thermal:basalz_rod:2:4",
						"item:thermal:basalz_powder:1:2",
						"item:druidcraft:fiery_glass:2:2"
				), this::isValidFoodConfig);
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
						"item:aoa3:fiery_chops:6:7",
						"item:aoa3:raw_chimera_chop:6:7",
						"item:aoa3:raw_furlion_chop:6:7",
						"item:aoa3:raw_halycon_beef:7:8",
						"item:aoa3:raw_charger_shank:6:7",
						"item:aoa3:trilliad_leaves:8:11",
						"item:aoa3:heart_fruit:9:10",
						"item:pamhc2foodextended:rawtofabbititem",
						"item:pamhc2foodextended:rawtofickenitem",
						"item:pamhc2foodextended:rawtofuttonitem",
						"item:alexsmobs:kangaroo_meat:5:6",
						"item:alexsmobs:moose_ribs:6:8",
						"item:simplefarming:raw_horse_meat:5:6",
						"item:simplefarming:raw_bacon:3:3",
						"item:simplefarming:raw_chicken_wings:2:3",
						"item:simplefarming:raw_sausage:3:4",
						"item:xenoclustwo:raw_tortice:7:8",
						"item:unnamedanimalmod:musk_ox_shank:7:8",
						"item:unnamedanimalmod:frog_legs:5:6",
						"item:unnamedanimalmod:mangrove_fruit:4:7",
						"item:betteranimalsplus:venisonraw:5:6",
						"item:betteranimalsplus:pheasantraw:7:5",
						"item:betteranimalsplus:turkey_leg_raw:4:5",
						"item:infernalexp:raw_hogchop:6:7",
						"item:infernalexp:cured_jerky:10:7",
						"item:druidcraft:elderberries:3:4",
						"item:rats:raw_rat:4:5",
						"item:aquaculture:frog:4:5",
						"item:aquaculture:frog_legs_raw:4:4",
						"item:aquaculture:box_turtle:4:5",
						"item:aquaculture:arrau_turtle:4:5",
						"item:aquaculture:starshell_turtle:4:5",
						"item:nethers_exoticism:kiwano:3:4",
						"item:undergarden:raw_gloomper_leg:4:5",
						"item:undergarden:raw_dweller_meat:6:7",
						"item:farmersdelight:chicken_cuts:3:3",
						"item:farmersdelight:bacon:3:3",
						"item:farmersdelight:ham:9:10",
						"item:farmersdelight:minced_beef:5:3",
						"item:farmersdelight:mutton_chops:5:3",
						"item:abnormals_delight:duck_fillet:2:3",
						"item:abnormals_delight:venison_shanks:7:3",
						"item:pickletweaks:diamond_apple",
						"item:pickletweaks:emerald_apple",
						"item:autumnity:foul_berries:2:4",
						"item:autumnity:turkey:7:8",
						"item:autumnity:turkey_piece:2:4",
						"item:autumnity:foul_soup:12:8",
						"item:endergetic:bolloom_fruit:3:4",
						"item:quark:frog_leg:4:5",
						"item:nethers_delight:hoglin_loin:8:6",
						"item:nethers_delight:raw_stuffed_hoglin:18:10",
						"item:xreliquary:zombie_heart:4:7",
						"item:xreliquary:bat_wing:2:2",
						"item:eidolon:zombie_heart:7:7",
						"item:forbidden_arcanus:bat_wing:5:2",
						"item:twilightforest:raw_venison:5:5",
						"item:twilightforest:raw_meef:9:5",
						"item:twilightforest:hydra_chop",
						"item:cyclic:chorus_flight",
						"item:cyclic:chorus_spectral",
						"item:cyclic:apple_ender",
						"item:cyclic:apple_honey",
						"item:cyclic:apple_chorus",
						"item:cyclic:apple_bone",
						"item:cyclic:apple_prismarine",
						"item:cyclic:apple_lapis",
						"item:cyclic:apple_iron",
						"item:cyclic:apple_diamond",
						"item:cyclic:apple_emerald",
						"item:cyclic:apple_chocolate",
						"item:cyclic:toxic_carrot:15:15",
						"item:artifacts:everlasting_beef",
						"item:resourcefulbees:rainbow_honey_bottle",
						"item:resourcefulbees:diamond_honeycomb:5:5",
						"item:byg:soul_shroom:9:5",
						"item:byg:death_cap:9:8",
						"item:byg:holly_berries:2:2",
						"item:minecolonies:chorus_bread",
						"item:wyrmroost:jewelled_apple",
						"item:wyrmroost:raw_lowtier_meat:3:2",
						"item:wyrmroost:raw_common_meat:5:3",
						"item:wyrmroost:raw_apex_meat:8:6",
						"item:wyrmroost:raw_behemoth_meat:11:12",
						"item:wyrmroost:desert_wyrm:4:3",
						"item:eanimod:rawchicken_darkbig:9:5",
						"item:eanimod:rawchicken_dark:5:4",
						"item:eanimod:rawchicken_darksmall:3:2",
						"item:eanimod:rawchicken_pale:5:3",
						"item:eanimod:rawchicken_palesmall:4:3",
						"item:eanimod:rawrabbit_small:4:4",
						"item:environmental:duck:4:3",
						"item:environmental:venison:7:7",
						"item:cnb:lizard_item_0:4:4",
						"item:cnb:lizard_item_1:4:4",
						"item:cnb:lizard_item_2:4:4",
						"item:cnb:lizard_item_3:4:4",
						"item:snowpig:frozen_porkchop:7:3",
						"item:snowpig:frozen_ham:5:7",
						"item:untamedwilds:snake_grass_snake:4:4",
						"item:untamedwilds:snake_green_mamba:4:4",
						"item:untamedwilds:snake_rattlesnake:4:4",
						"item:untamedwilds:snake_emerald:4:4",
						"item:untamedwilds:snake_carpet_python:4:4",
						"item:untamedwilds:snake_corn:4:4",
						"item:untamedwilds:snake_gray_kingsnake:4:4",
						"item:untamedwilds:snake_coral:4:4",
						"item:untamedwilds:snake_ball_python:4:4",
						"item:untamedwilds:snake_black_mamba:4:4",
						"item:untamedwilds:snake_western_rattlesnake:4:4",
						"item:untamedwilds:snake_taipan:4:4",
						"item:untamedwilds:snake_adder:4:4",
						"item:untamedwilds:snake_rice_paddy:4:4",
						"item:untamedwilds:snake_coral_blue:4:4",
						"item:untamedwilds:snake_cave_racer:4:4",
						"item:untamedwilds:snake_swamp_moccasin:4:4",
						"item:untamedwilds:softshell_turtle_pig_nose:4:4",
						"item:untamedwilds:softshell_turtle_flapshell:4:4",
						"item:untamedwilds:softshell_turtle_chinese:4:4",
						"item:untamedwilds:tortoise_asian_box:4:4",
						"item:untamedwilds:tortoise_gopher:4:4",
						"item:untamedwilds:tortoise_leopard:4:4",
						"item:untamedwilds:softshell_turtle_peacock:4:4",
						"item:untamedwilds:softshell_turtle_nile:4:4",
						"item:untamedwilds:softshell_turtle_spiny:4:4",
						"item:untamedwilds:tortoise_sulcata:4:4",
						"item:untamedwilds:tortoise_star:4:4",
						"item:untamedwilds:tortoise_marginated:4:4",
						"item:leescreatures:raw_boarlin:6:6",
						"item:mysticalworld:venison:5:5",
						"item:toadterror:toad_chops:8:7"
				), this::isValidFoodConfig);
		seaDragonFoods = builder
				.defineList("seaDragon", Arrays.asList(
						"tag:forge:raw_fishes:6:7",
						"item:minecraft:dried_kelp:1:1",
						"item:minecraft:kelp:2:3",
						"item:minecraft:pufferfish:10:15",
						"item:minecraft:golden_apple",
						"item:minecraft:enchanted_golden_apple",
						"item:minecraft:honey_bottle",
						"item:aoa3:raw_candlefish:9:9",
						"item:aoa3:raw_crimson_skipper:8:8",
						"item:aoa3:raw_fingerfish:4:4",
						"item:aoa3:raw_pearl_stripefish:5:4",
						"item:aoa3:raw_limefish:5:5",
						"item:aoa3:raw_sailback:6:5",
						"item:aoa3:raw_golden_gullfish:10:2",
						"item:aoa3:raw_turquoise_stripefish:7:6",
						"item:aoa3:raw_violet_skipper:7:7",
						"item:aoa3:raw_rocketfish:4:10",
						"item:aoa3:raw_crimson_stripefish:8:7",
						"item:aoa3:raw_sapphire_strider:9:8",
						"item:aoa3:raw_dark_hatchetfish:9:9",
						"item:aoa3:raw_ironback:10:9",
						"item:aoa3:raw_rainbowfish:11:11",
						"item:aoa3:raw_razorfish:12:14",
						"item:quark:golden_frog_leg",
						"item:alexsmobs:lobster_tail:4:5",
						"item:alexsmobs:blobfish:8:9",
						"item:oddwatermobs:raw_ghost_shark:8:8",
						"item:oddwatermobs:raw_isopod:4:2",
						"item:oddwatermobs:raw_mudskipper:6:7",
						"item:oddwatermobs:raw_coelacanth:9:10",
						"item:oddwatermobs:raw_anglerfish:6:6",
						"item:oddwatermobs:deep_sea_fish:4:2",
						"item:oddwatermobs:crab_leg:5:6",
						"item:simplefarming:raw_calamari:5:6",
						"item:unnamedanimalmod:elephantnose_fish:5:6",
						"item:unnamedanimalmod:flashlight_fish:5:6",
						"item:unnamedanimalmod:rocket_killifish:5:6",
						"item:unnamedanimalmod:leafy_seadragon:5:6",
						"item:unnamedanimalmod:elephantnose_fish:5:6",
						"item:betteranimalsplus:eel_meat_raw:5:6",
						"item:betteranimalsplus:calamari_raw:4:5",
						"item:betteranimalsplus:crab_meat_raw:4:4",
						"item:aquaculture:fish_fillet_raw:2:2",
						"item:aquaculture:goldfish:8:4",
						"item:aquaculture:box_turtle:4:5",
						"item:aquaculture:arrau_turtle:4:5",
						"item:aquaculture:starshell_turtle:4:5",
						"item:aquaculture:algae:3:2",
						"item:betterendforge:end_fish_raw:6:7",
						"item:betterendforge:hydralux_petal:3:3",
						"item:betterendforge:charnia_green:2:2",
						"item:shroomed:raw_shroomfin:5:6",
						"item:undergarden:raw_gwibling:5:6",
						"item:pickletweaks:diamond_apple",
						"item:pickletweaks:emerald_apple",
						"item:bettas:betta_fish:4:5",
						"item:quark:crab_leg:4:4",
						"item:pamhc2foodextended:rawtofishitem",
						"item:fins:banded_redback_shrimp:6:1",
						"item:fins:night_light_squid:6:2",
						"item:fins:night_light_squid_tentacle:6:2",
						"item:fins:emerald_spindly_gem_crab:7:2",
						"item:fins:amber_spindly_gem_crab:7:2",
						"item:fins:rubby_spindly_gem_crab:7:2",
						"item:fins:sapphire_spindly_gem_crab:7:2",
						"item:fins:pearl_spindly_gem_crab:7:2",
						"item:fins:papa_wee:6:2",
						"item:fins:bugmeat:4:2",
						"item:fins:raw_golden_river_ray_wing:6:2",
						"item:fins:red_bull_crab_claw:4:4",
						"item:fins:white_bull_crab_claw:4:4",
						"item:fins:wherble_fin:1:1",
						"item:forbidden_arcanus:tentacle:5:2",
						"item:pneumaticcraft:raw_salmon_tempura:6:10",
						"item:rats:ratfish:4:2",
						"item:cyclic:chorus_flight",
						"item:cyclic:chorus_spectral",
						"item:cyclic:apple_ender",
						"item:cyclic:apple_honey",
						"item:cyclic:apple_chorus",
						"item:cyclic:apple_bone",
						"item:cyclic:apple_prismarine",
						"item:cyclic:apple_lapis",
						"item:cyclic:apple_iron",
						"item:cyclic:apple_diamond",
						"item:cyclic:apple_emerald",
						"item:cyclic:apple_chocolate",
						"item:upgrade_aquatic:purple_pickerelweed:2:2",
						"item:upgrade_aquatic:blue_pickerelweed:2:2",
						"item:upgrade_aquatic:polar_kelp:2:2",
						"item:upgrade_aquatic:tongue_kelp:2:2",
						"item:upgrade_aquatic:thorny_kelp:2:2",
						"item:upgrade_aquatic:ochre_kelp:2:2",
						"item:upgrade_aquatic:lionfish:8:9",
						"item:resourcefulbees:gold_honeycomb:5:5",
						"item:resourcefulbees:rainbow_honey_bottle",
						"item:wyrmroost:jewelled_apple",
						"item:aquaculture:sushi:6:5",
						"item:freshwarriors:fresh_soup:15:10",
						"item:freshwarriors:beluga_caviar:10:3",
						"item:freshwarriors:piranha:4:1",
						"item:freshwarriors:tilapia:4:1",
						"item:freshwarriors:stuffed_piranha:4:1",
						"item:freshwarriors:tigerfish:5:5",
						"item:freshwarriors:toe_biter_leg:3:3",
						"item:untamedwilds:egg_arowana_black:4:4",
						"item:untamedwilds:egg_trevally_jack:4:4",
						"item:untamedwilds:egg_trevally_golden:4:4",
						"item:untamedwilds:egg_giant_salamander_chinese:6:4",
						"item:untamedwilds:egg_giant_salamander_hellbender:6:4",
						"item:untamedwilds:egg_giant_salamander_japanese:6:4",
						"item:untamedwilds:giant_clam_gigas:4:4",
						"item:untamedwilds:giant_clam_derasa:4:4",
						"item:untamedwilds:giant_clam_maxima:4:4",
						"item:untamedwilds:giant_clam_squamosa:4:4",
						"item:untamedwilds:egg_trevally_giant:6:4",
						"item:untamedwilds:egg_trevally_bluespotted:6:4",
						"item:untamedwilds:egg_trevally_bigeye:6:4",
						"item:untamedwilds:egg_sunfish_southern:6:4",
						"item:untamedwilds:egg_sunfish_sunfish:6:4",
						"item:untamedwilds:egg_giant_clam_squamosa:6:4",
						"item:untamedwilds:egg_giant_clam_gigas:6:4",
						"item:untamedwilds:egg_giant_clam_derasa:6:4",
						"item:untamedwilds:egg_giant_clam_maxima:6:4",
						"item:untamedwilds:egg_football_fish_atlantic:6:4",
						"item:untamedwilds:egg_arowana_silver:6:4",
						"item:untamedwilds:egg_arowana_jardini:6:4",
						"item:untamedwilds:egg_arowana_green:6:4",
						"item:mysticalworld:raw_squid:6:5",
						"item:aquafina:fresh_soup:15:10",
						"item:aquafina:beluga_caviar:10:3",
						"item:aquafina:raw_piranha:4:1",
						"item:aquafina:raw_tilapia:4:1",
						"item:aquafina:stuffed_piranha:4:1",
						"item:aquafina:tigerfish:5:5",
						"item:aquafina:toe_biter_leg:3:3",
						"item:aquafina:raw_angelfish:4:1",
						"item:aquafina:raw_football_fish:4:1",
						"item:aquafina:raw_foxface_fish:4:1",
						"item:aquafina:raw_royal_gramma:4:1",
						"item:aquafina:raw_starfish:4:1",
						"item:aquafina:spider_crab_leg:4:1",
						"item:aquafina:raw_stingray_slice:4:1"
				), this::isValidFoodConfig);
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
		return blockSplit.length == 3 && (blockSplit[0].equalsIgnoreCase("block") || blockSplit[0].equalsIgnoreCase("tag"));
	}
	
	private boolean isValidItemConfig(Object item) {
		final String[] itemSplit = String.valueOf(item).split(":");
		return itemSplit.length == 3 && (itemSplit[0].equalsIgnoreCase("item") || itemSplit[0].equalsIgnoreCase("tag"));
	}
    
}
