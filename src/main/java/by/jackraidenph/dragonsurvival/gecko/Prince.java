package by.jackraidenph.dragonsurvival.gecko;

import by.jackraidenph.dragonsurvival.util.PrinceTrades;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.merchant.villager.VillagerData;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MerchantOffers;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.Animation;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.manager.AnimationData;

import javax.annotation.Nullable;

public class Prince extends Princess {
    public Prince(EntityType<? extends VillagerEntity> entityType, World world) {
        super(entityType, world);
    }

    public Prince(EntityType<? extends VillagerEntity> entityType, World world, VillagerType villagerType) {
        super(entityType, world, villagerType);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(5, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
        goalSelector.availableGoals.removeIf(prioritizedGoal -> {
            Goal goal = prioritizedGoal.getGoal();
            return goal instanceof PanicGoal || goal instanceof AvoidEntityGoal;
        });
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficultyInstance, SpawnReason reason, @Nullable ILivingEntityData livingEntityData, @Nullable CompoundNBT compoundNBT) {
        setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.GOLDEN_SWORD));
        return super.finalizeSpawn(serverWorld, difficultyInstance, reason, livingEntityData, compoundNBT);
    }

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
            if (movement > 0.6)
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
    public void tick() {
        updateSwingTime();
        super.tick();
    }

    protected void updateTrades() {
        VillagerData villagerdata = getVillagerData();
        Int2ObjectMap<VillagerTrades.ITrade[]> int2objectmap = PrinceTrades.colorToTrades.get(getColor());
        if (int2objectmap != null && !int2objectmap.isEmpty()) {
            VillagerTrades.ITrade[] trades = int2objectmap.get(villagerdata.getLevel());
            if (trades != null) {
                MerchantOffers merchantoffers = getOffers();
                addOffersFromItemListings(merchantoffers, trades, 2);
            }
        }
    }
}

