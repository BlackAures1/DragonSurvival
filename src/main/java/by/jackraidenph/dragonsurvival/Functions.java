package by.jackraidenph.dragonsurvival;

import net.minecraft.util.math.MathHelper;

public class Functions {
    public static float getDefaultXRightLimbRotation(float limbSwing, float swingAmount) {
        return MathHelper.cos((float) (limbSwing + Math.PI)) * swingAmount;
    }

    public static float getDefaultXLeftLimbRotation(float limbSwing, float swingAmount) {
        return MathHelper.cos(limbSwing) * swingAmount;
    }

    /**
     * Angle Y or Z
     */
    public static float getDefaultHeadYaw(float netYaw) {
        return netYaw * 0.017453292F;
    }

    /**
     * Angle X
     */
    public static float getDefaultHeadPitch(float pitch) {
        return pitch * 0.017453292F;
    }

    public static float degreesToRadians(float degrees) {
        return (float) (degrees * Math.PI / 180);
    }

}
