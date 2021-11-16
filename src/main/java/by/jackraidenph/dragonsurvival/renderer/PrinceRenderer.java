package by.jackraidenph.dragonsurvival.renderer;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;

public class PrinceRenderer extends VillagerRenderer {
 private static final ResourceLocation TEXTURE = new ResourceLocation("dragonsurvival", "textures/dragon_prince.png");

 public PrinceRenderer(EntityRendererManager entityRendererManager, IReloadableResourceManager reloadableResourceManager) {
  super(entityRendererManager, reloadableResourceManager);
  this.layers.removeIf(villagerEntityVillagerModelLayerRenderer -> villagerEntityVillagerModelLayerRenderer instanceof net.minecraft.client.renderer.entity.layers.VillagerLevelPendantLayer);
 }


 public ResourceLocation getTextureLocation(VillagerEntity villagerEntity) {
  return TEXTURE;
 }
}

