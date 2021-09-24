package by.jackraidenph.dragonsurvival.util;

import net.minecraft.entity.MobEntity;
import net.minecraft.pathfinding.GroundPathNavigator;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

public class GroundNavigator extends GroundPathNavigator {
    public GroundNavigator(MobEntity p_i45875_1_, World p_i45875_2_) {
        super(p_i45875_1_, p_i45875_2_);
    }

    @Override
    protected void doStuckDetection(Vector3d v) {
        if (tick - lastStuckCheck > 60 && v.distanceToSqr(this.lastStuckCheckPos) < 5) {
            this.stop();
        }
        super.doStuckDetection(v);
    }
}
