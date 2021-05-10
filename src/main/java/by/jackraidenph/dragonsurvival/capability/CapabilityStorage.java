package by.jackraidenph.dragonsurvival.capability;

import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityStorage implements Capability.IStorage<DragonStateHandler> {

    @Override
    public INBT writeNBT(Capability<DragonStateHandler> capability, DragonStateHandler instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("isDragon", instance.isDragon());
        if (instance.isDragon()) {
            DragonStateHandler.DragonMovementData movementData = instance.getMovementData();
            tag.putInt("Base damage", instance.getBaseDamage());
            tag.putDouble("bodyYaw", movementData.bodyYaw);
            tag.putDouble("headYaw", movementData.headYaw);
            tag.putDouble("headPitch", movementData.headPitch);
            tag.putBoolean("isHiding", instance.isHiding());
            tag.putString("type", instance.getType().toString());
            tag.putFloat("size", instance.getSize());
            tag.putBoolean("hasWings", instance.hasWings());
        }
        return tag;
    }

    @Override
    public void readNBT(Capability<DragonStateHandler> capability, DragonStateHandler instance, Direction side, INBT base) {
        CompoundNBT tag = (CompoundNBT) base;
        instance.setIsDragon(tag.getBoolean("isDragon"));
        if (tag.getBoolean("isDragon")) {
            instance.setMovementData(tag.getDouble("bodyYaw"), tag.getDouble("headYaw"),
                    tag.getDouble("headPitch"));
            instance.setIsHiding(tag.getBoolean("isHiding"));
            instance.setType(DragonType.valueOf(tag.getString("type")));
            instance.setSize(tag.getFloat("size"));
            if (instance.getSize() == 0)
                instance.setSize(DragonLevel.BABY.initialHealth);
            instance.setHasWings(tag.getBoolean("hasWings"));
            int base_damage = tag.getInt("Base damage");
            if (base_damage == 0)
                instance.setBaseDamage(instance.getLevel().baseDamage);
            else
                instance.setBaseDamage(base_damage);
        }
    }
}
