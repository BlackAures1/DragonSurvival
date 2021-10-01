package by.jackraidenph.dragonsurvival.util;

import by.jackraidenph.dragonsurvival.handlers.ItemsInit;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.entity.merchant.villager.VillagerTrades;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Items;
import net.minecraft.util.Util;

import java.util.Map;

public class PrinceTrades {
    public static Map<Integer, Int2ObjectMap<VillagerTrades.ITrade[]>> colorToTrades;
    private static final ItemForItemTrade GOLD_NUGGETS = new ItemForItemTrade(Items.IRON_SWORD, 1, Items.GOLD_NUGGET, 3, 5, 2);
    private static final ItemForItemTrade RED_FLOWER = new ItemForItemTrade(Items.RED_TULIP, 10, Items.EXPERIENCE_BOTTLE, 1, 10, 4);
    private static final ItemForItemTrade YELLOW_FLOWER = new ItemForItemTrade(Items.DANDELION, 10, Items.EXPERIENCE_BOTTLE, 1, 10, 4);
    private static final ItemForItemTrade BLACK_FLOWER = new ItemForItemTrade(Items.WITHER_ROSE, 10, Items.EXPERIENCE_BOTTLE, 1, 10, 4);
    private static final ItemForItemTrade BLUE_FLOWER = new ItemForItemTrade(Items.BLUE_ORCHID, 10, Items.EXPERIENCE_BOTTLE, 1, 10, 4);
    private static final ItemForItemTrade PURPLE_FLOWER = new ItemForItemTrade(Items.LILAC, 10, Items.EXPERIENCE_BOTTLE, 1, 10, 4);
    private static final ItemForItemTrade WHITE_FLOWER = new ItemForItemTrade(Items.OXEYE_DAISY, 10, Items.EXPERIENCE_BOTTLE, 1, 10, 4);

    private static final ItemFor2ItemsTrade ELDER_DRAGON_DUST = new ItemFor2ItemsTrade(Items.GOLD_INGOT, 5, Items.EMERALD, 5, ItemsInit.elderDragonDust, 1, 12, 8);

    static {
        colorToTrades = Util.make(Maps.newHashMap(), integerInt2ObjectMapMap -> {
            integerInt2ObjectMapMap.put(DyeColor.RED.getId(), ItemForItemTrade.toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{GOLD_NUGGETS}, 2, new VillagerTrades.ITrade[]{RED_FLOWER}, 3, new VillagerTrades.ITrade[]{ELDER_DRAGON_DUST})));
            integerInt2ObjectMapMap.put(DyeColor.YELLOW.getId(), ItemForItemTrade.toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{GOLD_NUGGETS}, 2, new VillagerTrades.ITrade[]{YELLOW_FLOWER}, 3, new VillagerTrades.ITrade[]{ELDER_DRAGON_DUST})));
            integerInt2ObjectMapMap.put(DyeColor.BLACK.getId(), ItemForItemTrade.toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{GOLD_NUGGETS}, 2, new VillagerTrades.ITrade[]{BLACK_FLOWER}, 3, new VillagerTrades.ITrade[]{ELDER_DRAGON_DUST})));
            integerInt2ObjectMapMap.put(DyeColor.BLUE.getId(), ItemForItemTrade.toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{GOLD_NUGGETS}, 2, new VillagerTrades.ITrade[]{BLUE_FLOWER}, 3, new VillagerTrades.ITrade[]{ELDER_DRAGON_DUST})));
            integerInt2ObjectMapMap.put(DyeColor.PURPLE.getId(), ItemForItemTrade.toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{GOLD_NUGGETS}, 2, new VillagerTrades.ITrade[]{PURPLE_FLOWER}, 3, new VillagerTrades.ITrade[]{ELDER_DRAGON_DUST})));
            integerInt2ObjectMapMap.put(DyeColor.WHITE.getId(), ItemForItemTrade.toIntMap(ImmutableMap.of(1, new VillagerTrades.ITrade[]{GOLD_NUGGETS}, 2, new VillagerTrades.ITrade[]{WHITE_FLOWER}, 3, new VillagerTrades.ITrade[]{ELDER_DRAGON_DUST})));
        });
    }
}
