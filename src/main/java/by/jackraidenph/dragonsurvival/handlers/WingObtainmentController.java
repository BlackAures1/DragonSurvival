package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.network.RefreshDragons;
import by.jackraidenph.dragonsurvival.network.SynchronizeDragonCap;
import by.jackraidenph.dragonsurvival.util.ConfigurationHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.resources.Language;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Mod.EventBusSubscriber
public class WingObtainmentController {
	
	private static Map<String, Integer> dragonPhrases = new HashMap<String, Integer>();

	private static UUID enderDragonUUID = UUID.fromString("426642b9-2e88-4350-afa8-f99f75af5479");

	public static void loadDragonPhrases() {
		try {
			List<String> langs = new ArrayList<>();
			BufferedReader reader = new BufferedReader(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("assets/dragonsurvival/lang")));
			String file;
			while ((file = reader.readLine()) != null) {
				langs.add(file);
	        }
			reader.close();
			Gson gson = new Gson();
			Type type = new TypeToken<Map<String, String>>(){}.getType();
			for (String langFile : langs) {
				URL resource = Thread.currentThread().getContextClassLoader().getResource("assets/dragonsurvival/lang/" + langFile);
				Map<String, String> langData = gson.fromJson(new String(Files.readAllBytes(Paths.get(resource.toURI()))), type);
				int phraseCount = 0;
				for (String key : langData.keySet()) {
					if (key.contains("ds.endmessage"))
						phraseCount++;
				}
				if (phraseCount > 0)
					dragonPhrases.put(langFile.replace(".json", ""), phraseCount);
			}
		}catch (Exception ex) {
			ex.printStackTrace();
		}
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
                            playerEntity.sendMessage(new StringTextComponent("ds.endmessage"), enderDragonUUID);
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
    public static void clientMessageRecieved(ClientChatReceivedEvent event) {
    	DragonSurvivalMod.LOGGER.info(event.getMessage().getString());
    	if (event.getSenderUUID().equals(enderDragonUUID)) { 
			if (event.getMessage().getString().equals("ds.endmessage")) {
	    		Language language = Minecraft.getInstance().getLanguageManager().getSelected();
	    		ClientPlayerEntity player = Minecraft.getInstance().player;
	    		int messageId = player.getRandom().nextInt(dragonPhrases.getOrDefault(language.getCode(), dragonPhrases.getOrDefault("en_us", 1))) + 1;
	    		event.setMessage(new StringTextComponent((new TranslationTextComponent("ds.endmessage." + messageId)).getString().replace("()", player.getDisplayName().getString())));
			} else if (event.getMessage().getString().equals("ds.dragon.grants.wings")) {
				event.setMessage(new TranslationTextComponent("ds.dragon.grants.wings"));
			}
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
                                    playerEntity.sendMessage(new StringTextComponent("ds.dragon.grants.wings"), enderDragonUUID);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                            thread.start();
                            dragonStateHandler.setHasWings(true);
                            DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SynchronizeDragonCap(playerEntity.getId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.isDragon(), dragonStateHandler.getSize(), true));
                        }
                    }
                }
            }
        });
    }

    @SubscribeEvent
    public static void teleportAway(LivingDamageEvent damageEvent) {
    	if (!ConfigurationHandler.endVoidTeleport.get())
    		return;
        LivingEntity livingEntity = damageEvent.getEntityLiving();
        if (livingEntity instanceof PlayerEntity) {
            DamageSource damageSource = damageEvent.getSource();
            if (livingEntity.level.dimension() == World.END && damageSource == DamageSource.OUT_OF_WORLD && livingEntity.position().y < -60) {
                DragonStateProvider.getCap(livingEntity).ifPresent(dragonStateHandler -> {
                    if (dragonStateHandler.isDragon()) {
                        livingEntity.changeDimension(livingEntity.getServer().overworld());
                        DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new RefreshDragons(livingEntity.getId()));
                        damageEvent.setCanceled(true);
                    }
                });
            }
        }
    }
}
