package by.jackraidenph.dragonsurvival.renderer;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.entity.MagicalBeastEntity;
import by.jackraidenph.dragonsurvival.models.MagicalBeastModel;
import by.jackraidenph.dragonsurvival.shader.ModShaders;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.ARBShaderObjects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MagicalBeastRenderer extends MobRenderer<MagicalBeastEntity, MagicalBeastModel> {

    public static List<ResourceLocation> MAGICAL_BEAST_TEXTURES = new ArrayList<>(Arrays.asList(
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_dark.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_dark_broken.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_grass.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_gray.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_green.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_jungle.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_jungle_flowers.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_light.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_sand.png"),
            new ResourceLocation(DragonSurvivalMod.MODID, "textures/magical_beast/magical_predator_zombie.png")
    ));

    public MagicalBeastRenderer(EntityRendererManager p_i50961_1_) {
        super(p_i50961_1_, new MagicalBeastModel(), 0.5F);
        ModShaders.register();
    }

    @Override
    public ResourceLocation getEntityTexture(MagicalBeastEntity entity) {
        return MAGICAL_BEAST_TEXTURES.get(entity.type);
    }
}
