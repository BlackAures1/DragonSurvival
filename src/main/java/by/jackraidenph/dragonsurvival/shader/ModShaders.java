package by.jackraidenph.dragonsurvival.shader;

public class ModShaders {
    public static ShaderProgram color_cycle;

    public static void register() {
        color_cycle = new ShaderProgram().addFragment("shaders/color_cycle.frag").addVertex("shaders/color_cycle.vert").compile();
    }
}
