package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.HandSide;
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
    public PlayerEntity player;

    public DragonEntity(EntityType<? extends LivingEntity> type, World worldIn) {
        super(type, worldIn);
    }

    @Override
    public void registerControllers(AnimationData animationData) {
        animationData.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        if (!player.onGround && !player.isInWater() && player.getCapability(DragonStateProvider.PLAYER_STATE_HANDLER_CAPABILITY).orElse(null).hasWings()) {
            animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dragon.fly_slow", true));
        } else if (player.isSleeping()) {
            animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dragon.sleep", true));
        } else
            animationEvent.getController().setAnimation(new AnimationBuilder().addAnimation("animation.dragon.stand2", true));
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
}
