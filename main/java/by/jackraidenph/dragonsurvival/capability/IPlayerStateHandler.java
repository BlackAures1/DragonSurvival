package by.jackraidenph.dragonsurvival.capability;

import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.util.math.Vec3d;

public interface IPlayerStateHandler {

    void setNeckLength(double neckLength);

    boolean getIsDragon();

    void setData(PlayerStateHandler.DragonData data);

    void setType(DragonType type);

    void setLevel(int level);

    void setMovementData(double bodyYaw, double headYaw, double headPitch, Vec3d headPos, Vec3d tailPos);

    void setMovementLastTick(double bodyYawLastTick, double headYawLastTick, double headPitchLastTick, Vec3d headPosLastTick, Vec3d tailPosLastTick);
}
