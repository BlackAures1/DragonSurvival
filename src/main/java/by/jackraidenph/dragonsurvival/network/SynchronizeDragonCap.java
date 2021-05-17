package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.util.DragonType;

public class SynchronizeDragonCap {

    public int playerId;
    public boolean hiding;
    public DragonType dragonType;
    public float size;
    public boolean hasWings;
    public int lavaAirSupply;

    public SynchronizeDragonCap() {
    }

    public SynchronizeDragonCap(int playerId, boolean hiding, DragonType dragonType, float size, boolean hasWings, int lavaAirSupply) {
        this.playerId = playerId;
        this.hiding = hiding;
        this.dragonType = dragonType;
        this.size = size;
        this.hasWings = hasWings;
        this.lavaAirSupply = lavaAirSupply;
    }
}
