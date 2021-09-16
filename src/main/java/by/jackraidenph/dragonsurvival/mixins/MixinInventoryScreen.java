package by.jackraidenph.dragonsurvival.mixins;

import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.DisplayEffectsScreen;
import net.minecraft.client.gui.recipebook.IRecipeShownListener;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.util.text.ITextComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(InventoryScreen.class)
public abstract class MixinInventoryScreen extends DisplayEffectsScreen<PlayerContainer> implements IRecipeShownListener {
    public MixinInventoryScreen(PlayerContainer p_i51091_1_, PlayerInventory p_i51091_2_, ITextComponent p_i51091_3_) {
        super(p_i51091_1_, p_i51091_2_, p_i51091_3_);
    }

    @Redirect(method = "Lnet/minecraft/client/gui/screen/inventory/InventoryScreen;renderEntityInInventory(IIIFFLnet/minecraft/entity/LivingEntity;)V", at = @At(value="INVOKE",
            target="Lcom/mojang/blaze3d/systems/RenderSystem;runAsFancy(Ljava/lang/Runnable;)V"
    ))
    private static void dragonScreenEntityRender(Runnable p_runAsFancy_0_){
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (DragonStateProvider.getCap(player).isPresent() && DragonStateProvider.getCap(player).orElseGet(null).isDragon())
            DragonStateProvider.getCap(player).ifPresent(dragonStateHandler -> {
                double bodyYaw = dragonStateHandler.getMovementData().bodyYaw;
                double headYaw = dragonStateHandler.getMovementData().headYaw;
                double headPitch = dragonStateHandler.getMovementData().headPitch;
                dragonStateHandler.getMovementData().bodyYaw = player.yBodyRot; // ?
                dragonStateHandler.getMovementData().headYaw = player.yHeadRot;
                dragonStateHandler.getMovementData().headPitch = player.xRot;
                RenderSystem.runAsFancy(p_runAsFancy_0_);
                dragonStateHandler.getMovementData().bodyYaw = bodyYaw;
                dragonStateHandler.getMovementData().headYaw = headYaw;
                dragonStateHandler.getMovementData().headPitch = headPitch;
            });
        else
            RenderSystem.runAsFancy(p_runAsFancy_0_);
    }

    @Redirect(method = "Lnet/minecraft/client/gui/screen/inventory/InventoryScreen;renderEntityInInventory(IIIFFLnet/minecraft/entity/LivingEntity;)V", at = @At(value="INVOKE",
            target="Ljava/lang/Math;atan(D)D"
    ), require = 2)
    private static double dragonScreenEntityRenderAtan(double a) {
        if (DragonStateProvider.isDragon(Minecraft.getInstance().player))
            return Math.atan(a / 40.0);
        return Math.atan(a);
    }
}
