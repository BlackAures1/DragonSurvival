package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.entity.*;
import by.jackraidenph.dragonsurvival.gecko.DragonEntity;
import by.jackraidenph.dragonsurvival.gecko.Knight;
import by.jackraidenph.dragonsurvival.gecko.Princess;
import com.google.common.base.CaseFormat;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.PillagerEntity;
import net.minecraft.entity.monster.VindicatorEntity;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Arrays;
import java.util.List;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityTypesInit {

    private static final List<EntityType<?>> entities = Lists.newArrayList();
    private static final List<Item> spawnEggs = Lists.newArrayList();

    public static EntityType<MagicalPredatorEntity> MAGICAL_BEAST;
    public static EntityType<DragonEntity> DRAGON;
    public static EntityType<BolasEntity> BOLAS_ENTITY;
    public static EntityType<HunterHound> HUNTER_HOUND;
    public static EntityType<ShooterHunter> SHOOTER_HUNTER;
    public static EntityType<KnightHunter> KNIGHT_HUNTER;
    public static EntityType<SquireHunter> SQUIRE_HUNTER;
    public static EntityType<PrincessEntity> PRINCESS;
    public static EntityType<Prince> PRINCE;
    public static EntityType<Knight> KNIGHT;
    public static EntityType<Princess> PRINCESS_ON_HORSE;

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
        HUNTER_HOUND = createEntity(HunterHound.class, HunterHound::new, 0.6F, 0.85F, 10510648, 8934192);
        SHOOTER_HUNTER = createEntity(ShooterHunter.class, ShooterHunter::new, 0.6F, 1.95F, 12486764, 2690565);
        KNIGHT_HUNTER = createEntity(KnightHunter.class, KnightHunter::new, 0.6F, 1.95F, 12486764, 8552567);
        SQUIRE_HUNTER = createEntity(SquireHunter.class, SquireHunter::new, 0.6F, 1.95F, 12486764, 5318420);
        PRINCESS = createEntity(PrincessEntity.class, PrincessEntity::new, 0.6F, 1.9F, 16766495, 174864);
        PRINCE = createEntity(Prince.class, Prince::new, 0.6F, 1.9F, 4924973, 174864);
        KNIGHT = createEntity(Knight.class, Knight::new, 0.8f, 2.5f, 0xffffff, 0x510707);
        VillagerRelationsHandler.dragonHunters = Arrays.asList(HUNTER_HOUND, SHOOTER_HUNTER, SQUIRE_HUNTER, KNIGHT);
        PRINCESS_ON_HORSE = createEntity(Princess.class, Princess::new, 0.8f, 2.5f, 0xffd61f, 0x2ab10);
        for (Item spawnEgg : spawnEggs) {
            Preconditions.checkNotNull(spawnEgg.getRegistryName(), "registry name is null");
            event.getRegistry().register(spawnEgg);
        }
    }

    @SubscribeEvent
    public static void attributeCreationEvent(EntityAttributeCreationEvent event) {
        event.put(MAGICAL_BEAST, MagicalPredatorEntity.createMonsterAttributes().build());
        event.put(DRAGON, DragonEntity.createLivingAttributes().build());
        event.put(HUNTER_HOUND, WolfEntity.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.35).add(Attributes.ATTACK_DAMAGE, 4.0D).build());
        event.put(SHOOTER_HUNTER, PillagerEntity.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.35).build());
        event.put(KNIGHT_HUNTER, VindicatorEntity.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.35).add(Attributes.ATTACK_DAMAGE, 8.0D).add(Attributes.ARMOR, 10.0D).add(Attributes.MAX_HEALTH, 40.0D).build());
        event.put(SQUIRE_HUNTER, VindicatorEntity.createAttributes().add(Attributes.MOVEMENT_SPEED, 0.35).add(Attributes.ATTACK_DAMAGE, 2.0D).build());
        event.put(PRINCESS, VillagerEntity.createAttributes().build());
        event.put(PRINCESS_ON_HORSE, VillagerEntity.createAttributes().build());
        event.put(PRINCE, VillagerEntity.createAttributes().add(Attributes.ATTACK_DAMAGE, 1.0D).build());
        event.put(KNIGHT, Knight.createMobAttributes().add(Attributes.MOVEMENT_SPEED, 0.35).add(Attributes.ATTACK_DAMAGE, 8).add(Attributes.ARMOR, 10).add(Attributes.MAX_HEALTH, 40).build());
    }


}
