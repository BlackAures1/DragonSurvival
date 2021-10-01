package by.jackraidenph.dragonsurvival.util;

import by.jackraidenph.dragonsurvival.renderer.HelmetEntityRenderer;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.vector.Vector3f;

public class HelmetStackTileEntityRenderer extends ItemStackTileEntityRenderer {
    @Override
    public void renderByItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStack, IRenderTypeBuffer p_239207_4_, int p_239207_5_, int p_239207_6_) {
        if (transformType == ItemCameraTransforms.TransformType.GUI) {
            matrixStack.translate(0.5, -0.15, 0);
            matrixStack.mulPose(Vector3f.XP.rotationDegrees(45));
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(135));
        }
        BlockItem blockItem = (BlockItem) itemStack.getItem();
        HelmetEntityRenderer.renderHelmet(null, 0, blockItem.getBlock(), 0, matrixStack, p_239207_4_, p_239207_5_);
    }
}
