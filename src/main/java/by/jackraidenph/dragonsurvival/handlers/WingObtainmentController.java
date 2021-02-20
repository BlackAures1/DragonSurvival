package by.jackraidenph.dragonsurvival.handlers;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import by.jackraidenph.dragonsurvival.capability.DragonStateProvider;
import by.jackraidenph.dragonsurvival.network.SynchronizeDragonCap;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;

import java.util.Locale;

@Mod.EventBusSubscriber
public class WingObtainmentController {

    @SubscribeEvent
    public static void serverChatEvent(ServerChatEvent chatEvent) {
        String message = chatEvent.getMessage();
        ServerPlayerEntity playerEntity = chatEvent.getPlayer();
        String lowercase = message.toLowerCase(Locale.ROOT);
        DragonStateProvider.getCap(playerEntity).ifPresent(dragonStateHandler -> {
            if (dragonStateHandler.isDragon() && !dragonStateHandler.hasWings()) {
                if (playerEntity.getServerWorld().getDimension().getType() == DimensionType.THE_END) {
                    if (!playerEntity.getServerWorld().getDragons().isEmpty()) {
                        if (lowercase.contains("give") && lowercase.contains("wings")) {
                            Thread thread = new Thread(() -> {
                                try {
                                    Thread.sleep(1000);
                                    playerEntity.sendMessage(new StringTextComponent("<Ender dragon>: I bestow wings on you"));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });
                            thread.start();
                            dragonStateHandler.setHasWings(true);
                            //synchronize
                            DragonSurvivalMod.CHANNEL.send(PacketDistributor.ALL.noArg(), new SynchronizeDragonCap(playerEntity.getEntityId(), dragonStateHandler.isHiding(), dragonStateHandler.getType(), dragonStateHandler.getLevel(), dragonStateHandler.isDragon(), dragonStateHandler.getHealth(), true));
                        }
                    }
                }
            }
        });
    }
}
