package by.jackraidenph.dragonsurvival.mixins;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Optional;
import java.util.function.Predicate;

@Mixin(ProjectileHelper.class)
public abstract class MixinProjectileHelper {

    @Inject(method = "Lnet/minecraft/entity/projectile/ProjectileHelper;getEntityHitResult(Lnet/minecraft/entity/Entity;Lnet/minecraft/util/math/vector/Vector3d;Lnet/minecraft/util/math/vector/Vector3d;Lnet/minecraft/util/math/AxisAlignedBB;Ljava/util/function/Predicate;D)Lnet/minecraft/util/math/EntityRayTraceResult;",
    at = @At(value = "HEAD"), cancellable = true)
    private static void getEntityHitResultDragonIgnorePassenger(Entity p_221273_0_, Vector3d p_221273_1_, Vector3d p_221273_2_, AxisAlignedBB p_221273_3_, Predicate<Entity> p_221273_4_, double p_221273_5_, CallbackInfoReturnable<EntityRayTraceResult> ci){
        DragonStateProvider.getCap(p_221273_0_).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon()){
                World world = p_221273_0_.level;
                double d0 = p_221273_5_;
                Entity entity = null;
                Vector3d vector3d = null;

                for (Entity entity1 : world.getEntities(p_221273_0_, p_221273_3_, p_221273_4_)) {
                    AxisAlignedBB axisalignedbb = entity1.getBoundingBox().inflate((double) entity1.getPickRadius());
                    Optional<Vector3d> optional = axisalignedbb.clip(p_221273_1_, p_221273_2_);
                    if (axisalignedbb.contains(p_221273_1_)) {
                        if ((entity1.getRootVehicle() == p_221273_0_.getRootVehicle() && !entity1.canRiderInteract())
                                || (entity1.getId() == dragonStateHandler.getPassengerId() && entity1 instanceof PlayerEntity)){
                            if (d0 == 0.0D) {
                                entity = entity1;
                                vector3d = optional.orElse(p_221273_1_);
                            }
                        } else if (d0 >= 0.0D) {
                            entity = entity1;
                            vector3d = optional.orElse(p_221273_1_);
                            d0 = 0.0D;
                        }
                    } else if (optional.isPresent()) {
                        Vector3d vector3d1 = optional.get();
                        double d1 = p_221273_1_.distanceToSqr(vector3d1);
                        if (d1 < d0 || d0 == 0.0D) {
                            if ((entity1.getRootVehicle() == p_221273_0_.getRootVehicle() && !entity1.canRiderInteract())
                                || (entity1.getId() == dragonStateHandler.getPassengerId() && entity1 instanceof PlayerEntity)){
                                if (d0 == 0.0D) {
                                    entity = entity1;
                                    vector3d = vector3d1;
                                }
                            } else {
                                entity = entity1;
                                vector3d = vector3d1;
                                d0 = d1;
                            }
                        }
                    }
                }

                ci.setReturnValue(entity == null ? null : new EntityRayTraceResult(entity, vector3d));
            }
        });
    }
}
