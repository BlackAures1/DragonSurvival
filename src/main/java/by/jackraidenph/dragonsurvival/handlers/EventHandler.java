package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.capability.PlayerStateProvider;
import by.jackraidenph.dragonsurvival.containers.DragonInventoryContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.lang.reflect.Field;

@Mod.EventBusSubscriber
public class EventHandler {

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
        PlayerEntity playerEntity = playerTickEvent.player;
        if (playerEntity.isCreative() && playerEntity.container instanceof DragonInventoryContainer) {
            setPlayerContainer(playerEntity);
        } else if (!playerEntity.isCreative() && !playerEntity.isSpectator() && playerEntity.container.getClass() == PlayerContainer.class) {
            PlayerStateProvider.getCap(playerEntity).ifPresent(playerStateHandler -> {
                if (playerStateHandler.getIsDragon())
                    setDragonContainer(playerEntity);
            });
        }
    }

    public static void setPlayerContainer(PlayerEntity playerEntity) {
        Field field = PlayerEntity.class.getDeclaredFields()[15];
        if (field.getType() == PlayerContainer.class) {
            field.setAccessible(true);
            try {
                PlayerContainer playerContainer = new PlayerContainer(playerEntity.inventory, playerEntity.world.isRemote, playerEntity);
                field.set(playerEntity, playerContainer);
                playerEntity.openContainer = playerContainer;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setDragonContainer(PlayerEntity playerEntity) {
        Field field = PlayerEntity.class.getDeclaredFields()[15];
        if (field.getType() == PlayerContainer.class) {
            field.setAccessible(true);
            try {
                DragonInventoryContainer dragonInventoryContainer = new DragonInventoryContainer(playerEntity.inventory, playerEntity.world.isRemote, playerEntity);
                field.set(playerEntity, dragonInventoryContainer);
                playerEntity.openContainer = dragonInventoryContainer;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
