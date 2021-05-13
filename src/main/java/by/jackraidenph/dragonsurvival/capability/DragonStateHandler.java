package by.jackraidenph.dragonsurvival.capability;

import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;


public class DragonStateHandler {
    private boolean isDragon;
    private boolean isHiding;
    private DragonType type = DragonType.NONE;
    private final DragonMovementData data = new DragonMovementData(0, 0, 0, new Vector3d(0, 0, 0), false);
    private boolean hasWings;
    private float size;
    
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
    
    public void setMovementData(double bodyYaw, double headYaw, double headPitch, Vector3d deltaMovement, boolean bite) {
        data.bodyYaw = bodyYaw;
        data.headYaw = headYaw;
        data.headPitch = headPitch;
        data.deltaMovement = deltaMovement;
        data.bite = bite;
    }

    public DragonMovementData getMovementData() {
        return this.data;
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

        public Vector3d deltaMovement;
        public boolean bite;
        
        public DragonMovementData(double bodyYaw, double headYaw, double headPitch, Vector3d deltaMovement, boolean bite) {
            this.bodyYaw = bodyYaw;
            this.headYaw = headYaw;
            this.headPitch = headPitch;
            this.headYawLastTick = headYaw;
            this.headPitchLastTick = headPitch;
            this.bodyYawLastTick = bodyYaw;
            this.deltaMovement = deltaMovement;
            this.bite = bite;
        }

    }
}
