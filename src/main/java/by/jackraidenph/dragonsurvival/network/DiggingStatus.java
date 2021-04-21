package by.jackraidenph.dragonsurvival.network;

public class DiggingStatus {
    public int playerId;
    public boolean status;

    public DiggingStatus(int playerId, boolean status) {
        this.playerId = playerId;
        this.status = status;
    }

    public DiggingStatus() {
    }
}
