package by.jackraidenph.dragonsurvival.util;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.network.SyncConfig;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Arrays;
import java.util.List;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

@Mod.EventBusSubscriber
public class ConfigurationHandler {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final General GENERAL = new General(BUILDER);
    public static final Spawn SPAWN = new Spawn(BUILDER);
    public static final OreLoot ORE_LOOT = new OreLoot(BUILDER);
    public static final ForgeConfigSpec SPEC = BUILDER.build();

    private static ForgeConfigSpec.DoubleValue maxFlightSpeed;
    //public static ForgeConfigSpec.BooleanValue disableClientHandlerSpam;
    private static ForgeConfigSpec.ConfigValue<Boolean> mineStarBlock;
    private static ForgeConfigSpec.ConfigValue<Boolean> sizeChangesHitbox;
    private static ForgeConfigSpec.ConfigValue<Boolean> hitboxGrowsPastHuman;
    private static ForgeConfigSpec.ConfigValue<Boolean> startWithWings;
    public static ForgeConfigSpec.ConfigValue<DragonBodyMovementType> firstPersonBodyMovement;
    public static ForgeConfigSpec.ConfigValue<DragonBodyMovementType> thirdPersonBodyMovement;

    
    
    public static class General {

        private ForgeConfigSpec.BooleanValue enableDragonDebuffs;
        public ForgeConfigSpec.DoubleValue predatorDamageFactor;
        public ForgeConfigSpec.DoubleValue predatorHealthFactor;
        public ForgeConfigSpec.ConfigValue<Boolean> endVoidTeleport;

        General(ForgeConfigSpec.Builder builder) {
            builder.push("general");
            maxFlightSpeed = builder.defineInRange("Flight speed limiter", 0.3, 0.1, 1);
            predatorDamageFactor = builder.defineInRange("Predator damage factor", 1, 0.5, 10);
            predatorHealthFactor = builder.defineInRange("Predator health factor", 1, 0.2, 5);
            //disableClientHandlerSpam = builder.define("Disable \"Unknown custom packet identifier: dragonsurvival:main\" spam", true);
            mineStarBlock = builder.define("Silk hoe mines Predator stars", false);
            sizeChangesHitbox = builder.define("Dragon size changes hitbox", true);
            hitboxGrowsPastHuman = builder.define("Hitbox grows larger than human", true);
            startWithWings = builder.define("Dragons start with wings", false);
            endVoidTeleport = builder.define("End void teleports to overworld", true);
            enableDragonDebuffs = builder.define("Enable debuffs for dragons", true);
            firstPersonBodyMovement = builder.defineEnum("First person body movement type", DragonBodyMovementType.VANILLA, DragonBodyMovementType.values());
            thirdPersonBodyMovement = builder.defineEnum("Third person body movement type", DragonBodyMovementType.DRAGON, DragonBodyMovementType.values());
            builder.pop();
        }
    }

    public static class OreLoot {
        public final ForgeConfigSpec.DoubleValue humanOreDustChance;
        public final ForgeConfigSpec.DoubleValue dragonOreDustChance;
        public final ForgeConfigSpec.DoubleValue humanOreBoneChance;
        public final ForgeConfigSpec.DoubleValue dragonOreBoneChance;
        public final ForgeConfigSpec.ConfigValue<String> oreBlocksTag;

        OreLoot(ForgeConfigSpec.Builder builder) {
            builder.push("Ore bonus loot");
            humanOreDustChance = builder.defineInRange("Ore dust chance for human", 0.0033, 0.0, 1.0);
            dragonOreDustChance = builder.defineInRange("Ore dust chance for dragon", 0.4, 0.0, 1.0);
            humanOreBoneChance = builder.defineInRange("Ore bone chance for human", 0.0, 0.0, 1.0);
            dragonOreBoneChance = builder.defineInRange("Ore bone chance for dragon", 0.01, 0.0, 1.0);
            oreBlocksTag = builder.define("Ores tag", "forge:ores");
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

    public static class NetworkedConfig {
        private static boolean serverConnection = false;
        private static Double serverMaxFlightSpeed;
        private static Boolean serverMineStarBlock;
        private static Boolean serverSizeChangesHitbox;
        private static Boolean serverHitboxGrowsPastHuman;
        private static Boolean serverStartWithWings;
        private static Boolean serverDragonDebuffs;

        public static void setServerConnection(boolean connection) {
            serverConnection = connection;
        }

        public static void saveServerConfig(double maxFlightSpeed, boolean mineStarBlock, boolean sizeChangesHitbox, boolean hitboxGrowsPastHuman, boolean startWithWings, boolean enableDragonDebuffs) {
            serverMaxFlightSpeed = maxFlightSpeed;
            serverMineStarBlock = mineStarBlock;
            serverSizeChangesHitbox = sizeChangesHitbox;
            serverHitboxGrowsPastHuman = hitboxGrowsPastHuman;
            serverStartWithWings = startWithWings;
            serverDragonDebuffs = enableDragonDebuffs;
        }

        public static double getMaxFlightSpeed() {
            if (!serverConnection)
                return ConfigurationHandler.maxFlightSpeed.get();
            return serverMaxFlightSpeed == null ? ConfigurationHandler.maxFlightSpeed.get() : serverMaxFlightSpeed;
        }

        public static boolean getMineStarBlock() {
            if (!serverConnection)
                return ConfigurationHandler.mineStarBlock.get();
            return serverMineStarBlock == null ? ConfigurationHandler.mineStarBlock.get() : serverMineStarBlock;
        }

        public static boolean getSizeChangesHitbox() {
            if (!serverConnection)
                return ConfigurationHandler.sizeChangesHitbox.get();
            return serverSizeChangesHitbox == null ? ConfigurationHandler.sizeChangesHitbox.get() : serverSizeChangesHitbox;
        }


        public static boolean getHitboxGrowsPastHuman() {
            if (!serverConnection)
                return ConfigurationHandler.hitboxGrowsPastHuman.get();
            return serverHitboxGrowsPastHuman == null ? ConfigurationHandler.hitboxGrowsPastHuman.get() : serverHitboxGrowsPastHuman;
        }


        public static boolean getStartWithWings() {
            if (!serverConnection)
                return ConfigurationHandler.startWithWings.get();
            return serverStartWithWings == null ? ConfigurationHandler.startWithWings.get() : serverStartWithWings;
        }
        
        public static boolean getEnableDragonDebuffs() {
            if (!serverConnection)
                return ConfigurationHandler.GENERAL.enableDragonDebuffs.get();
            return serverDragonDebuffs == null ? ConfigurationHandler.GENERAL.enableDragonDebuffs.get() : serverDragonDebuffs;
        }
        

    }

    @SubscribeEvent
    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void ServerPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        ServerPlayerEntity player = (ServerPlayerEntity) event.getPlayer();
        DragonSurvivalMod.CHANNEL.send(PacketDistributor.PLAYER.with(() -> player), new SyncConfig(maxFlightSpeed.get(), mineStarBlock.get(), sizeChangesHitbox.get(), hitboxGrowsPastHuman.get(), startWithWings.get(), GENERAL.enableDragonDebuffs.get()));
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void ClientPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        NetworkedConfig.setServerConnection(false);
    }
}