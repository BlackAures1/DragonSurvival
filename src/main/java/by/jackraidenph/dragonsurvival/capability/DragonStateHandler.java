package by.jackraidenph.dragonsurvival.capability;

import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;


public class DragonStateHandler {
    private boolean isDragon;
    private boolean isHiding;
    private DragonType type = DragonType.NONE;
    private DragonLevel level = DragonLevel.BABY;
    @SuppressWarnings("OptionalUsedAsFieldOrParameterType")
    private Optional<DragonMovementData> data = Optional.empty();

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

//    public void setMovementData(DragonMovementData data, boolean doSync) {
//        if (doSync)
////            DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketSyncCapabilityMovement(data));
//        this.data = Optional.of(data);
//    }

    public DragonLevel getLevel() {
        return this.level;
    }

    public void setLevel(DragonLevel level) {
        this.level = level;
    }

    /**
     * Sets the level and initial health
     */
    public void setLevel(DragonLevel level, PlayerEntity playerEntity) {
        setLevel(level);
        playerEntity.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(level.initialHealth);
        playerEntity.heal(playerEntity.getMaxHealth());
    }


    public void setMovementData(double bodyYaw, double headYaw, double headPitch, Vec3d headPos, Vec3d tailPos) {
        this.data.ifPresent(dat -> {
            dat.setMovementData(bodyYaw, headYaw, headPitch, headPos, tailPos);
        });
    }

    public Optional<DragonMovementData> getMovementData() {
        return this.data;
    }

    public DragonType getType() {
        return this.type;
    }

    public void setType(DragonType type) {
        this.type = type;
    }

    /**
     * TODO remove
     * Synchronizes dragon capability data
     *
     * @param isServer is server side currently?
     */
    @Deprecated
    public void syncCapabilityData(boolean isServer) {
        if (isServer) {
//            DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketSyncCapability(this.isDragon, this.isHiding, this.getType(), this.level));
        } else {
//            DragonSurvivalMod.CHANNEL.sendToServer(new PacketSyncCapability(this.isDragon, this.isHiding, this.getType(), this.level));
        }
    }

    public void syncMovement(boolean isServer) {
        this.data.ifPresent(dat -> {
            if (isServer) {
//                DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketSyncCapabilityMovement(dat));
            } else {
//                DragonSurvivalMod.CHANNEL.sendToServer(new PacketSyncCapabilityMovement(dat));
            }
        });
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
