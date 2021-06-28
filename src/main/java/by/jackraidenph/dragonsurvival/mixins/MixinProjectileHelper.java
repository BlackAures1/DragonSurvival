package by.jackraidenph.dragonsurvival.mixins;

import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ProjectileHelper.class)
public abstract class MixinProjectileHelper {

    // This hack exists because a normal inject at head was causing JAVA crashes. ¯\_(ツ)_/¯
    @Redirect(method = "Lnet/minecraft/entity/projectile/ProjectileHelper;getEntityHitResult(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/vector/Vector3d;Lnet/minecraft/util/math/vector/Vector3d;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/function/Predicate;D)Lnet/minecraft/util/math/EntityRayTraceResult;",
            at = @At(value="INVOKE", target="Lnet/minecraft/entity/Entity;getBoundingBox()Lnet/minecraft/util/math/AxisAlignedBB;"
    ))
    private static AxisAlignedBB dragonEntityHitboxHack(Entity entity) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (DragonStateProvider.getCap(player).isPresent() && entity instanceof PlayerEntity) {
            DragonStateHandler cap = DragonStateProvider.getCap(player).orElseGet(null);
            if (cap.isDragon() && entity.getId() == cap.getPassengerId())
                return new AxisAlignedBB(0, -1000, 0, 0, -1000, 0);
        }
        return entity.getBoundingBox();
    }
}
