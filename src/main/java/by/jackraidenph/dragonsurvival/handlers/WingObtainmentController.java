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

    static ArrayList<TranslationTextComponent> dragonPhrases;
    static ArrayList<TranslationTextComponent> englishPhrases;

    static {
        dragonPhrases = new ArrayList<>(18);
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.1"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.2"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.3"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.4"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.5"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.6"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.7"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.8"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.9"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.10"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.11"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.12"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.13"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.14"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.15"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.16"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.17"));
        dragonPhrases.add(new TranslationTextComponent("ds.endmessage.18"));

        englishPhrases = new ArrayList<>(18);
        englishPhrases.add(new TranslationTextComponent("ds.endmessage.1"));
        englishPhrases.add(new TranslationTextComponent("ds.endmessage.2"));
        englishPhrases.add(new TranslationTextComponent("ds.endmessage.3"));
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

                            TranslationTextComponent randomPhrase;
                            if (language.getCode().equals("ru_ru")) {
                                randomPhrase = dragonPhrases.get(playerEntity.getRandom().nextInt(dragonPhrases.size()));
                            } else
                                randomPhrase = englishPhrases.get(playerEntity.getRandom().nextInt(englishPhrases.size()));
                            String phrase = randomPhrase.getString().replace("()", playerEntity.getDisplayName().getString());
                            playerEntity.sendMessage(new StringTextComponent(phrase), playerEntity.getUUID());
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
