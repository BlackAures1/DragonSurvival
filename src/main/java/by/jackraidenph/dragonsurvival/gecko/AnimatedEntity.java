package by.jackraidenph.dragonsurvival.gecko;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.HashMap;

public abstract class AnimatedEntity extends CreatureEntity implements IAnimatable {
    protected AnimationFactory animationFactory;
    /**
     * Holds current ticks of animations
     */
    protected HashMap<String, Integer> animationTicks;

    public AnimatedEntity(EntityType<? extends CreatureEntity> entityType, World world) {
        super(entityType, world);
        animationTicks = new HashMap<>();
        animationFactory = new AnimationFactory(this);
    }

    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
    }

    protected double getMovementSpeed() {
        return Math.sqrt(Math.pow(getX() - xo, 2) + Math.pow(getZ() - zo, 2));
    }

    protected void putDuration(String animation, int ticks) {
        animationTicks.put(animation, ticks);
    }

    protected void trackAnimation(String animation) {
        animationTicks.computeIfPresent(animation, (s, integer) -> --integer);
    }

    protected int getDuration(String animation) {
        return animationTicks.getOrDefault(animation, 0);
    }
}
