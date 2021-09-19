package by.jackraidenph.dragonsurvival.containers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.WorkbenchContainer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.network.play.server.SSetSlotPacket;
import net.minecraft.world.World;

import java.util.Optional;

public class CraftingContainer extends WorkbenchContainer {
    World world;
    PlayerEntity playerEntity;
    public CraftingContainer(int p_i50089_1_, PlayerInventory p_i50089_2_) {
        super(p_i50089_1_, p_i50089_2_);
        world=p_i50089_2_.player.level;
        playerEntity= p_i50089_2_.player;
    }

    @Override
    public boolean stillValid(PlayerEntity p_75145_1_) {
        return true;
    }

    @Override
    public void slotsChanged(IInventory p_75130_1_) {
        if (!world.isClientSide) {
            ServerPlayerEntity serverplayerentity = (ServerPlayerEntity) playerEntity;
            ItemStack itemstack = ItemStack.EMPTY;
            Optional<ICraftingRecipe> optional = world.getServer().getRecipeManager().getRecipeFor(IRecipeType.CRAFTING, craftSlots, world);
            if (optional.isPresent()) {
                ICraftingRecipe icraftingrecipe = optional.get();
                if (resultSlots.setRecipeUsed(world, serverplayerentity, icraftingrecipe)) {
                    itemstack = icraftingrecipe.assemble(craftSlots);
                }
            }

            resultSlots.setItem(0, itemstack);
            serverplayerentity.connection.send(new SSetSlotPacket(containerId, 0, itemstack));

        }
    }

    @Override
    public void removed(PlayerEntity playerEntity) {
        super.removed(playerEntity);
        clearContainer(playerEntity, world, craftSlots);

    }
}
