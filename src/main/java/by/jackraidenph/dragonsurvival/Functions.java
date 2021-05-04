package by.jackraidenph.dragonsurvival;

import org.lwjgl.opengl.GL11;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldVertexBufferUploader;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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
     * @param stack
     * @param startX   relative to left
     * @param startY   relative to top
     * @param textureX
     * @param textureY
     * @param width
     * @param height
     * @param sizeX    relative width
     * @param sizeY    relative height
     */
    public static void blit(MatrixStack stack, int startX, int startY, float textureX, float textureY, int width, int height, int sizeX, int sizeY) {
        //blit(stack, startX, startY, textureX, textureY, width, height, sizeX, sizeY);
    }
    
    public static void blit(MatrixStack stack, int startX, int startY, float textureX, float textureY, int width, int height, int sizeX, int sizeY, float alpha) {
    	//blit(stack, startX, startY, width, height, textureX, textureY, width, height, sizeX, sizeY);
    	//innerBlit(stack, startX, startX + width, startY, startY + height, 0, width, height, textureX, textureY, sizeX, sizeY);
    	//innerBlit(stack.last().pose(), startX, startX + width, startY, startY + height, 0, (textureX + 0.0F) / (float)sizeX, (textureX + (float)width) / (float)sizeX, (textureY + 0.0F) / (float)sizeY, (textureY + (float)height) / (float)sizeY);
    	BufferBuilder bufferbuilder = Tessellator.getInstance().getBuilder();
        bufferbuilder.begin(7, DefaultVertexFormats.POSITION_TEX);
        //bufferbuilder.vertex(p_238461_0_, (float)p_238461_1_, (float)p_238461_4_, (float)p_238461_5_).uv(p_238461_6_, p_238461_9_).endVertex();
        //bufferbuilder.vertex(p_238461_0_, (float)p_238461_2_, (float)p_238461_4_, (float)p_238461_5_).uv(p_238461_7_, p_238461_9_).endVertex();
        //bufferbuilder.vertex(p_238461_0_, (float)p_238461_2_, (float)p_238461_3_, (float)p_238461_5_).uv(p_238461_7_, p_238461_8_).endVertex();
        //bufferbuilder.vertex(p_238461_0_, (float)p_238461_1_, (float)p_238461_3_, (float)p_238461_5_).uv(p_238461_6_, p_238461_8_).endVertex();
        bufferbuilder.end();
        //RenderSystem.enableAlphaTest();
        RenderSystem.assertThread(RenderSystem::isOnGameThread);
        GlStateManager._enableAlphaTest();
        GL11.glEnable(GL11.GL_BLEND);
        
        WorldVertexBufferUploader.end(bufferbuilder);
    }

}
