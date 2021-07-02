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
        BlockPos blockPos = getBlockPos();
        return new SUpdateTileEntityPacket(blockPos, 0, getUpdateTag());
    }


    @Override
    public CompoundNBT getUpdateTag() {
        CompoundNBT supertag = super.getUpdateTag();
        save(supertag);
        return supertag;
    }

    @Override
    public void onDataPacket(NetworkManager net, SUpdateTileEntityPacket pkt) {
        CompoundNBT nbtTagCompound = pkt.getTag();
        load(getBlockState(), nbtTagCompound);
    }

    public int getX() {
        return getBlockPos().getX();
    }

    public int getY() {
        return getBlockPos().getY();
    }

    public int getZ() {
        return getBlockPos().getZ();
    }
}
