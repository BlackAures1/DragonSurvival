package by.jackraidenph.dragonsurvival.handlers;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.server.management.PlayerInteractionManager;
import net.minecraftforge.event.TickEvent;

import java.lang.reflect.Field;
import java.util.HashMap;

//@Mod.EventBusSubscriber(Dist.DEDICATED_SERVER)
public class ServerEvents {
    //TODO digging statuses
    public static HashMap<Integer, Boolean> playersDigging = new HashMap<>(20);

    //    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent playerTickEvent) {
        PlayerEntity playerEntity = playerTickEvent.player;
        if (playerEntity instanceof ServerPlayerEntity) {
            PlayerInteractionManager interactionManager = ((ServerPlayerEntity) playerEntity).interactionManager;
            Field field = PlayerInteractionManager.class.getDeclaredFields()[4];
            field.setAccessible(true);
            if (field.getType() == boolean.class) {
                try {
                    boolean isMining = field.getBoolean(interactionManager);
                    playersDigging.put(playerEntity.getEntityId(), isMining);
                    if (isMining)
                        System.out.println();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
