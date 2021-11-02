package by.jackraidenph.dragonsurvival.util;

import by.jackraidenph.dragonsurvival.registration.ItemsInit;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Items;
import net.minecraft.util.Util;

import java.util.Map;

public class PrincessTrades {
    public static ItemForItemTrade RED1 = new ItemForItemTrade(Items.POPPY, 5, Items.GOLD_NUGGET, 1, 16, 6);
    public static ItemForItemTrade RED2 = new ItemForItemTrade(Items.RED_TULIP, 5, Items.GOLD_NUGGET, 1, 16, 6);
    public static ItemForItemTrade RED3 = new ItemForItemTrade(Items.ROSE_BUSH, 5, Items.GOLD_NUGGET, 1, 16, 6);

    public static ItemForItemTrade YELLOW1 = new ItemForItemTrade(Items.DANDELION, 5, Items.GOLD_NUGGET, 1, 16, 6);
    public static ItemForItemTrade YELLOW2 = new ItemForItemTrade(Items.SUNFLOWER, 5, Items.GOLD_NUGGET, 1, 16, 6);
    public static ItemForItemTrade YELLOW3 = new ItemForItemTrade(Items.ORANGE_TULIP, 5, Items.GOLD_NUGGET, 1, 16, 6);

    public static ItemForItemTrade PURPLE1 = new ItemForItemTrade(Items.LILAC, 5, Items.GOLD_NUGGET, 1, 16, 6);
    public static ItemForItemTrade PURPLE2 = new ItemForItemTrade(Items.ALLIUM, 5, Items.GOLD_NUGGET, 1, 16, 6);
    public static ItemForItemTrade PURPLE3 = new ItemForItemTrade(Items.PEONY, 5, Items.GOLD_NUGGET, 1, 16, 6);

    public static ItemForItemTrade BLUE1 = new ItemForItemTrade(Items.BLUE_ORCHID, 5, Items.GOLD_NUGGET, 1, 16, 6);
    public static ItemForItemTrade BLUE2 = new ItemForItemTrade(Items.CORNFLOWER, 5, Items.GOLD_NUGGET, 1, 16, 6);

    public static ItemForItemTrade WHITE1 = new ItemForItemTrade(Items.OXEYE_DAISY, 5, Items.GOLD_NUGGET, 1, 16, 6);
    public static ItemForItemTrade WHITE2 = new ItemForItemTrade(Items.AZURE_BLUET, 5, Items.GOLD_NUGGET, 1, 16, 6);
    public static ItemForItemTrade WHITE3 = new ItemForItemTrade(Items.LILY_OF_THE_VALLEY, 5, Items.GOLD_NUGGET, 1, 16, 6);
    public static ItemForItemTrade WHITE4 = new ItemForItemTrade(Items.WHITE_TULIP, 5, Items.GOLD_NUGGET, 1, 16, 6);

    public static ItemForItemTrade BLACK = new ItemForItemTrade(Items.WITHER_ROSE, 1, Items.GOLD_NUGGET, 5, 10, 15);

    public static ItemFor2ItemsTrade XP_BOTTLE1 = new ItemFor2ItemsTrade(Items.GOLD_INGOT, 2, ItemsInit.elderDragonDust, 3, Items.EXPERIENCE_BOTTLE, 1, 12, 10);

    public static ItemFor2ItemsTrade ELDER_DRAGON_DUST = new ItemFor2ItemsTrade(Items.GOLD_INGOT, 5, Items.DIAMOND, 1, ItemsInit.elderDragonDust, 1, 12, 15);

    public static Map<Integer, Int2ObjectMap<VillagerTrades.ITrade[]>> colorToTrades;

    static {
        PrincessTrades.colorToTrades = Util.make(Maps.newHashMap(), objectObjectHashMap -> {
            objectObjectHashMap.put(DyeColor.RED.getId(), ItemForItemTrade.toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{PrincessTrades.RED1, PrincessTrades.RED2, PrincessTrades.RED3}, 2, new VillagerTrades.ITrade[]{PrincessTrades.XP_BOTTLE1}, 3, new VillagerTrades.ITrade[]{PrincessTrades.ELDER_DRAGON_DUST})));
            objectObjectHashMap.put(DyeColor.YELLOW.getId(), ItemForItemTrade.toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{PrincessTrades.YELLOW1, PrincessTrades.YELLOW2, PrincessTrades.YELLOW3}, 2, new VillagerTrades.ITrade[]{PrincessTrades.XP_BOTTLE1}, 3, new VillagerTrades.ITrade[]{PrincessTrades.ELDER_DRAGON_DUST})));
            objectObjectHashMap.put(DyeColor.PURPLE.getId(), ItemForItemTrade.toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{PrincessTrades.PURPLE1, PrincessTrades.PURPLE2, PrincessTrades.PURPLE3}, 2, new VillagerTrades.ITrade[]{PrincessTrades.XP_BOTTLE1}, 3, new VillagerTrades.ITrade[]{PrincessTrades.ELDER_DRAGON_DUST})));
            objectObjectHashMap.put(DyeColor.BLUE.getId(), ItemForItemTrade.toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{PrincessTrades.BLUE1, PrincessTrades.BLUE2}, 2, new VillagerTrades.ITrade[]{PrincessTrades.XP_BOTTLE1}, 3, new VillagerTrades.ITrade[]{PrincessTrades.ELDER_DRAGON_DUST})));
            objectObjectHashMap.put(DyeColor.WHITE.getId(), ItemForItemTrade.toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{PrincessTrades.WHITE1, PrincessTrades.WHITE2, PrincessTrades.WHITE3, PrincessTrades.WHITE4}, 2, new VillagerTrades.ITrade[]{PrincessTrades.XP_BOTTLE1}, 3, new VillagerTrades.ITrade[]{PrincessTrades.ELDER_DRAGON_DUST})));
            objectObjectHashMap.put(DyeColor.BLACK.getId(), ItemForItemTrade.toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{PrincessTrades.BLACK}, 2, new VillagerTrades.ITrade[]{PrincessTrades.XP_BOTTLE1}, 3, new VillagerTrades.ITrade[]{PrincessTrades.ELDER_DRAGON_DUST})));
        });
    }
}
