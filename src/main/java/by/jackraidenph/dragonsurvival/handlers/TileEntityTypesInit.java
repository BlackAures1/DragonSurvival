package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.nest.NestEntity;
import by.jackraidenph.dragonsurvival.nest.NestPlaceHolder;
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

    @SubscribeEvent
    public static void registerTE(RegistryEvent.Register<TileEntityType<?>> event) {
        nestEntityTile = TileEntityType.Builder.create(() -> new NestEntity(nestEntityTile), BlockInit.smallCaveNest, BlockInit.smallForestNest, BlockInit.smallSeaNest, BlockInit.mediumSeaNest).build(null);
        PREDATOR_STAR_TILE_ENTITY_TYPE = TileEntityType.Builder.create(PredatorStarTileEntity::new, BlockInit.PREDATOR_STAR_BLOCK).build(null);
        IForgeRegistry<TileEntityType<?>> registry = event.getRegistry();
        registry.registerAll(nestEntityTile.setRegistryName(DragonSurvivalMod.MODID, "dragon_nest"),
                PREDATOR_STAR_TILE_ENTITY_TYPE.setRegistryName(DragonSurvivalMod.MODID, "predator_star_te"));
        nestPlaceHolder = TileEntityType.Builder.create(() -> new NestPlaceHolder(nestPlaceHolder), BlockInit.mediumSeaNest).build(null);
        registry.register(nestPlaceHolder.setRegistryName("nest_placeholder"));
    }
}
