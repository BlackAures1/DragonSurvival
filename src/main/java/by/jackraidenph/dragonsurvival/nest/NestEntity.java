package by.jackraidenph.dragonsurvival.nest;

import by.jackraidenph.dragonsurvival.registration.ItemsInit;
import by.jackraidenph.dragonsurvival.tiles.BaseBlockEntity;
import by.jackraidenph.dragonsurvival.util.DragonType;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.items.ItemStackHandler;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class NestEntity extends BaseBlockEntity implements ITickableTileEntity, INamedContainerProvider {
    public int energy = 0;
    public int damageCooldown;
    public boolean regenerationMode = true;
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
        ItemStack itemStack = regenItem.getStackInSlot(0);
        if (!itemStack.isEmpty()) {
            int value = regenValue.get(itemStack.getItem());
            if (energy < 64 - value) {
                energy = Math.min(64, energy + value);
                itemStack.shrink(1);
            }
        }
        Block block = getBlockState().getBlock();
        if (block instanceof BigNestBlock) {
            List<PlayerEntity> playerEntities = level.getEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB(worldPosition).inflate(1, 0, 1).expandTowards(0, 1, 0), playerEntity -> playerEntity.getUUID().equals(ownerUUID));
            if (playerEntities.size() == 1) {
                PlayerEntity owner = playerEntities.get(0);
                if (owner.getHealth() < owner.getMaxHealth() && regenerationMode && energy > 0) {
                    if (level.getGameTime() % 10 == 0) {
                        owner.heal(1);
                        energy--;
                    }
                }
            }
        }
        if (!level.isClientSide) {
        	level.blockEvent(worldPosition, getBlockState().getBlock(), 0, energy);
        }
    }

    @Override
    public boolean triggerEvent(int id, int type) {
        if (id == 0) {
            energy = type;
            return true;
        }
        return super.triggerEvent(id, type);
    }

    @Override
    public CompoundNBT save(CompoundNBT compound) {
        compound.putInt("Health", energy);
        compound.putInt("Damage cooldown", damageCooldown);
        compound.putString("Type", type.name());
        if (ownerUUID != null)
            compound.putUUID("Owner", ownerUUID);
        compound.putBoolean("Regenerating", regenerationMode);
        compound.put("Item", regenItem.serializeNBT());
        return super.save(compound);
    }

    @Override
    public void load(BlockState state, CompoundNBT compound) {
        super.load(state, compound);
        energy = compound.getInt("Health");
        damageCooldown = compound.getInt("Damage cooldown");
        type = DragonType.valueOf(compound.getString("Type"));
        regenerationMode = compound.getBoolean("Regenerating");
        regenItem.deserializeNBT(compound.getCompound("Item"));
        ownerUUID = compound.getUUID("Owner");
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
        buffer.writeBlockPos(worldPosition);
        return new NestContainer(p_createMenu_1_, p_createMenu_2_, buffer);
    }
}
