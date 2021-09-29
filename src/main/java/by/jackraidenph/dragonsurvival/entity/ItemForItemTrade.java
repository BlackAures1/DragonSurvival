package by.jackraidenph.dragonsurvival.entity;

import by.jackraidenph.dragonsurvival.handlers.ItemsInit;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.*;
import net.minecraft.util.Util;

import javax.annotation.Nullable;
import java.util.Map;
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

    public static ItemForItemTrade RED1 = new ItemForItemTrade(Items.POPPY, 5, Items.GOLD_INGOT, 1, 16, 2);
    public static ItemForItemTrade RED2 = new ItemForItemTrade(Items.RED_TULIP, 5, Items.GOLD_INGOT, 1, 16, 2);
    public static ItemForItemTrade RED3 = new ItemForItemTrade(Items.ROSE_BUSH, 5, Items.GOLD_INGOT, 1, 16, 2);

    public static ItemForItemTrade YELLOW1 = new ItemForItemTrade(Items.DANDELION, 5, Items.GOLD_INGOT, 1, 16, 2);
    public static ItemForItemTrade YELLOW2 = new ItemForItemTrade(Items.SUNFLOWER, 5, Items.GOLD_INGOT, 1, 16, 2);
    public static ItemForItemTrade YELLOW3 = new ItemForItemTrade(Items.ORANGE_TULIP, 5, Items.GOLD_INGOT, 1, 16, 2);

    public static ItemForItemTrade PURPLE1 = new ItemForItemTrade(Items.LILAC, 5, Items.GOLD_INGOT, 1, 16, 2);
    public static ItemForItemTrade PURPLE2 = new ItemForItemTrade(Items.ALLIUM, 5, Items.GOLD_INGOT, 1, 16, 2);
    public static ItemForItemTrade PURPLE3 = new ItemForItemTrade(Items.PEONY, 5, Items.GOLD_INGOT, 1, 16, 2);

    public static ItemForItemTrade BLUE1 = new ItemForItemTrade(Items.BLUE_ORCHID, 5, Items.GOLD_INGOT, 1, 16, 2);
    public static ItemForItemTrade BLUE2 = new ItemForItemTrade(Items.CORNFLOWER, 5, Items.GOLD_INGOT, 1, 16, 2);

    public static ItemForItemTrade WHITE1 = new ItemForItemTrade(Items.OXEYE_DAISY, 5, Items.GOLD_INGOT, 1, 16, 2);
    public static ItemForItemTrade WHITE2 = new ItemForItemTrade(Items.AZURE_BLUET, 5, Items.GOLD_INGOT, 1, 16, 2);
    public static ItemForItemTrade WHITE3 = new ItemForItemTrade(Items.LILY_OF_THE_VALLEY, 5, Items.GOLD_INGOT, 1, 16, 2);
    public static ItemForItemTrade WHITE4 = new ItemForItemTrade(Items.WHITE_TULIP, 5, Items.GOLD_INGOT, 1, 16, 2);

    public static ItemForItemTrade BLACK = new ItemForItemTrade(Items.WITHER_ROSE, 5, Items.GOLD_INGOT, 1, 16, 2);

    public static ItemForItemTrade XP_BOTTLE1 = new ItemForItemTrade(Items.GOLD_INGOT, 2, Items.EXPERIENCE_BOTTLE, 1, 12, 4);
    public static ItemForItemTrade XP_BOTTLE2 = new ItemForItemTrade(ItemsInit.elderDragonDust, 3, Items.EXPERIENCE_BOTTLE, 1, 12, 4);

    public static ItemForItemTrade ELDER_DRAGON_DUST = new ItemForItemTrade(Items.GOLD_INGOT, 5, ItemsInit.elderDragonDust, 1, 12, 8);


    public static Map<Integer, Int2ObjectMap<VillagerTrades.ITrade[]>> colorToTrades;


    static {
        colorToTrades = Util.make(Maps.newHashMap(), objectObjectHashMap -> {
            objectObjectHashMap.put(DyeColor.RED.getId(), toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{RED1, RED2, RED3}, 2, new VillagerTrades.ITrade[]{XP_BOTTLE1, XP_BOTTLE2}, 3, new VillagerTrades.ITrade[]{ELDER_DRAGON_DUST})));
            objectObjectHashMap.put(DyeColor.YELLOW.getId(), toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{YELLOW1, YELLOW2, YELLOW3}, 2, new VillagerTrades.ITrade[]{XP_BOTTLE1, XP_BOTTLE2}, 3, new VillagerTrades.ITrade[]{ELDER_DRAGON_DUST})));
            objectObjectHashMap.put(DyeColor.PURPLE.getId(), toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{PURPLE1, PURPLE2, PURPLE3}, 2, new VillagerTrades.ITrade[]{XP_BOTTLE1, XP_BOTTLE2}, 3, new VillagerTrades.ITrade[]{ELDER_DRAGON_DUST})));
            objectObjectHashMap.put(DyeColor.BLUE.getId(), toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{BLUE1, BLUE2}, 2, new VillagerTrades.ITrade[]{XP_BOTTLE1, XP_BOTTLE2}, 3, new VillagerTrades.ITrade[]{ELDER_DRAGON_DUST})));
            objectObjectHashMap.put(DyeColor.WHITE.getId(), toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{WHITE1, WHITE2, WHITE3, WHITE4}, 2, new VillagerTrades.ITrade[]{XP_BOTTLE1, XP_BOTTLE2}, 3, new VillagerTrades.ITrade[]{ELDER_DRAGON_DUST})));
            objectObjectHashMap.put(DyeColor.BLACK.getId(), toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{BLACK}, 2, new VillagerTrades.ITrade[]{XP_BOTTLE1, XP_BOTTLE2}, 3, new VillagerTrades.ITrade[]{ELDER_DRAGON_DUST})));
        });
    }

    public static Int2ObjectMap<VillagerTrades.ITrade[]> toIntMap(ImmutableMap<Integer, VillagerTrades.ITrade[]> p_221238_0_) {
        return new Int2ObjectOpenHashMap<>(p_221238_0_);
    }
}