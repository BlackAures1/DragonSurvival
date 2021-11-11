package by.jackraidenph.dragonsurvival.capability;

import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;


public class DragonStateHandler {
    private boolean isHiding;
    private DragonType type = DragonType.NONE;
    private final DragonMovementData movementData = new DragonMovementData(0, 0, 0, false);
    private boolean hasWings;
    private float size;
    private final DragonDebuffData debuffData = new DragonDebuffData(0, 0);
    private int lavaAirSupply;
    private int passengerId;
    
    public static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("03574e62-f9e4-4f1b-85ad-fde00915e446");
    public static final UUID DAMAGE_MODIFIER_UUID = UUID.fromString("5bd3cebc-132e-4f9d-88ef-b686c7ad1e2c");
    public static final UUID SWIM_SPEED_MODIFIER_UUID = UUID.fromString("2a9341f3-d19e-446c-924b-7cf2e5259e10");
    
    public float getSize() {
        return size;
    }

    /**
     * Sets the size, health and base damage
     */
    public void setSize(float size, PlayerEntity playerEntity) {
        setSize(size);
    	AttributeModifier healthMod = buildHealthMod(size);
        updateHealthModifier(playerEntity, healthMod);
        AttributeModifier damageMod = buildDamageMod(getLevel(), isDragon());
        updateDamageModifier(playerEntity, damageMod);
        AttributeModifier swimSpeedMod = buildSwimSpeedMod(getType());
        updateSwimSpeedModifier(playerEntity, swimSpeedMod);
    }
    
    public void setSize(float size) {
    	this.size = size;
    }

    public boolean hasWings() {
        return hasWings;
    }

    public void setHasWings(boolean hasWings) {
        this.hasWings = hasWings;
    }

    public boolean isDragon() {
        return this.type != DragonType.NONE;
    }

    public boolean isHiding() {
        return isHiding;
    }

    public void setIsHiding(boolean hiding) {
        isHiding = hiding;
    }

    public DragonLevel getLevel() {
        if (size < 20F)
        	return DragonLevel.BABY;
        else if (size < 30F)
        	return DragonLevel.YOUNG;
        else
        	return DragonLevel.ADULT;
    }

    public boolean canHarvestWithPaw(BlockState state) {
    	int harvestLevel = state.getHarvestLevel();
    	switch(getLevel()) {
    		case BABY:
    			if (ConfigHandler.SERVER.bonusUnlockedAt.get() != DragonLevel.BABY){
    			    if (harvestLevel <= ConfigHandler.SERVER.baseHarvestLevel.get())
                        return true;
    			    break;
                }
    		case YOUNG:
    		    if (ConfigHandler.SERVER.bonusUnlockedAt.get() == DragonLevel.ADULT && getLevel() != DragonLevel.BABY){
    		        if (harvestLevel <= ConfigHandler.SERVER.baseHarvestLevel.get())
                        return true;
    		        break;
                }
            case ADULT:
            	if (harvestLevel <= ConfigHandler.SERVER.bonusHarvestLevel.get()) {
                    switch (getType()) {
                        case SEA:
                            if (state.isToolEffective(ToolType.SHOVEL))
                            	return true;
                            break;
                        case CAVE:
                            if (state.isToolEffective(ToolType.PICKAXE))
                            	return true;
                            break;
                        case FOREST:
                            if (state.isToolEffective(ToolType.AXE))
                                return true;
                    }
                }
            	if (harvestLevel <= ConfigHandler.SERVER.baseHarvestLevel.get())
                    return true;
    	}
    	return false;
    }
    
