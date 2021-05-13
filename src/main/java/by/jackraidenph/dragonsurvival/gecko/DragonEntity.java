package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.handlers.ClientEvents;
import by.jackraidenph.dragonsurvival.handlers.DragonSizeHandler;
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
        if (player != null) {
            Vector3d motio = player.getDeltaMovement();
            final AnimationController animationController = animationEvent.getController();
            if (player.getPose() == Pose.SWIMMING)
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.swim_fast", true));
            else if ((player.isInWaterOrBubble() || player.isInLava()) && (motio.x != 0 || motio.z != 0)) {
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.swim", true));
            } else if ((player.abilities.flying || ClientEvents.dragonsFlying.getOrDefault(player.getId(), false)) && !player.isOnGround() && !player.isInWater() && player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).hasWings()) {
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.fly_slow", true));
            } else if (player.isSprinting())
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.run", true));
            else if (player.isShiftKeyDown() || (!DragonSizeHandler.canPoseFit(player, Pose.STANDING) && DragonSizeHandler.canPoseFit(player, Pose.CROUCHING))) {
                if ((motio.x() != 0 || motio.z() != 0) && player.animationSpeed != 0f)
                    animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.stand5", true));
                else if (ClientEvents.dragonsDigging.getOrDefault(this.player, false)) {
                    animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.stand8", true));
                } else
                    animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.stand6", true));
            } else if (player.swinging && player.getAttackStrengthScale(-3.0f) != 1)
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.model.new"));
            else if (ClientEvents.dragonsJumpingTicks.getOrDefault(this.player, 0) > 0)
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.model.new2", true));
            else if ((motio.x() != 0 || motio.z() != 0) && player.animationSpeed != 0f)
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.stand3", true));
            else if (player.isSleeping()) {
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.sleep", true));
            } else if (ClientEvents.dragonsDigging.getOrDefault(this.player, false)) {
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.stand7", true));
            } else
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.stand3", true));
        }
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
        return (PlayerEntity) level.getEntity(player);
    }
}
