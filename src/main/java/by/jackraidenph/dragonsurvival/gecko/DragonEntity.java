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

public class DragonEntity extends LivingEntity implements IAnimatable, CommonTraits {
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

    @SuppressWarnings("rawtypes")
    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> animationEvent) {
        final PlayerEntity player = getPlayer();
        final AnimationController animationController = animationEvent.getController();
        AnimationBuilder builder = new AnimationBuilder();
        if (player != null) {
            DragonStateProvider.getCap(player).ifPresent(playerStateHandler -> {
                Vector3d motio = new Vector3d(player.getX() - player.xo, player.getY() - player.yo, player.getZ() - player.zo);
                boolean isMovingHorizontal = getMovementSpeed(this) > 0.005;
                // Main
                if (player.isSleeping())
                    builder.addAnimation("sleep", true);
                else if (player.isPassenger())
                    builder.addAnimation("sit", true);
                else if (player.isPassenger())
                    builder.addAnimation("idle", true); // TODO: Passenger animation for riding entities
                else if (playerStateHandler.getMovementData().bite)
                    builder.addAnimation("bite");
                else if (player.getPose() == Pose.SWIMMING)
                    builder.addAnimation("swim_fast", true);
                else if ((player.isInLava() || player.isInWaterOrBubble()) && !player.isOnGround())
                    builder.addAnimation("swim", true);
                else if ((player.abilities.flying || ClientEvents.dragonsFlying.getOrDefault(player.getId(), false)) && !player.isOnGround() && !player.isInWater() && !player.isInLava() && player.getCapability(DragonStateProvider.DRAGON_CAPABILITY).orElse(null).hasWings())
                    builder.addAnimation("fly", true);
                else if (!player.isOnGround() && motio.y() < 0) {
                    builder.addAnimation("land", false);
                    builder.addAnimation("idle", true);
                } else if (ClientEvents.dragonsJumpingTicks.getOrDefault(this.player, 0) > 0)
                    builder.addAnimation("jump", false);
                else if (player.isShiftKeyDown() || (!DragonSizeHandler.canPoseFit(player, Pose.STANDING) && DragonSizeHandler.canPoseFit(player, Pose.CROUCHING))) {
                    // Player is Sneaking
                    if (isMovingHorizontal && player.animationSpeed != 0f)
                        builder.addAnimation("sneak_walk", true);
                    else if (ClientEvents.dragonsDigging.getOrDefault(this.player, false))
                        builder.addAnimation("dig_sneak", true);
                    else
                        builder.addAnimation("sneak", true);
                } else if (player.isSprinting())
                    builder.addAnimation("run", true);
                else if (isMovingHorizontal && player.animationSpeed != 0f)
                    builder.addAnimation("walk", true);
                else if (ClientEvents.dragonsDigging.getOrDefault(this.player, false))
                    builder.addAnimation("dig", true);
                else
                    builder.addAnimation("idle", true);
            });
        } else
            builder.addAnimation("idle", true);
        animationController.setAnimation(builder);
        return PlayState.CONTINUE;
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return getPlayer().getArmorSlots();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlotType slotIn) {
        return getPlayer().getItemBySlot(slotIn);
    }

    @Override
    public void setItemSlot(EquipmentSlotType slotIn, ItemStack stack) {
        getPlayer().setItemSlot(slotIn, stack);
    }

    @Override
    public HandSide getMainArm() {
        return getPlayer().getMainArm();
    }

    PlayerEntity getPlayer() {
        return (PlayerEntity) level.getEntity(player);
    }
}
