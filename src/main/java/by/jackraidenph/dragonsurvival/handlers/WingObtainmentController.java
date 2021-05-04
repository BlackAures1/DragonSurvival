package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.network.RefreshDragons;
import by.jackraidenph.dragonsurvival.network.SynchronizeDragonCap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.Language;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class WingObtainmentController {

    static String m1 = "Ð�Ðµ Ñ�Ð¼ÐµÐ¹ Ð¿Ñ€Ð¸Ð±Ð»Ð¸Ð¶Ð°Ñ‚ÑŒÑ�Ñ� Ðº Ð¼Ð¾ÐµÐ¼Ñƒ Ð³Ð½ÐµÐ·Ð´Ñƒ, (). Ð”Ð»Ñ� Ñ‡ÐµÐ³Ð¾ Ñ‚Ñ‹ Ð¿Ñ€Ð¸ÑˆÑ‘Ð» Ñ�ÑŽÐ´Ð°? Ð¯ Ð±ÑƒÐ´Ñƒ Ð·Ð°Ñ‰Ð¸Ñ‰Ð°Ñ‚ÑŒÑ�Ñ�.";
    static String m2 = "Ð¯ Ð½Ðµ Ñ‚ÐµÑ€Ð¿Ð»ÑŽ Ñ‡ÑƒÐ¶Ð°ÐºÐ¾Ð² Ð½Ð° Ñ�Ð²Ð¾ÐµÐ¹ Ñ‚ÐµÑ€Ñ€Ð¸Ñ‚Ð¾Ñ€Ð¸Ð¸, (). Ð”ÐµÑ€Ð¶Ð¸Ñ�ÑŒ Ð¾Ñ‚ Ð¼ÐµÐ½Ñ� Ð¿Ð¾Ð´Ð°Ð»ÑŒÑˆÐµ. ÐšÐ°ÐºÐ¾Ð²Ñ‹ Ñ‚Ð²Ð¾Ð¸ Ð½Ð°Ð¼ÐµÑ€ÐµÐ½Ð¸Ñ�?";
    static String m3 = "Ð¢Ñ‹ Ð¿Ñ€Ð¸ÑˆÑ‘Ð» Ð·Ð° ÐºÑ€Ñ‹Ð»ÑŒÑ�Ð¼Ð¸? Ð£Ð±Ð¸Ñ€Ð°Ð¹Ñ�Ñ�, Ð¿Ð¾ÐºÐ° Ð½Ðµ Ñ�Ñ‚Ð°Ð»Ð¾ Ð¿Ð¾Ð·Ð´Ð½Ð¾, ().";
    static String m4 = "Ð­Ñ‚Ð¾ Ð¼Ð¾Ñ� Ð·ÐµÐ¼Ð»Ñ�, (), Ð¿Ñ€Ð¾Ñ�Ð²Ð¸ ÑƒÐ²Ð°Ð¶ÐµÐ½Ð¸Ðµ. Ð£Ñ…Ð¾Ð´Ð¸, Ð½Ð¾ Ñ�Ð½Ð°Ñ‡Ð°Ð»Ð° Ð¾Ñ‚Ð²ÐµÑ‚ÑŒ Ð·Ð°Ñ‡ÐµÐ¼ Ð¿Ñ€Ð¸ÑˆÑ‘Ð». Ð¢ÐµÐ±Ðµ Ð½ÑƒÐ¶Ð½Ñ‹ ÐºÑ€Ñ‹Ð»ÑŒÑ�?";
    static String m5 = "Ð¢Ñ‹ Ð¿Ñ€Ð¾ÑˆÐµÐ» Ñ‡ÐµÑ€ÐµÐ· Ð¼Ð½Ð¾Ð³Ð¾Ðµ Ñ‡Ñ‚Ð¾Ð±Ñ‹ Ð¿Ð¾Ð¿Ð°Ñ�Ñ‚ÑŒ Ñ�ÑŽÐ´Ð°, (), Ñ‡ÐµÐ³Ð¾ Ñ‚Ñ‹ Ð¶ÐµÐ»Ð°ÐµÑˆÑŒ?";
    static String m6 = "Ð¢Ñ‹ Ð¿Ñ€Ð¸ÑˆÑ‘Ð» Ð´Ð¾Ð³Ð¾Ð²Ð¾Ñ€Ð¸Ñ‚ÑŒÑ�Ñ� Ð¸Ð»Ð¸ Ð²Ð¾ÐµÐ²Ð°Ñ‚ÑŒ? Ð¯ ÑƒÐ±ÑŒÑŽ Ñ‚ÐµÐ±Ñ�, ÐµÑ�Ð»Ð¸ Ñ‚Ñ‹ Ð¿Ð¾Ñ�Ð¼ÐµÐµÑˆÑŒ Ð¿Ð¾Ð´Ð¾Ð¹Ñ‚Ð¸ Ðº Ð¼Ð¾ÐµÐ¼Ñƒ Ð³Ð½ÐµÐ·Ð´Ñƒ.";
    static String m7 = "Ð£ Ð¼ÐµÐ½Ñ� ÐµÑ�Ñ‚ÑŒ Ñ‚Ð¾, Ñ‡ÐµÐ³Ð¾ Ñ‚Ñ‹ Ð¶ÐµÐ»Ð°ÐµÑˆÑŒ, (). Ð’Ð¾Ð¿Ñ€Ð¾Ñ� Ð² Ñ‚Ð¾Ð¼, Ð´Ð¾Ñ�Ñ‚Ð¾Ð¸Ð½ Ð»Ð¸ Ñ‚Ñ‹ Ñ�Ñ‚Ð¾Ð³Ð¾?";
    static String m8 = "ÐœÑ‹ Ñ� Ñ‚Ð¾Ð±Ð¾Ð¹ Ð¾Ð´Ð½Ð¾Ð¹ ÐºÑ€Ð¾Ð²Ð¸, (), Ñ� Ð½Ðµ Ñ…Ð¾Ñ‡Ñƒ Ñ�Ñ€Ð°Ð¶Ð°Ñ‚ÑŒÑ�Ñ� Ñ� Ñ‚Ð¾Ð±Ð¾Ð¹. Ð�Ð¾ ÐµÑ‰Ðµ Ð±Ð¾Ð»ÑŒÑˆÐµ Ñ� Ð½Ðµ Ñ…Ð¾Ñ‡Ñƒ Ñ‡Ñ‚Ð¾Ð±Ñ‹ Ð¼Ð¾Ðµ Ð³Ð½ÐµÐ·Ð´Ð¾ Ð±Ñ‹Ð»Ð¾ ÑƒÐ½Ð¸Ñ‡Ñ‚Ð¾Ð¶ÐµÐ½Ð¾. Ð¡ÐºÐ°Ð¶Ð¸ Ñ‡Ñ‚Ð¾ Ñ‚Ñ‹ Ñ…Ð¾Ñ‡ÐµÑˆÑŒ Ð¸ ÑƒÑ…Ð¾Ð´Ð¸.";
    static String m9 = "Ð¢Ñ‹ Ð¿Ñ€Ð¾ÑˆÐµÐ» Ð³Ñ€Ð°Ð½ÑŒ Ð¼Ð¸Ñ€Ð¾Ð² Ð¸ Ð¾ÐºÐ°Ð·Ð°Ð»Ñ�Ñ� Ð·Ð´ÐµÑ�ÑŒ, (), Ð½Ð¾ Ñ€Ð°Ð´Ð¸ Ñ‡ÐµÐ³Ð¾? Ð¢ÐµÐ±Ñ� Ð¼Ð°Ð½Ð¸Ñ‚ Ð½ÐµÐ±Ð¾ ÐºÐ°Ðº Ð¸ Ð¼ÐµÐ½Ñ�?";
    static String m10 = "Ð¢Ñ‹ Ð½Ð°Ð³Ð»ÐµÑ†, (), Ñ€Ð°Ð· Ñ€ÐµÑˆÐ¸Ð» Ð¿Ð¾Ñ�Ð²Ð¸Ñ‚ÑŒÑ�Ñ� Ð·Ð´ÐµÑ�ÑŒ. Ð Ð°Ð´Ð¸ ÐºÐ°ÐºÐ¸Ñ… Ñ†ÐµÐ»ÐµÐ¹ Ñ‚Ñ‹ Ð¿Ñ€Ð¸ÑˆÐµÐ» Ñ�ÑŽÐ´Ð°?";
    static String m11 = "Ð¢Ñ‹ Ð¿Ñ€Ð¸ÑˆÑ‘Ð» ÑƒÐ±Ð¸Ñ‚ÑŒ Ð¼ÐµÐ½Ñ�? ÐŸÑ€Ð¾ÑˆÑƒ, Ñ�Ð¾Ñ…Ñ€Ð°Ð½Ð¸ Ð¼Ð¾Ñ‘ Ð´Ð¸Ñ‚Ñ�... Ð¯ Ñ€Ð°Ñ�Ñ�ÐºÐ°Ð¶Ñƒ Ñ‚ÐµÐ±Ðµ Ð¾ ÐºÑ€Ñ‹Ð»ÑŒÑ�Ñ…, ÐµÑ�Ð»Ð¸ Ñ‚Ñ‹ Ñ…Ð¾Ñ‡ÐµÑˆÑŒ Ñ�Ñ‚Ð¾Ð³Ð¾, Ð½Ð¾ Ð¾Ñ�Ñ‚Ð°Ð²ÑŒ Ð½Ð°Ñ� Ð² Ð¿Ð¾ÐºÐ¾Ðµ.";
    static String m12 = "Ð¡ÐºÐ¾Ð»ÑŒÐºÐ¾ Ð»ÐµÑ‚ Ñ� Ð½Ðµ Ð²Ð¸Ð´ÐµÐ»Ð° Ð¿Ð¾Ð´Ð¾Ð±Ð½Ñ‹Ñ… Ñ�ÐµÐ±Ðµ Ð¸ Ñ�Ñ‚Ð¾Ð»ÑŒÐºÐ¾ Ð¶Ðµ Ð½Ðµ Ñ…Ð¾Ñ‡Ñƒ Ð¸Ñ… Ð²Ð¸Ð´ÐµÑ‚ÑŒ. Ð�Ð¾ Ñ‚Ñ‹ Ð·Ð´ÐµÑ�ÑŒ, (), Ð¾Ñ‚Ð²ÐµÑ‡Ð°Ð¹ Ð±Ñ‹Ñ�Ñ‚Ñ€Ð¾ Ð¸ ÑƒÐ±Ð¸Ñ€Ð°Ð¹Ñ�Ñ� Ð¾Ñ‚Ñ�ÑŽÐ´Ð°. Ð—Ð°Ñ‡ÐµÐ¼ Ð¿Ñ€Ð¸ÑˆÑ‘Ð»?";
    static String m13 = "Ð‘ÑƒÐ´ÑŒ Ð¿Ñ€Ð¾ÐºÐ»Ñ�Ñ‚ Ñ‚Ð¾Ñ‚, ÐºÑ‚Ð¾ Ð½Ð°Ñ€ÑƒÑˆÐ¸Ð» Ð¼Ð¾Ð¹ Ð¿Ð¾ÐºÐ¾Ð¹. Ð£Ñ…Ð¾Ð´Ð¸ Ñ� Ð¼Ð¾ÐµÐ¹ Ñ‚ÐµÑ€Ñ€Ð¸Ñ‚Ð¾Ñ€Ð¸Ð¸, (), Ð¸Ð½Ð°Ñ‡Ðµ Ð¾Ñ‚ Ñ‚ÐµÐ±Ñ� Ð¾Ñ�Ñ‚Ð°Ð½ÑƒÑ‚Ñ�Ñ� Ð»Ð¸ÑˆÑŒ Ð²Ð¾Ñ�Ð¿Ð¾Ð¼Ð¸Ð½Ð°Ð½Ð¸Ñ�. ÐšÑ‚Ð¾ Ñ‚Ñ‹?";
    static String m14 = "Ð¡Ñ‚Ð¾Ð»ÑŒÐºÐ¾ Ð»ÐµÑ‚ Ñ� Ð±Ñ‹Ð»Ð° Ð·Ð´ÐµÑ�ÑŒ Ð¾Ð´Ð½Ð°, Ð½Ð¾ Ñ‚ÑƒÑ‚ Ð¿Ð¾Ñ�Ð²Ð»Ñ�ÐµÑˆÑŒÑ�Ñ� Ñ‚Ñ‹. ÐšÐ°ÐºÐ¸Ðµ Ñ†ÐµÐ»Ð¸ Ð¿Ñ€ÐµÑ�Ð»ÐµÐ´ÑƒÑŽÑ‚ Ñ‚ÐµÐ±Ñ�, ()?";
    static String m15 = "Ð¢Ñ‹ Ñ‡ÐµÐ³Ð¾-Ñ‚Ð¾ Ñ…Ð¾Ñ‡ÐµÑˆÑŒ Ð¾Ñ‚ Ð¼ÐµÐ½Ñ�, Ð²ÐµÑ€Ð½Ð¾? Ð�Ðµ Ð·Ñ€Ñ� Ð¶Ðµ Ñ‚Ñ‹ Ð¾ÐºÐ°Ð·Ð°Ð»Ñ�Ñ� Ð½Ð° Ð¼Ð¾ÐµÐ¹ Ñ‚ÐµÑ€Ñ€Ð¸Ñ‚Ð¾Ñ€Ð¸Ð¸, ().";
    static String m16 = "Ð¢Ð°Ðº Ð»ÐµÐ³ÐºÐ¾ Ð¿ÐµÑ€ÐµÑ�Ñ‚ÑƒÐ¿Ð¸Ñ‚ÑŒ Ñ‡ÐµÑ€ÐµÐ· Ð³Ñ€Ð°Ð½Ð¸Ñ†Ñ‹ Ð¼Ð¸Ñ€Ð¾Ð².... Ð¯ Ð½Ð°Ð´ÐµÑ�Ð»Ð°Ñ�ÑŒ Ñ�ÐºÑ€Ñ‹Ñ‚ÑŒÑ�Ñ� Ð·Ð´ÐµÑ�ÑŒ Ñ�Ð¾ Ñ�Ð²Ð¾ÐµÐ¹ Ñ�ÐµÐ¼ÑŒÑ‘Ð¹, Ð½Ð¾ Ð²Ñ‹ Ð½Ð°ÑˆÐ»Ð¸ Ð¼ÐµÐ½Ñ�. Ð˜Ð·Ð»Ð¾Ð¶Ð¸ Ñ�Ð²Ð¾Ð¸ Ð½Ð°Ð¼ÐµÑ€ÐµÐ½Ð¸Ñ�, ().";
    static String m17 = "ÐžÑ�Ñ‚Ð°Ð²ÑŒ Ð¼ÐµÐ½Ñ� Ð¸ Ð¼Ð¾Ðµ Ð³Ð½ÐµÐ·Ð´Ð¾ Ð² Ð¿Ð¾ÐºÐ¾Ðµ, Ð² Ð¾Ð±Ð¼ÐµÐ½ Ñ� Ð´Ð°Ð¼ Ñ‚ÐµÐ±Ðµ Ñ‡Ð°Ñ�Ñ‚ÑŒ Ñ�Ð²Ð¾ÐµÐ¹ Ñ�Ð¸Ð»Ñ‹. Ð¢Ñ‹ Ñ�Ð¼Ð¾Ð¶ÐµÑˆÑŒ Ð¿Ð¾ÐºÐ¾Ñ€Ð¸Ñ‚ÑŒ Ð½ÐµÐ±ÐµÑ�Ð°.";
    static String m18 = "Ð•Ñ‰Ðµ Ð¾Ð´Ð½Ð° Ð´ÑƒÑˆÐ°, Ð¿Ñ€Ð¸ÑˆÐµÐ´ÑˆÐ°Ñ� Ñ‡ÐµÑ€ÐµÐ· Ð¿Ð¾Ñ€Ñ‚Ð°Ð».... Ð�Ð¾ Ñ‚Ñ‹ Ð¾Ñ‚Ð»Ð¸Ñ‡Ð°ÐµÑˆÑŒÑ�Ñ� Ð¾Ñ‚ Ð½Ð¸Ñ…. Ð’ Ñ‚ÐµÐ±Ðµ ÐµÑ�Ñ‚ÑŒ Ñ�Ð¸Ð»Ð°. Ð¡ÐºÑ€Ñ‹Ñ‚Ð°Ñ� Ñ�Ð¸Ð»Ð°. Ð¯ Ð¼Ð¾Ð³Ñƒ Ð¿Ñ€Ð¾Ð±ÑƒÐ´Ð¸Ñ‚ÑŒ ÐµÐµ. Ð�Ð¾ Ð² Ð¾Ð±Ð¼ÐµÐ½ Ð½Ð° Ñ�Ñ‚Ð¾ Ñ‚Ñ‹ Ð¿Ð¾ÐºÐ¸Ð½ÐµÑˆÑŒ Ð¼Ð¾Ð¸ Ð·ÐµÐ¼Ð»Ð¸ Ð¸ Ð±Ð¾Ð»ÑŒÑˆÐµ Ð½Ð¸ÐºÐ¾Ð³Ð´Ð° Ð½Ðµ Ð¿Ð¾Ñ‚Ñ€ÐµÐ²Ð¾Ð¶Ð¸ÑˆÑŒ Ð¼ÐµÐ½Ñ�. Ð¡Ð¾Ð³Ð»Ð°Ñ�ÐµÐ½?";

    static String e3 = "Have you come here for the wings, ()? Get out, before it's too late.";
    static String e4 = "This is my land, (), show some respect. Go away, but first answer, why have you come here. Is it for wings?";
    static String e5 = "You have gone through lot to get here, (), what do you wish?";

    static ArrayList<String> dragonPhrases;
    static ArrayList<String> englishPhrases;

    static {
        dragonPhrases = new ArrayList<>(18);
        dragonPhrases.add(m1);
        dragonPhrases.add(m2);
        dragonPhrases.add(m3);
        dragonPhrases.add(m4);
        dragonPhrases.add(m5);
        dragonPhrases.add(m6);
        dragonPhrases.add(m7);
        dragonPhrases.add(m8);
        dragonPhrases.add(m9);
        dragonPhrases.add(m10);
        dragonPhrases.add(m11);
        dragonPhrases.add(m12);
        dragonPhrases.add(m13);
        dragonPhrases.add(m14);
        dragonPhrases.add(m15);
        dragonPhrases.add(m16);
        dragonPhrases.add(m17);
        dragonPhrases.add(m18);

        englishPhrases = new ArrayList<>(18);
        englishPhrases.add(e3);
        englishPhrases.add(e4);
        englishPhrases.add(e5);
    }

    @SubscribeEvent
    public static void inTheEnd(PlayerEvent.PlayerChangedDimensionEvent changedDimensionEvent) {
        PlayerEntity playerEntity = changedDimensionEvent.getPlayer();
        if (changedDimensionEvent.getTo() == World.END) {
            DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
                if (dragonStateHandler.isDragon() && !dragonStateHandler.hasWings()) {
                    Thread thread = new Thread(() -> {
                        try {
                            Thread.sleep(3000);
                            Language language = Minecraft.getInstance().getLanguageManager().getSelected();

                            String randomPhrase;
                            if (language.getCode().equals("ru_ru")) {
                                randomPhrase = dragonPhrases.get(playerEntity.getRandom().nextInt(dragonPhrases.size()));
                            } else
                                randomPhrase = englishPhrases.get(playerEntity.getRandom().nextInt(englishPhrases.size()));
                            randomPhrase = randomPhrase.replace("()", playerEntity.getDisplayName().getString());
                            playerEntity.sendMessage(new StringTextComponent(randomPhrase), playerEntity.getUUID());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });
                    thread.start();
                }
            });
        }
    }

    @SubscribeEvent
    public static void serverChatEvent(ServerChatEvent chatEvent) {
        String message = chatEvent.getMessage();
        ServerPlayerEntity playerEntity = chatEvent.getPlayer();
        String lowercase = message.toLowerCase();
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon() && !dragonStateHandler.hasWings()) {
                if (playerEntity.getLevel().dimension() == World.END) {
                    if (!playerEntity.getLevel().getDragons().isEmpty()) {
                        if (!lowercase.isEmpty()) {
                            Thread thread = new Thread(() -> {
                                try {
                                    Thread.sleep(2000);
                                    playerEntity.sendMessage(new TranslationTextComponent("ds.dragon.grants.wings"), playerEntity.getUUID());
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                            thread.start();
                            dragonStateHandler.setHasWings(true);
                            DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SynchronizeDragonCap(playerEntity.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getLevel(), dragonStateHandler.isDragon(), dragonStateHandler.getHealth(), true));
                        }
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void teleportAway(LivingDamageEvent damageEvent) {
        LivingEntity livingEntity = damageEvent.getEntityLiving();
        if (livingEntity instanceof PlayerEntity) {
            DamageSource damageSource = damageEvent.getSource();
            if (livingEntity.level.dimension() == World.END && damageSource == DamageSource.OUT_OF_WORLD) {
                DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
                    if (dragonStateHandler.isDragon()) {
                        livingEntity.changeDimension(livingEntity.getServer().overworld());
                        DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new RefreshDragons(livingEntity.getId()));
                    }
                });
            }
        }
    }
}
