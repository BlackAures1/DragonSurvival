package by.jackraidenph.dragonsurvival.util;

import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectInstance;

import javax.annotation.Nullable;

public class EffectInstance2 extends EffectInstance {
    public EffectInstance2(Effect effect) {
        super(effect);
    }

    public EffectInstance2(Effect effect, int p_i46812_2_) {
        super(effect, p_i46812_2_);
    }

    public EffectInstance2(Effect p_i46813_1_, int p_i46813_2_, int p_i46813_3_) {
        super(p_i46813_1_, p_i46813_2_, p_i46813_3_);
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
