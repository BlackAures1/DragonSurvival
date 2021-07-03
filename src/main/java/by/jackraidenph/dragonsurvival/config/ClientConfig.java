package by.jackraidenph.dragonsurvival.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class ClientConfig {

	// Movement
	public final ForgeConfigSpec.ConfigValue<DragonBodyMovementType> firstPersonBodyMovement;
    public final ForgeConfigSpec.ConfigValue<DragonBodyMovementType> thirdPersonBodyMovement;
    public final ForgeConfigSpec.BooleanValue dragonNameTags;
    
    ClientConfig(ForgeConfigSpec.Builder builder){
    	builder.push("client");
    	
    	// Movement
    	builder.push("movement");
    	firstPersonBodyMovement = builder
    			.comment("The type of body movement you use while in first person as a dragon.")
    			.defineEnum("firstPersonMovement", DragonBodyMovementType.VANILLA, DragonBodyMovementType.values());
    	thirdPersonBodyMovement = builder
    			.comment("The type of body movement you use while in third person as a dragon.")
    			.defineEnum("thirdPersonMovement", DragonBodyMovementType.DRAGON, DragonBodyMovementType.values());
    	builder.pop().push("nametag");
		dragonNameTags = builder
				.comment("Show name tags for dragons.")
				.define("dragonNameTags", false); // >:(
    }
	
}
