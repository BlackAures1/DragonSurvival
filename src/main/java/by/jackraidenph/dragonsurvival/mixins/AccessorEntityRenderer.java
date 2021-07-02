package by.jackraidenph.dragonsurvival.mixins;

import net.minecraft.client.renderer.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityRenderer.class)
public interface AccessorEntityRenderer {
    @Accessor("shadowRadius")
    public void setShadowRadius(float radius);
}
