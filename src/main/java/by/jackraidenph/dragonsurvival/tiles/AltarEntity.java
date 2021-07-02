package by.jackraidenph.dragonsurvival.tiles;

import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AltarEntity extends BaseBlockEntity implements ITickableTileEntity {
    public HashMap<UUID,Integer> usageCooldowns=new HashMap<>(20);

    public AltarEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }


    @Override
    public void tick() {
        usageCooldowns.forEach((uuid, integer) -> {
            if(integer>0)
                usageCooldowns.put(uuid,--integer);
        });
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putInt("Player count",usageCooldowns.size());
        int next=0;
        for (Map.Entry<UUID, Integer> entry : usageCooldowns.entrySet()) {
            compound.putUUID("Player#"+next,entry.getKey());
            compound.putInt("Time#"+next,entry.getValue());
            next++;
        }
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        int count=compound.getInt("Player count");
        for (int i = 0; i < count; i++) {
            UUID uuid=compound.getUUID("Player#"+i);
            int remaining=compound.getInt("Time#"+i);
            usageCooldowns.put(uuid,remaining);
        }
    }
}