package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.util.DragonType;

public class SynchronizeDragonCap {

    public int playerId;
    public boolean hiding;
    public DragonType dragonType;
    public boolean isDragon;
    public float size;
    public boolean hasWings;
    public int baseDamage;

    public SynchronizeDragonCap() {
    }

    public SynchronizeDragonCap(int playerId, boolean hiding, DragonType dragonType, boolean isDragon, float size, boolean hasWings, int baseDamage) {
        this.playerId = playerId;
        this.hiding = hiding;
        this.dragonType = dragonType;
        this.isDragon = isDragon;
        this.size = size;
        this.hasWings = hasWings;
        this.baseDamage = baseDamage;
    }
}
