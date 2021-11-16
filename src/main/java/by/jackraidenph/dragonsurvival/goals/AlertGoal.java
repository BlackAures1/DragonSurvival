package by.jackraidenph.dragonsurvival.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.Arrays;

public class AlertGoal<T extends LivingEntity> extends Goal {
    T owner;
    Class<? extends MobEntity>[] toAlert;

    public AlertGoal(T owner, Class<? extends MobEntity>... toAlert) {
        this.owner = owner;
        this.toAlert = toAlert;
    }

    public boolean canUse() {
        return this.owner.getLastHurtByMob() != null && this.owner.getLastHurtByMob().isAlive();
    }

    public void tick() {
        double range = this.owner.getAttributeValue(Attributes.FOLLOW_RANGE);
        AxisAlignedBB axisAlignedBB = (new AxisAlignedBB(this.owner.blockPosition())).inflate(range);
        Arrays.stream(this.toAlert).forEach(aClass -> this.owner.level.getEntitiesOfClass(aClass, axisAlignedBB).forEach(mobEntity -> mobEntity.setTarget(owner.getLastHurtByMob())));
    }
}
