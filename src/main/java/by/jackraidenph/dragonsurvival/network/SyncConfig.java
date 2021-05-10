package by.jackraidenph.dragonsurvival.network;

import java.util.function.Supplier;

import by.jackraidenph.dragonsurvival.PacketProxy;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

public class SyncConfig implements IMessage<SyncConfig> {
	
	public double serverMaxFlightSpeed;
	public boolean serverMineStarBlock;
	public boolean serverSizeChangesHitbox;
	public boolean serverHitboxGrowsPastHuman;
    public boolean serverStartWithWings;
	
	public SyncConfig(double maxFlightSpeed, boolean mineStarBlock, boolean sizeChangesHitbox, boolean hitboxGrowsPastHuman, boolean startWithWings) {
		this.serverMaxFlightSpeed = maxFlightSpeed;
		this.serverMineStarBlock = mineStarBlock;
		this.serverSizeChangesHitbox = sizeChangesHitbox;
		this.serverHitboxGrowsPastHuman = hitboxGrowsPastHuman;
		this.serverStartWithWings = startWithWings;
	}
    
	public SyncConfig() {
	}

	@Override
	public void encode(SyncConfig message, PacketBuffer buffer) {
		buffer.writeDouble(message.serverMaxFlightSpeed);
		buffer.writeBoolean(message.serverMineStarBlock);
		buffer.writeBoolean(message.serverSizeChangesHitbox);
		buffer.writeBoolean(message.serverHitboxGrowsPastHuman);
		buffer.writeBoolean(message.serverStartWithWings);
	}

	@Override
	public SyncConfig decode(PacketBuffer buffer) {
		return new SyncConfig(buffer.readDouble(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
	}

	@Override
	public void handle(SyncConfig message, Supplier<NetworkEvent.Context> supplier) {
		DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> new PacketProxy().saveServerConfig(message, supplier));
	}

}
