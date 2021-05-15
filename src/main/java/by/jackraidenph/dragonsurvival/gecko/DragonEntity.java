package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.handlers.ClientEvents;
import by.jackraidenph.dragonsurvival.handlers.DragonSizeHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.Collections;

public class DragonEntity extends LivingEntity implements IAnimatable {
    AnimationFactory animationFactory = new AnimationFactory(this);
    /**
     * This reference must be updated whenever player is remade, for example, when changing dimensions
     */
    public volatile int player;
    
    public DragonEntity(EntityType<? extends LivingEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 2, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        final PlayerEntity player = getPlayer();
        final AnimationController animationController = animationEvent.getController();
        AnimationBuilder builder = new AnimationBuilder();
        if (player != null) {
        	DragonStateProvider.getCap(player).ifPresent(playerStateHandler -> {
	        	Vector3d motio = playerStateHandler.getMovementData().deltaMovement;
	            if (motio == null)
	            	motio = new Vector3d(0, 0, 0);
	            boolean isMovingHorizontal = Math.sqrt(Math.pow(motio.x, 2) + Math.pow(motio.z, 2)) > 0.005;
	            // Main
	            if (player.isSleeping())
	            	builder.addAnimation("animation.dragon.sleep", true);
	            else if (playerStateHandler.getMovementData().bite)
	                builder.addAnimation("animation.dragon.bite");
	            else if (player.getPose() == Pose.SWIMMING)
	            	builder.addAnimation("animation.dragon.swim_fast", true);
	            else if ((player.isInLava() || player.isInWaterOrBubble()) && isMovingHorizontal)
	                builder.addAnimation("animation.dragon.swim", true);
	            else if ((player.abilities.flying || ClientEvents.dragonsFlying.getOrDefault(player.getId(), false)) && !player.isOnGround() && !player.isInWater() && player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).hasWings())
	                builder.addAnimation("animation.dragon.fly_slow", true);
	            else if (ClientEvents.dragonsJumpingTicks.getOrDefault(this.player, 0) > 0)
	                builder.addAnimation("animation.dragon.jump", true);
	            else if (player.isShiftKeyDown() || (!DragonSizeHandler.canPoseFit(player, Pose.STANDING) && DragonSizeHandler.canPoseFit(player, Pose.CROUCHING))) {
	            	// Player is Sneaking
	            	 if (isMovingHorizontal && player.animationSpeed != 0f)
	                     builder.addAnimation("animation.dragon.sneak_walk", true);
	                 else if (ClientEvents.dragonsDigging.getOrDefault(this.player, false))
	                     builder.addAnimation("animation.dragon.dig_sneak", true);
	                 else
	                     builder.addAnimation("animation.dragon.sneak", true);
	            }
	            else if (player.isSprinting())
	                builder.addAnimation("animation.dragon.run", true);
	            else if (isMovingHorizontal && player.animationSpeed != 0f)
	                builder.addAnimation("animation.dragon.walk", true);
	            else if (ClientEvents.dragonsDigging.getOrDefault(this.player, false))
	                builder.addAnimation("animation.dragon.dig", true);
	            else
	            	builder.addAnimation("animation.dragon.idle", true);
	        });
        } else
        	 builder.addAnimation("animation.dragon.idle", true);
        animationController.setAnimation(builder);
        return PlayState.CONTINUE;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    @Override
    public HandSide getMainArm() {
        return HandSide.RIGHT;
    }

    PlayerEntity getPlayer() {
        return (PlayerEntity)Minecraft.getInstance().player.clientLevel.getEntity(player);
    }
}
