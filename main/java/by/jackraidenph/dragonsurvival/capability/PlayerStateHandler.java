package by.jackraidenph.dragonsurvival.capability;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.network.MessageSyncCapability;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.HashMap;
import java.util.Optional;

public class PlayerStateHandler implements IPlayerStateHandler {
    private Optional<DragonData> data = Optional.ofNullable(null);

    @Override
    public boolean getIsDragon() {
        return data.isPresent();
    }

    @Override
    public void setData(DragonData data) {
        if (this.data.isPresent())
            DragonSurvivalMod.INSTANCE.send(PacketDistributor.ALL.noArg(), new MessageSyncCapability(this.data.get()));
        this.data = Optional.ofNullable(data);
    }

    @Override
    public int getLevel() {
        return this.data.orElse(null).getLevel();
    }

    @Override
    public void setLevel(int level) {
        if (this.data.isPresent())
            DragonSurvivalMod.INSTANCE.send(PacketDistributor.ALL.noArg(), new MessageSyncCapability(this.data.get()));
        data.orElse(null).setLevel(level);
    }

    @Override
    public double getNeckLength() {
        return this.data.orElse(null).getNeckLength();
    }

    @Override
    public void setNeckLength(double neckLength) {
        if (this.data.isPresent())
            DragonSurvivalMod.INSTANCE.send(PacketDistributor.ALL.noArg(), new MessageSyncCapability(this.data.get()));
        data.orElse(null).setNeckLength(neckLength);
    }

    @Override
    public void setMovementData(double bodyYaw, double headYaw, double headPitch, Vec3d headPos, Vec3d tailPos) {
        if (this.data.isPresent())
            DragonSurvivalMod.INSTANCE.send(PacketDistributor.ALL.noArg(), new MessageSyncCapability(this.data.get()));
        data.orElse(null).setMovementData(bodyYaw, headYaw, headPitch, headPos, tailPos);
    }

    @Override
    public void setMovementLastTick(double bodyYawLastTick, double headYawLastTick, double headPitchLastTick, Vec3d headPosLastTick, Vec3d tailPosLastTick) {
        if (this.data.isPresent())
            DragonSurvivalMod.INSTANCE.send(PacketDistributor.ALL.noArg(), new MessageSyncCapability(this.data.get()));
        data.orElse(null).setMovementLastTick(bodyYawLastTick, headYawLastTick, headPitchLastTick, headPosLastTick, tailPosLastTick);
    }

   /* @Override
    public HashMap<String, Object> getMovementDataLastTick() {
        return this.data.orElse(null).getMovementDataLastTick();
    }*/

    @Override
    public HashMap<String, Object> getMovementData() {
        return this.data.orElse(null).getMovementData();
    }

    @Override
    public DragonType getType() {
        return this.data.orElse(null).getType();
    }

    @Override
    public void setType(DragonType type) {
        if (this.data.isPresent())
            DragonSurvivalMod.INSTANCE.send(PacketDistributor.ALL.noArg(), new MessageSyncCapability(this.data.orElse(null)));
        data.orElse(null).setType(type);
    }

    public static class DragonData {
        DragonType type;
        int level;
        double bodyYaw;
        double neckLength;
        double headYaw;
        double headPitch;
        Vec3d headPos;
        Vec3d tailPos;

        Vec3d headPosLastTick;
        Vec3d tailPosLastTick;
        double headYawLastTick;
        double headPitchLastTick;
        double bodyYawLastTick;

        public DragonData(DragonType type,
                          int level,
                          double bodyYaw,
                          double neckLength,
                          double headYaw,
                          double headPitch,
                          Vec3d headPos,
                          Vec3d tailPos) {
            this.type = type;
            this.level = level;
            this.bodyYaw = bodyYaw;
            this.neckLength = neckLength;
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

        public double getNeckLength() {
            return this.neckLength;
        }

        void setNeckLength(double neckLength) {
            this.neckLength = neckLength;
        }

        public int getLevel() {
            return this.level;
        }

        void setLevel(int level) {
            this.level = level;
        }

        public DragonType getType() {
            return this.type;
        }

        void setType(DragonType type) {
            this.type = type;
        }

        public HashMap<String, Object> getMovementData() {
            HashMap<String, Object> data = new HashMap<>();
            data.put("bodyYaw", this.bodyYaw);
            data.put("headYaw", this.headYaw);
            data.put("headPitch", this.headPitch);
            data.put("headPos", this.headPos);
            data.put("tailPos", this.tailPos);
            return data;
        }

        /*public HashMap<String, Object> getMovementDataLastTick() {
            HashMap<String, Object> data = new HashMap<>();
            data.put("bodyYawLastTick", this.bodyYawLastTick);
            data.put("headYawLastTick", this.headYawLastTick);
            data.put("headPitchLastTick", this.headPitchLastTick);
            data.put("headPosLastTick", this.headPosLastTick);
            data.put("tailPosLastTick", this.tailPosLastTick);
            return data;
        }*/
    }
}
