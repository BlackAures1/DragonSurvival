package by.jackraidenph.dragonsurvival.capability;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapability;
import by.jackraidenph.dragonsurvival.network.PacketSyncCapabilityMovement;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Optional;


public class DragonStateHandler {
    private boolean isDragon;
    private boolean isHiding;
    private DragonType type = DragonType.NONE;
    private int level;
    private Optional<DragonMovementData> data = Optional.empty();

    public boolean getIsDragon() {
        return this.isDragon;
    }

    public void setIsDragon(boolean isDragon) {
        this.isDragon = isDragon;
    }

    public boolean getIsHiding() {
        return isHiding;
    }

    public void setIsHiding(boolean hiding) {
        isHiding = hiding;
    }

    public void setMovementData(DragonMovementData data, boolean doSync) {
        if (doSync)
            DragonSurvivalMod.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketSyncCapabilityMovement(data));
        this.data = Optional.of(data);
    }

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
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

    public void syncCapabilityData(boolean isServer) {
        if (isServer) {
            DragonSurvivalMod.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketSyncCapability(this.isDragon, this.isHiding, this.getType(), this.level));
        } else {
            DragonSurvivalMod.INSTANCE.sendToServer(new PacketSyncCapability(this.isDragon, this.isHiding, this.getType(), this.level));
        }
    }

    public void syncMovement(boolean isServer) {
        this.data.ifPresent(dat -> {
            if (isServer) {
                DragonSurvivalMod.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketSyncCapabilityMovement(dat));
            } else {
                DragonSurvivalMod.INSTANCE.sendToServer(new PacketSyncCapabilityMovement(dat));
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
