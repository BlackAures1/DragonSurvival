package by.jackraidenph.dragonsurvival.goals;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.util.math.AxisAlignedBB;

import java.util.List;

public class FollowMobGoal<T extends Class<? extends LivingEntity>> extends Goal {
    T classs;
    MobEntity follower;
    LivingEntity target;
    int distance;

    public FollowMobGoal(T classs, MobEntity follower, int distance) {
        this.classs = classs;
        this.follower = follower;
        this.distance = distance;
    }

    public boolean canUse() {
        if (this.target == null) {
            List<LivingEntity> list = this.follower.level.getEntitiesOfClass(this.classs, (new AxisAlignedBB(this.follower.blockPosition())).inflate(this.follower.getAttributeValue(Attributes.FOLLOW_RANGE)));
            if (!list.isEmpty()) {
                this.target = list.get(this.follower.getRandom().nextInt(list.size()));
            }
        }
        return (this.target != null);
    }

    public void tick() {
        if (this.follower.distanceToSqr(this.target) > (this.distance * this.distance)) {
            this.follower.getNavigation().moveTo(this.target, 1.0D);
        }
    }

    public void stop() {
        this.target = null;
    }
}
