package by.jackraidenph.dragonsurvival.util;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

public class ConfigurationHandler {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final Spawn SPAWN = new Spawn(BUILDER);
    public static final ForgeConfigSpec SPEC = BUILDER.build();
    public static ForgeConfigSpec.DoubleValue maxFlightSpeed;
    public static ForgeConfigSpec.DoubleValue predatorDamageFactor;
    public static ForgeConfigSpec.DoubleValue predatorHealthFactor;
    //public static ForgeConfigSpec.BooleanValue disableClientHandlerSpam;
    public static ForgeConfigSpec.DoubleValue humanOreDustChance;
    public static ForgeConfigSpec.DoubleValue dragonOreDustChance;
    public static ForgeConfigSpec.DoubleValue humanOreBoneChance;
    public static ForgeConfigSpec.DoubleValue dragonOreBoneChance;
    //public static ForgeConfigSpec.DoubleValue predatorDustChance;
    //public static ForgeConfigSpec.DoubleValue predatorBoneChance;

    public static class General {

        General(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            maxFlightSpeed = builder.defineInRange("Flight speed limiter", 0.3, 0.1, 1);
            predatorDamageFactor = builder.defineInRange("Predator damage factor", 1, 0.5, 10);
            predatorHealthFactor = builder.defineInRange("Predator health factor", 1, 0.2, 5);
            //disableClientHandlerSpam = builder.define("Disable \"Unknown custom packet identifier: dragonsurvival:main\" spam", true);
            humanOreDustChance = builder.defineInRange("Ore dust chance for human", 0.0033, 0.0, 1.0);
            dragonOreDustChance = builder.defineInRange("Ore dust chance for dragon", 0.4, 0.0, 1.0);
            humanOreDustChance = builder.defineInRange("Ore bone chance for human", 0.0, 0.0, 1.0);
            dragonOreBoneChance = builder.defineInRange("Ore bone chance for dragon", 0.01, 0.0, 1.0);
            //predatorDustChance = builder.defineInRange("Predator dust chance", 0.3, 0.0, 1.0);
            //predatorBoneChance = builder.defineInRange("Predator bone chance", 0.0, 0.0, 1.0);
            builder.pop();

        }
    }

    public static class Spawn {
        public final ForgeConfigSpec.IntValue min;
        public final ForgeConfigSpec.IntValue max;
        public final ForgeConfigSpec.IntValue weight;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> include;
        public final ForgeConfigSpec.ConfigValue<List<? extends String>> exclude;


        Spawn(ForgeConfigSpec.Builder builder) {
            builder.push("Predator spawn chances");
            builder.comment("Configure predator spawn weight & min/max group size. Set weight to 0 to disable.");
            min = builder.defineInRange("minimum", 1, 0, 64);
            max = builder.defineInRange("maximum", 3, 0, 64);
            weight = builder.defineInRange("weight", 4, 0, 100);
            builder.pop();
            builder.push("Predator spawn biomes");
            builder.comment("Biome types to include & exclude.");
            include = builder.defineList("include", Arrays.asList(FOREST.toString(), PLAINS.toString(), HILLS.toString(), SWAMP.toString(), SANDY.toString(), SNOWY.toString(), WASTELAND.toString(), BEACH.toString()), o -> BiomeDictionary.Type.getAll().contains(BiomeDictionaryHelper.getType(String.valueOf(o))));
            exclude = builder.defineList("exclude", Arrays.asList(MOUNTAIN.toString(), NETHER.toString()), o -> BiomeDictionary.Type.getAll().contains(BiomeDictionaryHelper.getType(String.valueOf(o))));
            builder.pop();
        }
    }
}
