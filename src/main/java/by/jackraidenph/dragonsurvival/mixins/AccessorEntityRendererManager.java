package by.jackraidenph.dragonsurvival.mixins;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.PlayerRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;

@Mixin(EntityRendererManager.class)
public interface AccessorEntityRendererManager {
    @Accessor("playerRenderers")
    Map<String, PlayerRenderer> getPlayerRenderers();
}
