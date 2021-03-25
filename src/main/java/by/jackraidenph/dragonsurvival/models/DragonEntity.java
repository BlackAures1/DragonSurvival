package by.jackraidenph.dragonsurvival.models;

import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class DragonEntity implements IAnimatable {
    AnimationFactory animationFactory = new AnimationFactory(this);

    @Override
    public void registerControllers(AnimationData animationData) {

    }

    @Override
    public AnimationFactory getFactory() {
        return animationFactory;
    }

}
