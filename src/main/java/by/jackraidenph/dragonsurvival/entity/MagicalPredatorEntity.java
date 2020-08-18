package by.jackraidenph.dragonsurvival.entity;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.handlers.BlocksInit;
import by.jackraidenph.dragonsurvival.network.PacketSyncPredatorStats;
import by.jackraidenph.dragonsurvival.network.PacketSyncXPDevour;
import by.jackraidenph.dragonsurvival.renderer.MagicalPredatorRenderer;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.SkeletonEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
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
import net.minecraftforge.fml.network.PacketDistributor;

import javax.annotation.Nullable;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class MagicalPredatorEntity extends MonsterEntity {

    public int type;
    public float size;
    private float scale;

    public MagicalPredatorEntity(EntityType<? extends MonsterEntity> entityIn, World worldIn) {
        super(entityIn, worldIn);
        this.type = worldIn.getRandom().nextInt(MagicalPredatorRenderer.MAGICAL_BEAST_TEXTURES.size());
        this.size = worldIn.getRandom().nextFloat() + 0.95F;
        scale = this.size / this.getHeight();
    }

    @Override
    protected boolean isDespawnPeaceful() {
        return true;
    }

    @Override
    public boolean preventDespawn() {
        return true;
    }

    protected static int getActualDistance(PlayerEntity player) {

        AtomicInteger distance = new AtomicInteger();

        if (player != null) {
            PlayerStateProvider.getCap(player).ifPresent(cap -> {
                if (cap.getIsDragon() && !cap.getIsHiding())
                    distance.set(30);
                else if (cap.getIsDragon())
                    distance.set(18);
                else
                    distance.set(10);
            });
        }
        return distance.get();
    }

    @Override
    protected void onDeathUpdate() {
        super.onDeathUpdate();
        if (this.deathTime == 19) {
            world.setBlockState(this.getPosition(), BlocksInit.PREDATOR_STAR_BLOCK.getDefaultState());
            this.spawnExplosionParticle();
        }
    }

    @Override
    public void livingTick() {
        super.livingTick();
        this.world.addParticle(
                ParticleTypes.SMOKE,
                this.getPosX() + this.world.getRandom().nextFloat() * 1.25 - 0.75F,
                this.getPosY() + this.getHeight() / 1.5F * scale,
                this.getPosZ() + this.world.getRandom().nextFloat() * 1.25 - 0.75F,
                0,
                this.world.getRandom().nextFloat() / 12.5f,
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
        this.goalSelector.addGoal(3, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(4, new devourXP(this.world, this));
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

        this.getAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.475D);
        this.getAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(2.0F);
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(2.0F);
        this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).setBaseValue(1.0F);
        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).setBaseValue(29.5F);

        this.getAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(new AttributeModifier("healthBoost", scale, AttributeModifier.Operation.MULTIPLY_BASE));
        this.getAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(new AttributeModifier("damageBoost", scale, AttributeModifier.Operation.MULTIPLY_BASE));
        this.getAttribute(SharedMonsterAttributes.ATTACK_KNOCKBACK).applyModifier(new AttributeModifier("attackBoost", scale, AttributeModifier.Operation.MULTIPLY_BASE));
    }

    private boolean teleportToEntity(Entity p_70816_1_) {
        Vec3d vec = p_70816_1_.getPositionVec().subtract(p_70816_1_.getLookVec().mul(3, 1, 3));
        double d1 = vec.getX();
        double d2 = 256;
        double d3 = vec.getZ();
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
        this.attemptTeleport(x, y, z, true);
        if (flag && !flag1) {
            this.world.playSound(null, this.prevPosX, this.prevPosY, this.prevPosZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
            this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
            return true;
        } else {
            return false;
        }
    }

    public boolean attemptTeleport(double p_213373_1_, double p_213373_3_, double p_213373_5_, boolean p_213373_7_) {
        double d0 = this.getPosX();
        double d1 = this.getPosY();
        double d2 = this.getPosZ();
        double d3 = p_213373_3_;
        boolean flag = false;
        BlockPos blockpos = new BlockPos(p_213373_1_, p_213373_3_, p_213373_5_);
        World world = this.world;
        if (world.isBlockLoaded(blockpos)) {
            boolean flag1 = false;

            while (!flag1 && blockpos.getY() > 0) {
                BlockPos blockpos1 = blockpos.down();
                BlockState blockstate = world.getBlockState(blockpos1);
                if (blockstate.getMaterial().blocksMovement()) {
                    flag1 = true;
                } else {
                    --d3;
                    blockpos = blockpos1;
                }
            }

            if (flag1) {
                this.setPositionAndUpdate(p_213373_1_, d3, p_213373_5_);
                if (world.hasNoCollisions(this) && !world.containsAnyLiquid(this.getBoundingBox())) {
                    flag = true;
                }
            }
        }

        if (!flag) {
            this.setPositionAndUpdate(d0, d1, d2);
            return false;
        } else {
            if (p_213373_7_) {
                world.setEntityState(this, (byte) 46);
            }

            if (this instanceof CreatureEntity) {
                this.getNavigator().clearPath();
            }

            return true;
        }
    }

    static class devourXP extends Goal {

        World world;
        MagicalPredatorEntity entity;

        public devourXP(World worldIn, MagicalPredatorEntity entityIn) {
            this.world = worldIn;
            this.entity = entityIn;
        }

        @Override
        public boolean shouldExecute() {
            return true;
        }

        @Override
        public void tick() {
            this.world.getEntitiesWithinAABB(EntityType.EXPERIENCE_ORB, this.entity.getBoundingBox().grow(4), Objects::nonNull).forEach(
                    xpOrb -> {
                        DragonSurvivalMod.INSTANCE.send(PacketDistributor.ALL.noArg(), new PacketSyncXPDevour(this.entity.getEntityId(), xpOrb.getEntityId()));
                    }
            );
            super.tick();
        }
    }

    static class isNearestDragonTargetGoal extends NearestAttackableTargetGoal {

        public isNearestDragonTargetGoal(MobEntity p_i50313_1_, boolean p_i50313_3_) {
            super(p_i50313_1_, PlayerEntity.class, p_i50313_3_);
        }

        @Override
        protected AxisAlignedBB getTargetableArea(double p_188511_1_) {
            PlayerEntity player = (PlayerEntity) this.nearestTarget;
            return this.goalOwner.getBoundingBox().grow(getActualDistance(player));
        }
    }

    static class FindPlayerGoal extends NearestAttackableTargetGoal<PlayerEntity> {
        private final MagicalPredatorEntity beast;

        public FindPlayerGoal(MagicalPredatorEntity beastIn) {
            super(beastIn, PlayerEntity.class, false);
            this.beast = beastIn;
        }

        @Override
        protected AxisAlignedBB getTargetableArea(double p_188511_1_) {
            PlayerEntity player = (PlayerEntity) this.nearestTarget;
            return this.goalOwner.getBoundingBox().grow(getActualDistance(player));
        }

        @Override
        public void tick() {
            if (this.nearestTarget != null) {
                if (this.nearestTarget instanceof PlayerEntity) {
                    float diff = getActualDistance((PlayerEntity) this.nearestTarget) - beast.getDistance(this.nearestTarget);
                    if (diff <= 3 & diff >= -2) {
                        beast.teleportToEntity(this.nearestTarget);
                    }
                }
            }
        }

    }
}

