package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.Functions;
import by.jackraidenph.dragonsurvival.entity.DragonHunter;
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
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class Knight extends CreatureEntity implements IAnimatable, DragonHunter, CommonTraits {
    AnimationFactory animationFactory = new AnimationFactory(this);

    public Knight(EntityType<? extends CreatureEntity> p_i48576_1_, World world) {
        super(p_i48576_1_, world);
    }

    AnimationTimer animationTimer = new AnimationTimer();

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "everything", 0, event -> {
            AnimationBuilder animationBuilder = new AnimationBuilder();

            AnimationController animationController = event.getController();
            double movement = getMovementSpeed(this);
            if (swingTime > 0) {
                Animation animation = animationController.getCurrentAnimation();
                if (animation != null) {
                    String name = animation.animationName;
                    switch (name) {
                        case "attack":
                            animationTimer.trackAnimation("attack");
                            if (animationTimer.getDuration("attack2") <= 0) {
                                if (random.nextBoolean())
                                    animationTimer.putAnimation("attack", 17, animationBuilder);
                                else
                                    animationTimer.putAnimation("attack2", 17, animationBuilder);
                            }
                            break;
                        case "attack2":
                            animationTimer.trackAnimation("attack2");
                            if (animationTimer.getDuration("attack") <= 0) {
                                if (random.nextBoolean())
                                    animationTimer.putAnimation("attack", 17, animationBuilder);
                                else
                                    animationTimer.putAnimation("attack2", 17, animationBuilder);
                            }
                            break;
                        default:
                            if (random.nextBoolean())
                                animationTimer.putAnimation("attack", 17, animationBuilder);
                            else
                                animationTimer.putAnimation("attack2", 17, animationBuilder);
                    }
                }
            }
            if (movement > 0.4)
                animationBuilder.addAnimation("run");
            else if (movement > 0.1) {
                animationBuilder.addAnimation("walk");
            } else {
                Animation animation = animationController.getCurrentAnimation();
                if (animation == null) {
                    animationTimer.putAnimation("idle", 88, animationBuilder);
                } else {
                    String name = animation.animationName;
                    switch (name) {
                        case "idle":
                            animationTimer.trackAnimation("idle");
                            if (animationTimer.getDuration("idle") <= 0) {
                                if (random.nextInt(2000) == 0) {
                                    animationTimer.putAnimation("idle_2", 145, animationBuilder);
                                }
                            }
                            break;
                        case "walk":
                        case "run":
                            animationTimer.putAnimation("idle", 88, animationBuilder);
                            break;
                        case "idle_2":
                            animationTimer.trackAnimation("idle_2");
                            if (animationTimer.getDuration("idle_2") <= 0) {
                                animationTimer.putAnimation("idle", 88, animationBuilder);
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
    public AnimationFactory getFactory() {
        return animationFactory;
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
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
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
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficultyInstance, SpawnReason spawnReason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT nbt) {
        populateDefaultEquipmentSlots(difficultyInstance);
        return super.finalizeSpawn(serverWorld, difficultyInstance, spawnReason, entityData, nbt);
    }

    @Override
    public void tick() {
        updateSwingTime();
        super.tick();
    }

    @Override
    public boolean isBlocking() {
        return random.nextBoolean();
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return tickCount >= Functions.minutesToTicks(15);
    }
}
