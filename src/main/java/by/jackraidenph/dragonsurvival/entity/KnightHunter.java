package by.jackraidenph.dragonsurvival.entity;

import by.jackraidenph.dragonsurvival.goals.AlertExceptHunters;
import by.jackraidenph.dragonsurvival.goals.RideHorse;
import by.jackraidenph.dragonsurvival.handlers.DragonEffects;
import by.jackraidenph.dragonsurvival.util.GroundNavigator;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.controller.MovementController;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.NearestAttackableTargetGoal;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.passive.horse.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.pathfinding.PathNavigator;
import net.minecraft.potion.Effects;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.util.Hand;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraft.world.raid.Raid;

import javax.annotation.Nullable;
@Deprecated
public class KnightHunter extends VindicatorEntity implements DragonHunter {
    public KnightHunter(EntityType<? extends VindicatorEntity> entityType, World world) {
        super(entityType, world);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.availableGoals.removeIf(prioritizedGoal -> {
            Goal goal = prioritizedGoal.getGoal();
            return goal instanceof NearestAttackableTargetGoal;
        });
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, 0, true, false, livingEntity ->
                (livingEntity.hasEffect(Effects.BAD_OMEN) || livingEntity.hasEffect(DragonEffects.EVIL_DRAGON))));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, MonsterEntity.class, 0, true, false, livingEntity ->
                (livingEntity instanceof net.minecraft.entity.monster.IMob && !(livingEntity instanceof DragonHunter))));
        this.targetSelector.addGoal(6, (new HurtByTargetGoal(this, ShooterHunter.class)).setAlertOthers());
        this.goalSelector.addGoal(7, new AlertExceptHunters<>(this, HunterHound.class, ShooterHunter.class, SquireHunter.class));
        goalSelector.addGoal(6, new RideHorse<>(this));
    }

    @Nullable
    @Override
    public Raid getCurrentRaid() {
        return null;
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficultyInstance, SpawnReason reason, @Nullable ILivingEntityData entityData, @Nullable CompoundNBT compoundNBT) {
        HorseEntity horse = new HorseEntity(EntityType.HORSE, this.level);
        horse.setPos(getX(), getY(), getZ());
        horse.finalizeSpawn(serverWorld, difficultyInstance, reason, entityData, compoundNBT);
        horse.setTamed(true);
        horse.equipSaddle(null);
        horse.setSlot(401, new ItemStack(Items.IRON_HORSE_ARMOR));
        this.level.addFreshEntity(horse);
        horse.goalSelector.availableGoals.removeIf(prioritizedGoal -> {
            Goal goal = prioritizedGoal.getGoal();
            return (goal instanceof net.minecraft.entity.ai.goal.PanicGoal || goal instanceof net.minecraft.entity.ai.goal.RunAroundLikeCrazyGoal || goal instanceof net.minecraft.entity.ai.goal.WaterAvoidingRandomWalkingGoal || goal instanceof net.minecraft.entity.ai.goal.LookRandomlyGoal);
        });

        horse.goalSelector.availableGoals.removeIf(prioritizedGoal -> prioritizedGoal.getGoal() instanceof net.minecraft.entity.ai.goal.AvoidEntityGoal);
        startRiding(horse);
        return super.finalizeSpawn(serverWorld, difficultyInstance, reason, entityData, compoundNBT);
    }

    public AbstractIllagerEntity.ArmPose getArmPose() {
        return AbstractIllagerEntity.ArmPose.ATTACKING;
    }

    public boolean isAggressive() {
        return true;
    }

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

    public boolean isBlocking() {
        return random.nextBoolean();
    }

    public boolean canBeLeader() {
        return false;
    }

    //these 2 overrides below allow horse riding
    @Override
    public PathNavigator getNavigation() {
        return navigation;
    }

    @Override
    public MovementController getMoveControl() {
        return moveControl;
    }

    @Override
    protected PathNavigator createNavigation(World world) {
        return new GroundNavigator(this, world);
    }
}
