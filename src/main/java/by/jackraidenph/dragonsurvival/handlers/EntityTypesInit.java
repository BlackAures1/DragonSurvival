package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.entity.BolasEntity;
import by.jackraidenph.dragonsurvival.entity.MagicalPredatorEntity;
import by.jackraidenph.dragonsurvival.gecko.DragonEntity;
import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityTypesInit {

    private static final List<EntityType<?>> entities = Lists.newArrayList();
    private static final List<Item> spawnEggs = Lists.newArrayList();

    public static EntityType<MagicalPredatorEntity> MAGICAL_BEAST;
    public static EntityType<DragonEntity> DRAGON;
    public static EntityType<BolasEntity> BOLAS_ENTITY;

    private static <T extends CreatureEntity> EntityType<T> createEntity(Class<T> entityClass, EntityType.IFactory<T> factory, float width, float height, int eggPrimary, int eggSecondary) {

        ResourceLocation location = new ResourceLocation(DragonSurvivalMod.MODID, classToString(entityClass));
        EntityType<T> entity = EntityType.Builder.of(factory, EntityClassification.MONSTER).sized(width, height).setTrackingRange(64).setUpdateInterval(1).build(location.toString());
        entity.setRegistryName(location);
        entities.add(entity);
        Item spawnEgg = new SpawnEggItem(entity, eggPrimary, eggSecondary, (new Item.Properties()).tab(ItemsInit.items));
        spawnEgg.setRegistryName(new ResourceLocation(DragonSurvivalMod.MODID, classToString(entityClass) + "_spawn_egg"));
        spawnEggs.add(spawnEgg);

        return entity;
    }

    private static String classToString(Class<? extends CreatureEntity> entityClass) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityClass.getSimpleName()).replace("entity_", "");
    }

    private static <T extends EntityType<?>> T cast(EntityType<?> entityType) {
        return (T) entityType;
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {

        IForgeRegistry<EntityType<?>> registry = event.getRegistry();
        for (EntityType entity : entities) {
            Preconditions.checkNotNull(entity.getRegistryName(), "registryName");
            registry.register(entity);
            EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::checkAnyLightMonsterSpawnRules);
        }

        DRAGON = new EntityType<DragonEntity>(DragonEntity::new, EntityClassification.MISC, true, false, false, false, ImmutableSet.of(), EntitySize.fixed(0.9f, 1.9f), 0, 0);
        DRAGON.setRegistryName(new ResourceLocation(DragonSurvivalMod.MODID, "dummy_dragon"));
        registry.register(DRAGON);
        BOLAS_ENTITY = cast(EntityType.Builder.of((p_create_1_, p_create_2_) -> new BolasEntity(p_create_2_), EntityClassification.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("bolas"));
        BOLAS_ENTITY.setRegistryName("dragonsurvival", "bolas");
        registry.register(BOLAS_ENTITY);
    }

    @SubscribeEvent
    public static void registerSpawnEggs(RegistryEvent.Register<Item> event) {
        MAGICAL_BEAST = createEntity(MagicalPredatorEntity.class, MagicalPredatorEntity::new, 1.1f, 1.5625f, 0x000000, 0xFFFFFF);
        for (Item spawnEgg : spawnEggs) {
            Preconditions.checkNotNull(spawnEgg.getRegistryName(), "registry name is null");
            event.getRegistry().register(spawnEgg);
        }
    }

    @SubscribeEvent
    public static void attributeCreationEvent(EntityAttributeCreationEvent event) {
        event.put(MAGICAL_BEAST, MagicalPredatorEntity.createMonsterAttributes().build());
        event.put(DRAGON, DragonEntity.createLivingAttributes().build());
    }


}
