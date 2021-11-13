package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.handlers.ClientEvents;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

public class DragonRenderer extends GeoEntityRenderer<DragonEntity> {
	public ResourceLocation glowTexture = null;
	
	public DragonRenderer(EntityRendererManager renderManager, AnimatedGeoModel<DragonEntity> modelProvider) {
        super(renderManager, modelProvider);
    }
	
	@Override
	public void render(DragonEntity entity, float entityYaw, float partialTicks, MatrixStack stack, IRenderTypeBuffer bufferIn, int packedLightIn)
	{
		super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
		
		if (glowTexture != null) {
			if(Minecraft.getInstance().textureManager.getTexture(glowTexture) != MissingTextureSprite.getTexture()) {
				ClientEvents.dragonModel.setCurrentTexture(glowTexture);
				
				renderType = RenderType.entityCutoutNoCullZOffset(glowTexture);
				super.render(entity, entityYaw, partialTicks, stack, bufferIn, 200);
				renderType = null;
			}
		}
	}
	
	public Color renderColor = new Color(255, 255, 255);
	public RenderType renderType;
    @Override
    public RenderType getRenderType(DragonEntity animatable, float partialTicks, MatrixStack stack,
			@Nullable IRenderTypeBuffer renderTypeBuffer, @Nullable IVertexBuilder vertexBuilder, int packedLightIn,
			ResourceLocation textureLocation) {
		return renderType == null ? RenderType.entityCutout(textureLocation) : renderType;
	}
<<<<<<< Updated upstream
=======
	
	@Override
	public Color getRenderColor(DragonEntity animatable, float partialTicks, MatrixStack stack,
			@Nullable IRenderTypeBuffer renderTypeBuffer,
			@Nullable IVertexBuilder vertexBuilder, int packedLightIn)
	{
		return renderColor;
	}
>>>>>>> Stashed changes
}
