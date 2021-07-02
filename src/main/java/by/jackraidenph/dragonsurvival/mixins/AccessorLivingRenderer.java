package by.jackraidenph.dragonsurvival.mixins;

import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;

@Mixin(LivingRenderer.class)
public interface AccessorLivingRenderer {
    @Accessor("layers")
    List<LayerRenderer> getRenderLayers();
}
