package by.jackraidenph.dragonsurvival.renderer;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.registration.BlockInit;
import by.jackraidenph.dragonsurvival.tiles.HelmetEntity;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.SkullBlock;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.GenericHeadModel;
import net.minecraft.client.renderer.entity.model.HumanoidHeadModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

import javax.annotation.Nullable;
import java.util.Map;

public class HelmetEntityRenderer extends TileEntityRenderer<HelmetEntity> {

    public HelmetEntityRenderer(TileEntityRendererDispatcher tileEntityRendererDispatcher) {
        super(tileEntityRendererDispatcher);
    }

    private static final Map<Block, ResourceLocation> TEXTURE_BY_TYPE = Util.make(Maps.newHashMap(), (resourceLocationHashMap) -> {
        resourceLocationHashMap.put(BlockInit.helmet1, new ResourceLocation(DragonSurvivalMod.MODID, "textures/block/broken_knight_helmet_0.png"));
        resourceLocationHashMap.put(BlockInit.helmet2, new ResourceLocation(DragonSurvivalMod.MODID, "textures/block/broken_knight_helmet_1.png"));
        resourceLocationHashMap.put(BlockInit.helmet3, new ResourceLocation(DragonSurvivalMod.MODID, "textures/block/broken_knight_helmet_2.png"));
    });
    static GenericHeadModel humanoidHeadModel = new HumanoidHeadModel();

    public void render(HelmetEntity helmetEntity, float p_225616_2_, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_225616_5_, int p_225616_6_) {
        BlockState blockstate = helmetEntity.getBlockState();
        float f1 = 22.5F * blockstate.getValue(SkullBlock.ROTATION);
        renderHelmet(null, f1, blockstate.getBlock(), 0, matrixStack, renderTypeBuffer, p_225616_5_);
    }

    public static void renderHelmet(@Nullable Direction direction, float p_228879_1_, Block helmetBlock, float p_228879_4_, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer, int p_228879_7_) {
        matrixStack.pushPose();
        if (direction == null) {
            matrixStack.translate(0.5D, 0.0D, 0.5D);
        } else {
            matrixStack.translate(0.5F - (float) direction.getStepX() * 0.25F, 0.25D, 0.5F - (float) direction.getStepZ() * 0.25F);
        }

        matrixStack.scale(-1.0F, -1.0F, 1.0F);
        IVertexBuilder ivertexbuilder = renderTypeBuffer.getBuffer(getRenderType(helmetBlock));
        humanoidHeadModel.setupAnim(p_228879_4_, p_228879_1_, 0.0F);
        humanoidHeadModel.renderToBuffer(matrixStack, ivertexbuilder, p_228879_7_, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();
    }

    private static RenderType getRenderType(Block block) {
        ResourceLocation resourcelocation = TEXTURE_BY_TYPE.get(block);
        return RenderType.entityCutoutNoCullZOffset(resourcelocation);
    }
}