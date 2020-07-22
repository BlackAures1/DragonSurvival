package by.jackraidenph.dragonsurvival.entity;

import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.renderer.MagicalBeastRenderer;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.Random;

public class MagicalBeastEntity extends MonsterEntity {

    public int type;
    public float size;
    private float scale;

    public MagicalBeastEntity(EntityType<? extends MonsterEntity> entityIn, World worldIn) {
        super(entityIn, worldIn);
        this.type = new Random().nextInt(MagicalBeastRenderer.MAGICAL_BEAST_TEXTURES.size());
        this.size = new Random().nextFloat() + 0.95F;
        scale = this.size / this.getHeight();
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        spawnDataIn = super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
        if (worldIn.getRandom().nextInt(3) == 0) {
            SkeletonEntity skeletonentity = EntityType.SKELETON.create(this.world);
            skeletonentity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
            skeletonentity.onInitialSpawn(worldIn, difficultyIn, reason, null, null);
            worldIn.addEntity(skeletonentity);
            skeletonentity.startRiding(this);
        }
        return spawnDataIn;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomWalkingGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(5, new LookRandomlyGoal(this));
        this.targetSelector.addGoal(1, new isNearestDragonTargetGoal(this, true));
    }

    @Override
    public double getMountedYOffset() {
        return (this.getHeight() * scale * 0.75D);
    }

    @Override
    protected void registerAttributes() {
        super.registerAttributes();

        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.375D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0F);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0F);
        this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).setBaseValue(1.0F);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(29.5F);

        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("healthAmount", scale, AttributeModifier.Operation.MULTIPLY_BASE));
        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(new AttributeModifier("movementSpeed", 1f / scale, AttributeModifier.Operation.MULTIPLY_BASE));
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier("damageAmount", scale, AttributeModifier.Operation.MULTIPLY_BASE));
        this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).applyModifier(new AttributeModifier("attackKnockback", scale, AttributeModifier.Operation.MULTIPLY_BASE));
    }

    static class isNearestDragonTargetGoal extends NearestAttackableTargetGoal {

        int distance;

        public isNearestDragonTargetGoal(MobEntity p_i50313_1_, boolean p_i50313_3_) {
            super(p_i50313_1_, PlayerEntity.class, p_i50313_3_);
        }

        @Override
        protected AxisAlignedBB getTargetableArea(double p_188511_1_) {

            distance = 10;
            PlayerEntity player = (PlayerEntity) this.nearestTarget;

            if (player != null) {
                PlayerStateProvider.getCap(player).ifPresent(cap -> {
                    if (cap.getIsDragon() && !cap.getIsHiding())
                        distance = 30;
                    else if (cap.getIsDragon())
                        distance = 18;
                    else
                        distance = 10;
                });
            }

            return this.goalOwner.getBoundingBox().grow(distance);
        }
    }

}
