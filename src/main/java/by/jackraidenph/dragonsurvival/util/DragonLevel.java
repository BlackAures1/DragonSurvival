package by.jackraidenph.dragonsurvival.util;

public enum DragonLevel {
    BABY(14, 1.1f, 0.4f, 0.9f, 0.9f, "newborn", 2),
    YOUNG(20, 1.6f, 0.9f, 1.9f, 1.9f, "young", 4),
    ADULT(30, 2.1f, 1.4f, 2.3f, 2.3f, "adult", 6);

    public int initialHealth;
    public float jumpHeight;
    public float maxWidth, maxHeight, maxLength;
    public String name;
    public int baseDamage;

    DragonLevel(int initialHealth, float jumpHeight, float maxWidth, float maxHeight, float maxLength, String name_, int baseDamage) {
        this.initialHealth = initialHealth;
        this.jumpHeight = jumpHeight;
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        this.maxLength = maxLength;
        this.name = name_;
        this.baseDamage = baseDamage;
    }
}
