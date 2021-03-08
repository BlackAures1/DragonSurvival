package by.jackraidenph.dragonsurvival.nest;

import by.jackraidenph.dragonsurvival.handlers.ItemsInit;
import by.jackraidenph.dragonsurvival.tiles.BaseBlockEntity;
import by.jackraidenph.dragonsurvival.util.DragonType;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashMap;
import java.util.UUID;

public class NestEntity extends BaseBlockEntity implements ITickableTileEntity, INamedContainerProvider {
    public int health = 64;
    final static int COOLDOWN_TIME = 10 * 20;
    public int damageCooldown;
    public boolean regenerationMode;
    public UUID ownerUUID;
    public DragonType type = DragonType.NONE;
    public ItemStackHandler regenItem = new ItemStackHandler(1);

    static HashMap<Item, Integer> regenValue;

    static {
        regenValue = new HashMap<>(6);
        regenValue.put(ItemsInit.elderDragonDust, 5);
        regenValue.put(Items.IRON_INGOT, 10);
        regenValue.put(Items.GOLD_INGOT, 20);
        regenValue.put(Items.DIAMOND, 30);
        regenValue.put(ItemsInit.elderDragonBone, 40);
        regenValue.put(ItemsInit.heartElement, 64);
    }

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
        compound.putString("Type", type.name());
        compound.putUniqueId("Owner", ownerUUID);
        compound.putBoolean("Regenerating", regenerationMode);
        compound.put("Item", regenItem.serializeNBT());
        return super.write(compound);
    }

    @Override
    public void read(CompoundNBT compound) {
        super.read(compound);
        health = compound.getInt("Health");
        damageCooldown = compound.getInt("Damage cooldown");
        type = DragonType.valueOf(compound.getString("Type"));
        regenerationMode = compound.getBoolean("Regenerating");
        regenItem.deserializeNBT(compound.getCompound("Item"));
        ownerUUID = compound.getUniqueId("Owner");
        if (ownerUUID.equals(new UUID(0, 0)))
            ownerUUID = null;
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
