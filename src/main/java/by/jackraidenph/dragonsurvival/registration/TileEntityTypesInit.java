package by.jackraidenph.dragonsurvival.registration;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.nest.NestEntity;
import by.jackraidenph.dragonsurvival.nest.NestPlaceHolder;
import by.jackraidenph.dragonsurvival.tiles.AltarEntity;
import by.jackraidenph.dragonsurvival.tiles.DragonBeaconEntity;
import by.jackraidenph.dragonsurvival.tiles.HelmetEntity;
import by.jackraidenph.dragonsurvival.tiles.PredatorStarTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.IForgeRegistry;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class TileEntityTypesInit {

    public static TileEntityType<PredatorStarTileEntity> PREDATOR_STAR_TILE_ENTITY_TYPE;
    public static TileEntityType<NestEntity> nestEntityTile;
    public static TileEntityType<NestPlaceHolder> nestPlaceHolder;
    public static TileEntityType<AltarEntity> altarEntityTile;
    public static TileEntityType<HelmetEntity> helmetTile;
    public static TileEntityType<DragonBeaconEntity> dragonBeacon;

    @SubscribeEvent
    public static void registerBlockEntities(RegistryEvent.Register<TileEntityType<?>> event) {
        nestEntityTile = TileEntityType.Builder.of(() -> new NestEntity(nestEntityTile),
                BlockInit.smallCaveNest, BlockInit.smallForestNest, BlockInit.smallSeaNest,
                BlockInit.mediumSeaNest, BlockInit.mediumCaveNest, BlockInit.mediumForestNest,
                BlockInit.bigCaveNest, BlockInit.bigSeaNest, BlockInit.bigForestNest).build(null);
        PREDATOR_STAR_TILE_ENTITY_TYPE = TileEntityType.Builder.of(PredatorStarTileEntity::new, BlockInit.PREDATOR_STAR_BLOCK).build(null);
        IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
        registry.registerAll(nestEntityTile.setRegistryName(DragonSurvivalMod.MODID, "dragon_nest"),
                PREDATOR_STAR_TILE_ENTITY_TYPE.setRegistryName(DragonSurvivalMod.MODID, "predator_star_te"));
        nestPlaceHolder = TileEntityType.Builder.of(() -> new NestPlaceHolder(nestPlaceHolder), BlockInit.mediumSeaNest, BlockInit.mediumForestNest, BlockInit.mediumCaveNest, BlockInit.bigForestNest, BlockInit.bigSeaNest, BlockInit.bigCaveNest).build(null);
        registry.register(nestPlaceHolder.setRegistryName("nest_placeholder"));
        altarEntityTile = TileEntityType.Builder.of(() -> new AltarEntity(altarEntityTile),
                BlockInit.dragon_altar_stone,
                BlockInit.dragon_altar_sandstone,
                BlockInit.dragon_altar_red_sandstone,
                BlockInit.dragon_altar_purpur_block,
                BlockInit.dragon_altar_oak_log,
                BlockInit.dragon_altar_nether_bricks,
                BlockInit.dragon_altar_mossy_cobblestone,
                BlockInit.dragon_altar_blackstone
        ).build(null);
        registry.register(altarEntityTile.setRegistryName(DragonSurvivalMod.MODID, "dragon_altar"));
        helmetTile = TileEntityType.Builder.of(HelmetEntity::new, BlockInit.helmet1, BlockInit.helmet2, BlockInit.helmet3).build(null);
        helmetTile.setRegistryName(DragonSurvivalMod.MODID, "knight_helmet");
        registry.register(helmetTile);
        dragonBeacon = TileEntityType.Builder.of(DragonBeaconEntity::new, BlockInit.dragonBeacon, BlockInit.peaceDragonBeacon, BlockInit.magicDragonBeacon, BlockInit.vetoDragonBeacon).build(null);
        dragonBeacon.setRegistryName(DragonSurvivalMod.MODID, "dragon_beacon");
        registry.register(dragonBeacon);
    }
}
