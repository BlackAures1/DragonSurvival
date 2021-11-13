package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;
import com.google.common.collect.HashMultimap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.MissingTextureSprite;
import net.minecraft.client.renderer.texture.NativeImage;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Optional;

public class DragonSkins
{
	public static final String SKINS = "https://raw.githubusercontent.com/DragonSurvivalTeam/DragonSurvival/master/src/test/resources/";
	/**
	 * Default skins
	 */
	static HashMultimap<String, ResourceLocation> skinCache = HashMultimap.create(1, 3);
	/**
	 * Skins by name
	 */
	static HashMultimap<String, ResourceLocation> skinCacheForName = HashMultimap.create(1, 3);
	
	static HashMap<String, Boolean> warningsForName = new HashMap<>();
	
	public static boolean hasCustomSkin(PlayerEntity playerEntity, DragonLevel stage){
		final String playerName = playerEntity.getGameProfile().getName();
		Optional<ResourceLocation> skinForName = skinCacheForName.get(playerName).stream().filter(location -> location.toString().endsWith(playerName.toLowerCase() + "_" +stage.name)).findFirst();
		return skinForName.isPresent();
	}
	
	public static ResourceLocation getSkin(PlayerEntity player, DragonStateHandler cap, DragonLevel dragonStage) {
		ResourceLocation texture;
		final String playerName = player.getGameProfile().getName();
		
		Optional<ResourceLocation> skinForName = skinCacheForName.get(playerName).stream().filter(location -> Boolean.parseBoolean(location.toString().endsWith(playerName.toLowerCase()+"_"+dragonStage.name)+"")).findFirst();
		if (skinForName.isPresent()) {
			return skinForName.get();
		} else {
			Optional<ResourceLocation> defSkin = skinCache.get(playerName).stream().filter(location -> location.toString().endsWith(cap.getType().toString().toLowerCase(Locale.ROOT) + "_" + dragonStage.name + ".png")).findFirst();
			
			if (defSkin.isPresent()) {
				return defSkin.get();
			}
			
			try {
				texture = loadCustomSkinForName(player, dragonStage);
				skinCacheForName.put(playerName, texture);
			} catch (IOException e) {
				if (warningsForName.get(playerName) == null) {
					DragonSurvivalMod.LOGGER.info("Custom skin for user {} doesn't exist", playerName);
					warningsForName.put(playerName, true);
				}
			} finally {
				texture = constructTexture(cap.getType(), dragonStage);
				skinCache.put(playerName, texture);
			}
		}
		return texture;
	}
	
	/**
	 * Loads a custom image for skin based on profile name
	 */
	public static ResourceLocation loadCustomSkinForName(PlayerEntity playerEntity, DragonLevel dragonStage) throws IOException {
		String name = playerEntity.getGameProfile().getName();
		URL url;
		switch (dragonStage) {
			case BABY:
				url = new URL(SKINS + name + "_newborn.png");
				break;
			case YOUNG:
				url = new URL(SKINS + name + "_young.png");
				break;
			case ADULT:
				url = new URL(SKINS + name + "_adult.png");
				break;
			default:
				url = null;
		}
		InputStream inputStream = url.openConnection().getInputStream();
		NativeImage customTexture = NativeImage.read(inputStream);
		ResourceLocation resourceLocation;
		Minecraft.getInstance().getTextureManager().register(resourceLocation = new ResourceLocation(DragonSurvivalMod.MODID,name.toLowerCase()+"_"+dragonStage.name), new DynamicTexture(customTexture));
		return resourceLocation;
	}
	
