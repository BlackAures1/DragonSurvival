package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.entity.ShooterHunter;
import by.jackraidenph.dragonsurvival.handlers.DragonEffects;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.Hand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;

import javax.annotation.Nullable;

public class Knight extends AnimatedEntity implements IAnimatable {

    public Knight(EntityType<? extends CreatureEntity> p_i48576_1_, World world) {
        super(p_i48576_1_, world);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "everything", 0, event -> {
            AnimationBuilder animationBuilder = new AnimationBuilder();
            AnimationController animationController = event.getController();
            double movement = getMovementSpeed();
            if (swingTime > 0) {
                Animation animation = animationController.getCurrentAnimation();
                if (animation != null) {
                    String name = animation.animationName;
                    switch (name) {
                        case "attack":
                            trackAnimation("attack");
                            break;
                        case "attack2":
                            trackAnimation("attack2");
                            break;
                    }
                    if (getDuration("attack") <= 0)
                        putAnimation("attack2", 17, animationBuilder);
                    else if (getDuration("attack2") <= 0)
                        putAnimation("attack", 17, animationBuilder);
                }
            }
            if (movement > 0) {
                if (movement > 0.4)
                    animationBuilder.addAnimation("run");
                else if (movement > 0.1) {
                    animationBuilder.addAnimation("walk");
                }
            } else {
                Animation animation = animationController.getCurrentAnimation();
                if (animation == null) {
                    putAnimation("idle", 88, animationBuilder);
                } else {
                    String name = animation.animationName;
                    switch (name) {
                        case "idle":
                            trackAnimation("idle");
                            if (getDuration("idle") <= 0) {
                                if (random.nextInt(10) == 0) {
                                    putAnimation("idle_2", 145, animationBuilder);
                                }
                            }
                            break;
                        case "walk":
                        case "run":
                            putAnimation("idle", 88, animationBuilder);
                            break;
                        case "idle_2":
                            trackAnimation("idle_2");
                            if (getDuration("idle_2") <= 0) {
                                putAnimation("idle", 88, animationBuilder);
                            }
                            break;
                    }
                }
            }
            animationController.setAnimation(animationBuilder);
            return PlayState.CONTINUE;
        }));

    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(0, new SwimGoal(this));
        goalSelector.addGoal(5, new WaterAvoidingRandomWalkingGoal(this, 1));
        goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.5, true));
        targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 1, true, false, livingEntity -> {
            return livingEntity.hasEffect(Effects.BAD_OMEN) || livingEntity.hasEffect(DragonEffects.EVIL_DRAGON);
        }));
        targetSelector.addGoal(6, new HurtByTargetGoal(this, ShooterHunter.class).setAlertOthers());
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_) {
        setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
        ItemStack itemStack = new ItemStack(Items.SHIELD);
        ListNBT listNBT = (new BannerPattern.Builder()).addPattern(BannerPattern.values()[this.random.nextInt((BannerPattern.values()).length)], DyeColor.values()[this.random.nextInt((DyeColor.values()).length)]).toListTag();
        CompoundNBT compoundNBT = new CompoundNBT();
        compoundNBT.putInt("Base", DyeColor.values()[this.random.nextInt((DyeColor.values()).length)].getId());
        compoundNBT.put("Patterns", listNBT);
        itemStack.addTagElement("BlockEntityTag", compoundNBT);
        setItemInHand(Hand.OFF_HAND, itemStack);
    }

    @Nullable
    @Override
    public ILivingEntityData finalizeSpawn(IServerWorld p_213386_1_, DifficultyInstance p_213386_2_, SpawnReason p_213386_3_, @Nullable ILivingEntityData p_213386_4_, @Nullable CompoundNBT p_213386_5_) {
        populateDefaultEquipmentSlots(p_213386_2_);
        return super.finalizeSpawn(p_213386_1_, p_213386_2_, p_213386_3_, p_213386_4_, p_213386_5_);
    }

    @Override
    public void tick() {
        updateSwingTime();
        super.tick();
    }
}
