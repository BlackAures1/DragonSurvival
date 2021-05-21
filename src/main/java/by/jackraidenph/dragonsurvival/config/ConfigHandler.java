package by.jackraidenph.dragonsurvival.config;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.common.Mod;
import org.apache.commons.lang3.tuple.Pair;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigHandler {

	public static final ClientConfig CLIENT;
	public static final ForgeConfigSpec clientSpec;
	public static final CommonConfig COMMON;
	public static final ForgeConfigSpec commonSpec;
	public static final ServerConfig SERVER;
	public static final ForgeConfigSpec serverSpec;
	
	static {
		final Pair<ClientConfig, ForgeConfigSpec> client = new ForgeConfigSpec.Builder().configure(ClientConfig::new);
		CLIENT = client.getLeft();
		clientSpec = client.getRight();
		final Pair<CommonConfig, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
		COMMON = common.getLeft();
		commonSpec = common.getRight();
		final Pair<ServerConfig, ForgeConfigSpec> server = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
		SERVER = server.getLeft();
		serverSpec = server.getRight();
	}
}
