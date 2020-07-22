package by.jackraidenph.dragonsurvival.entity;

import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.renderer.MagicalBeastRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleType;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
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
        this.type = worldIn.getRandom().nextInt(MagicalBeastRenderer.MAGICAL_BEAST_TEXTURES.size());
        this.size =  worldIn.getRandom().nextFloat() + 0.95F;
        scale = this.size / this.getHeight();
    }

    @Override
    public void livingTick() {
        super.livingTick();
        this.world.addParticle(
                this.world.getRandom().nextBoolean() ? ParticleTypes.SMOKE : ParticleTypes.LARGE_SMOKE,
                this.getPosX() + this.world.getRandom().nextFloat() - 0.5F,
                this.getPosY() + this.getHeight() / 1.5F * scale,
                this.getPosZ() + this.world.getRandom().nextFloat() - 0.5F,
                0,
                this.world.getRandom().nextFloat() / 5f,
                0);
    }

    @Override
    @Nullable
    public ILivingEntityData onInitialSpawn(IWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        super.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);

        if (worldIn.getRandom().nextInt(10) == 0) {
            SkeletonEntity skeletonentity = EntityType.SKELETON.create(this.world);
            skeletonentity.setLocationAndAngles(this.getPosX(), this.getPosY(), this.getPosZ(), this.rotationYaw, 0.0F);
            skeletonentity.onInitialSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
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
        this.targetSelector.addGoal(1, new FindPlayerGoal(this));
        this.targetSelector.addGoal(2, new isNearestDragonTargetGoal(this, true));
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

    private boolean teleportToEntity(Entity p_70816_1_) {
        Vec3d vec3d = new Vec3d(this.getPosX() - p_70816_1_.getPosX(), this.getPosYHeight(0.5D) - p_70816_1_.getPosYEye(), this.getPosZ() - p_70816_1_.getPosZ());
        vec3d = vec3d.normalize();
        double d1 = this.getPosX() + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.x * 16.0D;
        double d2 = this.getPosY() + (double) (this.rand.nextInt(16) - 8) - vec3d.y * 16.0D;
        double d3 = this.getPosZ() + (this.rand.nextDouble() - 0.5D) * 8.0D - vec3d.z * 16.0D;
        return this.teleportTo(d1, d2, d3);
    }

    private boolean teleportTo(double x, double y, double z) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z);

        while (blockpos$mutable.getY() > 0 && !this.world.getBlockState(blockpos$mutable).getMaterial().blocksMovement()) {
            blockpos$mutable.move(Direction.DOWN);
        }

        BlockState blockstate = this.world.getBlockState(blockpos$mutable);
        boolean flag = blockstate.getMaterial().blocksMovement();
        boolean flag1 = blockstate.getFluidState().isTagged(FluidTags.WATER);
        if (flag && !flag1) {
            net.minecraftforge.event.entity.living.EnderTeleportEvent event = new net.minecraftforge.event.entity.living.EnderTeleportEvent(this, x, y, z, 0);
            if (net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event)) return false;
            boolean flag2 = this.attemptTeleport(event.getTargetX(), event.getTargetY(), event.getTargetZ(), true);
            if (flag2) {
                this.world.playSound((PlayerEntity) null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
                this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            }

            return flag2;
        } else {
            return false;
        }
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

    static class FindPlayerGoal extends NearestAttackableTargetGoal<PlayerEntity> {
        private final MagicalBeastEntity beast;
        private final EntityPredicate field_220792_n = (new EntityPredicate()).setLineOfSiteRequired();

        private PlayerEntity player;
        private int aggroTime;
        private int teleportTime;

        public FindPlayerGoal(MagicalBeastEntity beastIn) {
            super(beastIn, PlayerEntity.class, false);
            this.beast = beastIn;
        }

        /**
         * Returns whether execution should begin. You can also read and cache any state necessary for execution in this
         * method as well.
         */
        public boolean shouldExecute() {
            return this.player != null;
        }

        /**
         * Execute a one shot task or start executing a continuous task
         */
        public void startExecuting() {
            this.aggroTime = 5;
            this.teleportTime = 0;
        }

        /**
         * Reset the task's internal state. Called when this task is interrupted by another one
         */
        public void resetTask() {
            this.player = null;
            super.resetTask();
        }

        /**
         * Returns whether an in-progress EntityAIBase should continue executing
         */
        public boolean shouldContinueExecuting() {
            if (this.player != null) {
                this.beast.faceEntity(this.player, 10.0F, 10.0F);
                return true;
            } else {
                return this.nearestTarget != null && this.field_220792_n.canTarget(this.beast, this.nearestTarget) ? true : super.shouldContinueExecuting();
            }
        }

        public void tick() {
            if (this.player != null) {
                if (--this.aggroTime <= 0) {
                    this.nearestTarget = this.player;
                    this.player = null;
                    super.startExecuting();
                }
            } else {
                if (this.nearestTarget != null && !this.beast.isPassenger()) {
                    this.teleportTime = 0;
                } else if (this.nearestTarget.getDistanceSq(this.beast) > 256.0D && this.teleportTime++ >= 30 && this.beast.teleportToEntity(this.nearestTarget)) {
                    this.teleportTime = 0;
                }
            }

            super.tick();
        }

    }
}

