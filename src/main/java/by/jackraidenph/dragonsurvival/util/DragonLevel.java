package by.jackraidenph.dragonsurvival.util;

public enum DragonLevel {
    BABY(14, 1.1f, 0.4f, 0.9f, "newborn", 2),
    YOUNG(20, 1.6f, 0.9f, 1.9f, "young", 3),
    ADULT(30, 2.1f, 1.4f, 2.3f, "adult", 4);

    public int initialHealth;
    public float jumpHeight;
    public float maxWidth;
    public float maxHeight;
    public String name;
    public int baseDamage;

    DragonLevel(int initialHealth, float jumpHeight, float maxWidth, float maxHeight, String name_, int baseDamage) {
        this.initialHealth = initialHealth;
        this.jumpHeight = jumpHeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.name = name_;
        this.baseDamage = baseDamage;
    }
}
