package by.jackraidenph.dragonsurvival.mixins;

import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.handlers.DragonSizeHandler;
import by.jackraidenph.dragonsurvival.util.DragonType;
import com.mojang.realmsclient.gui.ListButton;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntitySize;
import net.minecraft.entity.Pose;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.vector.Vector3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class MixinEntity extends net.minecraftforge.common.capabilities.CapabilityProvider<Entity>{
    @Shadow
    private EntitySize dimensions;

    protected MixinEntity(Class<Entity> baseClass) {
        super(baseClass);
    }

    @Inject(at = @At(value = "HEAD"), method = "Lnet/minecraft/entity/Entity;displayFireAnimation()Z", cancellable = true)
    private void hideCaveDragonFireAnimation(CallbackInfoReturnable<Boolean> ci){
        DragonStateProvider.getCap((Entity)(Object)this).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.getType() == DragonType.CAVE)
                ci.setReturnValue(false);
        });
    }

    @Inject(at = @At(value = "HEAD"), method = "Lnet/minecraft/entity/Entity;getPassengersRidingOffset()D", cancellable = true)
    public void getDragonPassengersRidingOffset(CallbackInfoReturnable<Double> ci) {
        if (DragonStateProvider.isDragon((Entity)(Object)this)){
            switch (this.getPose()){
                case FALL_FLYING:
                case SWIMMING:
                case SPIN_ATTACK:
                    ci.setReturnValue((double)this.dimensions.height * 0.6D);
                    break;
                case CROUCHING:
                    ci.setReturnValue((double)this.dimensions.height * 0.45D);
                    break;
                default:
                    ci.setReturnValue((double)this.dimensions.height * 0.5D);
            }
        }
    }



    @Inject(at = @At(value = "HEAD"), method = "Lnet/minecraft/entity/Entity;isVisuallyCrawling()Z", cancellable = true)
    public void isDragonVisuallyCrawling(CallbackInfoReturnable<Boolean> ci){
        if (DragonStateProvider.isDragon((Entity)(Object)this))
            ci.setReturnValue(false);
    }

    @Redirect(method = "canEnterPose(Lnet/minecraft/entity/Pose;)Z", at = @At(value="INVOKE",
            target="Lnet/minecraft/entity/Entity;getBoundingBoxForPose(Lnet/minecraft/entity/Pose;)Lnet/minecraft/util/math/AxisAlignedBB;"
    ))
    public AxisAlignedBB dragonPoseBB(Entity entity, Pose pose) {
        if (DragonStateProvider.isDragon(entity) && ConfigHandler.SERVER.sizeChangesHitbox.get()){
            float size = DragonStateProvider.getCap(entity).orElseGet(null).getSize();
            float height = DragonSizeHandler.calculateModifiedHeight(DragonSizeHandler.calculateDragonHeight(size, ConfigHandler.SERVER.hitboxGrowsPastHuman.get()), pose, ConfigHandler.SERVER.sizeChangesHitbox.get());
            float width = DragonSizeHandler.calculateDragonWidth(size, ConfigHandler.SERVER.hitboxGrowsPastHuman.get()) / 2.0F;
            Vector3d vector3d = new Vector3d(getX() - (double)width, getY(), getZ() - (double)width);
            Vector3d vector3d1 = new Vector3d(getX() + (double)width, getY() + (double)height, getZ() + (double)width);
            return new AxisAlignedBB(vector3d, vector3d1);
        } else
            return getBoundingBoxForPose(pose);
    }

    @Shadow
    public Pose getPose(){
        throw new IllegalStateException("Mixin failed to shadow getPose()");
    }

    @Shadow
    public AxisAlignedBB getBoundingBoxForPose(Pose pose){
        throw new IllegalStateException("Mixin failed to shadow getBoundingBoxForPose()");
    }

    @Shadow
    public double getX(){
        throw new IllegalStateException("Mixin failed to shadow getX()");
    }

    @Shadow
    public double getY(){
        throw new IllegalStateException("Mixin failed to shadow getY()");
    }

    @Shadow
    public double getZ(){
        throw new IllegalStateException("Mixin failed to shadow getZ()");
    }


}
