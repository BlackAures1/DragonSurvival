package by.jackraidenph.dragonsurvival.entity;

import by.jackraidenph.dragonsurvival.Functions;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.gecko.Knight;
import by.jackraidenph.dragonsurvival.goals.AlertExceptHunters;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class Shooter extends Hunter implements ICrossbowUser {
    protected int bolasCooldown = Functions.secondsToTicks(30);
    public Shooter(EntityType<? extends CreatureEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(8, new AlertExceptHunters<>(this, HunterHound.class, Knight.class, Squire.class));
    }

    @Override
    public void setChargingCrossbow(boolean p_213671_1_) {

    }

    @Override
    public void shootCrossbowProjectile(LivingEntity p_230284_1_, ItemStack p_230284_2_, ProjectileEntity p_230284_3_, float p_230284_4_) {

    }

    @Override
    public void onCrossbowAttackPerformed() {

    }

    @Override
    public void performRangedAttack(LivingEntity p_82196_1_, float p_82196_2_) {

    }

    public void tick() {
        super.tick();
        if (ConfigHandler.COMMON.hunterHasBolas.get()) {
            LivingEntity target = getTarget();
            if (target instanceof PlayerEntity && DragonStateProvider.isDragon(target))
                if (this.bolasCooldown == 0) {
                    performBolasThrow(target);
                    this.bolasCooldown = Functions.secondsToTicks(30);
                } else {
                    this.bolasCooldown--;
                }
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

    public void addAdditionalSaveData(CompoundNBT compoundNBT) {
        super.addAdditionalSaveData(compoundNBT);
        compoundNBT.putInt("Bolas cooldown", this.bolasCooldown);
    }

    public void readAdditionalSaveData(CompoundNBT compoundNBT) {
        super.readAdditionalSaveData(compoundNBT);
        this.bolasCooldown = compoundNBT.getInt("Bolas cooldown");
    }
}
