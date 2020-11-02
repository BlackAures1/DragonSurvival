package by.jackraidenph.dragonsurvival.capability;

import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityStorage implements Capability.IStorage<PlayerStateHandler> {
    private static CompoundNBT writeVec3d(Vec3d vec) {
        CompoundNBT comp = new CompoundNBT();
        comp.putDouble("X", vec.x);
        comp.putDouble("Y", vec.y);
        comp.putDouble("Z", vec.z);
        return comp;
    }

    private static Vec3d getVec3d(INBT nbt) {
        CompoundNBT tag = (CompoundNBT) nbt;
        Vec3d vec;
        vec = new Vec3d(tag.getDouble("X"), tag.getDouble("Y"), tag.getDouble("Z"));
        return vec;
    }

    @Override
    public INBT writeNBT(Capability<PlayerStateHandler> capability, PlayerStateHandler instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("isDragon", instance.getIsDragon());
        if (instance.getIsDragon()) {
            instance.getMovementData().ifPresent(data -> {
                        tag.putDouble("bodyYaw", data.bodyYaw);
                        tag.putDouble("headYaw", data.headYaw);
                        tag.putDouble("headPitch", data.headPitch);
                        tag.put("headPos", writeVec3d(data.headPos));
                        tag.put("tailPos", writeVec3d(data.tailPos));
                    }
            );
            tag.putBoolean("isHiding", instance.getIsHiding());
            tag.putString("type", instance.getType().toString());
            tag.putInt("level", instance.getLevel());
        }
        //TODO redo
        instance.syncCapabilityData(true);
        instance.syncMovement(true);
        return tag;
    }

    @Override
    public void readNBT(Capability<PlayerStateHandler> capability, PlayerStateHandler instance, Direction side, INBT base) {
        CompoundNBT tag = (CompoundNBT) base;
        instance.setIsDragon(tag.getBoolean("isDragon"));
        if (tag.getBoolean("isDragon")) {
            instance.setMovementData(
                    new PlayerStateHandler.DragonMovementData(
                            tag.getDouble("bodyYaw"),
                            tag.getDouble("headYaw"),
                            tag.getDouble("headPitch"),
                            getVec3d(tag.get("headPos")),
                            getVec3d(tag.get("tailPos"))
                    ), true);
            instance.setIsHiding(tag.getBoolean("isHiding"));
            instance.setType(DragonType.valueOf(tag.getString("type")));
            instance.setLevel(tag.getInt("level"));
        }
        instance.syncCapabilityData(true);
        instance.syncMovement(true);
    }
}
