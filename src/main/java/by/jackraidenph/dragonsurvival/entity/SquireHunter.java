package by.jackraidenph.dragonsurvival.entity;

import by.jackraidenph.dragonsurvival.goals.FollowMobGoal;
import by.jackraidenph.dragonsurvival.handlers.DragonEffects;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
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
        this.targetSelector.addGoal(5, (Goal) new NearestAttackableTargetGoal<>((MobEntity) this, PlayerEntity.class, 0, true, true, livingEntity ->

                (livingEntity.hasEffect(Effects.BAD_OMEN) || livingEntity.hasEffect(DragonEffects.EVIL_DRAGON))));

        this.targetSelector.addGoal(6, (Goal) new NearestAttackableTargetGoal((MobEntity) this, MonsterEntity.class, 0, true, true, livingEntity ->

                (livingEntity instanceof net.minecraft.entity.monster.IMob && !(livingEntity instanceof DragonHunter))));

        this.goalSelector.addGoal(7, (Goal) new FollowMobGoal(KnightHunter.class, (MobEntity) this, 15));
        this.targetSelector.addGoal(6, (Goal) (new HurtByTargetGoal((CreatureEntity) this)).setAlertOthers(new Class[0]));
//     this.field_70714_bg.func_75776_a(8, (Goal)new AlertGoal((LivingEntity)this, new Class[] { HunterHound.class, KnightHunter.class, ShooterHunter.class }));
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
}