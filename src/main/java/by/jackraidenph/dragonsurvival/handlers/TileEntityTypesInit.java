package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.nest.NestEntity;
import by.jackraidenph.dragonsurvival.tiles.PredatorStarTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DragonSurvivalMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class TileEntityTypesInit {

    public static TileEntityType<PredatorStarTileEntity> PREDATOR_STAR_TILE_ENTITY_TYPE;
    public static TileEntityType<NestEntity> nestEntityTile;

    @SubscribeEvent
    public static void registerTE(RegistryEvent.Register<TileEntityType<?>> event) {
        nestEntityTile = TileEntityType.Builder.create(() -> new NestEntity(nestEntityTile), BlockInit.nestBlock).build(null);
        PREDATOR_STAR_TILE_ENTITY_TYPE = TileEntityType.Builder.create(PredatorStarTileEntity::new, BlockInit.PREDATOR_STAR_BLOCK).build(null);
        event.getRegistry().registerAll(nestEntityTile.setRegistryName(DragonSurvivalMod.MODID, "dragon_nest"),
                PREDATOR_STAR_TILE_ENTITY_TYPE.setRegistryName(DragonSurvivalMod.MODID, "predator_star_te"));
    }
}
