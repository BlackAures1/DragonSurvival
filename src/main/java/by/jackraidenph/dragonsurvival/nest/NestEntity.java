package by.jackraidenph.dragonsurvival.nest;

import by.jackraidenph.dragonsurvival.tiles.BaseBlockEntity;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;

public class NestEntity extends BaseBlockEntity implements ITickableTileEntity, INamedContainerProvider {
    public int health = 100;
    final static int cooldownTime = 10 * 20;
    public int damageCooldown;

    public NestEntity(TileEntityType<?> tileEntityTypeIn) {
        super(tileEntityTypeIn);
    }

    @Override
    public void tick() {
        if (damageCooldown > 0) {
            damageCooldown--;
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT compound) {
        compound.putInt("Health", health);
        compound.putInt("Damage cooldown", damageCooldown);
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        health = compound.getInt("Health");
        damageCooldown = compound.getInt("Damage cooldown");
    }

    @Override
    public ITextComponent getDisplayName() {
        return new StringTextComponent("Nest");
    }


    @Override
    public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
        PacketBuffer buffer = new PacketBuffer(Unpooled.buffer());
        buffer.writeBlockPos(pos);
        return new NestContainer(p_createMenu_1_, p_createMenu_2_, buffer);
    }
}
