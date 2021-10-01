package by.jackraidenph.dragonsurvival.util;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

import javax.annotation.Nullable;

public class EffectInstance2 extends EffectInstance {
    public EffectInstance2(Effect effect) {
        super(effect);
    }

    public EffectInstance2(Effect effect, int duration) {
        super(effect, duration);
    }

    public EffectInstance2(Effect effect, int duration, int strength) {
        super(effect, duration, strength);
    }

    public EffectInstance2(Effect effect, int duration, int strength, boolean ambient, boolean visible) {
        super(effect, duration, strength, ambient, visible);
    }

    public EffectInstance2(Effect effect, int duration, int strength, boolean ambient, boolean visible, boolean showIcon) {
        super(effect, duration, strength, ambient, visible, showIcon);
    }

    public EffectInstance2(Effect effect, int duration, int strength, boolean ambient, boolean visible, boolean showIcon, @Nullable EffectInstance hiddenEffect) {
        super(effect, duration, strength, ambient, visible, showIcon, hiddenEffect);
    }

    public EffectInstance2(EffectInstance other) {
        super(other);
    }
}
