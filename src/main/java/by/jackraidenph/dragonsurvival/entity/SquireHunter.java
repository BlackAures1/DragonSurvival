package by.jackraidenph.dragonsurvival.entity;

import by.jackraidenph.dragonsurvival.Functions;
import by.jackraidenph.dragonsurvival.gecko.Knight;
import by.jackraidenph.dragonsurvival.goals.AlertExceptHunters;
import by.jackraidenph.dragonsurvival.goals.FollowMobGoal;
import by.jackraidenph.dragonsurvival.handlers.DragonEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Effects;
import net.minecraft.util.Hand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;

import javax.annotation.Nullable;

public class SquireHunter extends VindicatorEntity implements DragonHunter {
    public SquireHunter(EntityType<? extends VindicatorEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.availableGoals.removeIf(prioritizedGoal -> {
            Goal goal = prioritizedGoal.getGoal();
            return (goal instanceof NearestAttackableTargetGoal || goal instanceof HurtByTargetGoal);
        });
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, true, true, livingEntity ->
                (livingEntity.hasEffect(Effects.BAD_OMEN) || livingEntity.hasEffect(DragonEffects.EVIL_DRAGON))));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, MonsterEntity.class, 0, true, true, livingEntity ->
                (livingEntity instanceof net.minecraft.entity.monster.IMob && !(livingEntity instanceof DragonHunter))));
        this.goalSelector.addGoal(7, new FollowMobGoal<>(Knight.class, this, 15));
        this.targetSelector.addGoal(6, (new HurtByTargetGoal(this, ShooterHunter.class)).setAlertOthers());
        this.goalSelector.addGoal(8, new AlertExceptHunters<>(this, HunterHound.class, Knight.class, ShooterHunter.class));
    }

    @Nullable
    @Override
    public Raid getCurrentRaid() {
        return null;
    }

    public AbstractIllagerEntity.ArmPose getArmPose() {
        return AbstractIllagerEntity.ArmPose.ATTACKING;
    }

    public boolean isAggressive() {
        return true;
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        setItemInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_SWORD));
    }

    public boolean canBeLeader() {
        return false;
    }

    @Override
    public boolean removeWhenFarAway(double distance) {
        return tickCount >= Functions.minutesToTicks(15);
    }
}