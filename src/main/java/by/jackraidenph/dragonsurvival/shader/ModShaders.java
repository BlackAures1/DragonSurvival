package by.jackraidenph.dragonsurvival.shader;

@Deprecated
public class ModShaders {
    public static ShaderProgram color_cycle;

    public static void register() {
        color_cycle = new ShaderProgram().addFragment("shaders/bloom.frag").addVertex("shaders/bloom.vert").compile();
    }
}
