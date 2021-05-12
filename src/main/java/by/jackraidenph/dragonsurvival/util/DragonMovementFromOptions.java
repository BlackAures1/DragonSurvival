package by.jackraidenph.dragonsurvival.util;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.handlers.DragonSizeHandler;
import net.minecraft.client.GameSettings;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Pose;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DragonMovementFromOptions extends MovementInputFromOptions {
   private final GameSettings options;
   private final ClientPlayerEntity player;
   private final boolean sizeChangesHitbox;

   public DragonMovementFromOptions(GameSettings p_i1237_1_, ClientPlayerEntity player, boolean sizeChangesHitbox) {
      super(p_i1237_1_);
      this.options = p_i1237_1_;
      this.player = player;
      this.sizeChangesHitbox = sizeChangesHitbox;
   }

   public void tick(boolean p_225607_1_) {
      this.up = this.options.keyUp.isDown();
      this.down = this.options.keyDown.isDown();
      this.left = this.options.keyLeft.isDown();
      this.right = this.options.keyRight.isDown();
      this.forwardImpulse = this.up == this.down ? 0.0F : (this.up ? 1.0F : -1.0F);
      this.leftImpulse = this.left == this.right ? 0.0F : (this.left ? 1.0F : -1.0F);
      this.jumping = this.options.keyJump.isDown();
      this.shiftKeyDown = this.options.keyShift.isDown();
      DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
		    if (dragonStateHandler.isDragon()) {
	    	if ((player.isShiftKeyDown() || (!DragonSizeHandler.canPoseFit(player, Pose.STANDING) && DragonSizeHandler.canPoseFit(player, Pose.CROUCHING))) && 
	      		  !player.isInWaterOrBubble() && !player.isAutoSpinAttack()) {
	           this.leftImpulse = (float)((double)this.leftImpulse * 0.3D);
	           this.forwardImpulse = (float)((double)this.forwardImpulse * 0.3D);
				}
		    } else if (p_225607_1_) {
	    		this.leftImpulse = (float)((double)this.leftImpulse * 0.3D);
	            this.forwardImpulse = (float)((double)this.forwardImpulse * 0.3D);
		    }
      });
   }
}