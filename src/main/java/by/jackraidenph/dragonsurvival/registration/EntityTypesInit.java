package by.jackraidenph.dragonsurvival.registration;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.config.ConfigHandler;
import by.jackraidenph.dragonsurvival.entity.*;
import by.jackraidenph.dragonsurvival.gecko.DragonEntity;
import by.jackraidenph.dragonsurvival.gecko.Knight;
import by.jackraidenph.dragonsurvival.gecko.Princess;
import by.jackraidenph.dragonsurvival.handlers.VillagerRelationsHandler;
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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("rawtypes,unchecked")
@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityTypesInit {

    private static final List<EntityType<?>> entities = Lists.newArrayList();
    private static final List<Item> spawnEggs = Lists.newArrayList();

    public static EntityType<MagicalPredatorEntity> MAGICAL_BEAST;
    public static EntityType<DragonEntity> DRAGON;
    public static EntityType<BolasEntity> BOLAS_ENTITY;
    public static EntityType<HunterHound> HUNTER_HOUND;
    public static EntityType<Shooter> SHOOTER_HUNTER;
    public static EntityType<Squire> SQUIRE_HUNTER;
    public static EntityType<PrincessEntity> PRINCESS;
    public static EntityType<Knight> KNIGHT;
    public static EntityType<Princess> PRINCESS_ON_HORSE;
    public static EntityType<by.jackraidenph.dragonsurvival.gecko.Prince> PRINCE_ON_HORSE;

    private static <T extends CreatureEntity> EntityType<T> createEntity(Class<T> entityClass, EntityType.IFactory<T> factory, float width, float height, int eggPrimary, int eggSecondary, EntitySpawnPlacementRegistry.IPlacementPredicate spawnPlacementPredicate) {

        ResourceLocation location = new ResourceLocation(DragonSurvivalMod.MODID, classToString(entityClass));
        EntityType<T> entity = EntityType.Builder.of(factory, EntityClassification.MONSTER).sized(width, height).setTrackingRange(64).setUpdateInterval(1).build(location.toString());
        entity.setRegistryName(location);
        entities.add(entity);
        Item spawnEgg = new SpawnEggItem(entity, eggPrimary, eggSecondary, (new Item.Properties()).tab(ItemsInit.items));
        spawnEgg.setRegistryName(new ResourceLocation(DragonSurvivalMod.MODID, classToString(entityClass) + "_spawn_egg"));
        spawnEggs.add(spawnEgg);
        if (spawnPlacementPredicate == null)
            EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, (p_test_1_, p_test_2_, p_test_3_, p_test_4_, p_test_5_) -> MonsterEntity.checkAnyLightMonsterSpawnRules(cast(p_test_1_), p_test_2_, p_test_3_, p_test_4_, p_test_5_));
        else
            EntitySpawnPlacementRegistry.register(entity, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, spawnPlacementPredicate);
        return entity;
    }

    private static String classToString(Class<? extends CreatureEntity> entityClass) {
        return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, entityClass.getSimpleName()).replace("entity_", "");
    }

    /**
     * Because of generic quirks
     */
    private static <T extends EntityType<?>> T cast(EntityType<?> entityType) {
        return (T) entityType;
    }

    @SubscribeEvent
    public static void registerEntities(RegistryEvent.Register<EntityType<?>> event) {

        IForgeRegistry<EntityType<?>> registry = event.getRegistry();
        for (EntityType entity : entities) {
            Preconditions.checkNotNull(entity.getRegistryName(), "registryName");
            registry.register(entity);
        }

        DRAGON = new EntityType<>(DragonEntity::new, EntityClassification.MISC, true, false, false, false, ImmutableSet.of(), EntitySize.fixed(0.9f, 1.9f), 0, 0);
        DRAGON.setRegistryName(new ResourceLocation(DragonSurvivalMod.MODID, "dummy_dragon"));
        registry.register(DRAGON);
        BOLAS_ENTITY = cast(EntityType.Builder.of((p_create_1_, p_create_2_) -> new BolasEntity(p_create_2_), EntityClassification.MISC).sized(0.25F, 0.25F).clientTrackingRange(4).updateInterval(10).build("bolas"));
        BOLAS_ENTITY.setRegistryName("dragonsurvival", "bolas");
        registry.register(BOLAS_ENTITY);
    }

    @SubscribeEvent
    public static void registerSpawnEggs(RegistryEvent.Register<Item> event) {
        MAGICAL_BEAST = createEntity(MagicalPredatorEntity.class, MagicalPredatorEntity::new, 1.1f, 1.5625f, 0x000000, 0xFFFFFF, (p_test_1_, p_test_2_, p_test_3_, p_test_4_, p_test_5_) -> p_test_2_.getEntitiesOfClass(PlayerEntity.class, new AxisAlignedBB(p_test_4_).inflate(50), playerEntity -> playerEntity.hasEffect(DragonEffects.MAGIC)).isEmpty());
        HUNTER_HOUND = createEntity(HunterHound.class, HunterHound::new, 0.6F, 0.85F, 10510648, 8934192, null);
        SHOOTER_HUNTER = createEntity(Shooter.class, Shooter::new, 0.6F, 1.95F, 12486764, 2690565, null);
        SQUIRE_HUNTER = createEntity(Squire.class, Squire::new, 0.6F, 1.95F, 12486764, 5318420, null);
        PRINCESS = createEntity(PrincessEntity.class, PrincessEntity::new, 0.6F, 1.9F, 16766495, 174864, null);
        KNIGHT = createEntity(Knight.class, Knight::new, 0.8f, 2.5f, 0, 0x510707, null);
        PRINCE_ON_HORSE = createEntity(by.jackraidenph.dragonsurvival.gecko.Prince.class, by.jackraidenph.dragonsurvival.gecko.Prince::new, 0.8f, 2.5f, 0xffdd1f, 0x2ab10, null);
        VillagerRelationsHandler.dragonHunters = new ArrayList<>(4);
        if (ConfigHandler.COMMON.spawnHound.get())
            VillagerRelationsHandler.dragonHunters.add(cast(HUNTER_HOUND));
        if (ConfigHandler.COMMON.spawnSquire.get())
            VillagerRelationsHandler.dragonHunters.add(cast(SQUIRE_HUNTER));
        if (ConfigHandler.COMMON.spawnHunter.get())
            VillagerRelationsHandler.dragonHunters.add(cast(SHOOTER_HUNTER));
        if (ConfigHandler.COMMON.spawnKnight.get())
            VillagerRelationsHandler.dragonHunters.add(cast(KNIGHT));
        PRINCESS_ON_HORSE = createEntity(Princess.class, Princess::new, 0.8f, 2.5f, 0xffd61f, 0x2ab10, null);
        for (Item spawnEgg : spawnEggs) {
            Preconditions.checkNotNull(spawnEgg.getRegistryName(), "registry name is null");
            event.getRegistry().register(spawnEgg);
        }
    }

    @SubscribeEvent
    public static void attributeCreationEvent(EntityAttributeCreationEvent event) {
        event.put(MAGICAL_BEAST, MagicalPredatorEntity.createMonsterAttributes().build());
        event.put(DRAGON, DragonEntity.createLivingAttributes().build());
        event.put(HUNTER_HOUND, WolfEntity.createAttributes().add(Attributes.MOVEMENT_SPEED, ConfigHandler.COMMON.houndSpeed.get()).add(Attributes.ATTACK_DAMAGE, ConfigHandler.COMMON.houndDamage.get()).add(Attributes.MAX_HEALTH, ConfigHandler.COMMON.houndHealth.get()).build());
        event.put(SHOOTER_HUNTER, PillagerEntity.createAttributes().add(Attributes.MOVEMENT_SPEED, ConfigHandler.COMMON.hunterSpeed.get()).add(Attributes.MAX_HEALTH, ConfigHandler.COMMON.houndHealth.get()).add(Attributes.ARMOR, ConfigHandler.COMMON.hunterArmor.get()).add(Attributes.ATTACK_DAMAGE, ConfigHandler.COMMON.hunterDamage.get()).build());
        event.put(SQUIRE_HUNTER, VindicatorEntity.createAttributes().add(Attributes.MOVEMENT_SPEED, ConfigHandler.COMMON.squireSpeed.get()).add(Attributes.ATTACK_DAMAGE, ConfigHandler.COMMON.squireDamage.get()).add(Attributes.ARMOR, ConfigHandler.COMMON.squireArmor.get()).add(Attributes.MAX_HEALTH, ConfigHandler.COMMON.squireHealth.get()).build());
        event.put(PRINCESS, VillagerEntity.createAttributes().build());
        event.put(PRINCESS_ON_HORSE, VillagerEntity.createAttributes().build());
        event.put(KNIGHT, Knight.createMobAttributes().add(Attributes.MOVEMENT_SPEED, ConfigHandler.COMMON.knightSpeed.get()).add(Attributes.ATTACK_DAMAGE, ConfigHandler.COMMON.knightDamage.get()).add(Attributes.ARMOR, ConfigHandler.COMMON.knightArmor.get()).add(Attributes.MAX_HEALTH, ConfigHandler.COMMON.knightHealth.get()).build());
        event.put(PRINCE_ON_HORSE, VillagerEntity.createAttributes().add(Attributes.ATTACK_DAMAGE, ConfigHandler.COMMON.princeDamage.get()).add(Attributes.MAX_HEALTH, ConfigHandler.COMMON.princeHealth.get()).add(Attributes.ARMOR, ConfigHandler.COMMON.princeArmor.get()).add(Attributes.MOVEMENT_SPEED, ConfigHandler.COMMON.princeSpeed.get()).build());
    }
}
