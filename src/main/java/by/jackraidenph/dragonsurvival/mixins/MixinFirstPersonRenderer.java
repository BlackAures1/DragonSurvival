package by.jackraidenph.dragonsurvival.mixins;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.mojang.blaze3d.matrix.MatrixStack;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.handlers.DragonFoodHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.FirstPersonRenderer;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

@Mixin(FirstPersonRenderer.class)
public class MixinFirstPersonRenderer {
	
	@Shadow
	@Final
	public Minecraft minecraft;

	@Inject(at = @At(value = "HEAD"), method = "applyEatTransform", cancellable = true, expect = 1)
	public void applyDragonEatTransform(MatrixStack p_228398_1_, float p_228398_2_, HandSide p_228398_3_, ItemStack p_228398_4_, CallbackInfo ci) {
		DragonStateProvider.getCap(minecraft.player).ifPresent(dragonStateHandler -> {
			if (dragonStateHandler.isDragon()) {
				float f = (float)this.minecraft.player.getUseItemRemainingTicks() - p_228398_2_ + 1.0F;
				float f1 = f / (float)DragonFoodHandler.getUseDuration(p_228398_4_, dragonStateHandler.getType());
				if (f1 < 0.8F) {
					float f2 = MathHelper.abs(MathHelper.cos(f / 4.0F * (float)Math.PI) * 0.1F);
					p_228398_1_.translate(0.0D, (double)f2, 0.0D);
				}
			
				float f3 = 1.0F - (float)Math.pow((double)f1, 27.0D);
				int i = p_228398_3_ == HandSide.RIGHT ? 1 : -1;
				p_228398_1_.translate((double)(f3 * 0.6F * (float)i), (double)(f3 * -0.5F), (double)(f3 * 0.0F));
				p_228398_1_.mulPose(Vector3f.YP.rotationDegrees((float)i * f3 * 90.0F));
				p_228398_1_.mulPose(Vector3f.XP.rotationDegrees(f3 * 10.0F));
				p_228398_1_.mulPose(Vector3f.ZP.rotationDegrees((float)i * f3 * 30.0F));
				
				ci.cancel();
			}
		});
	}
	
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/FirstPersonRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/matrix/MatrixStack;Lnet/minecraft/util/HandSide;F)V", ordinal = 2), method = "renderArmWithItem", cancellable = true, expect = 1)
	public void renderDragonArmWithItem(AbstractClientPlayerEntity p_228405_1_, float p_228405_2_, float p_228405_3_, Hand p_228405_4_, float p_228405_5_, ItemStack p_228405_6_, float p_228405_7_, MatrixStack p_228405_8_, IRenderTypeBuffer p_228405_9_, int p_228405_10_, CallbackInfo ci) {
		DragonStateProvider.getCap(minecraft.player).ifPresent(dragonStateHandler -> {
			if (dragonStateHandler.isDragon() && DragonFoodHandler.isDragonEdible(p_228405_6_.getItem(), dragonStateHandler.getType()))
				this.applyEatTransform(p_228405_8_, p_228405_2_, p_228405_4_ == Hand.MAIN_HAND ? p_228405_1_.getMainArm() : p_228405_1_.getMainArm().getOpposite(), p_228405_6_);
		});
	
	}

	@Shadow
	public void applyEatTransform(MatrixStack p_228405_8_, float p_228405_2_, HandSide handSide, ItemStack p_228405_6_) {
		throw new IllegalStateException("Mixin failed to shadow applyEatTransform()");
	}
	
	
	
}
