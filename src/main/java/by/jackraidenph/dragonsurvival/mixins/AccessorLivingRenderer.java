package by.jackraidenph.dragonsurvival.mixins;

import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(LivingRenderer.class)
public interface AccessorLivingRenderer {
    @Accessor("layers")
    List<LayerRenderer> getRenderLayers();
    @Invoker("shouldShowName")
    boolean callShouldShowName(LivingEntity p_177070_1_);
}
