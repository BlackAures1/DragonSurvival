package by.jackraidenph.dragonsurvival.network;

/**
 * Jump animation length is 20.8 ticks
 */
public class StartJump {
    public int playerId;
    public int ticks;

    public StartJump(int playerId, int ticks) {
        this.playerId = playerId;
        this.ticks = ticks;
    }

    public StartJump() {
    }
}
