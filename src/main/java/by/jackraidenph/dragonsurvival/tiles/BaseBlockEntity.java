package by.jackraidenph.dragonsurvival.tiles;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nullable;

public class BaseBlockEntity extends TileEntity {
    public BaseBlockEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        BlockPos blockPos = getPos();
        return new SUpdateTileEntityPacket(blockPos, 0, getUpdateTag());
    }


    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT supertag = super.getUpdateTag();
        write(supertag);
        return supertag;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbtTagCompound = pkt.getNbtCompound();
        read(nbtTagCompound);
    }

    public int getX() {
        return getPos().getX();
    }

    public int getY() {
        return getPos().getY();
    }

    public int getZ() {
        return getPos().getZ();
    }
}
