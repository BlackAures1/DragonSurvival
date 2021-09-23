package by.jackraidenph.dragonsurvival.renderer;

import by.jackraidenph.dragonsurvival.entity.PrincessEntity;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.VillagerRenderer;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.resources.IReloadableResourceManager;
import net.minecraft.util.ResourceLocation;

public class PrincessRenderer extends VillagerRenderer {
    private static final ResourceLocation BLACK = new ResourceLocation("dragonsurvival", "textures/princess/princess_black.png");
    private static final ResourceLocation BLUE = new ResourceLocation("dragonsurvival", "textures/princess/princess_blue.png");
    private static final ResourceLocation PURPLE = new ResourceLocation("dragonsurvival", "textures/princess/princess_purple.png");
    private static final ResourceLocation RED = new ResourceLocation("dragonsurvival", "textures/princess/princess_red.png");
    private static final ResourceLocation WHITE = new ResourceLocation("dragonsurvival", "textures/princess/princess_white.png");
    private static final ResourceLocation YELLOW = new ResourceLocation("dragonsurvival", "textures/princess/princess_yellow.png");

    public PrincessRenderer(EntityRendererManager rendererManager, IReloadableResourceManager reloadableResourceManager) {
        super(rendererManager, reloadableResourceManager);
        this.layers.removeIf(villagerEntityVillagerModelLayerRenderer -> villagerEntityVillagerModelLayerRenderer instanceof net.minecraft.client.renderer.entity.layers.VillagerLevelPendantLayer);
    }

    public ResourceLocation getTextureLocation(VillagerEntity villagerEntity) {
        PrincessEntity princessEntity = (PrincessEntity) villagerEntity;
        switch (DyeColor.byId(princessEntity.getColor())) {
            case RED:
                return RED;
            case BLUE:
                return BLUE;
            case YELLOW:
                return YELLOW;
            case BLACK:
                return BLACK;
            case PURPLE:
                return PURPLE;
            case WHITE:
                return WHITE;
        }
        return super.getTextureLocation(villagerEntity);
    }
}
