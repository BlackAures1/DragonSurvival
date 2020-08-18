package by.jackraidenph.dragonsurvival.shader;

import by.jackraidenph.dragonsurvival.DragonSurvivalMod;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Deprecated
public class ShaderProgram {
    private int programID;

    public ShaderProgram() {
        programID = GL20.glCreateProgram();
    }

    public ShaderProgram addFragment(String path) {
        return add(path, GL20.GL_FRAGMENT_SHADER);
    }

    public ShaderProgram addVertex(String path) {
        return add(path, GL20.GL_VERTEX_SHADER);
    }


    public ShaderProgram add(String path, int shaderType) {
        int shaderID = GL20.glCreateShader(shaderType);
        GL20.glShaderSource(shaderID, readFile(path));
        GL20.glCompileShader(shaderID);

        if (GL20.glGetShaderi(shaderID,
                GL20.GL_COMPILE_STATUS) == GL20.GL_FALSE)
            throw new RuntimeException("Shader compilation error!\n " +
                    GL20.glGetShaderInfoLog(shaderID, GL20.
                            glGetShaderi(shaderID, GL20.GL_INFO_LOG_LENGTH)));


        GL20.glAttachShader(programID, shaderID);
        return this;
    }


    public ShaderProgram compile() {
        GL20.glLinkProgram(programID);
        return this;
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }


    public int getUniform(String name) {
        return GL20.glGetUniformLocation(programID, name);
    }


    private String readFile(String path) {
        try {
            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(Minecraft.getInstance().getResourceManager().getResource(new ResourceLocation(DragonSurvivalMod.MODID, path)).getInputStream(), "UTF-8"));
            String str;
            while ((str = reader.readLine()) != null)
                builder.append(str).append("\n");

            return builder.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