    @Nullable
    public static AttributeModifier getHealthModifier(PlayerEntity player) {
    	return Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).getModifier(HEALTH_MODIFIER_UUID);
    }
    
    @Nullable
    public static AttributeModifier getDamageModifier(PlayerEntity player) {
    	return Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_DAMAGE)).getModifier(DAMAGE_MODIFIER_UUID);
    }
    
    @Nullable
    public static AttributeModifier getSwimSpeedModifier(PlayerEntity player) {
    	return Objects.requireNonNull(player.getAttribute(ForgeMod.SWIM_SPEED.get())).getModifier(SWIM_SPEED_MODIFIER_UUID);
    }
    
    
    public static AttributeModifier buildHealthMod(float size) {
    	return new AttributeModifier(
    			HEALTH_MODIFIER_UUID,
    			"Dragon Health Adjustment",
    			((float)ConfigHandler.SERVER.minHealth.get() + (((size - 14) / 26F) * ((float)ConfigHandler.SERVER.maxHealth.get() - (float)ConfigHandler.SERVER.minHealth.get()))) - 20,
    			AttributeModifier.Operation.ADDITION
    		);
    }
    
    public static AttributeModifier buildDamageMod(DragonLevel level, boolean isDragon) {
    	return new AttributeModifier(
    			DAMAGE_MODIFIER_UUID,
    			"Dragon Damage Adjustment",
    			isDragon ? (level == DragonLevel.ADULT ? ConfigHandler.SERVER.adultBonusDamage.get() : level == DragonLevel.YOUNG ? ConfigHandler.SERVER.youngBonusDamage.get() : ConfigHandler.SERVER.babyBonusDamage.get()) : 0,
    			AttributeModifier.Operation.ADDITION
    		);
    }
    
    public static AttributeModifier buildSwimSpeedMod(DragonType dragonType) {
    	return new AttributeModifier(
    			SWIM_SPEED_MODIFIER_UUID,
    			"Dragon Swim Speed Adjustment",
    			dragonType == DragonType.SEA && ConfigHandler.SERVER.seaSwimmingBonuses.get() ? 1 : 0,
    			AttributeModifier.Operation.ADDITION
    		);
    }

    public static void updateModifiers(PlayerEntity oldPlayer, PlayerEntity newPlayer) {
    	AttributeModifier oldMod = getHealthModifier(oldPlayer);
        if (oldMod != null)
            updateHealthModifier(newPlayer, oldMod);
        oldMod = getDamageModifier(oldPlayer);
        if (oldMod != null)
            updateDamageModifier(newPlayer, oldMod);
        oldMod =getSwimSpeedModifier(oldPlayer);
        if (oldMod != null)
        	updateSwimSpeedModifier(newPlayer, oldMod);
    }
    
    
    public static void updateHealthModifier(PlayerEntity player, AttributeModifier mod) {
    	if (!ConfigHandler.SERVER.healthAdjustments.get())
    		return;
    	float oldMax = player.getMaxHealth();
    	ModifiableAttributeInstance max = Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH));
    	max.removeModifier(mod);
    	max.addPermanentModifier(mod);
    	float newHealth = player.getHealth() * player.getMaxHealth() / oldMax;
    	player.setHealth(newHealth);
    }
    
    public static void updateDamageModifier(PlayerEntity player, AttributeModifier mod) {
    	if (!ConfigHandler.SERVER.bonuses.get() || !ConfigHandler.SERVER.attackDamage.get())
    		return;
    	ModifiableAttributeInstance max = Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_DAMAGE));
    	max.removeModifier(mod);
    	max.addPermanentModifier(mod);
    }
    
    public static void updateSwimSpeedModifier(PlayerEntity player, AttributeModifier mod) {
    	if (!ConfigHandler.SERVER.bonuses.get() || !ConfigHandler.SERVER.seaSwimmingBonuses.get())
    		return;
    	ModifiableAttributeInstance max = Objects.requireNonNull(player.getAttribute(ForgeMod.SWIM_SPEED.get()));
    	max.removeModifier(mod);
    	max.addPermanentModifier(mod);
    }
    
    public void setMovementData(double bodyYaw, double headYaw, double headPitch, boolean bite) {
        movementData.bodyYaw = bodyYaw;
        movementData.headYaw = headYaw;
        movementData.headPitch = headPitch;
        movementData.bite = bite;
    }

    public DragonMovementData getMovementData() {
        return this.movementData;
    }
    
    public void setDebuffData(double timeWithoutWater, int timeInDarkness) {
    	debuffData.timeWithoutWater = timeWithoutWater;
    	debuffData.timeInDarkness = timeInDarkness;
    }
    
    public DragonDebuffData getDebuffData() {
    	return this.debuffData;
    }

    public DragonType getType() {
        return this.type;
    }

    public void setType(DragonType type) {
        this.type = type;
    }
    
    public int getLavaAirSupply() {
    	return this.lavaAirSupply;
    }
    
    public void setLavaAirSupply(int lavaAirSupply) {
    	this.lavaAirSupply = lavaAirSupply;
    }

    public int getPassengerId() {
        return this.passengerId;
    }

    public void setPassengerId( int passengerId){
        this.passengerId = passengerId;
    }

    public static class DragonMovementData {
        public double bodyYaw;
        public double headYaw;
        public double headPitch;

        public double headYawLastTick;
        public double headPitchLastTick;
        public double bodyYawLastTick;

        public boolean bite;
        
        public DragonMovementData(double bodyYaw, double headYaw, double headPitch, boolean bite) {
            this.bodyYaw = bodyYaw;
            this.headYaw = headYaw;
            this.headPitch = headPitch;
            this.headYawLastTick = headYaw;
            this.headPitchLastTick = headPitch;
            this.bodyYawLastTick = bodyYaw;
            this.bite = bite;
        }
    }
    
    public static class DragonDebuffData {
    	public double timeWithoutWater;
    	public int timeInDarkness;
    	
    	public DragonDebuffData(double timeWithoutWater, int timeInDarkness) {
    		this.timeWithoutWater = timeWithoutWater;
    		this.timeInDarkness = timeInDarkness;
    	}
    }
}
