package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.containers.CraftingContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

public class OpenCrafting implements IMessage<OpenCrafting>{
    @Override
    public void encode(OpenCrafting message, PacketBuffer buffer) {

    }

    @Override
    public OpenCrafting decode(PacketBuffer buffer) {
        return new OpenCrafting();
    }

    @Override
    public void handle(OpenCrafting message, Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context=supplier.get();
        context.getSender().openMenu(new INamedContainerProvider() {
            @Override
            public ITextComponent getDisplayName() {
                return new StringTextComponent("Crafting");
            }

            @Override
            public Container createMenu(int p_createMenu_1_, PlayerInventory p_createMenu_2_, PlayerEntity p_createMenu_3_) {
                return new CraftingContainer(p_createMenu_1_,p_createMenu_2_);
            }
        });
    }
}
