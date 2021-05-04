package by.jackraidenph.dragonsurvival.nest;

import by.jackraidenph.dragonsurvival.handlers.Containers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nonnull;

public class NestContainer extends Container {
    NestEntity nestEntity;

    public NestContainer(int windowId, PlayerInventory inv, PacketBuffer data) {
        super(Containers.nestContainer, windowId);
        nestEntity = (NestEntity) inv.player.level.getBlockEntity(data.readBlockPos());
        int index = 0;
        for (int i = 0; i < 9; i++) {
            addSlot(new Slot(inv, index++, 8 + 18 * i, 141));
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                addSlot(new Slot(inv, index++, 8 + 18 * j, 84 + 18 * i));
            }
        }

        addSlot(new SlotItemHandler(nestEntity.regenItem, 0, 8 + 18 * 7, 85 - 24) {

            @Override
            public boolean mayPlace(@Nonnull ItemStack stack) {
                Item item = stack.getItem();
                return NestEntity.regenValue.containsKey(item);
            }
        });
    }
    
    @Override
    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        Slot clickedSlot = this.slots.get(index);
        ItemStack stack;
        if (clickedSlot.hasItem()) {
            ItemStack clickedStack = clickedSlot.getItem();
            stack = clickedStack.copy();
            if (clickedStack.getCount() == 0) {
                clickedSlot.set(ItemStack.EMPTY);
            } else {
                clickedSlot.setChanged();
            }

            if (clickedStack.getCount() == stack.getCount()) {
                return ItemStack.EMPTY;
            }

            clickedSlot.onTake(playerIn, clickedStack);
        }
        return super.quickMoveStack(playerIn, index);
    }

    @Override
    public boolean stillValid(PlayerEntity playerIn) {
        return nestEntity.ownerUUID.equals(playerIn.getUUID());
    }
}
