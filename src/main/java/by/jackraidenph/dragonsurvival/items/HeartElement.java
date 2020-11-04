package by.jackraidenph.dragonsurvival.items;

import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.util.DragonLevel;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class HeartElement extends Item {
    public HeartElement(Properties properties) {
        super(properties);
    }

    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, PlayerEntity playerIn, Hand handIn) {
        PlayerStateProvider.getCap(playerIn).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.getIsDragon()) {
                float maxHealth = playerIn.getMaxHealth();
                if (maxHealth < 40) {
                    IAttributeInstance currentHealth = playerIn.getAttribute(SharedMonsterAttributes.MAX_HEALTH);
                    currentHealth.setBaseValue(currentHealth.getBaseValue() + 2);
                    playerIn.getHeldItem(handIn).shrink(1);
                    if (currentHealth.getBaseValue() >= DragonLevel.YOUNG.initialHealth - 1)
                        dragonStateHandler.setLevel(DragonLevel.YOUNG);
                    else if (currentHealth.getBaseValue() >= DragonLevel.ADULT.initialHealth - 1)
                        dragonStateHandler.setLevel(DragonLevel.ADULT);
                }
            }
        });
        return ActionResult.resultSuccess(playerIn.getHeldItem(handIn));
    }
}
