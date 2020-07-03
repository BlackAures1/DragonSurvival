package by.jackraidenph.dragonsurvival.capability;

import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.common.capabilities.Capability;

public class CapabilityStorage implements Capability.IStorage<IPlayerStateHandler> {
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
    public INBT writeNBT(Capability<IPlayerStateHandler> capability, IPlayerStateHandler instance, Direction side) {
        CompoundNBT tag = new CompoundNBT();
        tag.putBoolean("isDragon", instance.getIsDragon());
        if (instance.getIsDragon()) {
            System.out.println("stonks");
            tag.putString("type", instance.getType().toString());
            tag.putInt("level", instance.getLevel());
            tag.putDouble("bodyYaw", (Double) instance.getMovementData().get("bodyYaw"));
            tag.putDouble("headYaw", (Double) instance.getMovementData().get("headYaw"));
            tag.putDouble("neckLength", instance.getNeckLength());
            tag.putDouble("headPitch", (Double) instance.getMovementData().get("headPitch"));
            tag.put("headPos", writeVec3d((Vec3d) instance.getMovementData().get("headPos")));
            tag.put("tailPos", writeVec3d((Vec3d) instance.getMovementData().get("tailPos")));
        } else
            System.out.println("not stonks");
        return tag;
    }

    @Override
    public void readNBT(Capability<IPlayerStateHandler> capability, IPlayerStateHandler instance, Direction side, INBT base) {
        CompoundNBT tag = (CompoundNBT) base;
        if (tag.getBoolean("isDragon")) {
            System.out.println("stonks");
            instance.setData(
                    new PlayerStateHandler.DragonData(
                            DragonType.valueOf(tag.getString("type")),
                            tag.getInt("level"),
                            tag.getDouble("bodyYaw"),
                            tag.getDouble("headYaw"),
                            tag.getDouble("neckLength"),
                            tag.getDouble("headPitch"),
                            getVec3d(tag.get("headPos")),
                            getVec3d(tag.get("tailPos"))
                    ));
        } else
            System.out.println("not stonks");
    }
}
