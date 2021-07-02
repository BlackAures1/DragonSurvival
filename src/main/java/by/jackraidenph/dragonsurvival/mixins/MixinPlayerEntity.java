package by.jackraidenph.dragonsurvival.mixins;

import java.util.UUID;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.handlers.DragonFoodHandler;
import by.jackraidenph.dragonsurvival.handlers.DragonSizeHandler;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.Pose;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.passive.IFlyingAnimal;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Effects;
import net.minecraft.stats.Stat;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.DamageSource;
import net.minecraft.util.FoodStats;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;


@Mixin(PlayerEntity.class)
public abstract class MixinPlayerEntity extends LivingEntity{
	
	@Shadow
	@Final
	public PlayerAbilities abilities;


	protected MixinPlayerEntity(EntityType<? extends LivingEntity> p_i48577_1_, World p_i48577_2_) {
		super(p_i48577_1_, p_i48577_2_);
	}
	
	private static final UUID SLOW_FALLING_ID = UUID.fromString("A5B6CF2A-2F7C-31EF-9022-7C3E7D5E6ABA");
	private static final AttributeModifier SLOW_FALLING = new AttributeModifier(SLOW_FALLING_ID, "Slow falling acceleration reduction", -0.07, AttributeModifier.Operation.ADDITION); // Add -0.07 to 0.08 so we get the vanilla default of 0.01


	@Inject(method = "travel", at = @At("HEAD"), cancellable = true)
	public void travel(Vector3d p_213352_1_, CallbackInfo ci) {
		if (DragonStateProvider.isDragon(this)) {
			double d0 = this.getX();
			double d1 = this.getY();
			double d2 = this.getZ();
			if ((DragonStateProvider.getCap(this).isPresent() && ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.caveLavaSwimming.get() && DragonStateProvider.getCap(this).orElseGet(null).getType() == DragonType.CAVE && DragonSizeHandler.getOverridePose(this) == Pose.SWIMMING) 
					|| this.isSwimming() && !this.isPassenger()) {
				double d3 = this.getLookAngle().y;
				double d4 = d3 < -0.2D ? 0.085D : 0.06D;
				if (d3 <= 0.0D || this.jumping || !this.level.getBlockState(new BlockPos(this.getX(), this.getY() + 1.0D - 0.1D, this.getZ())).getFluidState().isEmpty()) {
					Vector3d vector3d1 = this.getDeltaMovement();
					this.setDeltaMovement(vector3d1.add(0.0D, (d3 - vector3d1.y) * d4, 0.0D));
				}
			}
			
			if (this.abilities.flying && !this.isPassenger()) {
				double d5 = this.getDeltaMovement().y;
				float f = this.flyingSpeed;
				this.flyingSpeed = this.abilities.getFlyingSpeed() * (float)(this.isSprinting() ? 2 : 1);
				dragonTravel(p_213352_1_);
				Vector3d vector3d = this.getDeltaMovement();
				this.setDeltaMovement(vector3d.x, d5 * 0.6D, vector3d.z);
				this.flyingSpeed = f;
				this.fallDistance = 0.0F;
				this.setSharedFlag(7, false);
			} else
				dragonTravel(p_213352_1_);
			this.checkMovementStatistics(this.getX() - d0, this.getY() - d1, this.getZ() - d2);
			ci.cancel();
		}
	  
    }
	
