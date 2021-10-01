package by.jackraidenph.dragonsurvival.util;

import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MerchantOffer;

import javax.annotation.Nullable;
import java.util.Random;

public class ItemFor2ItemsTrade implements VillagerTrades.ITrade {
    Item itemIn1, itemIn2;
    int cost1, cost2;
    Item itemOut;
    int countOut;
    int maxUses;
    int xpReward;

    public ItemFor2ItemsTrade(Item itemIn1, int cost1, Item itemIn2, int cost2, Item itemOut, int countOut, int maxUses, int xpReward) {
        this.itemIn1 = itemIn1;
        this.itemIn2 = itemIn2;
        this.cost1 = cost1;
        this.cost2 = cost2;
        this.itemOut = itemOut;
        this.countOut = countOut;
        this.maxUses = maxUses;
        this.xpReward = xpReward;
    }

    @Nullable
    @Override
    public MerchantOffer getOffer(Entity entity, Random random) {
        return new MerchantOffer(new ItemStack(itemIn1, cost1), new ItemStack(itemIn2, cost2), new ItemStack(itemOut, countOut), maxUses, xpReward, 0);
    }
}