	public static ResourceLocation constructTexture(DragonType dragonType, DragonLevel stage) {
	
	    String texture;
	    texture = "textures/dragon/";
	    switch (dragonType) {
	        case SEA:
	            texture += "sea";
	            break;
	        case CAVE:
	            texture += "cave";
	            break;
	        case FOREST:
	            texture += "forest";
	            break;
	    }
	
	    switch (stage) {
	        case BABY:
	            texture += "_newborn";
	            break;
	        case YOUNG:
	            texture += "_young";
	            break;
	        case ADULT:
	            texture += "_adult";
	            break;
	    }
	    texture += ".png";
	
	    return new ResourceLocation(DragonSurvivalMod.MODID, texture);
	}
	
	
	//Skin glows
	static HashMultimap<String, ResourceLocation> defaultGlows = HashMultimap.create(1, 3);
	/**
	 * Player glows
	 */
	static HashMultimap<String, ResourceLocation> glowCacheForName = HashMultimap.create(1, 3);
	
	
	public static boolean hasCustomGlow(PlayerEntity playerEntity, DragonLevel stage){
		final String playerName = playerEntity.getGameProfile().getName();
		Optional<ResourceLocation> glowForName = glowCacheForName.get(playerName).stream().filter(location -> location.getPath().endsWith(playerName.toLowerCase()+"_"+stage.name)).findFirst();
		return glowForName.isPresent();
	}
	
	
	public static ResourceLocation getCustomGlow(PlayerEntity playerEntity, DragonLevel stage){
		final String playerName = playerEntity.getGameProfile().getName();
		Optional<ResourceLocation> glowForName = glowCacheForName.get(playerName).stream().filter(location -> location.getPath().endsWith(playerName.toLowerCase()+"_"+stage.name)).findFirst();
		return glowForName.orElse(null);
	}
	
	
	public static ResourceLocation loadCustomGlowForName(PlayerEntity playerEntity, DragonLevel dragonStage) throws IOException {
		String name = playerEntity.getGameProfile().getName();
		URL url;
		switch (dragonStage) {
			case BABY:
				url = new URL(SKINS + name + "_newborn.png");
				break;
			case YOUNG:
				url = new URL(SKINS + name + "_young.png");
				break;
			case ADULT:
				url = new URL(SKINS + name + "_adult.png");
				break;
			default:
				url = null;
		}
		InputStream inputStream = url.openConnection().getInputStream();
		NativeImage customTexture = NativeImage.read(inputStream);
		ResourceLocation resourceLocation;
		Minecraft.getInstance().getTextureManager().register(resourceLocation = new ResourceLocation(DragonSurvivalMod.MODID,name.toLowerCase()+"_"+dragonStage.name), new DynamicTexture(customTexture));
		return resourceLocation;
	}
	
	public static ResourceLocation constructGlowTexture(DragonType dragonType, DragonLevel stage) {
		String texture;
		texture = "textures/dragon/";
		switch (dragonType) {
			case SEA:
				texture += "sea";
				break;
			case CAVE:
				texture += "cave";
				break;
			case FOREST:
				texture += "forest";
				break;
		}
		
		switch (stage) {
			case BABY:
				texture += "_newborn";
				break;
			case YOUNG:
				texture += "_young";
				break;
			case ADULT:
				texture += "_adult";
				break;
		}
		
		texture += "_glow.png";
		return new ResourceLocation(DragonSurvivalMod.MODID, texture);
	}
	
	public static ResourceLocation getSkinGlow(PlayerEntity player, DragonStateHandler cap, DragonLevel dragonStage) {
		ResourceLocation texture;
		final String playerName = player.getGameProfile().getName();
		boolean hasCustomSkin = hasCustomSkin(player, dragonStage);
		
		if (hasCustomSkin) {
			if(hasCustomGlow(player, dragonStage)){
				return getCustomGlow(player, dragonStage);
				
			}else{
				try {
					texture = loadCustomGlowForName(player, dragonStage);
					glowCacheForName.put(playerName, texture);
					return texture;
				} catch (IOException e) {}
			}
			
		} else {
			if(defaultGlows.containsKey(cap.getType())){
				Optional<ResourceLocation> defGlow = defaultGlows.get(cap.getType() + "_" + dragonStage.name).stream().findFirst();
				texture = defGlow.orElse(null);
				
				if(texture != null && Minecraft.getInstance().textureManager.getTexture(texture) == MissingTextureSprite.getTexture()){
					return null;
				}
				
				return texture;
			}else {
				texture = constructGlowTexture(cap.getType(), dragonStage);
				defaultGlows.put(cap.getType() + "_" + dragonStage.name, texture);
				return texture;
			}
		}
		
		return null;
	}
}
