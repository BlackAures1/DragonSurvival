package by.jackraidenph.dragonsurvival.network;

import java.util.function.Supplier;

import by.jackraidenph.dragonsurvival.PacketProxy;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent.Context;

public class SyncCapabilityDebuff implements IMessage<SyncCapabilityDebuff>{

	public int playerId;
	public int timeWithoutWater;
	public int timeInDarkness;
	
	public SyncCapabilityDebuff() {
    }
	
	public SyncCapabilityDebuff(int playerId, int timeWithoutWater, int timeInDarkness) {
        this.playerId = playerId;
        this.timeWithoutWater = timeWithoutWater;
        this.timeInDarkness = timeInDarkness;
    }
	
	@Override
	public void encode(SyncCapabilityDebuff message, PacketBuffer buffer) {
		buffer.writeInt(message.playerId);
		buffer.writeInt(message.timeWithoutWater);
		buffer.writeInt(message.timeInDarkness);
	}

	@Override
	public SyncCapabilityDebuff decode(PacketBuffer buffer) {
		return new SyncCapabilityDebuff(buffer.readInt(), buffer.readInt(), buffer.readInt());
	}

	@Override
	public void handle(SyncCapabilityDebuff message, Supplier<Context> supplier) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> new PacketProxy().handleCapabilityDebuff(message, supplier));
	}

}
