package by.jackraidenph.dragonsurvival.items;

import by.jackraidenph.dragonsurvival.capability.DragonStateHandler;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

public class HeartElement extends Item {
    public HeartElement(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        LazyOptional<DragonStateHandler> dragonStateHandlerLazyOptional = playerIn.getCapability(DragonStateProvider.PLAYER_STATE_HANDLER_CAPABILITY);
        if (dragonStateHandlerLazyOptional.isPresent()) {
            DragonStateHandler dragonStateHandler = dragonStateHandlerLazyOptional.orElseGet(() -> null);
            if (dragonStateHandler.isDragon()) {
                float maxHealth = playerIn.getMaxHealth();
                if (maxHealth < 40) {
                    IAttributeInstance currentHealth = playerIn.getAttribute(SharedMonsterAttributes.MAX_HEALTH);
                    currentHealth.setBaseValue(currentHealth.getBaseValue() + 2);
                    playerIn.getHeldItem(handIn).shrink(1);

                    if (currentHealth.getBaseValue() >= DragonLevel.ADULT.initialHealth)
                        dragonStateHandler.setLevel(DragonLevel.ADULT);
                    else if (currentHealth.getBaseValue() >= DragonLevel.YOUNG.initialHealth)
                        dragonStateHandler.setLevel(DragonLevel.YOUNG);
                    return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
                }
            }
        }
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }
}
