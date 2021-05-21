package by.jackraidenph.dragonsurvival.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.handlers.DragonFoodHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

@Mixin(PlayerInteractionManager.class)
public class MixinPlayerInteractionManager {

	
	@Inject(method = "useItem", at = @At(value = "INVOKE", 
			target = "Lnet/minecraft/item/ItemStack;use", 
			ordinal = 0, shift = Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
	public void dragonUseItem(ServerPlayerEntity player, World level, ItemStack stack, Hand hand, CallbackInfoReturnable<ActionResultType> ci, ActionResultType cancelResult, int i, int j) {
		DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
    		if (dragonStateHandler.isDragon()) {
    			ActionResult<ItemStack> actionresult = stack.use(level, player, hand);
    			ItemStack itemstack = actionresult.getObject();
    	         if (itemstack == stack && itemstack.getCount() == i && DragonFoodHandler.getUseDuration(itemstack, dragonStateHandler.getType()) <= 0 && itemstack.getDamageValue() == j) {
    	        	 ci.setReturnValue(actionresult.getResult());
    	         } else if (actionresult.getResult() == ActionResultType.FAIL && DragonFoodHandler.getUseDuration(itemstack, dragonStateHandler.getType()) > 0 && !player.isUsingItem()) {
    	        	 ci.setReturnValue(actionresult.getResult());
    	         } else {
    	        	 player.setItemInHand(hand, itemstack);
    	            if (this.isCreative()) {
    	               itemstack.setCount(i);
    	               if (itemstack.isDamageableItem() && itemstack.getDamageValue() != j) {
    	                  itemstack.setDamageValue(j);
    	               }
    	            }

    	            if (itemstack.isEmpty()) {
    	            	player.setItemInHand(hand, ItemStack.EMPTY);
    	            }

    	            if (!player.isUsingItem()) {
    	            	player.refreshContainer(player.inventoryMenu);
    	            }

    	            ci.setReturnValue(actionresult.getResult());
    	         }
    		}
		});
		
		
	}

	@Shadow
	public boolean isCreative() {
		throw new IllegalStateException("Mixin failed to shadow isCreative()");
	}
}
