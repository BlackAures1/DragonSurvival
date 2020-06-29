package by.jackraidenph.dragonsurvival.capability;

import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.util.math.Vec3d;

import java.util.Optional;

public class PlayerStateHandler implements IPlayerStateHandler {
    private Optional<DragonData> data;

    @Override
    public void setNeckLength(double neckLength) {
        assert data.orElse(null) != null;
        data.orElse(null).setNeckLength(neckLength);
    }

    @Override
    public boolean getIsDragon() {
        return data.isPresent();
    }

    @Override
    public void setData(DragonData data) {
        this.data = Optional.ofNullable(data);
    }

    @Override
    public void setType(DragonType type) {
        data.orElse(null).setType(type);
    }

    @Override
    public void setLevel(int level) {
        data.orElse(null).setLevel(level);
    }

    @Override
    public void setMovementData(double bodyYaw, double headYaw, double headPitch, Vec3d headPos, Vec3d tailPos) {
        data.orElse(null).setMovementData(bodyYaw, headYaw, headPitch, headPos, tailPos);
    }

    @Override
    public void setMovementLastTick(double bodyYawLastTick, double headYawLastTick, double headPitchLastTick, Vec3d headPosLastTick, Vec3d tailPosLastTick) {
        data.orElse(null).setMovementLastTick(bodyYawLastTick, headYawLastTick, headPitchLastTick, headPosLastTick, tailPosLastTick);
    }

    public static class DragonData {
        DragonType type;
        int level;
        double bodyYaw;
        double neckLength;
        double headYaw;
        Vec3d headPos;
        Vec3d tailPos;
        Vec3d headPosLastTick;
        Vec3d tailPosLastTick;
        double headYawLastTick;
        double headPitchLastTick;
        double bodyYawLastTick;
        double headPitch;

        public DragonData(DragonType type, int level, double bodyYaw, double neckLength, double headYaw, Vec3d headPos, Vec3d tailPos, Vec3d headPosLastTick, Vec3d tailPosLastTick, double headPitch) {
            this.type = type;
            this.level = level;
            this.bodyYaw = bodyYaw;
            this.neckLength = neckLength;
            this.headYaw = headYaw;
            this.headPos = headPos;
            this.tailPos = tailPos;
            this.headPosLastTick = headPosLastTick;
            this.tailPosLastTick = tailPosLastTick;
            this.headPitch = headPitch;
        }

        void setNeckLength(double neckLength) {
            this.neckLength = neckLength;
        }

        void setType(DragonType type) {
            this.type = type;
        }

        void setLevel(int level) {
            this.level = level;
        }

        void setMovementData(double bodyYaw, double headYaw, double headPitch, Vec3d headPos, Vec3d tailPos) {
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
