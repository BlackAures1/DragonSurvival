package by.jackraidenph.dragonsurvival.capability;

import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;


public class DragonStateHandler {
    private boolean isDragon;
    private boolean isHiding;
    private DragonType type = DragonType.NONE;
    private DragonLevel level = DragonLevel.BABY;
    /**
     * Current health, must be equal to the player's health
     */
    private float health = level.initialHealth;
    private final DragonMovementData data = new DragonMovementData(0, 0, 0, Vec3d.ZERO, Vec3d.ZERO);

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
        return this.level;
    }

    public void setLevel(DragonLevel level) {
        this.level = level;
    }

    public void setHealth(float health) {
        this.health = health;
    }

    public float getHealth() {
        return health;
    }

    /**
     * Sets the level and initial health
     */
    public void setLevel(DragonLevel level, PlayerEntity playerEntity) {
        setLevel(level);
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(level.initialHealth);
        playerEntity.heal(playerEntity.getMaxHealth());
        setHealth(level.initialHealth);
    }

    public void setLevelAndHealth(DragonLevel level, float health, PlayerEntity playerEntity) {
        setLevel(level);
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(health);
        playerEntity.heal(playerEntity.getMaxHealth());
        setHealth(health);
    }


    public void setMovementData(double bodyYaw, double headYaw, double headPitch, Vec3d headPos, Vec3d tailPos) {
        data.bodyYaw = bodyYaw;
        data.headYaw = headYaw;
        data.headPitch = headPitch;
        data.headPos = headPos;
        data.tailPos = tailPos;
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
        public Vec3d headPos;
        public Vec3d tailPos;

        public Vec3d headPosLastTick;
        public Vec3d tailPosLastTick;
        public double headYawLastTick;
        public double headPitchLastTick;
        public double bodyYawLastTick;

        public DragonMovementData(
                double bodyYaw,
                double headYaw,
                double headPitch,
                Vec3d headPos,
                Vec3d tailPos) {

            this.bodyYaw = bodyYaw;
            this.headYaw = headYaw;
            this.headPitch = headPitch;
            this.headPos = headPos;
            this.tailPos = tailPos;

            this.headPosLastTick = headPos;
            this.tailPosLastTick = tailPos;
            this.headYawLastTick = headYaw;
            this.headPitchLastTick = headPitch;
            this.bodyYawLastTick = bodyYaw;
        }

        void setMovementData(double bodyYaw, double headYaw, double headPitch, Vec3d headPos, Vec3d tailPos) {
            this.setMovementLastTick(this.bodyYaw, this.headYaw, this.headPitch, this.headPos, this.tailPos);
            this.bodyYaw = bodyYaw;
            this.headYaw = headYaw;
            this.headPitch = headPitch;
            this.headPos = headPos;
            this.tailPos = tailPos;
        }

        void setMovementLastTick(double bodyYawLastTick, double headYawLastTick, double headPitchLastTick, Vec3d headPosLastTick, Vec3d tailPosLastTick) {
            this.bodyYawLastTick = bodyYawLastTick;
            this.headYawLastTick = headYawLastTick;
            this.headPitchLastTick = headPitchLastTick;
            this.headPosLastTick = headPosLastTick;
            this.tailPosLastTick = tailPosLastTick;
        }
    }
}
