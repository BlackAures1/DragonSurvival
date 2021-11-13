package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class DragonSkins
{
	public static final String SKINS = "https://raw.githubusercontent.com/DragonSurvivalTeam/DragonSurvival/master/src/test/resources/";
	
	private static HashMap<String, ResourceLocation> defaultSkinCache = new HashMap<>();
	private static HashMap<String, ResourceLocation> playerSkinCache = new HashMap<>();
	
	private static HashMap<String, ResourceLocation> defaultGlowCache = new HashMap<>();
	private static HashMap<String, ResourceLocation> playerGlowCache = new HashMap<>();
	
	private static ArrayList<UUID> hasFailedFetch = new ArrayList<>();
	
	public static ResourceLocation getPlayerSkin(PlayerEntity player, DragonType type, DragonLevel dragonStage) {
		ResourceLocation texture = null;
		UUID id = player.getGameProfile().getId();
		String playerKey = id + "_" + dragonStage.name;
		String defaultKey = type + "_" + dragonStage.name;
		
		if(playerSkinCache.containsKey(playerKey)){
			return playerSkinCache.get(playerKey);
		}
		
		if(!hasFailedFetch.contains(id)){
			texture = fetchSkinFile(player, dragonStage);
			
			if(texture != null) {
				playerSkinCache.put(playerKey, texture);
			}
		}
		
		if(texture == null){
			if(defaultSkinCache.containsKey(defaultKey)){
				texture = defaultSkinCache.get(defaultKey);
			}else {
				texture = constructTexture(type, dragonStage);
				defaultSkinCache.put(defaultKey, texture);
			}
		}
		
		return texture;
	}
	
	public static ResourceLocation getGlowTexture(PlayerEntity player, DragonType type, DragonLevel dragonStage) {
		ResourceLocation texture = null;
		String playerKey = player.getGameProfile().getId() + "_" + dragonStage.name;
		String defaultKey = type + "_" + dragonStage.name;
		
		if (playerSkinCache.containsKey(playerKey)) {
			if(playerGlowCache.containsKey(playerKey)){
				return playerGlowCache.get(playerKey);
				
			}else{
				texture = fetchSkinFile(player, dragonStage, "glow");
				playerGlowCache.put(playerKey, texture);
			}
			
		} else {
			if(defaultGlowCache.containsKey(defaultKey)){
				texture = defaultGlowCache.get(defaultKey);
				
				if(texture != null && Minecraft.getInstance().textureManager.getTexture(texture) == MissingTextureSprite.getTexture()){
					return null;
				}
				
			}else {
				texture = constructTexture(type, dragonStage, "glow");
				defaultGlowCache.put(defaultKey, texture);
			}
		}
		
		return texture;
	}
	
	public static ResourceLocation fetchSkinFile(PlayerEntity playerEntity, DragonLevel dragonStage, String... extra) {
		ResourceLocation resourceLocation = null;
		String name = playerEntity.getGameProfile().getName();
		UUID id = playerEntity.getGameProfile().getId();
		
		String[] text = ArrayUtils.addAll(new String[]{name, dragonStage.name}, extra);
		String searchText = StringUtils.join(text, "_");
		
		try{
			URL url = new URL(SKINS + searchText + ".png");
			InputStream inputStream = url.openConnection().getInputStream();
			NativeImage customTexture = NativeImage.read(inputStream);
			resourceLocation = new ResourceLocation(DragonSurvivalMod.MODID, searchText.toLowerCase(Locale.ROOT));
			Minecraft.getInstance().getTextureManager().register(resourceLocation, new DynamicTexture(customTexture));
			
		}catch (IOException e){
			if (!hasFailedFetch.contains(id)) {
				DragonSurvivalMod.LOGGER.info("Custom skin for user {} doesn't exist", name);
				hasFailedFetch.add(id);
			}
			
			return null;
		}
		
		return resourceLocation;
	}
	
	private static ResourceLocation constructTexture(DragonType dragonType, DragonLevel stage, String... extra) {
		String[] text = ArrayUtils.addAll(new String[]{dragonType.name().toLowerCase(Locale.ROOT), stage.name}, extra);
		String texture = "textures/dragon/" + StringUtils.join(text, "_") + ".png";
	    return new ResourceLocation(DragonSurvivalMod.MODID, texture);
	}
}
