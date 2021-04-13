package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.entity.MagicalPredatorEntity;
import by.jackraidenph.dragonsurvival.gecko.DragonEntity;
import by.jackraidenph.dragonsurvival.util.BiomeDictionaryHelper;
import by.jackraidenph.dragonsurvival.util.ConfigurationHandler;
import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import net.minecraft.entity.*;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityTypesInit {

    private static final List<EntityType<?>> entities = Lists.newArrayList();
    private static final List<Item> spawnEggs = Lists.newArrayList();

    public static EntityType<MagicalPredatorEntity> MAGICAL_BEAST;
    public static EntityType<DragonEntity> dragonEntity;

    private static <T extends CreatureEntity> EntityType<T> createEntity(Class<T> entityClass, EntityType.IFactory<T> factory, float width, float height, int eggPrimary, int eggSecondary) {

        ResourceLocation location = new ResourceLocation(DragonSurvivalMod.MODID, classToString(entityClass));
        EntityType<T> entity = EntityType.Builder.create(factory, EntityClassification.MONSTER).size(width, height).setTrackingRange(64).setUpdateInterval(1).build(location.toString());
        entity.setRegistryName(location);
        entities.add(entity);
        Item spawnEgg = new SpawnEggItem(entity, eggPrimary, eggSecondary, (new Item.Properties()).group(ItemsInit.items));
        spawnEgg.setRegistryName(new ResourceLocation(DragonSurvivalMod.MODID, classToString(entityClass) + "_spawn_egg"));
        spawnEggs.add(spawnEgg);

        return entity;
    }

    private static String classToString(Class<? extends CreatureEntity> entityClass) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityClass.getSimpleName()).replace("entity_", "");
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {

        for (EntityType entity : entities) {
            Preconditions.checkNotNull(entity.getRegistryName(), "registryName");
            event.getRegistry().register(entity);
            EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MonsterEntity::canMonsterSpawn);
        }

        dragonEntity = new EntityType<>(DragonEntity::new, EntityClassification.MISC, true, false, false, false, EntitySize.fixed(0.9f, 1.9f));
        dragonEntity.setRegistryName(new ResourceLocation(DragonSurvivalMod.MODID, "dummy_dragon"));
        event.getRegistry().register(dragonEntity);
    }

    @SubscribeEvent
    public static void registerSpawnEggs(RegistryEvent.Register<Item> event) {
        MAGICAL_BEAST = createEntity(MagicalPredatorEntity.class, MagicalPredatorEntity::new, 1.1f, 1.5625f, 0x000000, 0xFFFFFF);
        for (Item spawnEgg : spawnEggs) {
            Preconditions.checkNotNull(spawnEgg.getRegistryName(), "registry name is null");
            event.getRegistry().register(spawnEgg);
        }
    }

    public static void addSpawn() {
        List<Biome> spawnableBiomes = Lists.newArrayList();

        List<BiomeDictionary.Type> includeList = Arrays.asList(BiomeDictionaryHelper.toBiomeTypeArray(ConfigurationHandler.SPAWN.include.get()));
        List<BiomeDictionary.Type> excludeList = Arrays.asList(BiomeDictionaryHelper.toBiomeTypeArray(ConfigurationHandler.SPAWN.exclude.get()));
        if (!includeList.isEmpty()) {
            for (BiomeDictionary.Type type : includeList) {
                for (Biome biome : BiomeDictionary.getBiomes(type)) {
                    if (!biome.getSpawns(EntityClassification.MONSTER).isEmpty()) {
                        spawnableBiomes.add(biome);
                    }
                }
            }
            if (!excludeList.isEmpty()) {
                for (BiomeDictionary.Type type : excludeList) {
                    Set<Biome> excludeBiomes = BiomeDictionary.getBiomes(type);
                    for (Biome biome : excludeBiomes) {
                        spawnableBiomes.remove(biome);
                    }
                }
            }
        } else {
            throw new IllegalArgumentException("Do not leave the BiomeDictionary type inclusion list empty. If you wish to disable spawning of an entity, set the weight to 0 instead.");
        }
        for (Biome biome : spawnableBiomes) {
            biome.getSpawns(EntityClassification.MONSTER).add(new Biome.SpawnListEntry(MAGICAL_BEAST, ConfigurationHandler.SPAWN.weight.get(), ConfigurationHandler.SPAWN.min.get(), ConfigurationHandler.SPAWN.max.get()));
        }
    }
}
