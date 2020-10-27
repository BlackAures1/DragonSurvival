package by.jackraidenph.dragonsurvival.util;

import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

public class ConfigurationHandler {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final Spawn SPAWN = new Spawn(BUILDER);
    public static final ForgeConfigSpec SPEC = BUILDER.build();

    public static class General {

        General(ForgeConfigSpec.Builder builder) {
            builder.push("general");

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
            min = builder.defineInRange("min", 1, 0, 64);
            max = builder.defineInRange("max", 3, 0, 64);
            weight = builder.defineInRange("weight", 4, 0, 100);
            builder.pop();
            builder.push("Predator spawn biomes");
            builder.comment("BiomeDictionary types to include & exclude.");
            include = builder.defineList("include", Collections.singletonList(FOREST.toString()), o -> BiomeDictionary.Type.getAll().contains(BiomeDictionaryHelper.getType(String.valueOf(o))));
            exclude = builder.defineList("exclude", Arrays.asList(MOUNTAIN.toString(), NETHER.toString()), o -> BiomeDictionary.Type.getAll().contains(BiomeDictionaryHelper.getType(String.valueOf(o))));
            builder.pop();
        }
    }
}
