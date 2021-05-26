package by.jackraidenph.dragonsurvival.util;

public enum DragonLevel {
    BABY(14, 1.1f, 0.4f, 0.9f, "newborn"),
    YOUNG(20, 1.6f, 0.9f, 1.9f, "young"),
    ADULT(30, 2.1f, 1.4f, 2.3f, "adult");

    public int size;
    public float jumpHeight;
    public float maxWidth;
    public float maxHeight;
    public String name;

    DragonLevel(int size, float jumpHeight, float maxWidth, float maxHeight, String name_) {
        this.size = size;
        this.jumpHeight = jumpHeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.name = name_;
    }
}
