package by.jackraidenph.dragonsurvival.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ILivingEntityData;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.HurtByTargetGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.villager.VillagerType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.IServerWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class Prince extends PrincessEntity {
    public Prince(EntityType<? extends VillagerEntity> entityType, World world) {
        super(entityType, world);
    }

    public Prince(EntityType<? extends VillagerEntity> entityType, World world, VillagerType villagerType) {
        super(entityType, world, villagerType);
    }

    protected void registerGoals() {
        super.registerGoals();
        this.targetSelector.addGoal(5, new HurtByTargetGoal(this));
        this.goalSelector.addGoal(5, new MeleeAttackGoal(this, 1.0D, true));
    }

    @Nullable
    public ILivingEntityData finalizeSpawn(IServerWorld serverWorld, DifficultyInstance difficultyInstance, SpawnReason reason, @Nullable ILivingEntityData livingEntityData, @Nullable CompoundNBT compoundNBT) {
        setItemInHand(Hand.MAIN_HAND, new ItemStack((IItemProvider) Items.GOLDEN_SWORD));
        return super.finalizeSpawn(serverWorld, difficultyInstance, reason, livingEntityData, compoundNBT);
    }
}

