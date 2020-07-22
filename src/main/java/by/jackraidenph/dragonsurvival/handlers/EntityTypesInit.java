package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.entity.MagicalBeastEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypesInit {

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = new DeferredRegister<>(ForgeRegistries.ENTITIES, DragonSurvivalMod.MODID);

    public static final RegistryObject<EntityType<MagicalBeastEntity>> MAGICAL_BEAST = ENTITY_TYPES.register("magical_beast", () -> EntityType.Builder.<MagicalBeastEntity>create(MagicalBeastEntity::new, EntityClassification.MONSTER).size(1.2f, 1.6f).build(new ResourceLocation(DragonSurvivalMod.MODID, "magical_beast").toString()));
}
