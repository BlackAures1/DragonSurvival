package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.handlers.ClientEvents;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Pose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
import net.minecraft.util.math.Vec3d;
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
            Vec3d motio = player.getMotion();
            final AnimationController animationController = animationEvent.getController();
            if (player.getPose() == Pose.SWIMMING)
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.swim_fast", true));
            else if (player.isInWaterOrBubbleColumn() && (motio.x != 0 || motio.z != 0)) {
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.swim", true));
            } else if ((player.abilities.isFlying || ClientEvents.dragonsFlying.getOrDefault(player.getEntityId(), false)) && !player.onGround && !player.isInWater() && player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).hasWings()) {
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.fly_slow", true));
            } else if (player.isSprinting())
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.run", true));
            else if (player.isSneaking()) {
                if ((motio.getX() != 0 || motio.getZ() != 0) && player.limbSwingAmount != 0f)
                    animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.stand5", true));
                else if (ClientEvents.dragonsDigging.getOrDefault(this.player, false)) {
                    animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.stand8", true));
                } else
                    animationController.setAnimation(new AnimationBuilder().addAnimation("animation.dragon.stand6", true));
            } else if (player.isSwingInProgress && player.getCooledAttackStrength(-3.0f) != 1)
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.model.new"));
            else if (ClientEvents.dragonsJumpingTicks.getOrDefault(this.player, 0) > 0)
                animationController.setAnimation(new AnimationBuilder().addAnimation("animation.model.new2", true));
            else if ((motio.getX() != 0 || motio.getZ() != 0) && player.limbSwingAmount != 0f)
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
    public Iterable<ItemStack> getArmorInventoryList() {
        return Collections.emptyList();
    }

    @Override
    public ItemStack getItemStackFromSlot(EquipmentSlotType slotIn) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemStackToSlot(EquipmentSlotType slotIn, ItemStack stack) {

    }

    @Override
    public HandSide getPrimaryHand() {
        return HandSide.RIGHT;
    }

    PlayerEntity getPlayer() {
        return (PlayerEntity) world.getEntityByID(player);
    }
}
