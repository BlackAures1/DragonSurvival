package by.jackraidenph.dragonsurvival.capability;

import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;

public interface IPlayerStateHandler {

    boolean getIsDragon();

    void setData(PlayerStateHandler.DragonData data);

    void setMovementData(double bodyYaw, double headYaw, double headPitch, Vec3d headPos, Vec3d tailPos);

    void setMovementLastTick(double bodyYawLastTick, double headYawLastTick, double headPitchLastTick, Vec3d headPosLastTick, Vec3d tailPosLastTick);

    HashMap<String, Object> getMovementData();

    DragonType getType();

    void setType(DragonType type);

    int getLevel();

    void setLevel(int level);

    double getNeckLength();

    void setNeckLength(double neckLength);
}
