package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;

public class SynchronizeDragonCap {

    public int playerId;
    public boolean hiding;
    public DragonType dragonType;
    public DragonLevel dragonLevel;
    public boolean isDragon;
    public float health;
    public boolean hasWings;

    public SynchronizeDragonCap() {
    }

    public SynchronizeDragonCap(int playerId, boolean hiding, DragonType dragonType, DragonLevel dragonLevel, boolean isDragon, float health, boolean hasWings) {
        this.playerId = playerId;
        this.hiding = hiding;
        this.dragonType = dragonType;
        this.dragonLevel = dragonLevel;
        this.isDragon = isDragon;
        this.health = health;
        this.hasWings = hasWings;
    }
}
