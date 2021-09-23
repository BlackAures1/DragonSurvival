package by.jackraidenph.dragonsurvival.goals;

import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.passive.horse.HorseEntity;

public class RideHorse<E extends MobEntity> extends Goal {
    protected E mob;

    public RideHorse(E mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return mob.getVehicle() instanceof HorseEntity;
    }

    @Override
    public void tick() {
        HorseEntity horseEntity = (HorseEntity) mob.getVehicle();
        horseEntity.yRot = mob.yRot;
        horseEntity.yBodyRot = mob.yBodyRot;
        horseEntity.getNavigation().moveTo(mob.getNavigation().getPath(), 2.5);
    }
}
