package by.jackraidenph.dragonsurvival.entity;

import by.jackraidenph.dragonsurvival.Functions;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.gecko.Knight;
import by.jackraidenph.dragonsurvival.goals.AlertExceptHunters;
import by.jackraidenph.dragonsurvival.goals.FollowMobGoal;
import by.jackraidenph.dragonsurvival.handlers.DragonEffects;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.potion.Effects;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;

import javax.annotation.Nullable;

public class ShooterHunter extends PillagerEntity implements DragonHunter {
    protected int bolasCooldown = Functions.secondsToTicks(30);

    public ShooterHunter(EntityType<? extends PillagerEntity> type, World world) {
        super(type, world);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.availableGoals.removeIf(prioritizedGoal -> {
            Goal goal = prioritizedGoal.getGoal();
            return (goal instanceof net.minecraft.entity.monster.AbstractRaiderEntity.FindTargetGoal || goal instanceof net.minecraft.entity.ai.goal.MoveTowardsRaidGoal);
        });
        this.targetSelector.availableGoals.removeIf(prioritizedGoal -> {
            Goal goal = prioritizedGoal.getGoal();
            return (goal instanceof NearestAttackableTargetGoal || goal instanceof HurtByTargetGoal);
        });
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, true, false, livingEntity ->
                (livingEntity.hasEffect(Effects.BAD_OMEN) || livingEntity.hasEffect(DragonEffects.EVIL_DRAGON))));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, MonsterEntity.class, 0, true, false, livingEntity ->
                (livingEntity instanceof net.minecraft.entity.monster.IMob && !(livingEntity instanceof DragonHunter))));
        this.goalSelector.addGoal(7, new FollowMobGoal<>(Knight.class, this, 15));
        this.targetSelector.addGoal(6, (new HurtByTargetGoal(this, ShooterHunter.class)).setAlertOthers());
        this.goalSelector.addGoal(7, new AlertExceptHunters<>(this, HunterHound.class, Knight.class, SquireHunter.class));
    }

    @Override
    public void applyRaidBuffs(int p_213660_1_, boolean p_213660_2_) {

    }

    @Nullable
    @Override
    public Raid getCurrentRaid() {
        return null;
    }

    public boolean canBeLeader() {
        return false;
    }

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putInt("Bolas cooldown", this.bolasCooldown);
    }

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        this.bolasCooldown = compoundNBT.getInt("Bolas cooldown");
    }

    public void tick() {
        super.tick();
        LivingEntity target = getTarget();
        if (target instanceof PlayerEntity && DragonStateProvider.isDragon(target))
            if (this.bolasCooldown == 0) {
                performBolasThrow(target);
                this.bolasCooldown = Functions.secondsToTicks(30);
            } else {
                this.bolasCooldown--;
            }
    }

    public void performBolasThrow(LivingEntity target) {
        BolasEntity bolas = new BolasEntity(this, this.level);
        double d0 = target.getEyeY() - (double) 1.1F;
        double d1 = target.getX() - this.getX();
        double d2 = d0 - bolas.getY();
        double d3 = target.getZ() - this.getZ();
        float f = MathHelper.sqrt(d1 * d1 + d3 * d3) * 0.2F;
        bolas.shoot(d1, d2 + f, d3, 1.6F, 12.0F);
        playSound(SoundEvents.WITCH_THROW, 1.0F, 0.4F / (random.nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(bolas);
    }
}
