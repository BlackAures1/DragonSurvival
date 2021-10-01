package by.jackraidenph.dragonsurvival.entity;

import by.jackraidenph.dragonsurvival.Functions;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.gecko.Knight;
import by.jackraidenph.dragonsurvival.goals.AlertExceptHunters;
import by.jackraidenph.dragonsurvival.goals.CrossbowAttackGoal;
import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ICrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.World;

public class Shooter extends Hunter implements ICrossbowUser {
    private static final DataParameter<Boolean> IS_CHARGING_CROSSBOW = EntityDataManager.defineId(Shooter.class, DataSerializers.BOOLEAN);

    protected int bolasCooldown = Functions.secondsToTicks(30);

    public Shooter(EntityType<? extends CreatureEntity> entityType, World world) {
        super(entityType, world);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        goalSelector.addGoal(3, new CrossbowAttackGoal<>(this, 1.0D, 8.0F));
        goalSelector.addGoal(8, new AlertExceptHunters<>(this, HunterHound.class, Knight.class, Squire.class));
    }

    @Override
    public void setChargingCrossbow(boolean p_213671_1_) {
        entityData.set(IS_CHARGING_CROSSBOW, p_213671_1_);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity p_230284_1_, ItemStack p_230284_2_, ProjectileEntity p_230284_3_, float p_230284_4_) {
        shootCrossbowProjectile(this, p_230284_1_, p_230284_3_, p_230284_4_, 1.6F);
    }

    @Override
    public void onCrossbowAttackPerformed() {
        noActionTime = 0;
        ItemStack crossbow = getItemInHand(Hand.MAIN_HAND);
        addArrow(crossbow);
    }

    @Override
    public void performRangedAttack(LivingEntity p_82196_1_, float p_82196_2_) {
        performCrossbowAttack(this, 1.6F);
    }

    public void tick() {
        super.tick();
        if (ConfigHandler.COMMON.hunterHasBolas.get()) {
            LivingEntity target = getTarget();
            if (target instanceof PlayerEntity && DragonStateProvider.isDragon(target))
                if (this.bolasCooldown == 0) {
                    performBolasThrow(target);
                    this.bolasCooldown = Functions.secondsToTicks(15);
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

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_CHARGING_CROSSBOW, false);
    }

    public boolean isChargingCrossbow() {
        return this.entityData.get(IS_CHARGING_CROSSBOW);
    }

    public AbstractIllagerEntity.ArmPose getArmPose() {
        if (this.isChargingCrossbow()) {
            return AbstractIllagerEntity.ArmPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(item -> item instanceof net.minecraft.item.CrossbowItem)) {
            return AbstractIllagerEntity.ArmPose.CROSSBOW_HOLD;
        } else {
            return this.isAggressive() ? AbstractIllagerEntity.ArmPose.ATTACKING : AbstractIllagerEntity.ArmPose.NEUTRAL;
        }
    }

    protected void populateDefaultEquipmentSlots(DifficultyInstance p_180481_1_) {
        ItemStack stack = new ItemStack(Items.CROSSBOW);
        addArrow(stack);
        this.setItemSlot(EquipmentSlotType.MAINHAND, stack);
    }

    private void addArrow(ItemStack stack) {
        CompoundNBT compoundNBT = stack.getOrCreateTag();
        ListNBT listNBT = compoundNBT.getList("ChargedProjectiles", 10);
        CompoundNBT nbt = new CompoundNBT();
        new ItemStack(Items.ARROW).save(nbt);
        listNBT.add(nbt);
        compoundNBT.put("ChargedProjectiles", listNBT);
    }
}
