package by.jackraidenph.dragonsurvival;

import net.minecraft.client.gui.AbstractGui;
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

    /**
     * @param startX   relative to left
     * @param startY   relative to top
     * @param textureX
     * @param textureY
     * @param width
     * @param height
     * @param sizeX    relative width
     * @param sizeY    relative height
     */
    public static void blit(int startX, int startY, float textureX, float textureY, int width, int height, int sizeX, int sizeY) {
        AbstractGui.blit(startX, startY, textureX, textureY, width, height, sizeX, sizeY);
    }

}
