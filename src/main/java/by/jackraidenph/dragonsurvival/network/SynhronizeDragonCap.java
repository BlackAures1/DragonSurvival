package by.jackraidenph.dragonsurvival.network;

import by.jackraidenph.dragonsurvival.util.DragonLevel;
import by.jackraidenph.dragonsurvival.util.DragonType;

public class SynhronizeDragonCap {

    public int playerId;
    public boolean hiding;
    public DragonType dragonType;
    public DragonLevel dragonLevel;
    public boolean isDragon;

    public SynhronizeDragonCap() {
    }

    public SynhronizeDragonCap(int playerId, boolean hiding, DragonType dragonType, DragonLevel dragonLevel, boolean isDragon) {
        this.playerId = playerId;
        this.hiding = hiding;
        this.dragonType = dragonType;
        this.dragonLevel = dragonLevel;
        this.isDragon = isDragon;
    }
}
