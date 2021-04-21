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
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.ArrayList;

@Mod.EventBusSubscriber
public class WingObtainmentController {

    static String m1 = "Не смей приближаться к моему гнезду, (). Для чего ты пришёл сюда? Я буду защищаться.";
    static String m2 = "Я не терплю чужаков на своей территории, (). Держись от меня подальше. Каковы твои намерения?";
    static String m3 = "Ты пришёл за крыльями? Убирайся, пока не стало поздно, ().";
    static String m4 = "Это моя земля, (), прояви уважение. Уходи, но сначала ответь зачем пришёл. Тебе нужны крылья?";
    static String m5 = "Ты прошел через многое чтобы попасть сюда, (), чего ты желаешь?";
    static String m6 = "Ты пришёл договориться или воевать? Я убью тебя, если ты посмеешь подойти к моему гнезду.";
    static String m7 = "У меня есть то, чего ты желаешь, (). Вопрос в том, достоин ли ты этого?";
    static String m8 = "Мы с тобой одной крови, (), я не хочу сражаться с тобой. Но еще больше я не хочу чтобы мое гнездо было уничтожено. Скажи что ты хочешь и уходи.";
    static String m9 = "Ты прошел грань миров и оказался здесь, (), но ради чего? Тебя манит небо как и меня?";
    static String m10 = "Ты наглец, (), раз решил появиться здесь. Ради каких целей ты пришел сюда?";
    static String m11 = "Ты пришёл убить меня? Прошу, сохрани моё дитя... Я расскажу тебе о крыльях, если ты хочешь этого, но оставь нас в покое.";
    static String m12 = "Сколько лет я не видела подобных себе и столько же не хочу их видеть. Но ты здесь, (), отвечай быстро и убирайся отсюда. Зачем пришёл?";
    static String m13 = "Будь проклят тот, кто нарушил мой покой. Уходи с моей территории, (), иначе от тебя останутся лишь воспоминания. Кто ты?";
    static String m14 = "Столько лет я была здесь одна, но тут появляешься ты. Какие цели преследуют тебя, ()?";
    static String m15 = "Ты чего-то хочешь от меня, верно? Не зря же ты оказался на моей территории, ().";
    static String m16 = "Так легко переступить через границы миров.... Я надеялась скрыться здесь со своей семьёй, но вы нашли меня. Изложи свои намерения, ().";
    static String m17 = "Оставь меня и мое гнездо в покое, в обмен я дам тебе часть своей силы. Ты сможешь покорить небеса.";
    static String m18 = "Еще одна душа, пришедшая через портал.... Но ты отличаешься от них. В тебе есть сила. Скрытая сила. Я могу пробудить ее. Но в обмен на это ты покинешь мои земли и больше никогда не потревожишь меня. Согласен?";

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
        if (changedDimensionEvent.getTo() == DimensionType.THE_END) {
            DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
                if (dragonStateHandler.isDragon() && !dragonStateHandler.hasWings()) {
                    Thread thread = new Thread(() -> {
                        try {
                            Thread.sleep(3000);
                            Language language = Minecraft.getInstance().getLanguageManager().getCurrentLanguage();

                            String randomPhrase;
                            if (language.getCode().equals("ru_ru")) {
                                randomPhrase = dragonPhrases.get(playerEntity.getRNG().nextInt(dragonPhrases.size()));
                            } else
                                randomPhrase = englishPhrases.get(playerEntity.getRNG().nextInt(englishPhrases.size()));
                            randomPhrase = randomPhrase.replace("()", playerEntity.getDisplayName().getString());
                            playerEntity.sendMessage(new StringTextComponent(randomPhrase));
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
                if (playerEntity.getServerWorld().getDimension().getType() == DimensionType.THE_END) {
                    if (!playerEntity.getServerWorld().getDragons().isEmpty()) {
                        if (!lowercase.isEmpty()) {
                            Thread thread = new Thread(() -> {
                                try {
                                    Thread.sleep(2000);
                                    playerEntity.sendMessage(new TranslationTextComponent("ds.dragon.grants.wings"));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                            thread.start();
                            dragonStateHandler.setHasWings(true);
                            DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SynchronizeDragonCap(playerEntity.getEntityId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getLevel(), dragonStateHandler.isDragon(), dragonStateHandler.getHealth(), true));
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
            if (livingEntity.world.getDimension().getType() == DimensionType.THE_END && damageSource == DamageSource.OUT_OF_WORLD) {
                DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
                    if (dragonStateHandler.isDragon()) {
                        livingEntity.changeDimension(DimensionType.OVERWORLD);
                        DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new RefreshDragons(livingEntity.getEntityId()));
                    }
                });
            }
        }
    }
}
