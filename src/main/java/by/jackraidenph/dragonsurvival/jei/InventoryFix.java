package by.jackraidenph.dragonsurvival.jei;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.containers.DragonContainer;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaRecipeCategoryUid;
import mezz.jei.api.recipe.transfer.IRecipeTransferHandler;
import mezz.jei.api.registration.IRecipeTransferRegistration;
import net.minecraft.util.ResourceLocation;

@JeiPlugin
@SuppressWarnings("unused")
public class InventoryFix implements IModPlugin, IRecipeTransferHandler {
    @Override
    public ResourceLocation getPluginUid() {
        return new ResourceLocation(DragonSurvivalMod.MODID, "fix");
    }

    @Override
    public Class getContainerClass() {
        return DragonContainer.class;
    }

    @Override
    public void registerRecipeTransferHandlers(IRecipeTransferRegistration registration) {
        registration.addRecipeTransferHandler(DragonContainer.class, VanillaRecipeCategoryUid.CRAFTING, 1, 9, 14, 36);
    }
}
