package by.jackraidenph.dragonsurvival.capability;

import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;


public class DragonStateHandler {
    private boolean isDragon;
    private boolean isHiding;
    private DragonType type = DragonType.NONE;
    private final DragonMovementData movementData = new DragonMovementData(0, 0, 0, false);
    private boolean hasWings;
    private float size;
    private final DragonDebuffData debuffData = new DragonDebuffData(0, 0);
    
    public static final UUID HEALTH_MODIFIER_UUID = UUID.fromString("03574e62-f9e4-4f1b-85ad-fde00915e446");
    public static final UUID DAMAGE_MODIFIER_UUID = UUID.fromString("5bd3cebc-132e-4f9d-88ef-b686c7ad1e2c");
    
    
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
        return this.isDragon;
    }

    public void setIsDragon(boolean isDragon) {
        this.isDragon = isDragon;
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
    			if (harvestLevel <= 0)
    				return true;
    			break;
    		case YOUNG:
            case ADULT:
            	if (harvestLevel == 1) {
                    switch (getType()) {
                        case SEA:
                            if (state.isToolEffective(ToolType.SHOVEL))
                            	return true;
                        case CAVE:
                            if (state.isToolEffective(ToolType.PICKAXE))
                            	return true;
                            break;
                        case FOREST:
                            if (state.isToolEffective(ToolType.AXE))
                                return true;
                    }
                } else if (harvestLevel <= 0)
                	return true;
            	break;
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
    
    public static AttributeModifier buildHealthMod(Float size) {
    	return new AttributeModifier(
    			HEALTH_MODIFIER_UUID,
    			"Dragon Health Adjustment",
    			(size - 20),
    			AttributeModifier.Operation.ADDITION
    		);
    }
    
    public static AttributeModifier buildDamageMod(DragonLevel level, boolean isDragon) {
    	return new AttributeModifier(
    			DAMAGE_MODIFIER_UUID,
    			"Dragon Damage Adjustment",
    			isDragon ? (level.baseDamage - 1) : 0,
    			AttributeModifier.Operation.ADDITION
    		);
    }

    public static void updateModifiers(PlayerEntity oldPlayer, PlayerEntity newPlayer) {
    	AttributeModifier oldMod = DragonStateHandler.getHealthModifier(oldPlayer);
        if (oldMod != null)
            DragonStateHandler.updateHealthModifier(newPlayer, oldMod);
        oldMod = DragonStateHandler.getDamageModifier(oldPlayer);
        if (oldMod != null)
            DragonStateHandler.updateDamageModifier(newPlayer, oldMod);
    }
    
    
    public static void updateHealthModifier(PlayerEntity player, AttributeModifier mod) {
    	float oldMax = player.getMaxHealth();
    	ModifiableAttributeInstance max = Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH));
    	max.removeModifier(mod);
    	max.addPermanentModifier(mod);
    	float newHealth = player.getHealth() * player.getMaxHealth() / oldMax;
    	player.setHealth(newHealth);
    }
    
    public static void updateDamageModifier(PlayerEntity player, AttributeModifier mod) {
    	ModifiableAttributeInstance max = Objects.requireNonNull(player.getAttribute(Attributes.ATTACK_DAMAGE));
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
    
    public void setDebuffData(int timeWithoutWater, int timeInDarkness) {
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
    	public int timeWithoutWater;
    	public int timeInDarkness;
    	
    	public DragonDebuffData(int timeWithoutWater, int timeInDarkness) {
    		this.timeWithoutWater = timeWithoutWater;
    		this.timeInDarkness = timeInDarkness;
    	}
    }
}
