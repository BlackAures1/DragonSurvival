package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.containers.DragonInventoryContainer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class EventHandler {
    @SubscribeEvent
    public static void onEntityJoinWorld(EntityJoinWorldEvent entityJoinWorldEvent) {
        //TODO change only when is dragon
        Entity entity = entityJoinWorldEvent.getEntity();
        if (entity instanceof PlayerEntity) {
            PlayerEntity playerEntity = (PlayerEntity) entity;
            Field field = PlayerEntity.class.getDeclaredFields()[15];
            if (field.getType() == PlayerContainer.class) {
                field.setAccessible(true);
                try {
                    DragonInventoryContainer dragonInventoryContainer = new DragonInventoryContainer(playerEntity.inventory, entityJoinWorldEvent.getWorld().isRemote, playerEntity);
                    field.set(playerEntity, dragonInventoryContainer);
                    playerEntity.openContainer = dragonInventoryContainer;
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
