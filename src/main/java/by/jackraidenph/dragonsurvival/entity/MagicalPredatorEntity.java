package by.jackraidenph.dragonsurvival.entity;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.blocks.PredatorStarBlock;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.handlers.BlockInit;
import by.jackraidenph.dragonsurvival.network.PacketSyncXPDevour;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.Direction;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class MagicalPredatorEntity extends MonsterEntity {
	
    public int type;
    public float size;
    private final float scale;
    private boolean deathStar;
    private int teleportationCooldown;

    public MagicalPredatorEntity(EntityType<? extends MonsterEntity> entityIn, World worldIn) {
        super(entityIn, worldIn);
        this.type = worldIn.getRandom().nextInt(10);
        this.size = worldIn.getRandom().nextFloat() + 0.95F;
        scale = this.size / this.getBbHeight();
        deathStar = false;
    }

    private static int getActualDistance(PlayerEntity player) {

        AtomicInteger distance = new AtomicInteger();

        if (player != null) {
            DragonStateProvider.getCap(player).ifPresent(cap -> {
                if (cap.isDragon() && !cap.isHiding())
                    distance.set(30);
                else if (cap.isDragon())
                    distance.set(18);
                else
                    distance.set(10);
            });
        }
        return distance.get();
    }

    @Override
    protected boolean shouldDespawnInPeaceful() {
        return true;
    }

    @Override
    protected void tickDeath() {
        super.tickDeath();
        if (this.deathTime == 19 && !deathStar) {
        	deathStar = true;
        	for (int i = 1; i < 10; ++i)
    		for (int r = 0; r < 5; ++r) {
    			BlockPos blockpos = this.blockPosition().offset(level.random.nextInt(i) - level.random.nextInt(i), level.random.nextInt(i) - level.random.nextInt(i), level.random.nextInt(i) - level.random.nextInt(i));
				if (level.getBlockState(blockpos).getBlockState().canBeReplaced(Fluids.LAVA) && level.getEntityCollisions(null, new AxisAlignedBB(blockpos), (entity) -> { 
					return entity instanceof LivingEntity; }).count() == 0) {
                	if (level.isClientSide)
                		this.spawnAnim();
                	else {
                		BlockState starState = BlockInit.PREDATOR_STAR_BLOCK.defaultBlockState();
                		starState.setValue(BlockStateProperties.WATERLOGGED, Boolean.valueOf(level.getFluidState(blockpos).getType() == Fluids.WATER));
                		if (!level.getBlockState(blockpos).getMaterial().isLiquid())
                			level.destroyBlock(blockpos, true);
                		level.setBlockAndUpdate(blockpos, starState);
                	}
                   return;
                }
			}
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        this.level.addParticle(ParticleTypes.SMOKE, this.getX() + this.level.getRandom().nextFloat() * 1.25 - 0.75F,
                this.getY() + this.getBbHeight() / 1.5F * scale, this.getZ() + this.level.getRandom().nextFloat() * 1.25 - 0.75F,
                0, this.level.getRandom().nextFloat() / 12.5f, 0);
        if (teleportationCooldown > 0)
            teleportationCooldown--;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource p_184601_1_) {
        return SoundEvents.SKELETON_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.SKELETON_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.SKELETON_AMBIENT;
    }

    @Override
    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld worldIn, DifficultyInstance difficultyIn, SpawnReason reason, @Nullable ILivingEntityData spawnDataIn, @Nullable CompoundNBT dataTag) {
        super.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);

        
        if (worldIn.getRandom().nextInt(10) == 0) {
            SkeletonEntity skeletonentity = EntityType.SKELETON.create(this.level);
            skeletonentity.absMoveTo(this.getX(), this.getY(), this.getZ(), this.yRot, 0.0F);
            skeletonentity.finalizeSpawn(worldIn, difficultyIn, reason, spawnDataIn, dataTag);
            worldIn.addFreshEntity(skeletonentity);
            skeletonentity.startRiding(this);
        }
        this.getAttribute(Attributes.MAX_HEALTH).addPermanentModifier(new AttributeModifier("healthBoost", scale, AttributeModifier.Operation.MULTIPLY_BASE));
        this.setHealth((float)this.getAttribute(Attributes.MAX_HEALTH).getValue());
        this.getAttribute(Attributes.ATTACK_DAMAGE).addPermanentModifier(new AttributeModifier("damageBoost", scale, AttributeModifier.Operation.MULTIPLY_BASE));
        this.getAttribute(Attributes.ATTACK_KNOCKBACK).addPermanentModifier(new AttributeModifier("attackBoost", scale, AttributeModifier.Operation.MULTIPLY_BASE));
        return spawnDataIn;
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new SwimGoal(this));
        this.goalSelector.addGoal(2, new MeleeAttackGoal(this, 1.0D, true));
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(4, new DevourXP(this.level, this));
        this.targetSelector.addGoal(1, new FindPlayerGoal(this));
        this.targetSelector.addGoal(2, new IsNearestDragonTargetGoal(this, true));
    }

    @Override
    public double getPassengersRidingOffset() {
        return (this.getBbHeight() * scale * 0.75D);
    }

    @Override
    public void addAdditionalSaveData(CompoundNBT compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("teleportationCooldown", teleportationCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundNBT compound) {
        super.readAdditionalSaveData(compound);
        teleportationCooldown = compound.getInt("teleportationCooldown");
    }
    
    
    public static AttributeModifierMap.MutableAttribute createMonsterAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.375D)
                .add(Attributes.ARMOR, 2.0F)
                .add(Attributes.ATTACK_DAMAGE, 2.0F * ConfigHandler.COMMON.predatorDamageFactor.get())
                .add(Attributes.ATTACK_KNOCKBACK, 1.0F)
                .add(Attributes.MAX_HEALTH, 29.5F * ConfigHandler.COMMON.predatorHealthFactor.get());
    }

    public boolean hurt(DamageSource source, float damage) {
        if (this.isInvulnerableTo(source)) {
           return false;
        } else if (source.getEntity() instanceof LivingEntity) {
        	teleportationCooldown = 30;
        }
        return super.hurt(source, damage);
     }
    
    public boolean doHurtTarget(Entity target) {
        if (!super.doHurtTarget(target)) {
           return false;
        } else {
        	teleportationCooldown = 30;
           return true;
        }
     }
    
    private void teleportToEntity(Entity p_70816_1_) {
    	Vector3d vec = p_70816_1_.position().subtract(p_70816_1_.getLookAngle().multiply(2, 1, 2));
        double d1 = vec.x();
        double d2 = 256;
        double d3 = vec.z();
        
        this._teleportTo(d1, d2, d3);
    }

    private void _teleportTo(double x, double y, double z) {
        BlockPos.Mutable blockpos$mutable = new BlockPos.Mutable(x, y, z);
        
        while (blockpos$mutable.getY() > 0 && !this.level.getBlockState(blockpos$mutable).getMaterial().blocksMotion()) {
            blockpos$mutable.move(Direction.DOWN);
        }

        BlockState blockstate = this.level.getBlockState(blockpos$mutable);
        boolean flag = blockstate.getMaterial().blocksMotion();
        boolean flag1 = blockstate.getFluidState().is(FluidTags.WATER);
        this.attemptTeleport(x, y, z, true);
        if (flag && !flag1) {
            this.level.playSound(null, this.xo, this.yo, this.zo, SoundEvents.ENDERMAN_TELEPORT, this.getSoundSource(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENDERMAN_TELEPORT, 1.0F, 1.0F);
        }
    }

    public boolean attemptTeleport(double p_213373_1_, double p_213373_3_, double p_213373_5_, boolean p_213373_7_) {
        double d0 = this.getX();
        double d1 = this.getY();
        double d2 = this.getZ();
        double d3 = p_213373_3_;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(p_213373_1_, p_213373_3_, p_213373_5_);
        World world = this.level;
        boolean flag1 = false;

        while (!flag1 && blockpos.getY() > 0) {
            BlockPos blockpos1 = blockpos.below();
            BlockState blockstate = world.getBlockState(blockpos1);
            if (blockstate.getMaterial().blocksMotion()) {
                flag1 = true;
            } else {
                --d3;
                blockpos = blockpos1;
            }
            
            if (flag1) {
                this.teleportTo(p_213373_1_, d3, p_213373_5_);
                if (world.noCollision(this)) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            this.teleportTo(d0, d1, d2);
            return false;
        } else {
            if (p_213373_7_) {
                world.broadcastEntityEvent(this, (byte) 46);
            }

            this.getNavigation().stop();

            return true;
        }
    }

    static class DevourXP extends Goal {

        World world;
        MagicalPredatorEntity entity;

        public DevourXP(World worldIn, MagicalPredatorEntity entityIn) {
            this.world = worldIn;
            this.entity = entityIn;
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public void tick() {
            this.world.getEntities(EntityType.EXPERIENCE_ORB, this.entity.getBoundingBox().inflate(4), Objects::nonNull).forEach(
                    xpOrb -> DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new PacketSyncXPDevour(this.entity.getId(), xpOrb.getId()))
            );
            super.tick();
        }
    }

    static class IsNearestDragonTargetGoal extends NearestAttackableTargetGoal<PlayerEntity> {

        public IsNearestDragonTargetGoal(MobEntity p_i50313_1_, boolean p_i50313_3_) {
            super(p_i50313_1_, PlayerEntity.class, p_i50313_3_);
        }

        @Override
        protected AxisAlignedBB getTargetSearchArea(double p_188511_1_) {
            PlayerEntity player = (PlayerEntity) this.target;
            return this.mob.getBoundingBox().inflate(getActualDistance(player));
        }
    }

    static class FindPlayerGoal extends NearestAttackableTargetGoal<PlayerEntity> {
        private final MagicalPredatorEntity beast;

        public FindPlayerGoal(MagicalPredatorEntity beastIn) {
            super(beastIn, PlayerEntity.class, false);
            this.beast = beastIn;
        }

        @Override
        protected AxisAlignedBB getTargetSearchArea(double p_188511_1_) {
            PlayerEntity player = (PlayerEntity) this.target;
            return this.mob.getBoundingBox().inflate(getActualDistance(player));
        }

        @Override
        public void tick() {
            if (this.target != null) {
                if (this.target instanceof PlayerEntity) {
                	if (beast.teleportationCooldown == 0) {
                        float diff = getActualDistance((PlayerEntity) this.target) - beast.distanceTo(this.target);
                        if (diff <= 16 & diff >= -2) {
                            beast.teleportToEntity(this.target);
                            beast.teleportationCooldown = 10;
                        }
                	}
                }
            }
        }
    }
}

