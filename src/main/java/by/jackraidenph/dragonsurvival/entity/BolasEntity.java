package by.jackraidenph.dragonsurvival.entity;

import by.jackraidenph.dragonsurvival.Functions;
import by.jackraidenph.dragonsurvival.registration.DragonEffects;
import by.jackraidenph.dragonsurvival.registration.EntityTypesInit;
import by.jackraidenph.dragonsurvival.registration.ItemsInit;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.projectile.ProjectileItemEntity;
import net.minecraft.item.Item;
import net.minecraft.network.IPacket;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.UUID;

public class BolasEntity extends ProjectileItemEntity {
    public static final UUID DISABLE_MOVEMENT = UUID.fromString("eab67409-4834-43d8-bdf6-736dc96375f2");
    public static final UUID DISABLE_JUMP = UUID.fromString("d7c976cd-edba-46aa-9002-294d429d7741");

    public BolasEntity(World world) {
        super(EntityTypesInit.BOLAS_ENTITY, world);
    }

    public BolasEntity(double p_i50156_2_, double p_i50156_4_, double p_i50156_6_, World world) {
        super(EntityTypesInit.BOLAS_ENTITY, p_i50156_2_, p_i50156_4_, p_i50156_6_, world);
    }

    public BolasEntity(LivingEntity shooter, World world) {
        super(EntityTypesInit.BOLAS_ENTITY, shooter, world);
    }


    protected Item getDefaultItem() {
        return ItemsInit.huntingNet;
    }


    protected void onHitEntity(EntityRayTraceResult entityRayTraceResult) {
        super.onHitEntity(entityRayTraceResult);
        Entity entity = entityRayTraceResult.getEntity();
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            ModifiableAttributeInstance attributeInstance = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
            AttributeModifier bolasTrap = new AttributeModifier(DISABLE_MOVEMENT, "Bolas trap", -attributeInstance.getValue(), AttributeModifier.Operation.ADDITION);
            boolean addEffect = false;
            if (!attributeInstance.hasModifier(bolasTrap)) {
                attributeInstance.addTransientModifier(bolasTrap);
                addEffect = true;
            }

            ModifiableAttributeInstance jump = livingEntity.getAttribute(ForgeMod.ENTITY_GRAVITY.get());
            if (jump != null) {
                AttributeModifier disableJump = new AttributeModifier(DISABLE_JUMP, "Jump debuff", 3, AttributeModifier.Operation.MULTIPLY_TOTAL);
                if (!jump.hasModifier(disableJump)) {
                    jump.addTransientModifier(disableJump);
                    addEffect = true;
                }
            }
            if (addEffect)
                livingEntity.addEffect(new EffectInstance(DragonEffects.TRAPPED, Functions.secondsToTicks(20)));

        }
    }


    protected void onHit(RayTraceResult p_70227_1_) {
        super.onHit(p_70227_1_);
        if (!this.level.isClientSide)
            remove();
    }

    @Override
    public IPacket<?> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }
}