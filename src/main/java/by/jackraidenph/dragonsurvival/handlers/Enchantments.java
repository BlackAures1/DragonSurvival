package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Collections;
import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class Enchantments {

    @SubscribeEvent
    public static void registerEnchantments(RegistryEvent.Register<Enchantment> enchantmentRegister) {
        IForgeRegistry<Enchantment> forgeRegistry = enchantmentRegister.getRegistry();
        forgeRegistry.register(new Enchantment(Enchantment.Rarity.COMMON, EnchantmentType.ARMOR, EquipmentSlotType.values()) {
            @Override
            public Map<EquipmentSlotType, ItemStack> getEntityEquipment(LivingEntity livingEntityIn) {
                return Collections.emptyMap();
            }
        }.setRegistryName(DragonSurvivalMod.MODID, "test"));
    }
}
