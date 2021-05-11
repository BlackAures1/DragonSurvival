package by.jackraidenph.dragonsurvival.capability;

import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.UUID;


public class DragonStateHandler {
    private boolean isDragon;
    private boolean isHiding;
    private DragonType type = DragonType.NONE;

    private final DragonMovementData data = new DragonMovementData(0, 0, 0);
    private boolean hasWings;
    private float size;
    /**
     * Base damage
     */
    private int baseDamage;

    public void setBaseDamage(int baseDamage) {
        this.baseDamage = baseDamage;
    }

    public void setBaseDamage(int baseDamage, PlayerEntity playerEntity) {
        setBaseDamage(baseDamage);
        playerEntity.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(baseDamage);
    }

    public int getBaseDamage() {
        return baseDamage;
    }

    public float getSize() {
        return size;
    }

    /**
     * Sets the size and initial health
     */
    public void setSize(float size, PlayerEntity playerEntity) {
        setSize(size);
    	AttributeModifier mod = buildHealthMod(size);
        updateHealthModifier(playerEntity, mod);
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
    	return Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH)).getModifier(UUID.fromString("03574e62-f9e4-4f1b-85ad-fde00915e446"));
    }
    
    public static AttributeModifier buildHealthMod(Float size) {
    	return new AttributeModifier(
        		UUID.fromString("03574e62-f9e4-4f1b-85ad-fde00915e446"),
    			"Dragon Health Adjustment",
    			(size - 20),
    			AttributeModifier.Operation.ADDITION
    		);
    }

    public static void updateHealthModifier(PlayerEntity player, AttributeModifier mod) {
    	float oldMax = player.getMaxHealth();
    	ModifiableAttributeInstance max = Objects.requireNonNull(player.getAttribute(Attributes.MAX_HEALTH));
    	max.removeModifier(mod);
    	max.addPermanentModifier(mod);
    	float newHealth = player.getHealth() * player.getMaxHealth() / oldMax;
    	player.setHealth(newHealth);
    }
    
    public void setMovementData(double bodyYaw, double headYaw, double headPitch) {
        data.bodyYaw = bodyYaw;
        data.headYaw = headYaw;
        data.headPitch = headPitch;
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

        public DragonMovementData(double bodyYaw, double headYaw, double headPitch) {
            this.bodyYaw = bodyYaw;
            this.headYaw = headYaw;
            this.headPitch = headPitch;
            this.headYawLastTick = headYaw;
            this.headPitchLastTick = headPitch;
            this.bodyYawLastTick = bodyYaw;
        }

    }
}