	public void dragonTravel(Vector3d p_213352_1_) {
		if (!DragonStateProvider.isDragon(this)) {
			super.travel(p_213352_1_);
			return;
		}
      if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
         double d0 = 0.08D;
         ModifiableAttributeInstance gravity = this.getAttribute(net.minecraftforge.common.ForgeMod.ENTITY_GRAVITY.get());
         boolean flag = this.getDeltaMovement().y <= 0.0D;
         if (flag && this.hasEffect(Effects.SLOW_FALLING)) {
            if (!gravity.hasModifier(SLOW_FALLING)) gravity.addTransientModifier(SLOW_FALLING);
            this.fallDistance = 0.0F;
         } else if (gravity.hasModifier(SLOW_FALLING)) {
            gravity.removeModifier(SLOW_FALLING);
         }
         d0 = gravity.getValue();

         FluidState fluidstate = this.level.getFluidState(this.blockPosition());
         if (this.isInWater() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate.getType())) {
            double d8 = this.getY();
            float f5 = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
            float f6 = 0.02F;
            float f7 = (float)EnchantmentHelper.getDepthStrider(this);
            if (f7 > 3.0F) {
               f7 = 3.0F;
            }

            if (!this.onGround) {
               f7 *= 0.5F;
            }

            if (f7 > 0.0F) {
               f5 += (0.54600006F - f5) * f7 / 3.0F;
               f6 += (this.getSpeed() - f6) * f7 / 3.0F;
            }

            if (this.hasEffect(Effects.DOLPHINS_GRACE)) {
               f5 = 0.96F;
            }

            f6 *= (float)this.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).getValue();
            this.moveRelative(f6, p_213352_1_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            Vector3d vector3d6 = this.getDeltaMovement();
            if (this.horizontalCollision && this.onClimbable()) {
               vector3d6 = new Vector3d(vector3d6.x, 0.2D, vector3d6.z);
            }

            this.setDeltaMovement(vector3d6.multiply((double)f5, (double)0.8F, (double)f5));
            Vector3d vector3d2 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
            this.setDeltaMovement(vector3d2);
            if (this.horizontalCollision && this.isFree(vector3d2.x, vector3d2.y + (double)0.6F - this.getY() + d8, vector3d2.z)) {
               this.setDeltaMovement(vector3d2.x, (double)0.3F, vector3d2.z);
            }
         } else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate.getType()) && (ConfigHandler.SERVER.bonuses.get() && ConfigHandler.SERVER.caveLavaSwimming.get() && DragonStateProvider.getCap(this).isPresent() && DragonStateProvider.getCap(this).orElseGet(null).getType() == DragonType.CAVE)) {
        	double d8 = this.getY();
            float f5 = this.isSprinting() ? 0.9F : this.getWaterSlowDown();
            float f6 = 0.02F;
            float f7 = (float)EnchantmentHelper.getDepthStrider(this);
            if (f7 > 3.0F) {
               f7 = 3.0F;
            }

            if (!this.onGround) {
               f7 *= 0.5F;
            }

            if (f7 > 0.0F) {
               f5 += (0.54600006F - f5) * f7 / 3.0F;
               f6 += (this.getSpeed() - f6) * f7 / 3.0F;
            }

            if (this.hasEffect(Effects.DOLPHINS_GRACE)) {
               f5 = 0.96F;
            }

            f6 *= (float)this.getAttribute(net.minecraftforge.common.ForgeMod.SWIM_SPEED.get()).getValue();
            this.moveRelative(f6, p_213352_1_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            Vector3d vector3d6 = this.getDeltaMovement();
            if (this.horizontalCollision && this.onClimbable()) {
               vector3d6 = new Vector3d(vector3d6.x, 0.2D, vector3d6.z);
            }

            this.setDeltaMovement(vector3d6.multiply((double)f5, (double)0.8F, (double)f5));
            Vector3d vector3d2 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
            this.setDeltaMovement(vector3d2);
            if (this.horizontalCollision && this.isFree(vector3d2.x, vector3d2.y + (double)0.6F - this.getY() + d8, vector3d2.z)) {
               this.setDeltaMovement(vector3d2.x, (double)0.3F, vector3d2.z);
            }
         } else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidstate.getType())) {
            double d7 = this.getY();
            this.moveRelative(0.02F, p_213352_1_);
            this.move(MoverType.SELF, this.getDeltaMovement());
            if (this.getFluidHeight(FluidTags.LAVA) <= this.getFluidJumpThreshold()) {
               this.setDeltaMovement(this.getDeltaMovement().multiply(0.5D, (double)0.8F, 0.5D));
               Vector3d vector3d3 = this.getFluidFallingAdjustedMovement(d0, flag, this.getDeltaMovement());
               this.setDeltaMovement(vector3d3);
            } else {
               this.setDeltaMovement(this.getDeltaMovement().scale(0.5D));
            }

            if (!this.isNoGravity()) {
               this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -d0 / 4.0D, 0.0D));
            }

            Vector3d vector3d4 = this.getDeltaMovement();
            if (this.horizontalCollision && this.isFree(vector3d4.x, vector3d4.y + (double)0.6F - this.getY() + d7, vector3d4.z)) {
               this.setDeltaMovement(vector3d4.x, (double)0.3F, vector3d4.z);
            }
         } else if (this.isFallFlying()) {
            Vector3d vector3d = this.getDeltaMovement();
            if (vector3d.y > -0.5D) {
               this.fallDistance = 1.0F;
            }

            Vector3d vector3d1 = this.getLookAngle();
            float f = this.xRot * ((float)Math.PI / 180F);
            double d1 = Math.sqrt(vector3d1.x * vector3d1.x + vector3d1.z * vector3d1.z);
            double d3 = Math.sqrt(getHorizontalDistanceSqr(vector3d));
            double d4 = vector3d1.length();
            float f1 = MathHelper.cos(f);
            f1 = (float)((double)f1 * (double)f1 * Math.min(1.0D, d4 / 0.4D));
            vector3d = this.getDeltaMovement().add(0.0D, d0 * (-1.0D + (double)f1 * 0.75D), 0.0D);
            if (vector3d.y < 0.0D && d1 > 0.0D) {
               double d5 = vector3d.y * -0.1D * (double)f1;
               vector3d = vector3d.add(vector3d1.x * d5 / d1, d5, vector3d1.z * d5 / d1);
            }

            if (f < 0.0F && d1 > 0.0D) {
               double d9 = d3 * (double)(-MathHelper.sin(f)) * 0.04D;
               vector3d = vector3d.add(-vector3d1.x * d9 / d1, d9 * 3.2D, -vector3d1.z * d9 / d1);
            }

            if (d1 > 0.0D) {
               vector3d = vector3d.add((vector3d1.x / d1 * d3 - vector3d.x) * 0.1D, 0.0D, (vector3d1.z / d1 * d3 - vector3d.z) * 0.1D);
            }

            this.setDeltaMovement(vector3d.multiply((double)0.99F, (double)0.98F, (double)0.99F));
            this.move(MoverType.SELF, this.getDeltaMovement());
            if (this.horizontalCollision && !this.level.isClientSide) {
               double d10 = Math.sqrt(getHorizontalDistanceSqr(this.getDeltaMovement()));
               double d6 = d3 - d10;
               float f2 = (float)(d6 * 10.0D - 3.0D);
               if (f2 > 0.0F) {
                  this.playSound(this.getFallDamageSound((int)f2), 1.0F, 1.0F);
                  this.hurt(DamageSource.FLY_INTO_WALL, f2);
               }
            }

            if (this.onGround && !this.level.isClientSide) {
               this.setSharedFlag(7, false);
            }
         } else {
            BlockPos blockpos = this.getBlockPosBelowThatAffectsMyMovement();
            float f3 = this.level.getBlockState(this.getBlockPosBelowThatAffectsMyMovement()).getSlipperiness(level, this.getBlockPosBelowThatAffectsMyMovement(), this);
            float f4 = this.onGround ? f3 * 0.91F : 0.91F;
            Vector3d vector3d5 = this.handleRelativeFrictionAndCalculateMovement(p_213352_1_, f3);
            double d2 = vector3d5.y;
            if (this.hasEffect(Effects.LEVITATION)) {
               d2 += (0.05D * (double)(this.getEffect(Effects.LEVITATION).getAmplifier() + 1) - vector3d5.y) * 0.2D;
               this.fallDistance = 0.0F;
            } else if (this.level.isClientSide && !this.level.hasChunkAt(blockpos)) {
               if (this.getY() > 0.0D) {
                  d2 = -0.1D;
               } else {
                  d2 = 0.0D;
               }
            } else if (!this.isNoGravity()) {
               d2 -= d0;
            }

            this.setDeltaMovement(vector3d5.x * (double)f4, d2 * (double)0.98F, vector3d5.z * (double)f4);
         }
      }

      this.calculateEntityAnimation(this, this instanceof IFlyingAnimal);
   }

	@Inject(at = @At("HEAD"), method = "eat", cancellable = true)
	public void dragonEat(World level, ItemStack itemStack, CallbackInfoReturnable<ItemStack> ci) {
		DragonStateProvider.getCap(this).ifPresent(dragonStateHandler -> {
			if (dragonStateHandler.isDragon()) {
				DragonFoodHandler.dragonEat(this.getFoodData(), itemStack.getItem(), itemStack, dragonStateHandler.getType());
				this.awardStat(Stats.ITEM_USED.get(itemStack.getItem()));
				level.playSound((PlayerEntity)null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_BURP, SoundCategory.PLAYERS, 0.5F, level.random.nextFloat() * 0.1F + 0.9F);
				if ((PlayerEntity)(Object)this instanceof ServerPlayerEntity) {
					CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayerEntity)(Object)this, itemStack);
				}
		
				ci.setReturnValue(super.eat(level, itemStack));
			}
		});
	}


    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/player/PlayerEntity;getMyRidingOffset()D", cancellable = true)
	public void dragonRidingOffset(CallbackInfoReturnable<Double> ci){
        DragonStateProvider.getCap(this).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon())
                ci.setReturnValue(0.25D);
        });
    }

	@Shadow
	public FoodStats getFoodData() {
		throw new IllegalStateException("Mixin failed to shadow getFoodData()");
	}


	@Shadow
	public void awardStat(Stat<Item> stat) {
		throw new IllegalStateException("Mixin failed to shadow awardStat()");
	}


	@Shadow
	public void checkMovementStatistics(double d, double e, double f) {
		throw new IllegalStateException("Mixin failed to shadow checkMovementStatistics()");
	}
}
