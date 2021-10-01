package by.jackraidenph.dragonsurvival.util;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;

import javax.annotation.Nullable;
import java.util.Random;

public class ItemForItemTrade implements VillagerTrades.ITrade {
    private final Item item;
    private final int cost;
    private final int maxUses;

    public ItemForItemTrade(Item itemIn, int cost, Item itemOut, int count, int maxUses, int xpReward) {
        this.item = itemIn;
        this.cost = cost;
        this.maxUses = maxUses;
        this.xpReward = xpReward;
        this.itemOut = itemOut;
        this.count = count;
    }

    private final int xpReward;
    private final Item itemOut;
    private final int count;

    @Nullable
    public MerchantOffer getOffer(Entity entity, Random random) {
        ItemStack buyIng = new ItemStack(this.item, this.cost);
        return new MerchantOffer(buyIng, new ItemStack(this.itemOut, this.count), this.maxUses, this.xpReward, 0.0F);
    }

    public static Int2ObjectMap<VillagerTrades.ITrade[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ITrade[]> p_221238_0_) {
        return new Int2ObjectOpenHashMap<>(p_221238_0_);
    }
}